package model.content;
import java.time.LocalDate; 
import java.util.List;
import java.util.ArrayList;

// Classe Podcast, representando os podcasts de um Criador, que um Ouvinte pode ouvir
public class Podcast  {
	// Atributos privados
	private int id, numEpisodios;
	private String nome, tema;
	private LocalDate criacao;
	
	// Lista de episódios do podcast
	private List <Episodio> episodios;
	
	// Métodos Getter e Setter padrão
    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}
    
    public int getNumEpisodios() {return this.numEpisodios;}
    public void setNumEpisodios(int numEpisodios) {this.numEpisodios = numEpisodios;}
    
    public String getNome() {return this.nome;}
    public void setNome(String nome) {this.nome = nome;}
    
    public String getTema() {return this.tema;}
    public void setTema(String tema) {this.tema = tema;}
    
    public LocalDate getCriacao() {return this.criacao;}
    public void setCriacao(LocalDate criacao) {this.criacao = criacao;}
    
    public List <Episodio> getEpisodios() {
    	return this.episodios;
    }
    
    // Método Construtor
    public Podcast(String nome, String tema) {
    	this.id = 0;
    	this.nome = nome;
    	this.tema = tema;
    	this.numEpisodios = 0;
    	this.criacao = LocalDate.now();
    	
    	this.episodios = new ArrayList<>();
    }
    
    // Método Construtor com todos os dados (recuperação bd -> objeto)
    public Podcast(int id, String nome, String tema, LocalDate criacao, int numEpisodios) {
    	this.id = id;
    	this.nome = nome;
    	this.tema = tema;
    	this.criacao = criacao;
    	this.numEpisodios = numEpisodios;
    	
    	this.episodios = new ArrayList<>();
    }
    
    // Método para mostrar o título dos episódios do podcast
    public void mostrarEpisodios() {
    	for (Episodio e : this.getEpisodios()) {
    		System.out.println(e.getTitulo());
    	}
    }
    
    // Método para adicionar um episódio ao podcast, com verificação
    public boolean adicionarEp(Episodio e) {
    	if (this.episodios.contains(e) || e == null) {
    		return false;
    	}
    	this.episodios.add(e);
    	this.numEpisodios++;
    	return true;
    }
    
    // Método para remover episódios do podcast
    public boolean removerEp(Episodio e) {
    	if (this.episodios.remove(e)) {
    		this.numEpisodios--;
    		return true;
    	};
    	return false;
    }
    
    // Método para recuperar os dados do podcast
    public String obterDados() {
    	return "ID: " + this.getId() + "\n" +
               "Nome: " + this.getNome() + "\n" +
    		   "Tema: " + this.getTema() + "\n" +
               "Episódios: " + this.getNumEpisodios() + "\n" +
    		   "Criação: " + this.getCriacao();
    }
}