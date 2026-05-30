package controller;

import database.*;
import model.actors.*;
import model.content.*;

// Classe Controller para as responsabilidades do criador de conteúdo
public class ControllerCriador {
	// Objetos DAO para  a manipulação de dados
	private CriadorDAO criadorDAO;
    private AlbumDAO albumDAO;
    private PodcastDAO podcastDAO;
    private MusicaDAO musicaDAO;
    private EpisodioDAO episodioDAO;
    private PlaylistDAO playlistDAO; // Necessário para a exclusão em cascata

    // Método Construtor
    public ControllerCriador() {
        this.criadorDAO = new CriadorDAO();
        this.albumDAO = new AlbumDAO();
        this.podcastDAO = new PodcastDAO();
        this.musicaDAO = new MusicaDAO();
        this.episodioDAO = new EpisodioDAO();
        this.playlistDAO = new PlaylistDAO();
    }
    
    // Método para recuperar o criador logado
    private Criador getCriadorLogado() {
    	Usuario usuario = ControllerAutenticador.getUsuarioLogado();
    	
    	if (usuario != null && usuario instanceof Criador) {
    		return (Criador) usuario;
    	}
    	
    	return null;
    }
    
    // Método para criar um álbum novo
    public boolean criarAlbum(String titulo, String tipo) {
    	Criador criador = this.getCriadorLogado();
    	if (criador == null) return false;
    	
    	Album novoAlbum = new Album(titulo, tipo);
    	this.albumDAO.salvar(novoAlbum);
    	
    	criador.adicionarAlbum(novoAlbum);
    	return this.criadorDAO.atualizar(criador);
    }
    
    // Método para adicionar uma música ao álbum, com verificações de objetos vazios e de existência do álbum
    public boolean adicionarMusicaAlbum(int idAlbum, String titulo, int duracao, String genero, String letra) {
    	Criador criador = this.getCriadorLogado();
    	Album album = this.albumDAO.buscarId(idAlbum);
    	
    	if (criador == null || album == null || !criador.getDiscografia().contains(album)) return false;
    		
    	Musica novaMusica = new Musica(titulo, duracao);
    	novaMusica.setGenero(genero);
    	novaMusica.setLetra(letra);
    	this.musicaDAO.salvar(novaMusica);
    	
    	album.adicionarFaixa(novaMusica);
    	this.albumDAO.atualizar(album);
    	
    	System.out.println("Faixa " + titulo + " adicionada ao álbum!");
    	return true;
    }
    
    // Método para remover uma música do álbum, validando os objetos e suas relações
    public boolean removerMusicaAlbum(int idAlbum, int idMusica) {
    	Criador criador = this.getCriadorLogado();
    	Album album = this.albumDAO.buscarId(idAlbum);
    	Musica musica = (Musica) this.musicaDAO.buscarId(idMusica);
    	
    	if (criador == null || album == null || musica == null) return false;
    	
    	if (!criador.getDiscografia().contains(album)) {
    		System.out.println("Erro: acesso negado ao álbum!");
    		return false;
    	}
    	
    	if (!album.getMusicas().contains(musica)) {
    		System.out.println("Erro: essa música não pertence ao álbum!");
    		return false;
    	}
    	
    	for (Playlist p : this.playlistDAO.listarPlaylists()) {
    		if (p.getMusicas().contains(musica)) {
    			p.removerMusica(musica);
    			this.playlistDAO.atualizar(p);
    		}
    	}
    	
    	album.removerFaixa(musica);
    	this.albumDAO.atualizar(album);
    	this.musicaDAO.deletar(idMusica);
    	
    	System.out.println("Música removida do álbum!");
    	return true;
    }
    
    // Método para lançar um álbum (status = true)
    public boolean lancarAlbum(int idAlbum) {
    	Criador criador = this.getCriadorLogado();
    	Album album = this.albumDAO.buscarId(idAlbum);
    	
    	if (criador != null && album != null && criador.getDiscografia().contains(album)) {
    		album.lancarAlbum();
    		this.albumDAO.atualizar(album);
    		return true;
    	}
    	
    	return false;
    }
    
    // Método para deletar um álbum existente, garante que as músicas também são removidas
    public boolean deletarALbum(int idAlbum) {
    	Criador criador = this.getCriadorLogado();
    	Album album = this.albumDAO.buscarId(idAlbum);
    	
    	if (criador == null || album == null || !criador.getDiscografia().contains(album)) return false;
    	
    	for (Musica faixa : album.getMusicas()) {
    		for (Playlist p: this.playlistDAO.listarPlaylists()) {
    			if (p.getMusicas().contains(faixa)) {
    				p.removerMusica(faixa);
    				this.playlistDAO.atualizar(p);
    			}
    		}
    		this.musicaDAO.deletar(faixa.getId());
    	}
    	
    	criador.removerAlbum(album);
    	this.criadorDAO.atualizar(criador);
    	this.albumDAO.deletar(idAlbum);
    	
    	System.out.println("Álbum e suas faixas removidos!");
    	return true;
    }
    
    // Método para criar um podcast novo
    public boolean criarPodcast(String nome, String tema) {
    	Criador criador = this.getCriadorLogado();
    	if (criador == null) return false;
    	
    	Podcast novoPodcast = new Podcast(nome, tema);
    	this.podcastDAO.salvar(novoPodcast);
    	
    	criador.adicionarPodcast(novoPodcast);
    	return this.criadorDAO.atualizar(criador);
    }
    
    // Método para adicionar um episódio ao podcast, com verificação de dados e relacionamentos
    public boolean adicionarEpPodcast(int idPodcast, String titulo, int duracao, int numEpisodio) {
    	Criador criador = this.getCriadorLogado();
    	Podcast podcast = this.podcastDAO.buscarId(idPodcast);
    	
    	if (criador == null || podcast == null || !criador.getPodcasts().contains(podcast)) return false;
    	
    	Episodio novoEp = new Episodio(titulo, duracao);
    	novoEp.setNumEpisodio(numEpisodio);
    	this.episodioDAO.salvar(novoEp);
    	
    	podcast.adicionarEp(novoEp);
    	this.podcastDAO.atualizar(podcast);
    	
    	System.out.println("Episódio adicionado ao podcast!");
    	return true;
    }
    
    // Método para remover um episódio do podcast, com tratamento do podcast e do episódio
    public boolean removerEpPodcast(int idPodcast, int idEpisodio) {
    	Criador criador = this.getCriadorLogado();
    	Podcast podcast = this.podcastDAO.buscarId(idPodcast);
    	Episodio episodio = (Episodio) this.episodioDAO.buscarId(idEpisodio);
    	
    	if (criador == null || podcast == null || episodio == null) return false;
    	
    	if (!criador.getPodcasts().contains(podcast)) {
    		System.out.println("Erro: acesso negado ao podcast!");
    		return false;
    	}
    	
    	if (!podcast.getEpisodios().contains(episodio)) {
    		System.out.println("Erro: episódio não encontrado!");
    		return false;
    	}
    	
    	podcast.removerEp(episodio);
    	this.podcastDAO.atualizar(podcast);
    	this.episodioDAO.deletar(idEpisodio);
    	
    	System.out.println("Episódio removido do podcast!");
    	return true;
    }
    
    // Método para remover o podcast, garantindo que os episódios também sejam removidos
    public boolean deletarPodcast(int idPodcast) {
    	Criador criador = this.getCriadorLogado();
    	Podcast podcast = this.podcastDAO.buscarId(idPodcast);
    	
    	if (criador == null || podcast == null || !criador.getPodcasts().contains(podcast)) return false;
    	
    	for (Episodio ep : podcast.getEpisodios()) {
    		this.episodioDAO.deletar(ep.getId());
    	}
    	
    	criador.removerPodcast(podcast);
    	this.criadorDAO.atualizar(criador);
    	this.podcastDAO.deletar(idPodcast);
    	
    	System.out.println("Podcasts e seus episódios removidos!");
    	return true;
    }
}
