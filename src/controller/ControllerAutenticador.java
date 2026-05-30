package controller;

import database.*;
import model.actors.*;
import java.time.LocalDate;

// Classe Controller para autenticação e cadastro de usuários
public class ControllerAutenticador {
	// Atributos das classes DAO para manipulação de dados
	private ContaDAO contaDAO;
	private OuvinteDAO ouvinteDAO;
	private CriadorDAO criadorDAO;
	
	// Atributo para guardar o usuário logado
	private static Usuario usuarioLogado = null;
	
	// Método Construtor
	public ControllerAutenticador() {
		this.contaDAO = new ContaDAO();
		this.ouvinteDAO = new OuvinteDAO();
		this.criadorDAO = new CriadorDAO();
	}
	
	// Retorna o usuário logado;
	public static Usuario getUsuarioLogado() {
		return usuarioLogado;
	}
	
	// Realiza a autenticação / login do usuário no sistema, retorna o usuário ou null (se falhar)
	// Verificações: login compatível, objeto vazio, conta ativa, senha correta
	public Usuario login(String login, String senha) {
		Conta contaEncontrada = null;
		
		for (Conta c : this.contaDAO.listarContas()) {
			if (c.getLogin().equalsIgnoreCase(login)) {
				contaEncontrada = c;
				break;
			}
		}
		
		if (contaEncontrada == null || !contaEncontrada.isAtiva()) {
			System.out.println("Erro: Usuário não cadastrado ou conta inativa!");
			return null;
		}
		
		if (!contaEncontrada.validarSenha(senha)) {
			System.out.println("Erro: senha incorreta!");
			return null;
		}
		
		for (Usuario u : BancoMemoria.usuarios) {
			if (u.getConta().getId() == contaEncontrada.getId()) {
				usuarioLogado = u;
				System.out.println("Login efetuado com sucesso! Usuário: " + u.getNome());
				
				return u;
			}
		}
		
		System.out.println("Erro: Conta encontrada, mas nenhum perfil associado!");
		return null;
	}
	
	// Realiza o logout do usuário
	public void logout() {
		usuarioLogado = null;
		System.out.println("Sessão finalizada!");
	}
	
	// Cadastra o novo ouvinte no sistema, salvando o objeto na lista de Usuários do banco
	public boolean cadastrarOuvinte(String login, String senha, String nome, String sexo, LocalDate aniversario) {
		for (Conta c : this.contaDAO.listarContas()) {
			if (c.getLogin().equalsIgnoreCase(login)) {
				System.out.println("Erro: este login já está sendo utilizado!");
				return false;
			}
		}
		
		Conta novaConta = new Conta(login, senha);
		this.contaDAO.salvar(novaConta);
		
		Ouvinte novoOuvinte = new Ouvinte(novaConta, nome, sexo, aniversario);
		
		return this.ouvinteDAO.salvar(novoOuvinte);
	}
	
	// Cadastra o novo criador no sistema, salvando o objeto na lista de Usuários do banco
	public boolean cadastrarCriador(String login, String senha, String nome, String sexo, LocalDate aniversario) {
		for (Conta c : this.contaDAO.listarContas()) {
			if (c.getLogin().equalsIgnoreCase(login)) {
				System.out.println("Erro: este login já está sendo utilizado!");
				return false;
			}
		}
		
		Conta novaConta = new Conta(login, senha);
		this.contaDAO.salvar(novaConta);
		
		Criador novoCriador = new Criador(novaConta, nome, sexo, aniversario);
		return this.criadorDAO.salvar(novoCriador);
	}
}
