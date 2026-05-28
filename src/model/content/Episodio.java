package model.content;
import java.time.LocalDate; 
import java.util.List;
import java.util.ArrayList;

// Classe Episodio, filha de Conteúdo, representando um episódio de podcast
public class Episodio extends Conteudo {
	// Atributos privados
    private int numEpisodio;
    private LocalDate lancamento;
    
    // Lista de convidados do episódio
    private List<String> convidados;
    
    // Método Construtor com apenas os dados de Conteúdo
    public Episodio(int id, String titulo, int duracaoMin) {
    	super(id, titulo, duracaoMin);
    	this.numEpisodio = 0;
    	this.lancamento = LocalDate.now();	
        convidados = new ArrayList<>();
    }
    
    // Método Construtor com todos os dados
    public Episodio(int id, String titulo, int duracaoMin, int numEpisodio, LocalDate lancamento) {
        super(id, titulo, duracaoMin);

        this.numEpisodio = numEpisodio;
        this.lancamento = lancamento;
        this.convidados = new ArrayList<>();
    }
    
    // Métodos Getter e Setter padrão
    public int getNumEpisodio() {return this.numEpisodio;}
    public void setNumEpisodio(int episodio) {this.numEpisodio = episodio;}

    public LocalDate getLancamento() {return this.lancamento;}
    public void setLancamento(LocalDate lancamento) {this.lancamento = lancamento;}

    public List<String> getConvidados() {return this.convidados;}
    
    // Método para retornar os dados do episódio de podcast
    @Override
    public String obterDados() {
        return super.obterDados() +
               "\nNúmero do episódio: " + this.getNumEpisodio() +
               "\nLançamento: " + this.getLancamento();
    }
    
    // Método para mostrar os convidados do episódio
    public void mostrarConvidados() {
        for (String nome : convidados) {
            System.out.println(nome);
        }
    }
    
    // Método para adicionar convidados ao episódio, com verificação
    public boolean adicionarConvidado(String nome) {
    	if (this.convidados.contains(nome) || nome == null) {
    		return false;
    	}
    	
        return convidados.add(nome);
    }
    
    // Método para remover convidados do episódio
    public boolean removerConvidado(String nome) {
        return convidados.remove(nome);
    }
}