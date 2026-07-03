package model.actors;
import java.time.LocalDate; 
import java.time.Period;

/**
 * Classe Usuario, representando um usuário geral do sistema, está associado a uma conta.
 */
public abstract class Usuario {
	
	// Atributos privados do usuário.
    protected Conta conta;
    protected int id;
    protected String nome;
    protected String sexo;
    protected LocalDate aniversario;
    
    // Métodos Getter e Setter padrão.
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
    
    /**
     * Método Construtor padrão para instanciação.
     * 
     * @param conta Objeto da Conta associado ao usuário;
     * @param nome Nome do usuário;
     * @param sexo Sexo do usuário;
     * @param aniversario Data de aniversário do usuário.
     */
    public Usuario(Conta conta, String nome, String sexo, LocalDate aniversario) {
        this.conta = conta;
        this.id = 0;
        this.nome = nome;
        this.sexo = sexo;
        this.aniversario = aniversario;
    }
    
    /**
     * Método Construtor com todos os dados (recuperação bd -> objeto).
     * 
     * @param conta Objeto de Conta associado ao usuário recuperado;
     * @param id ID do usuário recuperado;
     * @param nome Nome do usuário recuperado;
     * @param sexo Sexo do usuário recuperado;
     * @param aniversario Data de aniversário do usuário recuperado.
     */
    public Usuario(Conta conta, int id, String nome, String sexo, LocalDate aniversario) {
    	this.conta = conta;
    	this.id = id;
    	this.nome = nome;
    	this.sexo = sexo;
    	this.aniversario = aniversario;
    }
    
    /**
     * Método para alterar o nome do usuário, com verificação de dados.
     * 
     * @param novoNome Novo nome do usuário;
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean alterarNome(String novoNome) {
    	if (novoNome == null || novoNome.equalsIgnoreCase(this.getNome())) {
    		return false;
    	}
        this.nome = novoNome;
        return true;
    }
    
    /**
     * Método para alterar o sexo do usuário, com verificação de dados.
     * 
     * @param novoSexo Novo sexo do usuário;
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean alterarSexo(String novoSexo) {
    	if (novoSexo == null || novoSexo.equalsIgnoreCase(this.getSexo())) {
    		return false;
    	}
        this.sexo = novoSexo;
        return true;
    }
    
    /**
     * Método para calcular a idade do usuário com base no seu aniversário;
     * 
     * @return Retorna a idade do usuário.
     */
    public int calcularIdade() {
    	return Period.between(this.aniversario, LocalDate.now()).getYears();
    }
    
    /**
     * Método para recuperar os dados do usuário.
     *
     * @return Retorna os dados em formato de String.
     */
    public String obterDados() {
        return "Id: " + this.getId() +
               "\nNome: " + this.getNome() +
               "\nSexo: " + this.getSexo() +
               "\nIdade: " + this.calcularIdade();
    }
}
