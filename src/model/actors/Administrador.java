package model.actors;
 
import java.time.LocalDate;

/**
 * Classe Administrador, filha de Usuario, representa um moderador do sistema operando sobre os dados.
 */
public class Administrador extends Usuario {
	
	// Atributo privado específico do administrador.
    private String credencial;
    
    // Métodos Getter e Setter padrão.
    public String getCredencial() {return this.credencial;}
    public void setCredencial(String credencial) {this.credencial = credencial;}
    
    /**
     * Método Construtor padrão para instanciação.
     * Usa os atributos de Usuario (superclasse).
     * 
     * @param conta Conta associada ao usuário;
     * @param nome Nome do usuário;
     * @param sexo Sexo do usuário;
     * @param aniversario Data de aniversário do usuário;
     * @param credencial Credencial do administrador.
     */
    public Administrador(Conta conta, String nome, String sexo, LocalDate aniversario, String credencial) {
    	super(conta, nome, sexo, aniversario);
        this.credencial = credencial;
    }
    
    /**
     * Método Construtor com todos os dados (recuperação bd -> objeto).
     * 
     * @param conta Conta associada ao usuário recuperado;
     * @param id ID do usuário recuperado;
     * @param nome Nome do usuário recuperado;
     * @param sexo Sexo do usuário recuperado;
     * @param aniversario Data de aniversário recuperada;
     * @param credencial Credencial do administrador recuperado.
     */
    public Administrador(Conta conta, int id, String nome, String sexo, LocalDate aniversario, String credencial) {
    	super(conta, id, nome, sexo, aniversario);
    	this.credencial = credencial;
    }
    
    /**
     * Método para recuperar os dados do Administrador.
     * 
     * @return Retorna os dados em formato de string.
     */
    @Override
    public String obterDados() {
    	String superDados = super.obterDados();
    	return superDados + "\n" +
    		   "Credencial: " + this.getCredencial();	
    }
}

