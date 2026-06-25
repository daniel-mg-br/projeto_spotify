package controller;

import dao.*;   
import model.actors.*;
import model.content.*;

/*
 * Classe Controller para as responsabilidades do criador de conteúdo.
 */
public class ControllerCriador {
	// Objetos DAO como atributos para a manipulação de dados.
    private AlbumDAO albumDAO;
    private PodcastDAO podcastDAO;
    private MusicaDAO musicaDAO;
    private EpisodioDAO episodioDAO;

    /**
     * Método Construtor do Controller.
     */
    public ControllerCriador() {
        this.albumDAO = new AlbumDAO();
        this.podcastDAO = new PodcastDAO();
        this.musicaDAO = new MusicaDAO();
        this.episodioDAO = new EpisodioDAO();
    }
    
    /**
     * Método auxiliar para recuperar o Criador logado.
     * 
     * @return Retorna o objeto de Criador com seus dados.
     */
    private Criador getCriadorLogado() {
    	Usuario usuario = ControllerAutenticador.getUsuarioLogado();
    	
    	if (usuario != null && usuario instanceof Criador) {
    		return (Criador) usuario;
    	}
    	
    	return null;
    }
    
    /**
     * Método para criar um álbum novo e registrá-lo no banco de dados.
     * 
     * @param titulo Título do álbum;
     * @param tipo Tipo do álbum;
     * @param criadorId ID do Criador de conteúdo;
     * @return Retorna true se a operação for bem sucedida, e false se não.	
     */
    public boolean criarAlbum(String titulo, String tipo, int criadorId) {
    	Criador criador = this.getCriadorLogado();
    	if (criador == null) return false;
    	
    	Album novoAlbum = new Album(titulo, tipo, criadorId);
    	this.albumDAO.salvar(novoAlbum);
    	
    	criador.adicionarAlbum(novoAlbum);
    	return true;
    }
    
    /**
     * Método para adicionar uma música ao álbum, com verificações de objetos vazios e de existência do álbum.
     * 
     * @param idAlbum ID do álbum a receber a música;
     * @param titulo Título da música;
     * @param duracao Duração em minutos da música;
     * @param genero Gênero musical;
     * @param letra Letra da música.
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean adicionarMusicaAlbum(int idAlbum, String titulo, int duracao, String genero, String letra) {
    	Criador criador = this.getCriadorLogado();
    	Album album = this.albumDAO.buscarId(idAlbum);
    	
    	if (criador == null || album == null || album.getCriadorId() != criador.getId()) return false;
    		
    	Musica novaMusica = new Musica(titulo, duracao);
    	novaMusica.setGenero(genero);
    	novaMusica.setLetra(letra);
    	this.musicaDAO.salvar(novaMusica);
    	
    	album.adicionarFaixa(novaMusica);
    	this.albumDAO.atualizar(album);
    	
    	System.out.println("Faixa " + titulo + " adicionada ao álbum!");
    	return true;
    }
    
    /**
     * Método para remover uma música do álbum, validando os objetos e suas relações.
     * 
     * @param idAlbum ID do álbum a perder a música;
     * @param idMusica ID da música a ser removida;
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean removerMusicaAlbum(int idAlbum, int idMusica) {
    	Criador criador = this.getCriadorLogado();
    	Album album = this.albumDAO.buscarId(idAlbum);
    	Musica musica = this.musicaDAO.buscarId(idMusica);
    	
    	if (criador == null || album == null || musica == null) return false;
    	
    	if (album.getCriadorId() != criador.getId()) {
    		System.out.println("Erro: acesso negado ao álbum!");
    		return false;
    	}
    	
    	boolean pertenceAoAlbum = album.getMusicas().stream().anyMatch(m -> m.getId() == musica.getId());
    	if (!pertenceAoAlbum) {
    		System.out.println("Erro: essa música não pertence ao álbum!");
    		return false;
    	}
    	
    	album.removerFaixa(musica);
    	this.albumDAO.atualizar(album);
    	this.musicaDAO.deletar(idMusica);
    	
    	System.out.println("Música removida do álbum!");
    	return true;
    }
    
    /**
     * Método para lançar um álbum (status = true).
     * 
     * @param idAlbum ID do álbum a ser lançado;
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean lancarAlbum(int idAlbum) {
    	Criador criador = this.getCriadorLogado();
    	Album album = this.albumDAO.buscarId(idAlbum);
    	
    	if (criador != null && album != null && album.getCriadorId() == criador.getId()) {
    		album.lancarAlbum();
    		this.albumDAO.atualizar(album);
    		return true;
    	}
    	return false;
    }
    
    /**
     * Método para deletar um álbum existente, garante que as músicas também são removidas.
     * 
     * @param idAlbum ID do álbum a ser removido;
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean deletarAlbum(int idAlbum) {
    	Criador criador = this.getCriadorLogado();
    	Album album = this.albumDAO.buscarId(idAlbum);
    	
    	if (criador == null || album == null || album.getCriadorId() != criador.getId()) return false;
    	
    	for (Musica faixa : album.getMusicas()) {
    		this.musicaDAO.deletar(faixa.getId());
    	}
    	
    	criador.removerAlbum(album);
    	this.albumDAO.deletar(idAlbum);
    	
    	System.out.println("Álbum e suas faixas removidos!");
    	return true;
    }
    
    /**
     * Método para criar um podcast novo e salvá-lo no banco de dados.
     * 
     * @param nome Nome do podcast novo;
     * @param tema Tema do podcast;
     * @param criadorId ID do criador de conteúdo responsável;
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean criarPodcast(String nome, String tema, int criadorId	) {
    	Criador criador = this.getCriadorLogado();
    	if (criador == null) return false;
    	
    	Podcast novoPodcast = new Podcast(nome, tema, criadorId);
    	this.podcastDAO.salvar(novoPodcast);
    	
    	criador.adicionarPodcast(novoPodcast);
    	return true;
    }
    
    /**
     * Método para adicionar um episódio ao podcast, com verificação de dados e relacionamentos.
     * 
     * @param idPodcast ID do podcast a receber o episódio;
     * @param titulo Titulo do episódio;
     * @param duracao Duração em minutos do episódio;
     * @param numEpisodio Número do episódio;
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean adicionarEpPodcast(int idPodcast, String titulo, int duracao, int numEpisodio) {
    	Criador criador = this.getCriadorLogado();
    	Podcast podcast = this.podcastDAO.buscarId(idPodcast);
    	
    	if (criador == null || podcast == null || podcast.getCriadorId() != criador.getId()) return false;
    	
    	Episodio novoEp = new Episodio(titulo, duracao);
    	novoEp.setNumEpisodio(numEpisodio);
    	this.episodioDAO.salvar(novoEp);
    	
    	podcast.adicionarEp(novoEp);
    	this.podcastDAO.atualizar(podcast);
    	
    	System.out.println("Episódio adicionado ao podcast!");
    	return true;
    }
    
    /**
     * Método para remover um episódio do podcast, com tratamento do podcast e do episódio.
     * 
     * @param idPodcast ID do podcast a perder o episódio;
     * @param idEpisodio ID do episódio a ser removido;
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean removerEpPodcast(int idPodcast, int idEpisodio) {
    	Criador criador = this.getCriadorLogado();
    	Podcast podcast = this.podcastDAO.buscarId(idPodcast);
    	Episodio episodio = this.episodioDAO.buscarId(idEpisodio);
    	
    	if (criador == null || podcast == null || episodio == null) return false;
    	
    	if (podcast.getCriadorId() != criador.getId()) {
    		System.out.println("Erro: acesso negado ao podcast!");
    		return false;
    	}
    	
    	boolean pertenceAoPodcast = podcast.getEpisodios().stream().anyMatch(ep -> ep.getId() == episodio.getId());
    	if (!pertenceAoPodcast) {
    		System.out.println("Erro: episódio não encontrado!");
    		return false;
    	}
    	
    	podcast.removerEp(episodio);
    	this.podcastDAO.atualizar(podcast);
    	this.episodioDAO.deletar(idEpisodio);
    	
    	System.out.println("Episódio removido do podcast!");
    	return true;
    }
    
    /**
     * Método para remover o podcast, garantindo que os episódios também sejam removidos.
     * 
     * @param idPodcast ID do podcast a ser removido;
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean deletarPodcast(int idPodcast) {
    	Criador criador = this.getCriadorLogado();
    	Podcast podcast = this.podcastDAO.buscarId(idPodcast);
    	
    	if (criador == null || podcast == null || podcast.getCriadorId() != criador.getId()) return false;
    	
    	for (Episodio ep : podcast.getEpisodios()) {
    		this.episodioDAO.deletar(ep.getId());
    	}
    	
    	criador.removerPodcast(podcast);
    	this.podcastDAO.deletar(idPodcast);
    	
    	System.out.println("Podcasts e seus episódios removidos!");
    	return true;
    }
}