package controller;

import dao.*;  
import model.actors.*;
import model.content.*;

/**
 * Classe Controller para as responsabilidades do Administrador.
 */
public class ControllerAdmin {
	// Objetos DAO como atributos para manipulação de dados.
	private ContaDAO contaDAO;
	private OuvinteDAO ouvinteDAO;
	private CriadorDAO criadorDAO;
	private MusicaDAO musicaDAO;
	private EpisodioDAO episodioDAO;
	
	/**
	 * Método Construtor do Controller.
	 */
	public ControllerAdmin() {
		this.contaDAO = new ContaDAO();
		this.ouvinteDAO = new OuvinteDAO();
		this.criadorDAO = new CriadorDAO();
		this.musicaDAO = new MusicaDAO();
		this.episodioDAO = new EpisodioDAO();
	}
	
	/**
	 * Método auxiliar para recuperar o Administrador logado.
	 * 
	 * @return Retorna o objeto de Administrador com seus dados, ou null se a operação falhar.
	 */
	private Administrador getAdminLogado() {
		Usuario usuario = ControllerAutenticador.getUsuarioLogado();
		
		if (usuario != null && usuario instanceof Administrador) {
			return (Administrador) usuario;
		}
		
		return null;
	}
	
	/**
	 * Método para suspender usuário, com verificações de acesso e de tipo de usuário.
	 * 
	 * @param idUsuario ID do usuário a ser suspenso.
	 * @return Retorna true se a operação for bem sucedida, ou false se não.
	 */
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
	
	/**
	 *  Método para remover conteúdos registrados, sejam músicas ou episódios de podcast. 
	 *  As playlists / álbuns / podcasts também são atualizados, devido ao ON DELETE CASCADE.
	 *  
	 * @param idConteudo ID do conteúdo a ser removido.
	 * @return Retorna true se a operação for bem sucedida e false se não.
	 */
	public boolean removerConteudo(int idConteudo) {
		Administrador admin = this.getAdminLogado();
		
		if (admin == null) {
			System.out.println("Acesso negado!");
			return false;
		}
		
		Musica musicaRemover = this.musicaDAO.buscarId(idConteudo);
		
		if (musicaRemover != null) {
			this.musicaDAO.deletar(idConteudo);
			System.out.println("Música removida com sucesso!");
			return true;
		}
		
		Episodio episodioRemover = this.episodioDAO.buscarId(idConteudo);
		
		if (episodioRemover != null) {
			this.episodioDAO.deletar(idConteudo);
			System.out.println("Episódio de podcast removido com sucesso!");
			return true;
		}
		
		System.out.println("Erro: nenhum conteúdo encontrado com esse ID!");
		return false;
	}
}