package model.content;
import java.time.LocalDate; 
import java.util.List;
import java.util.ArrayList;

/**
 * Classe Podcast, representando os podcasts de um Criador que um Ouvinte pode ouvir.
 */
public class Podcast  {
	
	// Atributos privados do podcast.
	private int id;
	private int criadorId;
	private String nome, tema;
	private LocalDate criacao;
	
	// Lista de episódios do podcast.
	private List <Episodio> episodios;
	
	// Métodos Getter e Setter padrão.
    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}
    
    public int getCriadorId() {return this.criadorId;}
    public void setCriadorId(int criadorId) {this.criadorId = criadorId;}
    
    public int getNumEpisodios() {
    	return this.episodios.size();
    }
    
    public String getNome() {return this.nome;}
    public void setNome(String nome) {this.nome = nome;}
    
    public String getTema() {return this.tema;}
    public void setTema(String tema) {this.tema = tema;}
    
    public LocalDate getCriacao() {return this.criacao;}
    public void setCriacao(LocalDate criacao) {this.criacao = criacao;}
    
    public List <Episodio> getEpisodios() {
    	return this.episodios;
    }
    
    /**
     * Método Construtor padrão para instanciação.
     * 
     * @param nome Nome do podcast;
     * @param tema Tema do podcast;
     * @param criadorId ID do criador do podcast.
     */
    public Podcast(String nome, String tema, int criadorId) {
    	this.id = 0;
    	this.nome = nome;
    	this.tema = tema;
    	this.criadorId = criadorId;
    	this.criacao = LocalDate.now();
    	
    	this.episodios = new ArrayList<>();
    }
    
    /**
     * Método Construtor com todos os dados (recuperação bd -> objeto).
     * 
     * @param id ID do podcast recuperado;
     * @param nome Nome do podcast recuperado;
     * @param tema Tema do podcast recuperado;
     * @param criadorId ID do criador do podcast;
     * @param criacao Data de criação do podcast.
     */
    public Podcast(int id, String nome, String tema, int criadorId, LocalDate criacao) {
    	this.id = id;
    	this.nome = nome;
    	this.tema = tema;
    	this.criadorId = criadorId;
    	this.criacao = criacao;
    	
    	this.episodios = new ArrayList<>();
    }
    
    /**
     * Método para mostrar o título dos episódios do podcast.
     */
    public void mostrarEpisodios() {
    	for (Episodio e : this.getEpisodios()) {
    		System.out.println(e.getTitulo());
    	}
    }
    
    /**
     * Método para adicionar um episódio ao podcast, com verificação.
     * 
     * @param e Objeto de Episodio com os dados a serem salvos;
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean adicionarEp(Episodio e) {
    	if (e == null) return false;
    	
    	// .stream() transforma a lista em uma sequência de elementos;
    	// .anyMatch() compara se a música existe nessa sequência.
    	boolean jaExiste = this.episodios.stream().anyMatch(ep -> ep.getId() == e.getId());
    	if (jaExiste) return false;
    	
    	return this.episodios.add(e);
    }
    
    /**
     * Método para remover episódios do podcast.
     * 
     * @param e Objeto de Episodio com o episodio a ser removido;
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean removerEp(Episodio e) {
    	// .removeIf() remove o(s) elemento(s) que cumpre(m) a condição entre parênteses.
    	return this.episodios.removeIf(ep -> ep.getId() == e.getId());
    }
    
    /**
     * Método para recuperar os dados do podcast.
     * 
     * @return Retorna os dados em formato de String.
     */
    public String obterDados() {
    	return "ID: " + this.getId() + "\n" +
    		   "ID do criador: " + this.getCriadorId() + "\n" +	 
               "Nome: " + this.getNome() + "\n" +
    		   "Tema: " + this.getTema() + "\n" +
               "Episódios: " + this.getNumEpisodios() + "\n" +
    		   "Criação: " + this.getCriacao();
    }
}