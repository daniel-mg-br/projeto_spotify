package dao;

import java.util.List; 
import java.util.ArrayList;
import java.sql.*;
import java.time.LocalDate;
import model.actors.Conta;

/**
 *  Classe DAO para manipulação de contas dos usuários (agora com JDBC e SQLite)
 */
public class ContaDAO {
	
	/**
	 * Método para inserir uma nova conta à tabela.
	 * 
	 * @param novaConta Objeto do tipo Conta, com os dados da conta nova.
	 * @return Retorna true se a inserção deu certo, e falso se não.
	 */
	public boolean salvar(Conta novaConta) {
		if (novaConta == null) {
			return false;
		}
		
		// String com o comando SQL a ser executado. Os '?' são preenchidos com os valores dos atributos
		String sql = "INSERT INTO conta (login, senha, plano, status, data_criacao) VALUES (?, ?, ?, ?, ?)";
		
		// Tratamento de exceção para a conexão com o banco de dados.
		// RETURN GENERATED KEYS avisa ao JDBC que o ID que o SQLite vai gerar será usado
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			// Preenchendo os '?' com os dados, o primeiro número é a posição do '?'
			stmt.setString(1, novaConta.getLogin());
			stmt.setString(2, novaConta.getSenha());
			stmt.setString(3, novaConta.getPlano() != null ? novaConta.getPlano() : "Free");
			stmt.setString(4, novaConta.getStatus() != null ? novaConta.getStatus() : "ATIVO");
			stmt.setDate(5, Date.valueOf(novaConta.getDataCriacao())); // Converte LocalDate para sql.Date
			
			int linhasAfetadas = stmt.executeUpdate();
			if (linhasAfetadas > 0) {
				// Se inseriu com sucesso, o ID gerado é associado ao objeto
				try (ResultSet rs = stmt.getGeneratedKeys()) {
					if (rs.next()) {
						novaConta.setId(rs.getInt(1));
					}
				}
				return true;
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao salvar conta: " + e.getMessage());
		}	
		return false;
	}
	
	/**
	 * Método para buscar a conta pelo ID.
	 * 
	 * @param id ID da conta a ser consultada;
	 * @return Retorna o objeto da Conta, ou null se a consulta falhou.
	 */
	public Conta buscarId(int id) {
		String sql = "SELECT * FROM conta WHERE id = ?";
		
		// Execute query é usado para o comando SELECT, retornando um Result Set (uma tabela virtual)
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			
			try (ResultSet rs = stmt.executeQuery()) {
				// Retorna o objeto Conta em memória usando os dados no banco
				// Isso sempre que houver uma próxima linha
				if (rs.next()) return mapearConta(rs);
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao buscar a conta: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Método para consultar uma conta por meio do login.
	 * 
	 * @param login Login da conta a ser buscada;
	 * @return Retorna o objeto da Conta, ou null se a consulta falhar.
	 */
	public Conta buscarLogin(String login) {
		String sql = "SELECT * FROM conta WHERE login = ?";
		
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, login);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) return mapearConta(rs);
				}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao buscar conta: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Método para retornar os dados de todas as contas registradas.
	 * 
	 * @return Retorna a lista com todas as Contas registradas.
	 */
	public List <Conta> listarContas() {
		String sql = "SELECT * FROM conta";
		
		List <Conta> contas = new ArrayList<>();
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery())
		{
			// O while roda para cada linha que existir na tabela do banco
			// Assim, ele cria os objetos e armazena na lista de usuários
			while (rs.next()) {
				contas.add(mapearConta(rs));
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao listar contas: " + e.getMessage());
		}
		return contas;
	}
	
	/**
	 * Método para atualizar uma conta existente na tabela.
	 * 
	 * @param contaAtualizada Objeto Conta com os dados atualizados;
	 * @return Retorna true se a atualização foi bem sucedida ou false se não.
	 */
	public boolean atualizar(Conta contaAtualizada) {
		String sql = "UPDATE conta SET login = ?, senha = ?, plano = ?, status = ? WHERE id = ?";
		
		// Atualizando os atributos conforme os dados da conta atualizada
		try (Connection conn = ConnectionFactory.getConexao();
		     PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, contaAtualizada.getLogin());
			stmt.setString(2, contaAtualizada.getSenha());
			stmt.setString(3, contaAtualizada.getPlano());
			stmt.setString(4, contaAtualizada.getStatus());
			stmt.setInt(5, contaAtualizada.getId()); // O id referente à cláusula WHERE
			
			// execute update retorna a quantidade de linhas alteradas no banco
			int linhasAfetadas = stmt.executeUpdate();
			return linhasAfetadas > 0;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao atualizar conta: " + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Método para remover uma conta da tabela.
	 * 
	 * @param id ID da conta a ser removida;
	 * @return Retorna true se a operação deu certo ou false se não.
	 */
	public boolean deletar(int id) {
		String sql = "DELETE FROM conta WHERE id = ?";
		
		try (Connection conn = ConnectionFactory.getConexao();
			 PreparedStatement stmt = conn.prepareStatement(sql)) 
		{
			// Substitui com o id da conta a ser escolhida
			stmt.setInt(1, id);
			int linhasAfetadas = stmt.executeUpdate();
			return linhasAfetadas > 0;
		}
		catch (SQLException e)
		{
			System.out.println("Erro ao deletar conta: " + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Método auxiliar para reconstruir um objeto de Conta com os dados retornados pelo SELECT
	 * 
	 * @param rs Result Set retornado com os dados da conta;
	 * @return Objeto do tipo Conta com os dados obtidos do SELECT.
	 * @throws SQLException Caso haja um erro de SQL, dispara uma exceção.
	 */
	private Conta mapearConta(ResultSet rs) throws SQLException {
		Date dataBanco = rs.getDate("data_criacao");
		LocalDate dataCriacao = (dataBanco != null) ? dataBanco.toLocalDate() : null;
		
		return new Conta(
				rs.getInt("id"),
				rs.getString("login"),
				rs.getString("senha"),
				rs.getString("plano"),
				rs.getString("status"),
				dataCriacao
		);
	}
}