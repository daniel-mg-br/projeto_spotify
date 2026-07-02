package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;  
import java.util.List;
import model.actors.Administrador;
import model.actors.Conta;

/**
 * Classe DAO para manipulação dos dados do(s) administrador(es) no banco de dados.
 */
public class AdministradorDAO {
	
	// Conta DAO como atributo para manipulação de dados.
	private ContaDAO contaDAO;
	
	/**
	 * Método Construtor instanciando a classe DAO.
	 */
	public AdministradorDAO() {
		this.contaDAO = new ContaDAO();
	}
	
	/**
	 * Salva um novo administrador (e seu usuário pai) para o sistema no banco de dados.
	 * 
	 * @param novoAdmin Objeto do tipo Administrador com os dados do novo administrador;
	 * @return Retorna true se a operação foi bem sucedida, ou false se não foi.
	 */
	public boolean salvar(Administrador novoAdmin) {
		if (novoAdmin == null || novoAdmin.getConta() == null) return false;
		
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
			
			inserirUsuario(conn, novoAdmin);
			inserirAdministrador(conn, novoAdmin);
			
			// Se der certo, aplica as mudanças.
			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			// Se der errado, tenta o Rollback da transação.
			System.out.println("Erro ao salvar administrador, rollback... " + e.getMessage());
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
	 * Busca um administrador do sistema por meio do seu ID.
	 * 
	 * @param ID do administrador a ser consultado;
	 * @return Retorna o objeto do tipo Administrador com os dados requeridos. 
	 */
	public Administrador buscarId(int id) {
		
		// String sql com a consulta a ser executada.
		String sql = "SELECT u.id, u.nome, u.sexo, u.aniversario, u.conta_id, " +
					 		"a.credencial " +
					 		"FROM usuario AS u JOIN administrador AS a ON u.id = a.usuario_id " +
					 		"WHERE u.id = ?";
				
		// Tenta estabelecer a conexão e executar a consulta.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			
			// Reconstrói o Administrador a partir do ResultSet.
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) return mapearAdministrador(conn, rs);
			}
		}
		catch (SQLException e)
		{	
			System.out.println("Erro ao buscar administrador: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Retorna os dados do(s) administrador(es) do sistema.
	 * 
	 * @return Retorna a lista de administradores do sistema (a princípio, apenas um)
	 */
	public List <Administrador> listarAdministradores() {
		
		// String sql com a consulta a ser executada.
		String sql = "SELECT u.id, u.nome, u.sexo, u.aniversario, u.conta_id, " +
							"a.credencial " +
							"FROM usuario AS u JOIN administrador AS a ON u.id = a.usuario_id";
		
		// Lista para guardar os Administradores recuperados.
		List <Administrador> admins = new ArrayList <>();
		
		// Tenta a conexão e a execução da query.
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery())
		{
			// Preenche a lista com os administradores reconstruídos.
			while (rs.next()) {
				admins.add(mapearAdministrador(conn, rs));
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao listar administradores: " + e.getMessage());
		}
		return admins;
	}
	
	/**
	 * Atualiza os dados do administrador (e do usuário pai) do sistema.
	 * 
	 * @param adminAtualizado Objeto do tipo Administrador com os novos dados;
	 * @return Retorna true se a operação foi bem sucedida, ou false se não foi.
	 */
	public boolean atualizar(Administrador adminAtualizado) {
		if (adminAtualizado == null) return false;
		
		// Estabelece a conexão, mantendo o mesmo esquema com o Auto Commit e o Rollback anteriores.
		Connection conn = null;
		try
		{
			conn = ConnectionFactory.getConexao();
			conn.setAutoCommit(false);
			
			modificarUsuario(conn, adminAtualizado);
			modificarAdministrador(conn, adminAtualizado);
			
			conn.commit();
			return true;
		}
		catch (SQLException e)
		{
			// Tenta o Rollback da transação.
			System.out.println("Erro ao atualizar administrador, rollback... " + e.getMessage());
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
	 * Remove um administrador do sistema.
	 * 
	 * @param id ID do administrador a ser removido (se necessário);
	 * @return Retorna true se a operação foi bem sucedida, ou false se não foi.
	 */
	public boolean deletar(int id) {
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
			System.out.println("Erro ao deletar administrador: " + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Método auxiliar para inserir um novo usuário a partir dos dados herdados pela classe Administrador.
	 * 
	 * @param conn A conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param admin Objeto do tipo Administrador com dados do novo usuário;
	 * @throws SQLException Caso haja um erro de SQL, dispara uma exceção.
	 */
	private void inserirUsuario(Connection conn, Administrador admin) throws SQLException {
		String sql = "INSERT INTO usuario (nome, sexo, aniversario, conta_id) VALUES (?,?,?,?)";
		
		// Tenta estabelecer a conexão e inserir o usuário.
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			stmt.setString(1, admin.getNome());
			stmt.setString(2, admin.getSexo());
			stmt.setDate(3, Date.valueOf(admin.getAniversario()));
			stmt.setInt(4, admin.getConta().getId());
			stmt.executeUpdate();
			
			// Preenche o ID do usuário com o ID automático da query.
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) admin.setId(rs.getInt(1));
				else throw new SQLException("Falha ao gerar ID do usuário!");
			}
		}
	}
	
	/**
	 * Método auxiliar para inserir um novo administrador na tabela.
	 * 
	 * @param conn A conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param admin Objeto do tipo Administrador com os dados a serem inseridos;
	 * @throws SQLException Caso haja um erro de SQL, dispara uma exceção.
	 */
	private void inserirAdministrador(Connection conn, Administrador admin) throws SQLException {
		String sql = "INSERT INTO administrador (usuario_id, credencial) VALUES (?,?)";
		
		// Tenta estabelecer a conexão e inserir o administrador.
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, admin.getId());
			stmt.setString(2, admin.getCredencial());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para modificar um usuário a partir dos dados herdados pela classe Administrador.
	 * 
	 * @param conn A conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param admin Objeto do tipo Administrador com os novos dados;
	 * @throws SQLException Caso haja um erro de SQL, dispara uma exceção.
	 */
	private void modificarUsuario(Connection conn, Administrador admin) throws SQLException {
		String sql = "UPDATE usuario SET nome = ?, sexo = ?, aniversario = ? WHERE id = ?";
		
		// Tenta estabelecer a conexão e atualizar a tabela.
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, admin.getNome());
			stmt.setString(2, admin.getSexo());
			stmt.setDate(3, Date.valueOf(admin.getAniversario()));
			stmt.setInt(4, admin.getId());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para modificar e salvar os dados de um administrador existente.
	 * 
	 * @param conn A conexão JDBC com o SQLite, fornecida pela ConnectionFactory;
	 * @param admin Objeto do tipo Administrador com os novos dados;
	 * @throws SQLException Caso haja um erro de SQL, dispara uma exceção.
	 */
	private void modificarAdministrador(Connection conn, Administrador admin) throws SQLException {
		String sql = "UPDATE administrador SET credencial = ? WHERE usuario_id = ?";
		
		// Estabelece a conexão e atualiza o administrador.
		try (PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, admin.getCredencial());
			stmt.setInt(2, admin.getId());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Método auxiliar para reconstruir um objeto de Administrador com os dados retornados pelo SELECT.
	 * 
	 * @param conn Conexão com o SQLite fornecida pela ConnectionFactory;
	 * @param rs Result Set retornado com os dados do Administrador;
	 * @return Objeto do tipo Administrador com os dados obtidos do SELECT.
	 * @throws SQLException Caso haja um erro de SQL, dispara uma exceção.
	 */
	private Administrador mapearAdministrador(Connection conn, ResultSet rs) throws SQLException {
		int idConta = rs.getInt("conta_id");
		Conta conta = contaDAO.buscarId(idConta);
		
		String dataTexto = rs.getString("aniversario");
		LocalDate aniversario = null;
		
		// Lógica para recuperar as datas em formato de texto no banco de dados.
		if (dataTexto != null && !dataTexto.isEmpty()) {
			if (dataTexto.contains("-")) {
				// Formato texto (SQL).
				aniversario = LocalDate.parse(dataTexto.substring(0, 10));
			} else {
				// Formato milissegundos (Java JDBC).
				try {
					long timestamp = Long.parseLong(dataTexto);
					aniversario = new java.sql.Date(timestamp).toLocalDate();
				} catch (Exception e) {
					aniversario = LocalDate.now();
				}
			}
		}
		
		// Instancia o administrador com os dados do ResultSet.
		return new Administrador(
				conta, 
				rs.getInt("id"), 
				rs.getString("nome"), 
				rs.getString("sexo"),
				aniversario,
				rs.getString("credencial")
		);
	}
}