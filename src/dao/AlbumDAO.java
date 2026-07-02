package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List; 	
import model.content.Album;
import model.content.Musica;

/*
 * Classe AlbumDAO para manipulação de dados dos álbuns do banco.
 */
public class AlbumDAO {
	
	// ClasseDAO como atributo para manipulação de dados.
	private MusicaDAO musicaDAO;
	
	/**
	 * Método Construtor instanciando a DAO.
	 */
	public AlbumDAO() {
		this.musicaDAO = new MusicaDAO();
	}
	
	/**
	 * Salvar um novo álbum no banco.
	 * 
	 * @param novoAlbum Objeto de Album com os dados do novo álbum;
	 * @return Retorna true se a operação é bem sucedida, ou false se não.
	 */
	public boolean salvar(Album novoAlbum) {
		if (novoAlbum == null) return false;
		
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

			inserirAlbum(conn, novoAlbum);
			sincronizarMusicas(conn, novoAlbum);
			
			// Se der certo, aplica as mudanças.
			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao salvar álbum, rollback..." + e.getMessage());
			
			// Se não der certo, tenta o Rollback da transação.
			try {
				if (conn != null) conn.rollback();
			} catch (SQLException ex) {
				System.out.println("Erro no rollback: " + ex.getMessage());
			}
		}
		finally
		{
			// Habilita o Auto Commit e fecha a conexeção.
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
	 * Buscar um álbum existente pelo seu ID.
	 * 
	 * @param id ID do álbum a ser consultado;
	 * @return Retorna o objeto de Album com os dados requeridos.
	 */
	public Album buscarId(int id) {
		
		// String sql com a consulta a ser executada.
		String sql = "SELECT id, titulo, tipo, status, lancamento, criador_id FROM album WHERE id = ?";
		
		// Try With Resources para garantir que a conexão seja fechada em caso de erro.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			
			// Reconstrói o objeto do Album com os dados retornados pelo ResultSet.
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Album albumEncontrado = mapearAlbum(conn, rs);
					return albumEncontrado;
				}
			}
		} 
		catch (SQLException e)
		{
			System.out.println("Erro ao buscar álbum: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Lista todos os álbuns pertencentes a um criador.
	 * 
	 * @param conn Conexão com SQLite fornecida pela ConnectionFactory.
	 * @param criadorId ID do criador em questão;
	 * @return Retorna uma lista com todos os álbuns registrados do criador.
	 */
	public List <Album> buscarPorCriador(Connection conn, int criadorId) {
		
		// String sql com a consulta a ser executada.
		String sql = "SELECT id, titulo, tipo, status, lancamento, criador_id "
				+ "FROM album WHERE criador_id = ?";
		
		List <Album> albuns = new ArrayList<>();
		
		// Tenta a conexão e a consulta.
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, criadorId);
			
			// Preenche a lista de álbuns reconstruídos com os dados do ResultSet.
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Album a = mapearAlbum(conn, rs);
					albuns.add(a);
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao buscar álbuns: " + e.getMessage());
		}
		return albuns;
	}
	
	/**
	 * Recupera os dados de todos os álbuns.
	 * 
	 * @return Retorna a lista de álbuns registrados.
	 */
	public List <Album> listarAlbuns() {
		// String sql com a consulta a ser executada.
		String sql = "SELECT id, titulo, tipo, status, lancamento, criador_id FROM album";
		
		// Lista para guardar os álbuns recuperados.
		List <Album> albuns = new ArrayList<>();
		
		// Tenta a conexão e a consulta.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery())
		{
			// Itera pelo ResultSet reconstruindo os objetos com os dados retornados.
			while (rs.next()) {
				Album album = mapearAlbum(conn, rs);
				albuns.add(album);
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao listar álbuns: " + e.getMessage());
		}
		return albuns;
	}
	
	/**
	 * Atualizar um álbum existente no banco.
	 * 
	 * @param albumAtualizado Objeto de álbum com os novos dados a serem salvos;
	 * @return Retorna true se a operação é bem sucedida, ou false se não.
	 */
	public boolean atualizar(Album albumAtualizado) {
		if (albumAtualizado == null) return false;
		
		// Estabelece a conexão, seguindo o mesmo esquema com o Auto Commit e Rollback anteriores.
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConexao();
			conn.setAutoCommit(false); 

			modificarAlbum(conn, albumAtualizado);
			sincronizarMusicas(conn, albumAtualizado);

			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			// Tenta o Rollback da transação.
			System.out.println("Erro ao atualizar álbum, rollback... " + e.getMessage());
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
	 * Remover uma álbum existente do banco.
	 * 
	 * @param id ID do álbum a ser removido;
	 * @return Retorna true se a operação é bem sucedida, ou false se não.
	 */
	public boolean deletar(int id) {
		String sql = "DELETE FROM album WHERE id = ?";
		
		// Tenta a conexão com o banco de dados e a deleção.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao deletar álbum: " + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Método auxiliar para inserir um novo álbum ao banco.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param album Objeto de Album, com os dados do novo álbum;
	 * @throws SQLException Caso haja um erro de SQL, dispara a exceção.
	 */
	private void inserirAlbum(Connection conn, Album album) throws SQLException {
		String sql = "INSERT INTO album (titulo, tipo, status, lancamento, criador_id) "
				+ "VALUES (?,?,?,?,?)";
		
		// Tenta a conexão do bbanco de dados e a inserção do álbum.
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			stmt.setString(1, album.getTitulo());
			stmt.setString(2, album.getTipo());
			stmt.setBoolean(3, album.isStatus());
			stmt.setDate(4, Date.valueOf(album.getLancamento()));
			stmt.setInt(5, album.getCriadorId());
			stmt.executeUpdate();
			
			// Define o ID do álbum como o ID gerado pela query.
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) album.setId(rs.getInt(1));
				else throw new SQLException("Falha ao gerar ID do álbum!");
			}
 		}
	}
	
	/**
	 * Método auxiliar para modificar os dados de um álbum existente.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory; 
	 * @param album Objeto de Album, com os novos dados a serem salvos;
	 * @throws SQLException Caso haja um erro de SQL, dispara a exceção.
	 */
	private void modificarAlbum(Connection conn, Album album) throws SQLException {
		String sql = "UPDATE album SET titulo = ?, tipo = ?, status = ?, lancamento = ?, criador_id = ? " +
					 "WHERE id = ?";
		
		// Tenta a conexão e a atualização do banco de dados.
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, album.getTitulo());
			stmt.setString(2, album.getTipo());
			stmt.setBoolean(3, album.isStatus());
			stmt.setDate(4, Date.valueOf(album.getLancamento()));
			stmt.setInt(5, album.getCriadorId());
			stmt.setInt(6, album.getId());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para manter as músicas dos álbuns atualizadas.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param album Objeto de Album com os dados das músicas a serem salvas;
	 * @throws SQLException Caso haja um erro de SQL, dispara a exceção.
	 */
	private void sincronizarMusicas(Connection conn, Album album) throws SQLException {
		
		// Primeiro, retira os IDs dos álbuns associados às músicas.
		String sqlDesvincular = "UPDATE musica SET album_id = NULL WHERE album_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sqlDesvincular))
		{
			stmt.setInt(1, album.getId());
			stmt.executeUpdate();
		}
		
		if (album.getMusicas() == null || album.getMusicas().isEmpty()) return;
		
		// Depois, coloca os novos IDs dos álbuns.
		String sqlVincular = "UPDATE musica SET album_id = ? WHERE conteudo_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sqlVincular))
		{
			for (Musica m : album.getMusicas()) {
				stmt.setInt(1, album.getId()); 
				stmt.setInt(2, m.getId());     
				stmt.executeUpdate();
			}
		}
	}
	
	/**
	 * Método auxiliar para recuperar as músicas de um álbum.
	 * 
	 * @param conn Conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param album Objeto de Album com os dados das músicas requeridas;
	 * @throws SQLException Caso haja um erro de SQL, dispara a exceção.
	 */
	private void carregarMusicas(Connection conn, Album album) throws SQLException {
		String sql = "SELECT conteudo_id FROM musica WHERE album_id = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, album.getId());
			
			// Itera no ResultSet para preencher as músicas do álbum.
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int musicaId = rs.getInt("conteudo_id");
					Musica m = musicaDAO.buscarId(musicaId);
					if (m != null) album.adicionarFaixa(m);	
				}
			}
		}
	}
	
	/**
	 * Método auxiliar para reconstruir um objeto a partir dos dados das consultas.
	 * 
	 * @param conn Conexão com o SQLite fornecida pela ConnectionFactory;
	 * @param rs ResultSet com os dados retornados pelo SELECT;
	 * @return Retorna um objeto de Album com os dados provenientes da consulta;
	 * @throws SQLException Caso haja um erro de SQL, dispara a exceção.
	 */
	private Album mapearAlbum(Connection conn, ResultSet rs) throws SQLException {
		String dataTexto = rs.getString("lancamento");
		LocalDate lancamento = null;
		
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
		
		// Instancia um novo álbum com os dados do ResultSet.
		Album albumEncontrado = new Album(
				rs.getInt("id"),
				rs.getString("titulo"),
				rs.getString("tipo"),
				rs.getInt("criador_id"),
				rs.getBoolean("status"),
				lancamento
		);
		
		carregarMusicas(conn, albumEncontrado);
		return albumEncontrado;
	}
}
