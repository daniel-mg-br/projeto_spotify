package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList; 
import java.util.List;
import model.content.Episodio;


/**
 * Classe EpisodioDAO para manipulação de dados de episódios de podcast.
 */
public class EpisodioDAO {
	
	/**
	 * Salvar um novo episódio de podcast (e seu conteúdo pai) no banco;
	 * 
	 * @param novoEpisodio Objeto com os dados do novo Episodio;
	 * @return Retorna true se a operação foi bem sucedida, ou false se não.
	 */
	public boolean salvar(Episodio novoEpisodio) {
		if (novoEpisodio == null) return false;
		
		/*
		 *  Estabelece a conexão JDBC.
		 *  
		 *  Atribuindo 'false' ao Auto Commit por segurança, assim o rollback é possível em caso de
		 *  erros nas operações envolvendo a música e o conteúdo.
		 */
		Connection conn = null;
		try 
		{
			conn = ConnectionFactory.getConexao();
			conn.setAutoCommit(false); 
			
			inserirConteudo(conn, novoEpisodio);
			inserirEpisodio(conn, novoEpisodio);
			sincronizarConvidados(conn, novoEpisodio); 
			
			// Se der certo, aplica as mudanças.
			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao salvar episódio, rollback..." + e.getMessage());
			
			// Se não der certo, tenta o Rollback da transação.
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
	 * Buscar um episódio de podcast pelo seu ID.
	 * 
	 * @param id ID do episódio em questão;
	 * @return Retorna o objeto de Episodio com os dados requisitados.
	 */
	public Episodio buscarId(int id) {
		
		// String sql com a operação a ser executada.
		String sql = "SELECT c.id, c.titulo, c.duracao_min, " +
					 "e.num_episodio, e.lancamento " +
					 "FROM conteudo AS c JOIN episodio AS e ON c.id = e.conteudo_id " +
					 "WHERE c.id = ?";
		
		// Try With Resources para garantir que a conexão seja fechada em caso de erro.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			
			// Tenta ler o ResultSet, reconstruindo o objeto do Episodio.
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Episodio epEncontrado = mapearEpisodio(conn, rs);
					return epEncontrado;
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao buscar episódio: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Recupera os dados de todos os episódios registrados.
	 * 
	 * @return Retorna uma lista com os episódios de podcast.
	 */
	public List <Episodio> listarEpisodios() {
		
		// String com sql com a consulta a ser executada.
		String sql = "SELECT c.id, c.titulo, c.duracao_min, " +
					 "e.num_episodio, e.lancamento " +
					 "FROM conteudo AS c JOIN episodio AS e ON c.id = e.conteudo_id";
		
		// Lista para guardar os objetos reconstruídos.
		List <Episodio> episodios = new ArrayList<>();
		
		// Tenta estabelecer a conexão e executar a query.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery())
		{
			// Lê as linhas do ResultSet e reconstrói os objetos com os dados retornados.
			while (rs.next()) {
				Episodio ep = mapearEpisodio(conn, rs);
				episodios.add(ep);
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erroa ao listar episódios: " + e.getMessage());
		}
		return episodios;
	}
	
	/**
	 * Atualizar um episódio (e seu conteúdo pai) existente no banco.
	 * 
	 * @param episodioAtualizado Objeto de Episodio com os novos dados;
	 * @return Retorna true se a operação foi bem sucedida, ou false se não.
	 */
	public boolean atualizar(Episodio episodioAtualizado) {
		if (episodioAtualizado == null) return false;
		
		// Tenta estabelecer a conexão, seguindo o mesmo esquema com o Auto Commit e Rollback anteriores.
		Connection conn = null;
		try 
		{
			conn = ConnectionFactory.getConexao();
			conn.setAutoCommit(false); 
			
			modificarConteudo(conn, episodioAtualizado);
			modificarEpisodio(conn, episodioAtualizado);
			sincronizarConvidados(conn, episodioAtualizado); 
			
			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			// Tenta o Rollback da transação em caso de erro.
			System.out.println("Erro ao atualizar episódio, rollback... " + e.getMessage());
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
	 * Remover um episódio de podcast existente  tnaabela.
	 * 
	 * @param id ID do episódio a ser removido;
	 * @return Retorna true se a operação foi bem sucedida, ou false se não.
	 */
	public boolean deletar(int id) {
		String sql = "DELETE FROM conteudo WHERE id = ?";
		
		// Tenta a conexão com o banco de dados e a execução da operação.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao remover episódio: " + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Método auxiliar para inserir um novo conteúdo ao banco de dados, com base nos dados herdados
	 * pela classe Episodio.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param episodio Objeto do tipo Episodio, com os dados base do novo conteúdo;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void inserirConteudo(Connection conn, Episodio episodio) throws SQLException {
		String sql = "INSERT INTO conteudo (titulo, duracao_min) VALUES (?,?)";
		
		// Tenta a conexão com o banco de dados e a inserção do conteúdo.
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			stmt.setString(1, episodio.getTitulo());
			stmt.setInt(2, episodio.getDuracaoMin());
			stmt.executeUpdate();
			
			// Aplica o ID gerado automaticamente pela query.
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) episodio.setId(rs.getInt(1));
				else throw new SQLException("Falha ao gerar ID do conteúdo!");
			}
		}
	}
	
	/**
	 * Método auxiliar para inserir um novo episódio de podcast na tabela.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param episodio Objeto de Episodio com os dados do novo episódio;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void inserirEpisodio(Connection conn, Episodio episodio) throws SQLException {
		String sql = "INSERT INTO episodio (conteudo_id, num_episodio, lancamento) VALUES (?,?,?)";
		
		// Tenta a conexão com banco de dados e a inserção do episódio.
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, episodio.getId());
			stmt.setInt(2, episodio.getNumEpisodio());
			stmt.setDate(3, Date.valueOf(episodio.getLancamento()));
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para modificar um conteúdo já existente na tabela, usando os dados herdados
	 * pela classe Episodio.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param episodio Objeto de episodio com os novos dados a serem salvos;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void modificarConteudo(Connection conn, Episodio episodio) throws SQLException {
		String sql = "UPDATE conteudo SET titulo = ?, duracao_min = ? WHERE id = ?";
		
		// Tenta estabelecer a conexão com o banco de dados e executar a atualização.
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, episodio.getTitulo());
			stmt.setInt(2, episodio.getDuracaoMin());
			stmt.setInt(3, episodio.getId());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para modificar os dados de um episódio já existente.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param episodio Objeto de Episodio com os novos dados a serem salvos;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void modificarEpisodio(Connection conn, Episodio episodio) throws SQLException {
		String sql = "UPDATE episodio SET num_episodio = ?, lancamento = ? WHERE conteudo_id = ?";
		
		// Tenta a conexão com o banco de dados e a execução da operação.
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, episodio.getNumEpisodio());
			stmt.setDate(2, Date.valueOf(episodio.getLancamento()));
			stmt.setInt(3, episodio.getId());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para atualizar a lista de convidados de um episódio.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param episodio Objeto de Episodio com a lista de convidados;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void sincronizarConvidados(Connection conn, Episodio episodio) throws SQLException {
		
		// Primeiro, remove a lista com os dados antigos dos convidados.
		String sqlDel = "DELETE FROM episodio_convidado WHERE episodio_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sqlDel))
		{
			stmt.setInt(1, episodio.getId());
			stmt.executeUpdate();
		}
		
		if (episodio.getConvidados() == null ||  episodio.getConvidados().isEmpty()) return;
		
		// Depois, adicionar a lista com os novos dados dos convidados.
		String sqlIns = "INSERT INTO episodio_convidado (episodio_id, nome_convidado) VALUES (?,?)";
		try (PreparedStatement stmt = conn.prepareStatement(sqlIns))
		{
			for (String convidado : episodio.getConvidados()) {
				stmt.setInt(1, episodio.getId());
				stmt.setString(2, convidado);
				stmt.executeUpdate();
			}
		}
	}
	
	/**
	 * Método auxiliar que carrega a lista de convidados do episódio em um objeto de Episodio.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param episodio Objeto de Episodio que vai receber a lista de convidados;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void carregarConvidados(Connection conn, Episodio episodio) throws SQLException {
		String sql = "SELECT nome_convidado FROM episodio_convidado WHERE episodio_id = ?";
		
		// Prepara a query a ser executada.
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, episodio.getId());
			
			try (ResultSet rs = stmt.executeQuery()) {
				// Lê os dados e adiciona o convidados à lista.
				while (rs.next()) {
					episodio.adicionarConvidado(rs.getString("nome_convidado"));
				}
			}
		}
	}
	
	/**
	 * Método auxiliar para reconstruir um objeto Episodio com os dados fornecidos do SELECT.
	 * 
	 * @param conn Conexão com o SQLite fornecida pela ConnectionFactory.
	 * @param rs ResultSet retornado pelo SELECT;
	 * @return Retorna o objeto de Episodio com os dados gerados pelal consulta;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private Episodio mapearEpisodio(Connection conn, ResultSet rs) throws SQLException {
		String dataTexto = rs.getString("lancamento");
		LocalDate lancamento = 	null;
		
		// Lógica para converter o texto com a data no banco de dados para um LocalDate.
		if (dataTexto != null && !dataTexto.isEmpty()) {
			if (dataTexto.contains("-")) {
				lancamento = LocalDate.parse(dataTexto.substring(0, 10));
			} else {
				try {
					long timestamp = Long.parseLong(dataTexto);
					lancamento = new java.sql.Date(timestamp).toLocalDate();
				} catch (Exception e) {
					lancamento = LocalDate.now();
				}
			}
		}
		
		// Instancia o episódio com os dados do ResultSet.
		Episodio epEncontrado = new Episodio(
				rs.getInt("id"),
				rs.getString("titulo"),
				rs.getInt("duracao_min"),
				rs.getInt("num_episodio"),
				lancamento
		);
		
		carregarConvidados(conn, epEncontrado);
		return epEncontrado;
	}
}