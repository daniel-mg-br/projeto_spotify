package model.actors;
import java.time.LocalDate;

// Classe Conta, a qual os estão vinculados (1 conta por usuário)
public class Conta {
	// Atributos privados da Conta, incluindo um estático para gerar um ID sequencial (temporário)
	private static int codSequencia = 1;
	private int idUsuario;
	private String login, senha, plano, status;
	private LocalDate dataCriacao;
	
	// Métodos Getter e Setter padrão
	public int getIdUsuario() {return this.idUsuario;}
	public void setIdUsuario(int idUsuario) {this.idUsuario = idUsuario;}
	
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
	public void setDataCriacao() {this.dataCriacao = LocalDate.now();}
	
	// Método Construtor
	public Conta(String login, String senha) {
		this.idUsuario = codSequencia;
		this.login = login;
		this.senha = senha;
		this.plano = "Free";
		this.status = "Ativo";
		this.setDataCriacao();
		codSequencia++;
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
		return "ID: " + this.getIdUsuario() +
				"Login: " + this.getLogin() +
				"Senha: " + this.getSenha() +
				"Plano: " + this.getPlano() +
				"Status: " + this.getStatus() +
				"Data de criação: " + this.getDataCriacao();
	}
}
