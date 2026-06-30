package dao;

import java.sql.*;  
import java.util.ArrayList;  
import java.util.List;
import model.actors.Conta;
import model.actors.Criador;

/**
 * Classe CriadorDAO para a manipulação de dados dos criadores de conteúdo.
 */
public class CriadorDAO {
	
	// Classes DAO como atributos para manipulação de dados.
	private ContaDAO contaDAO;
	private AlbumDAO albumDAO;
	private PodcastDAO podcastDAO;
	
	/**
	 * Método construtor instanciando as classes DAO.
	 */
	public CriadorDAO() {
		this.contaDAO = new ContaDAO();
		this.albumDAO = new AlbumDAO();
		this.podcastDAO = new PodcastDAO();
	}
	
	/**
	 * Método para salvar um novo criador (e seu usuário pai) de conteúdo na tabela.
	 * 
	 * @param novoCriador Objeto do tipo Criador com os seus dados;
	 * @return Retorna true se a inserção foi bem sucedida e false se não.
	 */
	public boolean salvar(Criador novoCriador) {
		if (novoCriador == null || novoCriador.getConta() == null) return false;
		
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
			
			inserirUsuario(conn, novoCriador);
			inserirCriador(conn, novoCriador);
			
			// Se der certo, aplica as mudanças.
			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao salvar criador, rollback...	" + e.getMessage());
			
			// Se der errado, tenta o Rollback da transação.
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
	 * Recuperar os dados de um criador por meio de seu ID.
	 * 
	 * @param id ID do usuário a ser consultado;
	 * @return Retorna o objeto do Criador, ou null se a consulta falhar.
	 */
	public Criador buscarId(int id) {
		// String sql com a consulta a ser executada.
		String sql = "SELECT u.id, u.nome, u.sexo, u.aniversario, u.conta_id, " +
					        "c.nome_artistico, c.biografia, c.ouvintes_mensais, c.verificado " +
					        "FROM usuario AS u JOIN criador AS c ON u.id = c.usuario_id " +
					        "WHERE u.id = ?";
		
		// Tenta estabelecer a conexão e executar a consulta.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			
			// Reconstrói o objeto procurado.
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) return mapearCriador(conn, rs);
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao consultar usuário: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Método para retornar todos os criadores de conteúdo existentes.
	 * 
	 * @return Lista de criadores registrados na tabela.
	 */
	public List <Criador> listarCriadores() {
		
		// String sql com a consulta a ser executada.
		String sql = "SELECT u.id, u.nome, u.sexo, u.aniversario, u.conta_id, " +
							"c.nome_artistico, c.biografia, c.ouvintes_mensais, c.verificado " +
							"FROM usuario AS u JOIN criador AS c ON u.id = c.usuario_id";
		
		// Lista para guardar os criadores recuperados.
		List <Criador> criadores = new ArrayList<>();
		
		// Tenta a conexão e a execução da query.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery())
		{
			// Preenche a lista com os Criadores reconstruídos.
			while (rs.next()) {
				criadores.add(mapearCriador(conn, rs));
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao listar criadores: " + e.getMessage());
		}
		return criadores;
	}
	
	/**
	 * Método para atualizar um criador (e seu usuário pai) na tabela.
	 * 
	 * @param criadorAtualizado Objeto do Criador com os dados atualizados;
	 * @return Retorna true se a operação foi bem sucedida e false se não.
	 */
	public boolean atualizar(Criador criadorAtualizado) {
		if (criadorAtualizado == null) return false;
		
		// Estabelece a conexão, mantendo o esquema do Auto Commit e do Rollback anteriores.
		Connection conn = null;
		try
		{
			conn = ConnectionFactory.getConexao();
			conn.setAutoCommit(false);
			
			modificarUsuario(conn, criadorAtualizado);
			modificarCriador(conn, criadorAtualizado);
			
			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			// Tenta o Rollback da transação.
			System.out.println("Erro ao atualizar, rollback... " + e.getMessage());
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
	 * Método pare remover um criador da tabela.
	 * 
	 * @param id ID do criador a ser removido;
	 * @return Retorna true se a operação foi bem sucedida e false se não.
	 */
	public boolean deletar(int id) {
		
		// String sql com a operação a ser executada.
		String sql = "DELETE FROM usuario WHERE id = ?";
		
		// Tenta executar a conexão e a deleção.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao deletar criador: " + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Método para inserir um usuário a partir dos dados herdados pela classe Criador.
	 * 
	 * @param conn Conexão com com SQLite via JDBC, fornecida pelo ConnectionFactory;
	 * @param criador Objeto do tipo Criador, com os dados referentes ao usuário (abstrato);
	 * @throws SQLException SQLException Se houver um erro de SQL, dispara a exceção.
	 */
	private void inserirUsuario(Connection conn, Criador criador) throws SQLException {
		String sql = "INSERT INTO usuario (nome, sexo, aniversario, conta_id) VALUES (?,?,?,?)";
		
		// Tenta a conexão com o banco de dados e a inserção do usuário.
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			stmt.setString(1, criador.getNome());
			stmt.setString(2, criador.getSexo());
			stmt.setDate(3, Date.valueOf(criador.getAniversario()));
			stmt.setInt(4, criador.getConta().getId());
			stmt.executeUpdate();
			
			// Preenche o ID do criador com o ID gerado pela query.
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) criador.setId(rs.getInt(1));
				else throw new SQLException("Falha ao gerar ID do usuário!");
			}
		}
	}
	
	/**
	 * Método para inserir um criador na tabela.
	 * 
	 * @param conn Conexão com com SQLite via JDBC, fornecida pelo ConnectionFactory;
	 * @param criador Objeto do tipo Criador com os dados a serem inseridos;
	 * @throws SQLException SQLException Se houver um erro de SQL, dispara a exceção.
	 */
	private void inserirCriador(Connection conn, Criador criador) throws SQLException {
		String sql = "INSERT INTO criador (usuario_id, nome_artistico, biografia, ouvintes_mensais, verificado) " +
					 "VALUES (?,?,?,?,?)";
		
		// Tenta estabelecer a conexão e a inserção do criador.
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, criador.getId());
			stmt.setString(2, criador.getNomeArtistico());
			stmt.setString(3, criador.getBiografia());
			stmt.setInt(4, criador.getOuvintesMensais());
			stmt.setBoolean(5, criador.isVerificado());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método para atualizar um usuário a partir dos dados herdados pela classe Criador.
	 * 
	 * @param conn Conexão com com SQLite via JDBC, fornecida pelo ConnectionFactory;
	 * @param criador objeto da classe Criador com os dados atualizados;
	 * @throws SQLException SQLException Se houver um erro de SQL, dispara a exceção.
	 */
	private void modificarUsuario(Connection conn, Criador criador) throws SQLException {
		String sql = "UPDATE usuario SET nome = ?, sexo = ?, aniversario = ? WHERE id = ?";
		
		// Estabelece a conexão com o banco de dados e atualiza o usuário.
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, criador.getNome());
			stmt.setString(2, criador.getSexo());
			stmt.setDate(3, Date.valueOf(criador.getAniversario()));
			stmt.setInt(4, criador.getId());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método para atualizar os dados de um criador de conteúdo existente.
	 * 
	 * @param conn Conexão com com SQLite via JDBC, fornecida pelo ConnectionFactory;
	 * @param criador objeto da classe Criador com os novos dados;
	 * @throws SQLException Se houver um erro de SQL, dispara a exceção.
	 */
	private void modificarCriador(Connection conn, Criador criador) throws SQLException {
		String sql = "UPDATE criador SET nome_artistico = ?, biografia = ?, ouvintes_mensais = ?, verificado = ?"
					+ "WHERE usuario_id = ?";
		
		// Estabelece a conexão com o banco de dados e atualiza a tabela do criador.
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, criador.getNomeArtistico());
			stmt.setString(2, criador.getBiografia());
			stmt.setInt(3, criador.getOuvintesMensais());
			stmt.setBoolean(4, criador.isVerificado());
			stmt.setInt(5, criador.getId());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método para reconstrutir um Criador a partir dos dados em um ResultSet de um SELECT.
	 * 
	 * @param conn Conexão com o SQLite fornecida pela ConnectionFactory;
	 * @param rs ResultSet retornado pela execução query;
	 * @return Retorna um objeto do tipo Criador com todos os dados recuperados.
	 */
	private Criador mapearCriador(Connection conn, ResultSet rs) throws SQLException {
		int contaId = rs.getInt("conta_id");
		Conta conta = contaDAO.buscarId(contaId);
		
		// Instancia o criador com os dados do ResultSet.
		Criador criadorEncontrado = new Criador(
				conta, 
				rs.getInt("id"), 
				rs.getString("nome"), 
				rs.getString("sexo"),
				rs.getDate("aniversario").toLocalDate(),
				rs.getString("nome_artistico"),
				rs.getString("biografia"),
				rs.getInt("ouvintes_mensais"),
				rs.getBoolean("verificado")
		);
		
		// Carrega a discografia e os podcasts do criador.
		criadorEncontrado.getDiscografia().addAll(albumDAO.buscarPorCriador(conn, criadorEncontrado.getId()));
		criadorEncontrado.getPodcasts().addAll(podcastDAO.buscarPorCriador(conn, criadorEncontrado.getId()));
		
		return criadorEncontrado;
	}
}