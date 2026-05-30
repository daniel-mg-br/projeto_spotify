package controller;

import database.*;
import model.actors.*;
import model.content.*;

// Classe Controller para as responsabilidades do Administrador
public class ControllerAdmin {
	// Objetos DAO para manipulação de dados
	private ContaDAO contaDAO;
	private OuvinteDAO ouvinteDAO;
	private CriadorDAO criadorDAO;
	private MusicaDAO musicaDAO;
	private EpisodioDAO episodioDAO;
	private AlbumDAO albumDAO;
	private PlaylistDAO playlistDAO;
	private PodcastDAO podcastDAO;
	
	// Método Construtor
	public ControllerAdmin() {
		this.contaDAO = new ContaDAO();
		this.ouvinteDAO = new OuvinteDAO();
		this.criadorDAO = new CriadorDAO();
		this.musicaDAO = new MusicaDAO();
		this.episodioDAO = new EpisodioDAO();
		this.albumDAO = new AlbumDAO();
		this.playlistDAO = new PlaylistDAO();
		this.podcastDAO = new PodcastDAO();
	}
	
	// Método para recuperar o Administrador logado
	private Administrador getAdminLogado() {
		Usuario usuario = ControllerAutenticador.getUsuarioLogado();
		
		if (usuario != null && usuario instanceof Administrador) {
			return (Administrador) usuario;
		}
		
		return null;
	}
	
	// Método para suspender usuário, com verificações de acesso e de tipo de usuário
	public boolean suspenderUsuario(int idUsuario) {
		Administrador admin = this.getAdminLogado();
		
		if (admin == null) {
			System.out.println("Acesso negado: apenas administradores permitidos!");
			return false;
		}
		
		Usuario usuarioAlvo = this.ouvinteDAO.buscarId(idUsuario);
		
		if (usuarioAlvo == null) {
			usuarioAlvo = this.criadorDAO.buscarId(idUsuario);
		}
		
		if (usuarioAlvo == null) {
			System.out.println("Erro: Usuário não encontrado!");
			return false;
		}
		
		Conta contaAlvo = usuarioAlvo.getConta();
		contaAlvo.setStatus("Suspenso");
		
		boolean atualizou = this.contaDAO.atualizar(contaAlvo);
		
		if (atualizou) {
			System.out.println("Conta do usuário " + usuarioAlvo.getNome() + " suspensa!");
		}
		
		return atualizou;
	}
	
	// Método para remover conteúdo, sejam músicas ou episódios de podcast
	// As playlists / álbuns / podcasts também são atualizados
	public boolean removerConteudo(int idConteudo) {
		Administrador admin = this.getAdminLogado();
		
		if (admin == null) {
			System.out.println("Acesso negado!");
			return false;
		}
		
		Musica musicaRemover = this.musicaDAO.buscarId(idConteudo);
		
		if (musicaRemover != null) {
			for (Album album : this.albumDAO.listarAlbuns()) {
				if (album.getMusicas().contains(musicaRemover)) {
					album.removerFaixa(musicaRemover);
					this.albumDAO.atualizar(album);
				}
			}
			
			for (Playlist playlist : this.playlistDAO.listarPlaylists()) {
				if (playlist.getMusicas().contains(musicaRemover)) {
					playlist.removerMusica(musicaRemover);
					this.playlistDAO.atualizar(playlist);
				}
			}
			
			this.musicaDAO.deletar(idConteudo);
			System.out.println("Música removida com sucesso!");
			return true;
		}
		
		Episodio episodioRemover = this.episodioDAO.buscarId(idConteudo);
		
		if (episodioRemover != null) {
			for (Podcast podcast : this.podcastDAO.listarPodcasts()) {
				if (podcast.getEpisodios().contains(episodioRemover)) {
					podcast.removerEp(episodioRemover);
					this.podcastDAO.atualizar(podcast);
				}
			}
			
			this.episodioDAO.deletar(idConteudo);
			System.out.println("Episódio de podcast removido com sucesso!");
			return true;
		}
		
		System.out.println("Erro: nenhum conteúdo encontrado com esse ID!");
		return false;
	}
}