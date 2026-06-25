package dao;

import java.sql.*; 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.content.Podcast;
import model.content.Episodio;

/**
 * Classe DAO para manipulação de podcasts registrados no banco de dados.
 */
public class PodcastDAO {
	EpisodioDAO episodioDAO;
	
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
		
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConexao();
			conn.setAutoCommit(false); 

			inserirPodcast(conn, novoPodcast);
			sincronizarEps(conn, novoPodcast); 

			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao salvar podcast, rollback... " + e.getMessage());
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
    * Recuperar dados de um podcast pelo seu ID.
    * 
    * @param id ID do Podcast a ser consultado;
    * @return Retorna o objeto de Podcast com os dados requeridos, ou null de a operação falhar.
    */
	public Podcast buscarId(int id) {
		String sql = "SELECT id, nome, tema, criacao, criador_id "
				+ "FROM podcast WHERE id = ?";
		
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setInt(1, id);
			
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
	 * @param criadorId ID do criador em questão;
	 * @return Retorna uma lista com todos os podcasts registrados do criador.
	 */
	public List <Podcast> buscarPorCriador(Connection conn, int criadorId) {
		String sql = "SELECT id, nome, tema, criacao, criador_id "
				+ "FROM podcast WHERE criador_id = ?";
		
		List <Podcast> podcasts = new ArrayList<>();
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, criadorId);
			
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
		String sql = "SELECT id, nome, tema, criacao, criador_id "
				+ "FROM podcast";
		
		List <Podcast> podcasts = new ArrayList<>();
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery())
		{
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
			System.out.println("Erro ao atualizar podcast, rollback... " + e.getMessage());
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
    * Remover um podcast existente no banco.
    * 
    * @param id ID do podcast a ser removido;
    * @return Retorna true se a operação foi bem sucedida, ou false se não.
    */
	public boolean deletar(int id) {
		String sql = "DELETE FROM podcast WHERE id = ?";
		
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
		
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			stmt.setString(1, podcast.getNome());
			stmt.setString(2, podcast.getTema());
			stmt.setDate(3, Date.valueOf(podcast.getCriacao()));
			stmt.setInt(4, podcast.getCriadorId());
			stmt.executeUpdate();
			
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
		String sqlDesvinc = "UPDATE episodio SET podcast_id = NULL WHERE podcast_id = ?";
		try (PreparedStatement stmtDes = conn.prepareStatement(sqlDesvinc)) {
			stmtDes.setInt(1, podcast.getId());
			stmtDes.executeUpdate();
		}
		
		if (podcast.getEpisodios() == null || podcast.getEpisodios().isEmpty()) return;
		
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
	 * @param rs ResultSet com os dados, gerado pelo SELECT;
	 * @return Retorna um objeto de Podcast com os dados completos;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private Podcast mapearPodcast(Connection conn, ResultSet rs) throws SQLException {
		Date dataBanco = rs.getDate("criacao");
		LocalDate criacao = (dataBanco != null) ? dataBanco.toLocalDate() : null;
		
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
