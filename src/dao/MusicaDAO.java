package dao;

import java.sql.*; 
import java.util.ArrayList; 
import java.util.List;
import model.content.Musica;

/**
 * Classe MusicaDAO responsável pela manipulação de dados de conteúdos e às músicas/episódios associados.
 */
public class MusicaDAO {
/**
 * Método para salvar uma nova música (e seu conteúdo pai) à tabela.
 * 
 * @param novaMusica Objeto do tipo Musica com os dados da música a ser inserida;
 * @return Retorna true se a operação foi bem sucedida, ou false se não.
 */
	public boolean salvar(Musica novaMusica) {
		if (novaMusica == null) return false;
		
		/*
		 *  Estabelece a conexão JDBC.
		 *  
		 *  Atribuindo 'false' ao Auto Commit por segurança, assim o rollback é possível em caso de
		 *  erros nas operações envolvendo a música e o conteúdo.
		 */
		
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConexao();
			conn.setAutoCommit(false);
			
			inserirConteudo(conn, novaMusica);
			inserirMusica(conn, novaMusica);
			sincronizarEquipe(conn, novaMusica);
			
			// Se der certo, aplica as mudanças.
			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			// Se não der certo, tenta fazer o rollback da transação.
			System.out.println("Erro ao salvar música, rollback... " + e.getMessage());
			try {
				if (conn != null) conn.rollback();
			} catch (SQLException ex) {
				System.out.println("Erro no rollback: " + ex.getMessage());
			}
		}
		finally
		{	
			// Atribui 'true' ao Auto Commit e fecha a conexão.
			if (conn != null) {
				try {
					conn.setAutoCommit(true); 
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	 * Método para recuperar os dados de uma música por meio de seu ID.
	 * 
	 * @param id ID da música requisistada;
	 * @return Retorna o objeto de Musica com os dados, ou null se a operação falhar.
	 */
	public Musica buscarId(int id) {
		// String sql com a operação de consulta.
		String sql = "SELECT c.id, c.titulo, c.duracao_min, " +
					 "m.genero, m.letra " +
					 "FROM conteudo AS c JOIN musica AS m ON c.id = m.conteudo_id " +
					 "WHERE c.id = ?";
		
		// Try With Resources para garantir que a conexão seja fechada em caso de falha.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			
			// Tenta ler o Result Set com as informações, reconstruindo o objeto com os dados.
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Musica musicaEncontrada = mapearMusica(conn, rs);
					return musicaEncontrada;
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao buscar música: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Recupera os dados de todas as músicas registradas no banco.
	 * 
	 * @return Retorna a lista com todas as músicas.
	 */
	public List <Musica> listarMusicas() {
		// String sql com a operação de consulta.
		String sql = "SELECT c.id, c.titulo, c.duracao_min, " +
					 "m.genero, m.letra " +
					 "FROM conteudo AS c JOIN musica AS m ON c.id = m.conteudo_id";
		
		// Lista para armazenar os objetos recuperados.
		List <Musica> musicas = new ArrayList<>();
		
		// Try With Resources para garantir a segurança com a conexão e com os dados a serem consultados.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery())
		{
			// Itera pelo ResultSet reconstruindo os objetos com os dados retornados.
			while (rs.next()) {
				Musica m = mapearMusica(conn, rs);
				musicas.add(m);
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao listar músicas: " + e.getMessage());
		}
		return musicas;
	}
	
	/**
	 * Atualiza uma música (e o seu conteúdo pai) já registrada na tabela.
	 * 
	 * @param musicaAtualizada Objeto do tipo Musica com os novos dados a serem gravados;
	 * @return Retorna true se a operação foi bem sucedida, ou false se não.
	 */
	public boolean atualizar(Musica musicaAtualizada) {
		if (musicaAtualizada == null) return false;
		
		// Estabelece a conexão com o JDBC, seguindo o mesmo esquema com o Auto Commit e Rollback anteriores.
		Connection conn = null;
		try
		{
			conn = ConnectionFactory.getConexao();
			conn.setAutoCommit(false);
			
			modificarConteudo(conn, musicaAtualizada);
			modificarMusica(conn, musicaAtualizada);
			sincronizarEquipe(conn, musicaAtualizada);
			
			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			// Em caso de erro, tenta o Rollback da transação.
			System.out.println("Erro ao atualizar música, rollback... " + e.getMessage());
			try {
				if (conn != null) conn.rollback();
			} catch (SQLException ex) {
				System.out.println("Erro no rollback: " + ex.getMessage());
			}
		}
		finally
		{
			// Habilita o Auto Commit e fecha a conexão.
			if (conn != null) {
				try {
					conn.setAutoCommit(true); 
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
		
	/**
	 * Método para deletar uma música registrada na tabela.
	 * 
	 * @param id ID da música a ser removida;
	 * @return Retorna true se a operação foi bem sucedida, ou false se não.
	 */
	public boolean deletar(int id) {
		String sql = "DELETE FROM conteudo WHERE id = ?";
		
		// Tenta a conexão com o banco de dados.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql);)
		{
			// Verifica se a execupção da operação deu certo.
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao remover música: " + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Método auxiliar para inserir um novo conteúdo ao banco de dados, com base nos dados herdados
	 * pela classe Musica.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param musica Objeto do tipo Musica, com os dados base do novo conteúdo;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void inserirConteudo(Connection conn, Musica musica) throws SQLException {
		String sql = "INSERT INTO conteudo (titulo, duracao_min) VALUES (?,?)";
		
		// Tenta a conexão com o banco de dados e a execução da query.
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			stmt.setString(1, musica.getTitulo());
			stmt.setInt(2, musica.getDuracaoMin());
			stmt.executeUpdate();
			
			// Aplica o ID gerado automaticamente pela query.
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) musica.setId(rs.getInt(1));
				else throw new SQLException("Falha ao gerar ID do conteúdo!");
			}
		}
	}
	
	/**
	 * Método auxiliar para inserir uma nova música ao banco de dados.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param musica Objeto do tipo Musica, com os dados da nova música a ser inserida; 
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void inserirMusica(Connection conn, Musica musica) throws SQLException {
		String sql = "INSERT INTO musica (conteudo_id, genero, letra) VALUES (?,?,?)";
		
		// Tenta a conexão e a execução da query.
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, musica.getId());
			stmt.setString(2, musica.getGenero());
			stmt.setString(3, musica.getLetra());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para modificar um conteúdo já existente da tabela, usando os dados herdados
	 * pela classe Musica.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param musica Objeto do tipo Musica, com os dados base atualizados do Conteúdo;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void modificarConteudo(Connection conn, Musica musica) throws SQLException {
		String sql = "UPDATE conteudo SET titulo = ?, duracao_min = ? WHERE id = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, musica.getTitulo());
			stmt.setInt(2, musica.getDuracaoMin());
			stmt.setInt(3, musica.getId());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para modificar uma música já existente na tabela.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param musica Objeto do tipo Musica, com os novos dados da Música;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void modificarMusica(Connection conn, Musica musica) throws SQLException {
		String sql = "UPDATE musica SET genero = ?, letra = ? WHERE conteudo_id = ?";
		
		// Tenta a conexão e a execução da atualização.
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, musica.getGenero());
			stmt.setString(2, musica.getLetra());
			stmt.setInt(3, musica.getId());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para registrar a equipe da música, deletando a a lista antiga e adicionando
	 * a nova lista da equipe.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param musica Objeto do tipo Musica, com os dados para registrar a equipe da música;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void sincronizarEquipe(Connection conn, Musica musica) throws SQLException {
		
		// Primeiro, tenta deletar os dados antigos da equipe da música.
		String sqlDelete= "DELETE FROM musica_equipe WHERE musica_id = ?";
		try (PreparedStatement stmtDel = conn.prepareStatement(sqlDelete))
		{
			stmtDel.setInt(1, musica.getId());
			stmtDel.executeUpdate();
		}
		
		if (musica.getEquipe() == null || musica.getEquipe().isEmpty()) return;
		
		// Depois, reinsere os novos dados da equipe da música.
		String sqlInsert = "INSERT INTO musica_equipe (musica_id, nome_membro) VALUES (?,?)";
		try (PreparedStatement stmtIns = conn.prepareStatement(sqlInsert))
		{
			for (String membro : musica.getEquipe()) {
				stmtIns.setInt(1, musica.getId());
				stmtIns.setString(2, membro);
				stmtIns.executeUpdate();
			}
		}
	}
	
	/**
	 * Método auxiliar para recuperar a lista de nomes da equipe por trás da música.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param musica Objeto do tipo Musica, com os dados da equipe da música;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void carregarEquipe(Connection conn, Musica musica) throws SQLException {
		String sql = "SELECT nome_membro FROM musica_equipe WHERE musica_id = ?";
		
		// Tenta a conexão com banco de dados e posteriormente e leitura do ResultSet.
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, musica.getId());
			
			// Preenche a lista com os membros da equipe.
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					musica.adicionarMembroEquipe(rs.getString("nome_membro"));
				}
			}
		}
	}
	
	/**
	 * Método auxiliar para reconstruir um objeto de Musica com os dados retornados pelo SELECT.
	 * 
	 * @param conn Conexão com SQLite fornecida pela ConnectionFactory.
	 * @param rs ResultSet gerado pelo SELECT;
	 * @return Retorna o objeto do tipo Musica com os dados recuperados da consulta;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private Musica mapearMusica(Connection conn, ResultSet rs) throws SQLException {
		
		// Instancia uma música com base nos dados fornecidos pelo ResultSet.
		Musica musicaEncontrada = new Musica (
				rs.getInt("id"),
				rs.getString("titulo"),
				rs.getInt("duracao_min"),
				rs.getString("genero"),
				rs.getString("letra")
		);
		
		carregarEquipe(conn, musicaEncontrada);
		return musicaEncontrada;
	}
}