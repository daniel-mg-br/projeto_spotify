package model.actors;
import java.time.LocalDate;

// Classe Conta, a qual os estão vinculados (1 conta por usuário)
public class Conta {
	// Atributos privados da Conta
	private int id;
	private String login, senha, plano, status;
	private LocalDate dataCriacao;
	
	// Métodos Getter e Setter padrão
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
	
	// Método Construtor com os dados básicos para a criação
	public Conta(String login, String senha) {
		this.id = 0;
		this.login = login;
		this.senha = senha;
		this.plano = "Free";
		this.status = "Ativa";
		this.setDataCriacao(LocalDate.now());
	}
	
	// Método Construtor com todos os dados (recuperação de dados bd -> objeto)
	public Conta(int id, String login, String senha, String plano, String status, LocalDate dataCriacao) {
		this.id = id;
		this.login = login;
		this.senha = senha;
		this.plano = plano;
		this.status = status;
		this.dataCriacao = dataCriacao;
	}
	
	// Método de validação de senha para login do usuário
	public boolean validarSenha(String senha) {
		return this.senha.equalsIgnoreCase(senha);	
	}
	
	// Método de alteração de senha com validação
	public boolean alterarSenha(String novaSenha) {
		if (this.senha.equalsIgnoreCase(novaSenha) || novaSenha == null) {
			return false;
		}
		
		this.senha = novaSenha;
		return true;
	}
	
	// Método para atualizar o plano (free -> premium / premium -> free) com validação
	public boolean atualizarPlano(String novoPlano) {
		if (this.plano.equalsIgnoreCase(novoPlano) || novoPlano == null) {
			return false;
		}
		
		this.plano = novoPlano;
		return true;
	}
	
	// Método que retorna se a conta está ativa
	public boolean isAtiva() {
		return this.status.equalsIgnoreCase("Ativa");
	}
	
	// Método que retorna os dados da conta (String)
	public String getDados() {
		return "ID: " + this.getId() +
				"Login: " + this.getLogin() +
				"Senha: " + this.getSenha() +
				"Plano: " + this.getPlano() +
				"Status: " + this.getStatus() +
				"Data de criação: " + this.getDataCriacao();
	}
}
