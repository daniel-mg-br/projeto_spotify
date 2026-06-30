package dao;

import java.sql.*; 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.content.Podcast;
import model.content.Episodio;

/**
 * Classe PodcastDAO para manipulação de podcasts registrados no banco de dados.
 */
public class PodcastDAO {
	
	// EpisodioDAO como atributo para manipulação de dados.
	EpisodioDAO episodioDAO;
	
	/**
	 * Método Construtor instanciando a DAO.
	 */
	public PodcastDAO() {
		this.episodioDAO = new EpisodioDAO();
	}
	
   /**
    * Salvar um novo podcast no banco.
    * 
    * @param novoPodcast Objeto de Podcast com os dados do novo podcast;
    * @return Retorna true se a operação foi bem sucedida, ou false se não.
    */
	public boolean salvar(Podcast novoPodcast) {
		if (novoPodcast == null) return false;
		
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

			inserirPodcast(conn, novoPodcast);
			sincronizarEps(conn, novoPodcast); 
			
			// Se der certo, aplica as mudanças.
			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			// Se der errado, tenta o Rollback da transação.
			System.out.println("Erro ao salvar podcast, rollback... " + e.getMessage());
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
    * Recuperar dados de um podcast pelo seu ID.
    * 
    * @param id ID do Podcast a ser consultado;
    * @return Retorna o objeto de Podcast com os dados requeridos, ou null de a operação falhar.
    */
	public Podcast buscarId(int id) {
		
		// String sql com a consulta a ser executada.
		String sql = "SELECT id, nome, tema, criacao, criador_id "
				+ "FROM podcast WHERE id = ?";
		
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setInt(1, id);
			
			// Lê a linha com os dados e mapeia o objeto de Podcast.
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Podcast podcastEncontrado = mapearPodcast(conn, rs);
					return podcastEncontrado;
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao buscar podcast: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Lista todos os podcasts pertencentes a um criador.
	 * 
	 * @param conn Conexão com SQLite fornecida pela ConnectionFactory.
	 * @param criadorId ID do criador em questão;
	 * @return Retorna uma lista com todos os podcasts registrados do criador.
	 */
	public List <Podcast> buscarPorCriador(Connection conn, int criadorId) {
		
		// String sql com a consulta a ser executada.
		String sql = "SELECT id, nome, tema, criacao, criador_id "
				+ "FROM podcast WHERE criador_id = ?";
		
		// Lista para armazenar os podcasts.
		List <Podcast> podcasts = new ArrayList<>();
		
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, criadorId);
			
			// Tenta ler os dados do ResultSet e preenche a lista com os objetos de Podcast recuperados.
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Podcast p = mapearPodcast(conn, rs);
					podcasts.add(p);
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao buscar podcasts: " + e.getMessage());
		}
		return podcasts;
	}
		
   /**
    * Recuperar os dados de todos os podcasts registrados.
    * 
    * @return Retorna uma lista com os podcasts do sistema.
    */
	public List <Podcast> listarPodcasts() {
		
		// String sql com a consulta a ser executada.
		String sql = "SELECT id, nome, tema, criacao, criador_id "
				+ "FROM podcast";
		
		// Lista para armazenar os podcasts recuperados.
		List <Podcast> podcasts = new ArrayList<>();
		
		// Tenta estabelecer a conexão e executar a consulta.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery())
		{
			// Itera pelo ResultSet e preenche a lista de podcasts.
			while (rs.next()) {
				Podcast p = mapearPodcast(conn, rs);
				podcasts.add(p);
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao listar podcasts: " + e.getMessage());
		}
		return podcasts;
	}
		
   /**
    * Atualizar os dados de um podcast existente.
    * 
    * @param podcastAtualizado Objeto de Podcast com os novos dados;
    * @return Retorna true se a operação foi bem sucedida, ou false se não.
    */
	public boolean atualizar(Podcast podcastAtualizado) {
		if (podcastAtualizado == null) return false;
		
		// Estabelece a conexão, seguindo o esquema de Auto Commit e Rollback anteriores.
		Connection conn = null;
		try
		{
			conn = ConnectionFactory.getConexao();
			conn.setAutoCommit(false); 

			modificarPodcast(conn, podcastAtualizado);
			sincronizarEps(conn, podcastAtualizado); 

			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			// Se der errado, tenta o Rollback da transação.
			System.out.println("Erro ao atualizar podcast, rollback... " + e.getMessage());
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
				try {conn.setAutoCommit(true); 
					 conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return false;
	}
		
   /**
    * Remover um podcast existente no banco.
    * 
    * @param id ID do podcast a ser removido;
    * @return Retorna true se a operação foi bem sucedida, ou false se não.
    */
	public boolean deletar(int id) {
		
		// String sql com a deleção a ser executada.
		String sql = "DELETE FROM podcast WHERE id = ?";
		
		// Tenta a conexão com banco de dados e a operação.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao deletar podcast: " + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Método auxiliar para inserir um novo podcast ao banco de dados.
	 * 
	 * @param conn Conexão do JDBC com SQLite, fornecida pela ConnectionFactory;
	 * @param podcast Objeto de Podcast com os dados do novo podcast;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void inserirPodcast(Connection conn, Podcast podcast) throws SQLException {
		String sql = "INSERT INTO podcast (nome, tema, criacao, criador_id) VALUES (?,?,?,?)";
		
		// Tenta estabelecer a conexão e inserir o podcast.
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			stmt.setString(1, podcast.getNome());
			stmt.setString(2, podcast.getTema());
			stmt.setDate(3, Date.valueOf(podcast.getCriacao()));
			stmt.setInt(4, podcast.getCriadorId());
			stmt.executeUpdate();
			
			// Preenche o ID do podcast com o ID gerado pela query.
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) podcast.setId(rs.getInt(1));
				else throw new SQLException("Falha ao gerar ID do podcast!");
			}
		}
	}
	
	/**
	 * Método auxiliar para alterar os dados de um podcast existente.
	 * 
	 * @param conn Conexão do JDBC com SQLite, fornecida pela ConnectionFactory;
	 * @param podcast Objeto de Podcasts com os novos dados a serem salvos;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void modificarPodcast(Connection conn, Podcast podcast) throws SQLException {
		String sql = "UPDATE podcast SET nome = ?, tema = ?, criacao = ?, criador_id = ? "
				+ " WHERE id = ?";
		
		// Tenta estabelecer a conexão e atualizar a tabela.
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, podcast.getNome());
			stmt.setString(2, podcast.getTema());
			stmt.setDate(3, Date.valueOf(podcast.getCriacao()));
			stmt.setInt(4, podcast.getCriadorId());
			stmt.setInt(5, podcast.getId());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para atualizar dados de episódios dos podcasts.
	 * 
	 * @param conn Conexão do JDBC com SQLite, fornecida pela ConnectionFactory;
	 * @param podcast Objeto de Podcast com os dados de episódios a serem salvos;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void sincronizarEps(Connection conn, Podcast podcast) throws SQLException {
		
		// Primeiro, retira os IDs antigos dos podcasts associados aos episódios.
		String sqlDesvinc = "UPDATE episodio SET podcast_id = NULL WHERE podcast_id = ?";
		try (PreparedStatement stmtDes = conn.prepareStatement(sqlDesvinc)) {
			stmtDes.setInt(1, podcast.getId());
			stmtDes.executeUpdate();
		}
		
		if (podcast.getEpisodios() == null || podcast.getEpisodios().isEmpty()) return;
		
		// Depois, insere os novos IDs dos podcasts.
		String sqlVinc = "UPDATE episodio SET podcast_id = ? WHERE conteudo_id = ?";
		try (PreparedStatement stmtVin = conn.prepareStatement(sqlVinc)) {
			for (Episodio ep : podcast.getEpisodios()) {
				stmtVin.setInt(1, podcast.getId());  
				stmtVin.setInt(2, ep.getId());       
				stmtVin.executeUpdate();
			}
		}
	}
	
	/**
	 * Método auxiliar para recuperar os episódios de um podcast registrado.
	 * 
	 * @param conn Conexão do JDBC com SQLite, fornecida pela ConnectionFactory;
	 * @param podcast Objeto de Podcast com os dados dos episódios;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void carregarEps(Connection conn, Podcast podcast) throws SQLException {
		String sql = "SELECT conteudo_id FROM episodio WHERE podcast_id = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, podcast.getId());
			
			// Carrega os episódios do banco de dados para a lista.
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int episodioId = rs.getInt("conteudo_id");
					Episodio ep = episodioDAO.buscarId(episodioId);
					if (ep != null) podcast.adicionarEp(ep);
				}
			}
		}
	}
	
	/**
	 * Método auxiliar para reconstruir um objeto de Podcast com os dados da consulta.
	 * 
	 * @param conn Conexão com SQLite fornecida pela ConnectionFactory.
	 * @param rs ResultSet com os dados, gerado pelo SELECT;
	 * @return Retorna um objeto de Podcast com os dados completos;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private Podcast mapearPodcast(Connection conn, ResultSet rs) throws SQLException {
		Date dataBanco = rs.getDate("criacao");
		LocalDate criacao = (dataBanco != null) ? dataBanco.toLocalDate() : null;
		
		// Instancia o podcast com os dados do ResultSet.
		Podcast podcastEncontrado = new Podcast (
				rs.getInt("id"),
				rs.getString("nome"),
				rs.getString("tema"),
				rs.getInt("criador_id"),
				criacao
		);
		
		carregarEps(conn, podcastEncontrado);
		return podcastEncontrado;
	}
}