package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.content.Playlist;
import model.content.Musica;

/**
 * Classe PlaylistDAO para manipulação de playlists do banco.
 */
public class PlaylistDAO {
	
	// MusicaDAO como atributo para manipulação de dados.
	private MusicaDAO musicaDAO;
	
	/**
	 * Método Construtor instanciando a classe DAO.
	 */
	public PlaylistDAO() {
		this.musicaDAO = new MusicaDAO();
	}
	
	/**
	 * Salvar uma nova playlist no banco.
	 * 
	 * @param novaPlaylist ;
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean salvar(Playlist novaPlaylist) {
		if (novaPlaylist == null) return false;
		
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

			inserirPlaylist(conn, novaPlaylist);
			sincronizarMusicas(conn, novaPlaylist); 
			
			// Se der certo, aplica as mudanças.
			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			// Se der errado, tenta o Rollback da transação.
			System.out.println("Erro ao salvar playlist, rollback... " + e.getMessage());
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
	 * Buscar uma playlist pelo ID.
	 * 
	 * @param id ID da música a ser consultada;
	 * @return Retorna o objeto de Musica com os dados, ou null se a consulta falhar.
	 */
	public Playlist buscarId(int id) {
		
		// String sql com a consulta a ser executada. 
		String sql = "SELECT id, titulo, descricao, compartilhar, criacao, ouvinte_id " +
					 "FROM playlist WHERE id = ?";
		
		// Tenta a conexão com o banco de dados e a execução da consulta.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			
			// Recupera o objeto com os dados do ResultSet.
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Playlist playlistEncontrada = mapearPlaylist(conn, rs);
					return playlistEncontrada;
				}
			}
		}
		catch (SQLException e) 
		{
			System.out.println("Erro ao buscar playlist: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Recuperar os dados das playlists do banco de dados.
	 * 
	 * @return Retorna a lista com todas as playlists registradas.
	 */
	public List <Playlist> listarPlaylists() {
		
		// String sql com a consulta a ser executada.
		String sql = "SELECT id, titulo, descricao, compartilhar, criacao, ouvinte_id "
				   + " FROM playlist";
		
		// Lista para guardar as playlists recuperadas.
		List <Playlist> playlists = new ArrayList<>();
		
		// Tenta a conexão com o banco de dados e a execução da consulta.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery())
		{
			
			// Itera pelo ResultSet preenchendo a lista com as playlists reconstruídas.
			while (rs.next()) {
				Playlist p = mapearPlaylist(conn, rs);
				playlists.add(p);
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao listar playlists: " + e.getMessage());
		}
		return playlists;
	}
	
	/**
	 * Recuperar os dados das playlists de um determinado ouvinte.
	 * 
	 * @param usuarioId ID do ouvinte proprietário das playists;
	 * @return Retorna a lista de playlists do ouvinte.
	 */
	public List <Playlist> buscarPorUsuario(Connection conn, int usuarioId) {
		
		// String sql com a consulta a ser executada.
		String sql = "SELECT id, titulo, descricao, compartilhar, criacao, ouvinte_id "
				   + "FROM playlist WHERE ouvinte_id = ?";
		
		// Lista para guardar as playlists reconstruídas.
		List <Playlist> playlists = new ArrayList<>();
		
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, usuarioId);
			
			// Tenta ler os dados do ResultSet e preenche a lista com as playlists recuperadas
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Playlist p = mapearPlaylist(conn, rs);
					playlists.add(p);
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao listar playlists: " + e.getMessage());
		}
		return playlists;
	}
	
	/**
	 * Atualizar uma playlist já existente.
	 * 
	 * @param playlistAtualizada Objeto de Playlist com os novos dados a serem salvos;
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean atualizar(Playlist playlistAtualizada) {
		if (playlistAtualizada == null) return false;
		
		// Estabelece a conexão, mantendo o esquema com o Auto Commit e Rollback anteriores.
		Connection conn = null;
		try
		{
			conn = ConnectionFactory.getConexao();
			conn.setAutoCommit(false);

			modificarPlaylist(conn, playlistAtualizada);
			sincronizarMusicas(conn, playlistAtualizada);

			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			// Tenta o Rollback da transação.
			System.out.println("Erro ao atualizar playlist, rollback... " + e.getMessage());
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
	 * Remover uma playlist existente do banco.
	 * 
	 * @param id ID da playlist a ser removida;
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean deletar(int id) {
		String sql = "DELETE FROM playlist WHERE id = ?";
		
		// Tenta a conexão com banco de dados e a deleção.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao deletar playlist: " + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Método auxiliar para inserir um novo álbum ao banco de dados.
	 * 
	 * @param conn Conexão JDBC com SQLite, fornecida pela ConnectionFactory;
	 * @param playlist Objeto de Playlist, com os dados a serem inseridos;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void inserirPlaylist(Connection conn, Playlist playlist) throws SQLException {
		String sql = "INSERT INTO playlist (titulo, descricao, compartilhar, criacao, ouvinte_id) "
				+ "VALUES (?,?,?,?,?)";
		
		// Tenta a conexão com o banco de dados e a inserção da playlist.
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			stmt.setString(1, playlist.getTitulo());
			stmt.setString(2, playlist.getDescricao());
			stmt.setBoolean(3, playlist.isCompartilhar());
			stmt.setDate(4, Date.valueOf(playlist.getCriacao()));
			stmt.setInt(5, playlist.getUsuarioId());
			stmt.executeUpdate();
			
			// Preenche o ID da playlist nova com o ID automático da query.
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) playlist.setId(rs.getInt(1));
				else throw new SQLException("Falha ao gerar ID da playlist");
			}
		}
	}
	
	/**
	 * Método auxiliar para modificar os dados de um álbum existente.
	 * 
	 * @param conn Conexão JDBC com SQLite, fornecida pela ConnectionFactory;
	 * @param playlist Objeto de Playlists com os novos dados a serem salvos;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void modificarPlaylist(Connection conn, Playlist playlist) throws SQLException {
		String sql = "UPDATE playlist SET titulo = ?, descricao = ?, compartilhar = ?, criacao = ?, ouvinte_id = ? "
				   + "WHERE id = ?";
		
		// Tenta estabelecer a conexão e atualizar a tabela.
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, playlist.getTitulo());
			stmt.setString(2, playlist.getDescricao());
			stmt.setBoolean(3, playlist.isCompartilhar());
			stmt.setDate(4, Date.valueOf(playlist.getCriacao()));
			stmt.setInt(5, playlist.getUsuarioId());
			stmt.setInt(6, playlist.getId());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para manter as músicas da playlist atualizadas.
	 * 
	 * @param conn Conexão JDBC com SQLite, fornecida pela ConnectionFactory;
	 * @param playlist Objeto de Playlist com os dados das músicas a serem atualizadas;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void sincronizarMusicas(Connection conn, Playlist playlist) throws SQLException {
		
		// Primeiro, deleta as músicas existentes na lista (antigas).
		String sqlDel = "DELETE FROM playlist_musica WHERE playlist_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sqlDel))
		{
			stmt.setInt(1, playlist.getId());
			stmt.executeUpdate();
		}
		
		if (playlist.getMusicas() == null || playlist.getMusicas().isEmpty()) return;
		
		// Segundo, reinsere as músicas, incluindo as novas.
		String sqlIns = "INSERT INTO playlist_musica (playlist_id, musica_id) VALUES (?,?)";
		try (PreparedStatement stmt = conn.prepareStatement(sqlIns))	
		{
			for (Musica m : playlist.getMusicas()) {
				stmt.setInt(1, playlist.getId());
				stmt.setInt(2, m.getId());
				stmt.executeUpdate();
			}
		}
	}
	
	/**
	 * Método auxiliar para recuperar as músicas de um álbum.
	 * 
	 * @param conn Conexão JDBC com SQLite, fornecida pela ConnectionFactory;
	 * @param playlist Objeto de Playlist com os dados das músicas a serem carregados;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private void carregarMusicas(Connection conn, Playlist playlist) throws SQLException {
		String sql = "SELECT musica_id FROM playlist_musica WHERE playlist_id = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, playlist.getId());
			
			// Tenta ler o ResultSet e carregar as músicas a partir dos dados retornados.
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int musicaId = rs.getInt("musica_id");
					Musica m =	musicaDAO.buscarId(musicaId);
					if (m != null) playlist.adicionarMusica(m);
				}
			}
		}
	}
	
	/**
	 * Método auxiliar para a reconstrução de um objeto a partir dos dados da consulta.
	 * 
	 * @param conn Conexão com SQLite fornecida pela ConnectionFactory.
	 * @param rs ResultSet com os dados da playlist gerado pelo SELECT;
	 * @throws SQLException Caso haja erro de SQL, dispara uma exceção.
	 */
	private Playlist mapearPlaylist(Connection conn, ResultSet rs) throws SQLException {
		Date dataBanco = rs.getDate("criacao");
		LocalDate criacao = (dataBanco != null) ? dataBanco.toLocalDate() : null;

		// Instancia a playlist a partir dos dados do ResultSet.
		Playlist playlistEncontrada = new Playlist(
				rs.getInt("id"),
				rs.getInt("ouvinte_id"),
				rs.getString("titulo"),
				rs.getString("descricao"),
				rs.getBoolean("compartilhar"),
				criacao
		);
		
		carregarMusicas(conn, playlistEncontrada);
		return playlistEncontrada;
	}
}
