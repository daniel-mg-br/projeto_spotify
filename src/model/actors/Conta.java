package model.actors;
import java.time.LocalDate;

// Classe Conta, a qual os estão vinculados (1 conta por usuário)
public class Conta {
	// Atributos privados da Conta, incluindo um estático para gerar um ID sequencial
	private static int codSequencia = 0;
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
		codSequencia++;
		this.setIdUsuario(codSequencia);
		this.setLogin(login);
		this.setSenha(senha);
		this.setPlano("Free");
		this.setStatus("Ativo");
		this.setDataCriacao();
	}
}
