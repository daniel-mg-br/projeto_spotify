package model.actors;

import model.content.*;  
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe Ouvinte, filha de Usuário, representando um ouvinte da plataforma de streaming.
 */
public class Ouvinte extends Usuario {
	
	// Atributos privados específicos do ouvinte.
	private int totalMinutos;
	private String generoFavorito;
	
	// Listas para os playlist, álbuns e podcasts que o usuário escuta.
	private List <Playlist> playlists;
	private List <Album> albunsFavoritos;
	private List <Podcast> podcastsFavoritos;
	
	// Métodos Getter e Setter padrão.
	public int getTotalMinutos() {return this.totalMinutos;}
	public void setTotalMinutos(int minutos) {this.totalMinutos = minutos;}
	
	public String getGeneroFavorito() {return this.generoFavorito;}
	public void setGeneroFavorito(String generoFavorito) {this.generoFavorito = generoFavorito;}
	
	public List <Playlist> getPlaylists() {
		return this.playlists;
	}
	
	public List <Album> getAlbunsFavoritos() {
		return this.albunsFavoritos;
	}
	
	public List <Podcast> getPodcastsFavoritos() {
		return this.podcastsFavoritos;
	}
	
	/**
	 * Método Construtor padrão para instanciação.
	 * Usa apenas os dados de Usuario (superclasse).
	 * 
	 * @param conta Conta associada ao usuário;
	 * @param nome Nome do usuário;
	 * @param sexo Sexo do usuário;
	 * @param aniversario Data de aniversário do usuário.
	 */
	public Ouvinte(Conta conta, String nome, String sexo, LocalDate aniversario) {
		super(conta, nome, sexo, aniversario);
		this.totalMinutos = 0;
		this.generoFavorito = "";
		
		this.playlists = new ArrayList<>();
		this.albunsFavoritos = new ArrayList<>();
		this.podcastsFavoritos = new ArrayList<>();
	}
	
	/**
	 * Método Construtor com todos os dados (recuperação bd -> objeto).
	 * 
	 * @param conta Conta do usuário recuperdo;
	 * @param id ID do usuário recuperado;
	 * @param nome Nome do usuário recuperado;
	 * @param sexo Sexo do usuário recuperado;
	 * @param aniversario Aniversário do usuário;
	 * @param totalMinutos Total de minutos ouvidos;
	 * @param generoFavorito Gênero de música favorito do usuário.
	 */
	public Ouvinte(Conta conta, int id, String nome, String sexo, LocalDate aniversario, int totalMinutos, String generoFavorito) {
		super(conta, id, nome, sexo, aniversario);
		
		this.totalMinutos = totalMinutos;
		this.generoFavorito = generoFavorito;
		
		this.playlists = new ArrayList<>();
		this.albunsFavoritos = new ArrayList<>();
		this.podcastsFavoritos = new ArrayList<>();
	}
	
	/**
	 * Método para verificar se o Ouvinte tem o plano premium.
	 * 
	 * @return Retorna true se tiver, e false se não.
	 */
	public boolean isPremium() {
		return this.getConta().getPlano().equalsIgnoreCase("premium");
	}
	
	/**
	 * Método para mostrar as playlists criadas pelo usuário.
	 */
	public void mostrarPlaylists() {
		for (Playlist p : this.getPlaylists()) {
			System.out.println(p.getTitulo());
		}
	}

	/**
	 * Método para adicionar playlists à lista.
	 * 
	 * @param p Objeto da nova Playlist;
	 * @return Retorna true se a operação foi bem sucedida e false se não.
	 */
	public boolean adicionarPlaylist(Playlist p) {
		boolean jaExiste = this.playlists.stream().anyMatch(playlist -> playlist.getId() == p.getId());
		
		if (jaExiste || p == null) return false;
		
		this.playlists.add(p);
		return true;
	}
	
	/**
	 * Método para remover playlists da lista.
	 * 
	 * @param p Objeto de Playlist a ser removida;
	 * @return Retorna true se a operação foi bem sucedida e false se não.
	 */
	public boolean removerPlaylist(Playlist p) {
		return this.playlists.removeIf(playlist -> playlist.getId() == p.getId());
	}
		
	/**
	 * Método para mostrar o título dos álbuns favoritos do ouvinte.
	 */
	public void mostrarAlbunsFav() {
		for (Album a: this.getAlbunsFavoritos()) {
			System.out.println(a.getTitulo());
		}
	}
	
	/**
	 * Método para adicionar álbuns favoritos.
	 * 
	 * @param a Objeto do novo Album favorito;
	 * @return Retorna true se a operação foi bem sucedida e false se não.
	 */
	public boolean adicionarAlbumFav(Album a) {
		boolean jaExiste = this.albunsFavoritos.stream().anyMatch(album -> album.getId() == a.getId());
		
		if (jaExiste || a == null) return false;
		
		this.albunsFavoritos.add(a);
		return true;
	}
	
	/**
	 * Método para remover um álbum dos favoritos.
	 * 
	 * @param a Objeto do Album a ser removido;
	 * @return Retorna true se a operação foi bem sucedida e false se não.
	 */
	public boolean removerAlbumFav(Album a) {
		return this.albunsFavoritos.removeIf(album -> album.getId() == a.getId());
	}
	
	/**
	 * Método para mostrar os podcasts favoritos do ouvinte.
	 */
	public void mostrarPodcastsFav() {
		for (Podcast p : this.getPodcastsFavoritos()) {
			System.out.println(p.getNome());
		}
	}
	
	/**
	 * Método para adicionar podcasts aos favoritos.
	 * 
	 * @param p Objeto do novo Podcast favorito;
	 * @return Retorna true se a operação foi bem sucedida e false se não.
	 */
	public boolean adicionarPodcastFav(Podcast p) {
		boolean jaExiste = this.podcastsFavoritos.stream().anyMatch(podcast -> podcast.getId() == p.getId());
		
		if (jaExiste || p == null) return false;
		
		this.podcastsFavoritos.add(p);
		return true;
	}
	
	/**
	 * Método para remover podcasts dos favoritos.
	 * 
	 * @param p Objeto do Podcast a ser removido;
	 * @return Retorna true se a operação foi bem sucedida e false se não.
	 */
	public boolean removerPodcastFav(Podcast p) {
		return this.podcastsFavoritos.removeIf(podcast -> podcast.getId() == p.getId());
	}
	
	/**
	 * Método auxiliar para adicionar minutos ouvidos ao total do ouvinte.
	 * 
	 * @param tempo Tempo em minutos (duracaoMin ou a soma total).
	 */
		public void adicionarTempo(int tempo) {
			if (tempo > 0) this.totalMinutos += tempo;
		}
	
	/**
	 * Método para retornar os dados do ouvinte.
	 * 
	 * @return Retorna os dados em formato de string.
	 */
	@Override
	public String obterDados() {
		String dadosSuper = super.obterDados();
		return dadosSuper + "\n" +
			   "Total de minutos: "	+ this.getTotalMinutos() + "\n" +
			   "Gênero favorito: " + this.getGeneroFavorito() + "\n" +
			   "Premium: " + this.isPremium();
	}
}