package model.actors;
import java.time.LocalDate;
import java.time.Period;

// Classe Usuario, representando um usuário geral do sistema, está associado a uma conta
public abstract class Usuario {
	
	// Atributos privados
    private Conta conta;
    private int id;
    private String nome;
    private String sexo;
    private LocalDate aniversario;
    
    // Métodos Getter e Setter padrão
    public Conta getConta() {return this.conta;}
    public void setConta(Conta conta) {this.conta = conta;}
    
    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}
    
    public String getNome() {return this.nome;}
    public void setNome(String nome) {this.nome = nome;}
    
    public String getSexo() {return this.sexo;}
    public void setSexo(String sexo) {this.sexo = sexo;}
    
    public LocalDate getAniversario() {return this.aniversario;}
    public void setAniversario(LocalDate aniversario) {this.aniversario = aniversario;}
    
    // Método Construtor
    public Usuario(Conta conta, int id, String nome, String sexo, LocalDate aniversario) {
        this.conta = conta;
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.aniversario = aniversario;
    }
    
    // Método para alterar o nome do usuário, com verificação
    public boolean alterarNome(String novoNome) {
    	if (novoNome == null || novoNome.equalsIgnoreCase(this.getNome())) {
    		return false;
    	}
        this.nome = novoNome;
        return true;
    }
    
    // Método para alterar o sexo do usuário, com verificação
    public boolean alterarSexo(String novoSexo) {
    	if (novoSexo == null || novoSexo.equalsIgnoreCase(this.getSexo())) {
    		return false;
    	}
        this.sexo = novoSexo;
        return true;
    }
    
    // Método para calcular a idade do usuário com base no seu aniversário
    public int calcularIdade() {
    	return Period.between(this.aniversario, LocalDate.now()).getYears();
    }
    
    // Método para recuperar os dados do usuário
    public String obterDados() {
        return "Id: " + this.getId() +
               "\nNome: " + this.getNome() +
               "\nSexo: " + this.getSexo() +
               "\nIdade: " + this.calcularIdade();
    }
}
