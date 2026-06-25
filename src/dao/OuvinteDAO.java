package dao;

import java.sql.*;  
import java.util.ArrayList;  
import java.util.List;
import model.actors.Ouvinte;
import model.actors.Conta;
import model.content.Album;
import model.content.Podcast;

/**
 * Classe OuvinteDAO para manipulação de dados do ouvinte.
 */
public class OuvinteDAO {
	
	// Instanciando o ContaDAO para ajudar a reconstruir o objeto nas buscas
	// Instanciando os DAOs dos agrupadores para manipulação de dados das relações de conteúdos favoritos.
	private ContaDAO contaDAO;
	private PlaylistDAO playlistDAO;
	private AlbumDAO albumDAO;
	private PodcastDAO podcastDAO;
	
	public OuvinteDAO() {
		this.contaDAO = new ContaDAO();
		this.playlistDAO = new PlaylistDAO();
		this.albumDAO = new AlbumDAO();
		this.podcastDAO = new PodcastDAO();
	}
	
	/**
	 * Método para salvar um novo usuário juntamente com o novo ouvinte associado.
	 * Utiliza o método auxiliar para inserção das duas entidades.
	 * 
	 * @param novoOuvinte Objeto do tipo ouvinte contendo os dados a serem inseridos;
	 * @return Retorna true se a operação foi bem sucessida e false se não.
	 */
	public boolean salvar(Ouvinte novoOuvinte) {
		if (novoOuvinte == null || novoOuvinte.getConta() == null) return false;
		
		Connection conn = null;
		try 
		{
			conn = ConnectionFactory.getConexao();
			conn.setAutoCommit(false);
			
			inserirUsuario(conn, novoOuvinte);
			inserirOuvinte(conn, novoOuvinte);
			sincronizarAlbunsFavoritos(conn, novoOuvinte);
			sincronizarPodcastsFavoritos(conn, novoOuvinte);
			
			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao salvar ouvinte. Rollback... " + e.getMessage());
			
			try { 
				if (conn != null) conn.rollback();
			} 
			catch (SQLException ex) {
				System.out.println(e.getMessage());
			}
		}
		finally
		{
			if (conn != null) {
				try {conn.setAutoCommit(true); conn.close();} catch(SQLException e) { e.printStackTrace();}
			}
		}
		return false;
	}
	
	/**
	 * Método para consultar os dados de um Ouvinte pelo ID.
	 * 
	 * @param id ID do ouvinte em questão;
	 * @return Objeto do tipo ouvinte contendo os dados requisitados, ou null em caso de falha.
	 */
	public Ouvinte buscarId(int id) {
		String sql = "SELECT u.id, u.nome, u.sexo, u.aniversario, u.conta_id, " +
							 "o.total_minutos, o.genero_favorito " +
							 "FROM usuario AS u JOIN ouvinte AS o ON u.id = o.usuario_id " +
					 		 "WHERE u.id = ?";
		
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) return mapearOuvinte(conn, rs);
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao buscar ouvinte: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Método para listar todos os ouvintes da tabela.
	 * 
	 * @return Uma lista com todos os ouvintes
	 */
	public List <Ouvinte> listarOuvintes() {
		String sql = "SELECT u.id, u.nome, u.sexo, u.aniversario, u.conta_id, " +
					         "o.total_minutos, o.genero_favorito " +
					         "FROM usuario AS u JOIN ouvinte AS o ON u.id = o.usuario_id";
		
		List <Ouvinte> ouvintes = new ArrayList<>();
		
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql);
		     ResultSet rs = stmt.executeQuery())
		{
			while (rs.next()) {
				ouvintes.add(mapearOuvinte(conn, rs));
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao listar ouvintes: " + e.getMessage());
		}
		return ouvintes;
	}
	
	/**
	 * Método para atualizar os dados de um usuário e o Ouvinte associado a ele.
	 * 
	 * @param ouvinteAtualizado Objeto do tipo ouvinte com os dados novos;
	 * @return Retorna true se a atualização foi bem sucedida ou false se não.
	 */
	public boolean atualizar(Ouvinte ouvinteAtualizado) {
		if (ouvinteAtualizado == null) return false;
		
		Connection conn = null;
		try 
		{
			conn = ConnectionFactory.getConexao();
			conn.setAutoCommit(false);
			
			modificarUsuario(conn, ouvinteAtualizado);
			modificarOuvinte(conn, ouvinteAtualizado);
			sincronizarAlbunsFavoritos(conn, ouvinteAtualizado);
			sincronizarPodcastsFavoritos(conn, ouvinteAtualizado);
			
			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao atualizar ouvinte. Rollback..." + e.getMessage());
			
			try {
				if (conn != null) conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		finally
		{
			if (conn != null) {
				try { conn.setAutoCommit(true); conn.close();} catch (SQLException e) {e.printStackTrace();}
			}
		}
		return false;
	}
	
	/**
	 * Método para remover um ouvinte da tabela.
	 * 
	 * @param id Id do ouvinte a ser removido.
	 * @return Retorna true se a operação foi bem sucedida e false se não.
	 */
	public boolean deletar(int id) {
		String sql = "DELETE FROM usuario WHERE id = ?";
		
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao deletar ouvinte: " + e.getMessage());
		}
		return false;
	}
	
	// MÉTODOS AUXILIARES PARA MANTEREM O CÓDIGO LIMPO
	
	/**
	 * Método auxiliar para inserir ouvintes na tabela do banco de dados	
	 * 
	 * @param conn A conexão JDBC com o SQLite fornecida pela ConnectionFactory;
	 * @param ouvinte Objeto do tipo Ouvinte para manipulação de dados;
	 * @throws SQLException Em caso de erro de SQL será disparada uma exceção.
	 */
	private void inserirUsuario(Connection conn, Ouvinte ouvinte) throws SQLException {
		String sql = "INSERT INTO usuario (nome, sexo, aniversario, conta_id) VALUES (?,?,?,?)";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			stmt.setString(1, ouvinte.getNome());
			stmt.setString(2, ouvinte.getSexo());
			stmt.setDate(3, Date.valueOf(ouvinte.getAniversario()));
			stmt.setInt(4, ouvinte.getConta().getId());
			stmt.executeUpdate();
			
			try (ResultSet rs = stmt.getGeneratedKeys())
			{
				if (rs.next()) {
					ouvinte.setId(rs.getInt(1));
				} else {
					throw new SQLException("Falha ao gerar ID do usuário!");
				}
			}
		}
	}
	
	/**
	 * Método auxiliar para a inserção de um ouvinte.
	 * 
	 * @param conn A conexão JDBC com o SQLite fornecida pela ConnectionFactory;
	 * @param ouvinte Objeto do tipo ouvinte com os dados a serem inseridos;
	 * @throws SQLException A conexão JDBC com o SQLite fornecida pela ConnectionFactory;
	 */
	private void inserirOuvinte(Connection conn, Ouvinte ouvinte) throws SQLException {
		String sql = "INSERT INTO ouvinte (usuario_id, total_minutos, genero_favorito) VALUES (?,?,?)";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, ouvinte.getId());
			stmt.setInt(2, ouvinte.getTotalMinutos());
			stmt.setString(3, ouvinte.getGeneroFavorito());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para a a atualização de dados do usuário.
	 * 
	 * @param conn A conexão JDBC com o SQLite fornecida pela ConnectionFactory;
	 * @param ouvinte Objeto do tipo Ouvinte com os dados novos;
	 * @throws SQLException A conexão JDBC com o SQLite fornecida pela ConnectionFactory.
	 */
	private void modificarUsuario(Connection conn, Ouvinte ouvinte) throws SQLException {
		String sql = "UPDATE usuario SET nome = ?, sexo = ?, aniversario = ? WHERE id = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, ouvinte.getNome());
			stmt.setString(2, ouvinte.getSexo());
			stmt.setDate(3, Date.valueOf(ouvinte.getAniversario()));
			stmt.setInt(4, ouvinte.getId());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para atualizar os dados do ouvinte na tabela.
	 * 
	 * @param conn A conexão JDBC com o SQLite fornecida pela ConnectionFactory;
	 * @param ouvinte Objeto do tipo Ouvinte com os dados novos do ouvinte;
	 * @throws SQLException Em caso de erro de SQL será disparada uma exceção.
	 */
	private void modificarOuvinte(Connection conn, Ouvinte ouvinte) throws SQLException {
		String sql = "UPDATE ouvinte SET total_minutos = ?, genero_favorito = ? WHERE usuario_id = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, ouvinte.getTotalMinutos());
			stmt.setString(2, ouvinte.getGeneroFavorito());
			stmt.setInt(3, ouvinte.getId());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para atualizar os dados dos álbuns favoritos dos ouvintes.
	 * 
	 * @param conn A conexão JDBC com o SQLite fornecida pela ConnectionFactory;
	 * @param ouvinte Objeto de ouvinte com os dados de suas playlists favoritas;
	 * @throws SQLException Em caso de erro de SQL será disparada uma exceção.
	 */
	private void sincronizarAlbunsFavoritos(Connection conn, Ouvinte ouvinte) throws SQLException {
		String sqlDel = "DELETE FROM ouvinte_album_favorito WHERE ouvinte_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sqlDel)) 
		{
			stmt.setInt(1, ouvinte.getId());
			stmt.executeUpdate();
		}
		
		if (ouvinte.getAlbunsFavoritos() == null || ouvinte.getAlbunsFavoritos().isEmpty()) return;
		
		String sqlIns = "INSERT INTO ouvinte_album_favorito (ouvinte_id, album_id) VALUES (?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sqlIns))
		{
			for (Album a : ouvinte.getAlbunsFavoritos()) {
				stmt.setInt(1, ouvinte.getId());
				stmt.setInt(2, a.getId());
				stmt.executeUpdate();
			}
		}	
	}
	
	/**
	 * Método auxiliar para recuperar os dados dos álbuns favoritos do ouvinte.
	 * 
	 * @param ouvinte Objeto de ouvinte com os dados dos álbuns favoritos;
	 * @throws SQLException Em caso de erro de SQL será disparada uma exceção.
	 */
	private void carregarAlbunsFavoritos(Connection conn, Ouvinte ouvinte) throws SQLException {
		String sql = "SELECT album_id FROM ouvinte_album_favorito WHERE ouvinte_id = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql)) 
		{
			stmt.setInt(1, ouvinte.getId());
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Album a = albumDAO.buscarId(rs.getInt("album_id"));
					if (a != null) ouvinte.adicionarAlbumFav(a);
				}
			}
		}
	}
	
	/**
	 * Método auxiliar para atualizar os dados dos podcasts favoritos do ouvinte.
	 * 
	 * @param conn A conexão JDBC com o SQLite fornecida pela ConnectionFactory;
	 * @param ouvinte Objeto de ouvinte com os dados dos podcasts favoritos;
	 * @throws SQLException Em caso de erro de SQL será disparada uma exceção.
	 */
	private void sincronizarPodcastsFavoritos(Connection conn, Ouvinte ouvinte) throws SQLException {
		String sqlDel = "DELETE FROM ouvinte_podcast_favorito WHERE ouvinte_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sqlDel)) 
		{
			stmt.setInt(1, ouvinte.getId());
			stmt.executeUpdate();
		}
		
		if (ouvinte.getPodcastsFavoritos() == null || ouvinte.getPodcastsFavoritos().isEmpty()) return;
		
		String sqlIns = "INSERT INTO ouvinte_podcast_favorito (ouvinte_id, podcast_id) VALUES (?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sqlIns))
		{
			for (Podcast p : ouvinte.getPodcastsFavoritos()) {
				stmt.setInt(1, ouvinte.getId());
				stmt.setInt(2, p.getId());
				stmt.executeUpdate();
			}
		}
	}
	
	/**
	 * Método auxiliar para recuperar os dados dos podcasts favoritos do ouvinte.
	 * 
	 * @param ouvinte Objeto de ouvinte com os dados dos podcasts favoritos;
	 * @throws SQLException Em caso de erro de SQL será disparada uma exceção.
	 */
	private void carregarPodcastsFavoritos(Connection conn, Ouvinte ouvinte) throws SQLException {
		String sql = "SELECT podcast_id FROM ouvinte_podcast_favorito WHERE ouvinte_id = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql)) 
		{
			stmt.setInt(1, ouvinte.getId());
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Podcast p = podcastDAO.buscarId(rs.getInt("podcast_id"));
					if (p != null) ouvinte.adicionarPodcastFav(p);
				}
			}
		}
	}
	
	/**
	 * Método auxiliar para auxiliar a reconstrução do ouvinte para recuperação de dados.
	 * 
	 * @param rs Result Set relacionado ao retorno de linhas da tabela em um SELECT
	 * @return Objeto do tipo ouvinte, recriado com os dados da tabela
	 * @throws SQLException	Em caso de erro de SQL será disparada uma exceção.
	 */
	private Ouvinte mapearOuvinte(Connection conn, ResultSet rs) throws SQLException {
		int contaId = rs.getInt("conta_id");
		Conta conta = contaDAO.buscarId(contaId);
		
		Ouvinte ouvinteEncontrado = new Ouvinte(
				conta,
				rs.getInt("id"),
				rs.getString("nome"),
				rs.getString("sexo"),
				rs.getDate("aniversario").toLocalDate(),
				rs.getInt("total_minutos"),
				rs.getString("genero_favorito")
		);
		
		ouvinteEncontrado.getPlaylists().addAll(playlistDAO.buscarPorUsuario(conn, ouvinteEncontrado.getId()));
		carregarAlbunsFavoritos(conn, ouvinteEncontrado);
		carregarPodcastsFavoritos(conn, ouvinteEncontrado);
		
		return ouvinteEncontrado;
	}
}
