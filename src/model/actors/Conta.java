package model.actors;
import java.time.LocalDate;

/**
 * Classe Conta, a qual os estão vinculados (1 conta por usuário).
 */
public class Conta {
	// Atributos privados da Conta.
	private int id;
	private String login, senha, plano, status;
	private LocalDate dataCriacao;
	
	// Métodos Getter e Setter padrão.
	public int getId() {return this.id;}
	public void setId(int id) {this.id = id;}
	
	public String getLogin() {return this.login;}
	public void setLogin(String login) {this.login = login;}
	
	public String getSenha() {return this.senha;}
	public void setSenha(String senha) {this.senha = senha;}
	
	public String getPlano() {return this.plano;}
	public void setPlano(String plano) {this.plano = plano;}
	
	public String getStatus() {return this.status;}
	public void setStatus(String status) {this.status = status;}
	
	// A data de criação da conta é definida como a data atual
	public LocalDate getDataCriacao() {return this.dataCriacao;}
	public void setDataCriacao(LocalDate dataCriacao) {this.dataCriacao = dataCriacao;}
	
	/**
	 * Método Construtor padrão para instanciação.
	 * 
	 * @param login Login da conta;
	 * @param senha Senha da conta.
	 */
	public Conta(String login, String senha) {
		this.id = 0;
		this.login = login;
		this.senha = senha;
		this.plano = "Free";
		this.status = "ATIVO";
		this.setDataCriacao(LocalDate.now());
	}
	
	/**
	 * Método Construtor com todos os dados (recuperação de dados bd -> objeto).
	 * 
	 * @param id ID da conta recuperada;
	 * @param login Login da conta recuperada;
	 * @param senha Senha da conta recuperada;
	 * @param plano Plano da conta recuperada (Free, Premium, Admin);
	 * @param status Status da conta (Ativa, Suspensa);
	 * @param dataCriacao Data de criação da conta.
	 */
	public Conta(int id, String login, String senha, String plano, String status, LocalDate dataCriacao) {
		this.id = id;
		this.login = login;
		this.senha = senha;
		this.plano = plano;
		this.status = status;
		this.dataCriacao = dataCriacao;
	}
	
	/**
	 * Método de validação de senha para login do usuário.
	 * 
	 * @param senha Senha inserida que será comparada com a atual;
	 * @return Retorna true se a comparação é verdadeira, e false se não.
	 */
	public boolean validarSenha(String senha) {
		return this.senha.equalsIgnoreCase(senha);	
	}
	
	/**
	 * Método de alteração de senha com validação.
	 * 
	 * @param novaSenha Nova senha, que substituirá a antiga;
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean alterarSenha(String novaSenha) {
		if (this.senha.equalsIgnoreCase(novaSenha) || novaSenha == null) {
			return false;
		}
		
		this.senha = novaSenha;
		return true;
	}
	
	/**
	 * Método para atualizar o plano (free <-> premium) com validação de dados.
	 * 
	 * @param novoPlano String com o novo plano da conta;
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean atualizarPlano(String novoPlano) {
		if (this.plano.equalsIgnoreCase(novoPlano) || novoPlano == null) {
			return false;
		}
		
		this.plano = novoPlano;
		return true;
	}
	
	/**
	 * Método que retorna se a conta está ativa.
	 * 
	 * @return Retorna true se a conta está ativa, e false se não estiver.
	 */
	public boolean isAtiva() {
		return this.status.equalsIgnoreCase("Ativo");
	}
	
	/**
	 * Método que retorna os dados da conta.
	 * 
	 * @return Retorna os dados em formato de String.
	 */
	public String obterDados() {
		return "ID: " + this.getId() +
				"\nLogin: " + this.getLogin() +
				"\nSenha: " + this.getSenha() +
				"\nPlano: " + this.getPlano() +
				"\nStatus: " + this.getStatus() +
				"\nData de criação: " + this.getDataCriacao();
	}
}
