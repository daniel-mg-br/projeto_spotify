package model.actors;

import model.content.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

// Classe Ouvinte, filha de Usuário, representando um ouvinte da plataforma de streaming
public class Ouvinte extends Usuario {
	// Atributos privados
	private int totalMinutos;
	private String generoFavorito;
	
	// Listas para os playlist, álbuns e podcasts que o usuário escuta
	private List <Playlist> playlists;
	private List <Album> albunsFavoritos;
	private List <Podcast> podcastsFavoritos;
	
	// Métodos Getter e Setter padrão
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
	
	// Método Construtor
	public Ouvinte(Conta conta, int id, String nome, String sexo, Date aniversario) {
		super(conta, id, nome, sexo, aniversario);
		this.totalMinutos = 0;
		this.generoFavorito = "";
		
		this.playlists = new ArrayList<>();
		this.albunsFavoritos = new ArrayList<>();
		this.podcastsFavoritos = new ArrayList<>();
	}
	
	// Método para calcular o gênero favorito do usuário (em breve)
	public String obterGeneroFavorito() {
		return "";
	}
	
	// Método para verificar se o Ouvinte tem o plano premium
	public boolean isPremium() {
		return this.getConta().getPlano().equalsIgnoreCase("premium");
	}
	
	// Método para mostrar as playlists criadas pelo usuário
	public void mostrarPlaylists() {
		for (Playlist p : this.getPlaylists()) {
			System.out.println(p.getTitulo());
		}
	}
	
	// Método para o usuário criar uma playlist nova
	public Playlist criarPlaylist(int id, String nome, String descricao) {
		Playlist nova =new Playlist(id, nome, descricao);
		this.adicionarPlaylist(nova);
		return nova;
	}
	
	// Método para adicionar playlists à lista
	public boolean adicionarPlaylist(Playlist p) {
		if (this.playlists.contains(p) || p == null) {
			return false;
		}
		
		this.playlists.add(p);
		return true;
	}
	
	// Método para remover playlists da lista
	public boolean removerPlaylist(Playlist p) {
		return this.playlists.remove(p);
	}
	
	
	// Método para mostrar o título dos álbuns favoritos do ouvinte
	public void mostrarAlbunsFav() {
		for (Album a: this.getAlbunsFavoritos()) {
			System.out.println(a.getTitulo());
		}
	}
	
	// Método para adicionar álbuns favoritos
	public boolean adicionarAlbumFav(Album a) {
		if (this.albunsFavoritos.contains(a) || a == null) {
			return false;
		}
		
		this.albunsFavoritos.add(a);
		return true;
	}
	
	// Método para remover um álbum dos favoritos
	public boolean removerAlbumFav(Album a) {
		return this.albunsFavoritos.remove(a);
	}
	
	// Método para mostrar os podcasts favoritos do ouvinte
	public void mostrarPodcastsFav() {
		for (Podcast p : this.getPodcastsFavoritos()) {
			System.out.println(p.getNome());
		}
	}
	
	// Método para adicionar podcasts aos favoritos
	public boolean adicionarPodcastFav(Podcast p) {
		if (this.podcastsFavoritos.contains(p) || p == null) {
			return false;
		}
		
		this.podcastsFavoritos.add(p);
		return true;
	}
	
	// Método para remover podcasts dos favoritos
	public boolean removerPodcastFav(Podcast p) {
		return this.podcastsFavoritos.remove(p);
	}
	
	// Método para retornar os dados do ouvinte
	@Override
	public String obterDados() {
		String dadosSuper = super.obterDados();
		return dadosSuper + "\n" +
			   "Total de minutos: "	+ this.getTotalMinutos() + "\n" +
			   "Gênero favorito: " + this.getGeneroFavorito() + "\n" +
			   "Premium: " + this.isPremium();
	}
}
