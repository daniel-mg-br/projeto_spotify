package model.content;
import java.time.LocalDate; 
import java.util.List;
import java.util.ArrayList;

/**
 * Classe Episodio, filha de Conteúdo, representando um episódio de podcast.
 */
public class Episodio extends Conteudo {
	// Atributos privados específicos do Episódio.
    private int numEpisodio;
    private LocalDate lancamento;
    
    // Lista de convidados do episódio
    private List<String> convidados;
    
    /**
     * Método Construtor padrão para instanciação.
     * 
     * @param titulo Título do conteúdo referente ao episódio;
     * @param duracaoMin Duração em minutos do conteúdo.
     */
    public Episodio(String titulo, int duracaoMin) {
    	super(titulo, duracaoMin);
    	
    	this.numEpisodio = 0;
    	this.lancamento = LocalDate.now();
    	
        convidados = new ArrayList<>();
    }
    
    /**
     * Método Construtor com todos os dados (recuperação bd -> objeto).
     * 
     * @param id ID do conteúdo referente ao episódio recuperado;
     * @param titulo Título do conteúdo referente ao episódio recuperando;
     * @param duracaoMin Duração em minutos do conteúdo;
     * @param numEpisodio Número do episódio recuperado;
     * @param lancamento Data de lançamento do episódio recuperado.
     */
    public Episodio(int id, String titulo, int duracaoMin, int numEpisodio, LocalDate lancamento) {
        super(id, titulo, duracaoMin);

        this.numEpisodio = numEpisodio;
        this.lancamento = lancamento;
        
        this.convidados = new ArrayList<>();
    }
    
    // Métodos Getter e Setter padrão.
    public int getNumEpisodio() {return this.numEpisodio;}
    public void setNumEpisodio(int episodio) {this.numEpisodio = episodio;}

    public LocalDate getLancamento() {return this.lancamento;}
    public void setLancamento(LocalDate lancamento) {this.lancamento = lancamento;}

    public List<String> getConvidados() {
    	return this.convidados;
    }
    
    /**
     * Método para retornar os dados do episódio de podcast.
     * 
     * @return Os dados em formato de string.
     */
    @Override
    public String obterDados() {
        return super.obterDados() +
               "\nNúmero do episódio: " + this.getNumEpisodio() +
               "\nLançamento: " + this.getLancamento();
    }
    
    /**
     * Método para mostrar os convidados do episódio.
     */
    public void mostrarConvidados() {
        for (String nome : convidados) {
            System.out.println(nome);
        }
    }
    
    /**
     * Método para adicionar convidados ao episódio, com verificação.
     * 
     * @param nome Nome do convidado que participa do episódio;
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean adicionarConvidado(String nome) {
    	if (this.convidados.contains(nome) || nome == null) {
    		return false;
    	}
    	
        return convidados.add(nome);
    }
    
    /**
     * Método para remover convidados do episódio.
     * 
     * @param nome Nome do convidado a ser removido;
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean removerConvidado(String nome) {
        return convidados.remove(nome);
    }
}