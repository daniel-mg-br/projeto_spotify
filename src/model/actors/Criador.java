package model.actors;

import model.content.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe Criador, filha de Usuário, que representa um criador de conteúdo da plataforma de streaming.
 */
public class Criador extends Usuario {
	
	// Atributos privados específicos do criador.
	private String nomeArtistico, biografia;
	private int ouvintesMensais;
	private boolean verificado;
	
	// Listas para representar os álbuns e podcasts do criador de conteúdo.
	private List <Album> discografia;
	private List <Podcast> podcasts;
	
	// Métodos Getter e Setter padrão.
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
	
	/**
	 * Método Construtor padrão para instanciação.
	 * Usa os dados de Usuario (superclasse).
	 * 
	 * @param conta Conta associada ao usuário;
	 * @param nome Nome do usuário;
	 * @param sexo Sexo do usuário;
	 * @param aniversario Data de aniversário do usuário.
	 */
	public Criador(Conta conta, String nome, String sexo, LocalDate aniversario) {
		super(conta, nome, sexo, aniversario);
		this.nomeArtistico = "";
		this.biografia = "";
		this.ouvintesMensais = 0;
		this.verificado = false;
		
		this.discografia = new ArrayList<>();
		this.podcasts = new ArrayList<>();
	}
	
	/**
	 * Método Construtor com todos os dados do Criador (recuperação bd -> objeto).
	 * 
	 * @param conta Conta associada ao usuário recuperado;
	 * @param id ID do usuário recuperado;
	 * @param nome Nome do usuário recuperado;
	 * @param sexo Sexo do usuário recuperado;
	 * @param aniversario Data de aniversário do usuário;
	 * @param nomeArtistico Nome artístico do criador;
	 * @param biografia Biografia do criador recuperado;
	 * @param ouvintesMensais Ouvintes mensais do Criador (contador de engajamento);
	 * @param verificado Se o criador é verificado (true) ou não (false).
	 */
	public Criador(Conta conta, int id, String nome, String sexo, LocalDate aniversario, String nomeArtistico, String biografia, int ouvintesMensais, boolean verificado) {
		super(conta, id, nome, sexo, aniversario);
		this.nomeArtistico = nomeArtistico;
		this.biografia = biografia;
		this.ouvintesMensais = ouvintesMensais;
		this.verificado = verificado;
		
		this.discografia = new ArrayList<>();
		this.podcasts = new ArrayList<>(); 
	}
	
	/**
	 * Método para adicionar um novo álbum à discografia, com verificação de dados.
	 * 
	 * @param a Objeto do álbum com seus dados;
	 * @return Retorna true se a operação foi bem sucedida e false se não.
	 */
	public boolean adicionarAlbum(Album a) {
		boolean jaExiste = this.discografia.stream().anyMatch(album -> album.getId() == a.getId());
		
		if (jaExiste || a == null) return false;
		
		this.discografia.add(a);
		return true;
	}
	
	/**
	 * Método para remover um álbum da discografia.
	 * 
	 * @param a Objeto de Album a ser removido;
	 * @return Retorna true se a operação foi bem sucedida e false se não.
	 */
	public boolean removerAlbum(Album a) {
		return this.discografia.removeIf(album -> album.getId() == a.getId());
	}
	
	/**
	 * Método para mostrar a discografia completa do Criador.
	 */
	public void mostrarAlbuns() {
		for (Album a : this.getDiscografia()) {
			System.out.println(a.getTitulo());
		}
	}
	
	/**
	 * Método para mostrar os podcasts do Criador.
	 */
	public void mostrarPodcasts() {
		for (Podcast p : this.getPodcasts()) {
			System.out.println(p.getNome());
		}
	}

	/**
	 * Método para adicionar um podcast à lista do Criador, com verificação de dados.
	 * 
	 * @param p Objeto de podcast com seus dados;
	 * @return Retorna true se a operação foi bem sucedida e false se não.
	 */
	public boolean adicionarPodcast(Podcast p) {
		boolean jaExiste = this.podcasts.stream().anyMatch(podcast -> podcast.getId() == p.getId());
		
		if (jaExiste || p == null) return false;
		
		this.podcasts.add(p);
		return true;
	}
	
	/**
	 * Método para remover um podcast da lista.
	 * 
	 * @param p Objeto de Podcast a ser removido;
	 * @return Retorna true se a operação foi bem sucedida e false se não.
	 */
	public boolean removerPodcast(Podcast p) {
		return this.podcasts.removeIf(podcast -> podcast.getId() == p.getId());
	}
	
	/**
	 * Método auxiliar para incrementar os ouvintes mensais toda vez que um conteúdo é "consumido".
	 */
	public void adicionarEngajamento() {
		this.ouvintesMensais++;
	}
	
	/**
	 * Método para recuperar os dados do Criador.
	 * 
	 * @return Retorna os dados em formato de string.
	 */
	@Override 
	public String obterDados() {
		String superDados = super.obterDados();
		return superDados + "\n" +
			   "Nome artístico: " + this.getNomeArtistico() + "\n" +
			   "Biografia: " + this.getBiografia() + "\n" +
			   "Ouvintes mensais: " + this.getOuvintesMensais() + "\n" +
			   "Verificado: " + this.isVerificado();
	}
}