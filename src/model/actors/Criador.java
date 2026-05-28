package model.actors;

import model.content.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

// Classe Criador, filha de Usuário, que representa um criador de conteúdo da plataforma de streaming
public class Criador extends Usuario {
	// Atributos privados
	private String nomeArtistico, biografia;
	private int ouvintesMensais;
	private boolean verificado;
	
	// Listas para representar os álbuns e podcasts do criador de conteúdo
	private List <Album> discografia;
	private List <Podcast> podcasts;
	
	// Métodos Getter e Setter padrão
	public String getNomeArtistico() {return this.nomeArtistico;}
	public void setNomeArtistico(String nomeArtistico) {this.nomeArtistico = nomeArtistico;}
	
	public String getBiografia() {return this.biografia;}
	public void setBiografia(String biografia) {this.biografia = biografia;}
	
	public int getOuvintesMensais() {return this.ouvintesMensais;}
	public void setOuvintesMensais(int ouvintes) {this.ouvintesMensais = ouvintes;}
	
	public boolean isVerificado() {return this.verificado;}
	public void setVerificado(boolean b) {this.verificado = b;}
	
	public List <Album> getDiscografia() {
		return this.discografia;
	}
	
	public List <Podcast> getPodcasts() {
		return this.podcasts;
	}
	
	// Método Construtor com apenas os dados de Usuário
	public Criador(Conta conta, int id, String nome, String sexo, LocalDate aniversario) {
		super(conta, id, nome, sexo, aniversario);
		this.nomeArtistico = "";
		this.biografia = "";
		this.ouvintesMensais = 0;
		this.verificado = false;
		
		this.discografia = new ArrayList<>();
		this.podcasts = new ArrayList<>();
	}
	
	// Método Construtor com todos os dados do Criador
	public Criador(Conta conta, int id, String nome, String sexo, LocalDate aniversario, String nomeArtistico, String biografia) {
		super(conta, id, nome, sexo, aniversario);
		this.nomeArtistico = nomeArtistico;
		this.biografia = biografia;
		this.ouvintesMensais = 0;
		this.verificado = false;
		
		this.discografia = new ArrayList<>();
		this.podcasts = new ArrayList<>(); 
	}
	
	// Método para o Criador criar um álbum novo
	public Album criarAlbum(int id, String titulo, String tipo) {		
		Album novo = new Album(id, titulo, tipo);
		this.adicionarAlbum(novo);
		return novo; 
	}
	
	// Método para adicionar um novo álbum à discografia
	public boolean adicionarAlbum(Album a) {
		if (this.discografia.contains(a) || a == null) {
			return false;
		}
		
		this.discografia.add(a);
		return true;
	}
	
	// Método para remover um álbum da discografia
	public boolean removerAlbum(Album a) {
		return this.discografia.remove(a);
	}
	
	// Método para mostrar a discografia completa do Criador, com verificação
	public void mostrarAlbuns() {
		for (Album a : this.getDiscografia()) {
			System.out.println(a.getTitulo());
		}
	}
	
	// Método para mostrar os podcasts do Criador
	public void mostrarPodcasts() {
		for (Podcast p : this.getPodcasts()) {
			System.out.println(p.getNome());
		}
	}
	
	// Método para o Criador criar um podcast novo
	public Podcast criarPodcast(int id, String nome, String tema) {
		Podcast novo = new Podcast(id, nome, tema);
		this.adicionarPodcast(novo);
		return novo; 
	}
	
	// Método para adicionar um podcast à lista do Criador, com verificação
	public boolean adicionarPodcast(Podcast p) {
		if (this.podcasts.contains(p) || p == null) {
			return false;
		}
		
		this.podcasts.add(p);
		return true;
	}
	
	// Método para remover um podcast da lista
	public boolean removerPodcast(Podcast p) {
		return this.podcasts.remove(p);
	}
	
	// Método para recuperar os dados do Criador
	@Override 
	public String obterDados() {
		return "Nome artístico: " + this.getNomeArtistico() + "\n" +
			   "Biografia: " + this.getBiografia() + "\n" +
			   "Ouvintes mensais: " + this.getOuvintesMensais() + "\n" +
			   "Verificado: " + this.isVerificado();
	}
}
