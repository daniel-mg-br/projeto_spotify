package dao;

import java.sql.*; 
import java.util.ArrayList; 
import java.util.List;
import model.content.Musica;

/**
 * Classe responsável pela manipulação de dados de conteúdos e às músicas associadas.
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
		
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConexao();
			conn.setAutoCommit(false);
			
			inserirConteudo(conn, novaMusica);
			inserirMusica(conn, novaMusica);
			sincronizarEquipe(conn, novaMusica);
			
			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao salvar música, rollback... " + e.getMessage());
			try {
				if (conn != null) conn.rollback();
			} catch (SQLException ex) {
				System.out.println("Erro no rollback: " + ex.getMessage());
			}
		}
		finally
		{
			if (conn != null) {
				try {conn.setAutoCommit(true); conn.close();} catch (SQLException e) {e.printStackTrace();}
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
		String sql = "SELECT c.id, c.titulo, c.duracao_min, " +
					 "m.genero, m.letra " +
					 "FROM conteudo AS c JOIN musica AS m ON c.id = m.conteudo_id " +
					 "WHERE c.id = ?";
		
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			
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
		String sql = "SELECT c.id, c.titulo, c.duracao_min, " +
					 "m.genero, m.letra " +
					 "FROM conteudo AS c JOIN musica AS m ON c.id = m.conteudo_id";
		
		List <Musica> musicas = new ArrayList<>();
		
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery())
		{
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
	 * Atualiza uma música já registrada na tabela.
	 * 
	 * @param musicaAtualizada Objeto do tipo Musica com os novos dados a serem gravados;
	 * @return Retorna true se a operação foi bem sucedida, ou false se não.
	 */
	public boolean atualizar(Musica musicaAtualizada) {
		if (musicaAtualizada == null) return false;
		
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
			System.out.println("Erro ao atualizar música, rollback... " + e.getMessage());
			try {
				if (conn != null) conn.rollback();
			} catch (SQLException ex) {
				System.out.println("Erro no rollback: " + ex.getMessage());
			}
		}
		finally
		{
			if (conn != null) {
				try {conn.setAutoCommit(true); conn.close();} catch (SQLException e) {e.printStackTrace();}
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
		
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql);)
		{
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
	 * Método auxiliar para inserir um novo conteúdo ao banco de dados, com base nos dados (herdados)
	 * da classe Musica.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param musica Objeto do tipo Musica, com os dados base do novo conteúdo;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void inserirConteudo(Connection conn, Musica musica) throws SQLException {
		String sql = "INSERT INTO conteudo (titulo, duracao_min) VALUES (?,?)";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			stmt.setString(1, musica.getTitulo());
			stmt.setInt(2, musica.getDuracaoMin());
			stmt.executeUpdate();
			
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
		
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, musica.getId());
			stmt.setString(2, musica.getGenero());
			stmt.setString(3, musica.getLetra());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para modificar um conteúdo já existente da tabela.
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
		String sqlDelete= "DELETE FROM musica_equipe WHERE musica_id = ?";
		try (PreparedStatement stmtDel = conn.prepareStatement(sqlDelete))
		{
			stmtDel.setInt(1, musica.getId());
			stmtDel.executeUpdate();
		}
		
		if (musica.getEquipe() == null || musica.getEquipe().isEmpty()) return;
		
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
		
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, musica.getId());
			
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
	 * @param rs ResultSet gerado pelo SELECT;
	 * @return Retorna o objeto do tipo Musica com os dados recuperados da consulta;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private Musica mapearMusica(Connection conn, ResultSet rs) throws SQLException {
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