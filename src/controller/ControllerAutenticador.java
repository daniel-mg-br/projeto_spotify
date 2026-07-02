package controller;

import dao.*;  	  
import model.actors.*;
import java.time.LocalDate;

/**
 * Classe ControllerAutenticator para autenticação (login) e cadastro de usuários.
 */
public class ControllerAutenticador {
	// Classes DAO como atributos para manipulação de dados.
	private ContaDAO contaDAO;
	private OuvinteDAO ouvinteDAO;
	private CriadorDAO criadorDAO;
	private AdministradorDAO adminDAO; 
	
	// Atributo para guardar o usuário logado
	private static Usuario usuarioLogado = null;
	
	/**
	 * Método construtor do Controller instanciando as classes DAO.
	 */
	public ControllerAutenticador() {
		this.contaDAO = new ContaDAO();
		this.ouvinteDAO = new OuvinteDAO();
		this.criadorDAO = new CriadorDAO();
		this.adminDAO = new AdministradorDAO();
	}
	
	/**
	 * Método auxiliar para recuperar o usuario logado.
	 * 
	 * @return Retorna o objeto de uma das classes filhas de Usuario, com seus dados.
	 */
	public static Usuario getUsuarioLogado() {
		return usuarioLogado;
	}
	
	/**
	 * Realiza a autenticação / login do usuário no sistema, retorna o usuário ou null (se falhar).
	 * Busca o login por meio da busca com a classe DAO e faz verificações de senha e de status da conta.
	 * Depois recupera os dados do usuário em questão.
	 * 
	 * @param login 
	 * @param senha
	 * @return
	 */
	public Usuario login(String login, String senha) {
		Conta contaEncontrada = this.contaDAO.buscarLogin(login);
		
		// Validação de conta nula.
		if (contaEncontrada == null || !contaEncontrada.isAtiva()) {
			System.out.println("Erro: Usuário não cadastrado ou conta inativa!");
			return null;
		}
		
		// Validação de senha correta.
		if (!contaEncontrada.validarSenha(senha)) {
			System.out.println("Erro: senha incorreta!");
			return null;
		}
		
		// Tenta buscar um ouvinte com associado à Conta encontrada.
		for (Ouvinte o : ouvinteDAO.listarOuvintes()) {
			if (o.getConta().getId() == contaEncontrada.getId()) {
				usuarioLogado = o;
				System.out.println("Login efetuado com sucesso! Bem-vindo(a): " + o.getNome());
				return o;
			}
		}
		
		// Se der errado, tenta buscar um Criador.
		for (Criador c : criadorDAO.listarCriadores()) {
			if (c.getConta().getId() == contaEncontrada.getId()) {
				usuarioLogado = c;
				System.out.println("Login efetuado com sucesso! Bem-vindo(a): " + c.getNome());
				return c;
			}
		}
		
		// Por fim, busca um Administrador associado à Conta.
		for (Administrador a : adminDAO.listarAdministradores()) {
			if (a.getConta().getId() == contaEncontrada.getId()) {
				usuarioLogado = a;
				System.out.println("Login efetuado com sucesso! Bem-vindo(a): " + a.getNome());
				return a;
			}
		}
		
		System.out.println("Erro: Conta encontrada, mas nenhum perfil associado!");
		return null;
	}
	
	/**
	 * Método auxiliar para realizar o logout do usuário.
	 */
	public void logout() {
		usuarioLogado = null;
		System.out.println("Sessão finalizada!");
	}
	
	/**
	 * Cadastra o novo ouvinte no sistema, salvando os dados do Objeto na tabela do Banco de Dados
	 * por meio da classe DAO.
	 * 
	 * @param login Login do ouvinte;
	 * @param senha Senha do ouvinte;
	 * @param nome Nome do ouvinte;
	 * @param sexo Sexo do ouvinte;
	 * @param aniversario Aniversário do ouvinte;
	 * @return Retorna true se a operação foi bem sucedida, ou false se não.
	 */
	public boolean cadastrarOuvinte(String login, String senha, String nome, String sexo, LocalDate aniversario) {
		
		// Verifica se o login já está sendo utilizado por uma Conta existente.
		if (this.contaDAO.buscarLogin(login) != null) {
			System.out.println("Erro: este login já está sendo utilizado!");
			return false;
		}
		
		// Cria a nova Conta e salva no banco de dados.
		Conta novaConta = new Conta(login, senha);
		this.contaDAO.salvar(novaConta);
		
		// Cria um novo Ouvinte associado a essa Conta e salva.
		Ouvinte novoOuvinte = new Ouvinte(novaConta, nome, sexo, aniversario);
		return this.ouvinteDAO.salvar(novoOuvinte);
	}
	
	/**
	 * Cadastra o novo criador no sistema, salvando o objeto na lista de Usuários do banco.
	 * 
	 * @param login Login da conta do criador;
	 * @param senha Senha da conta do criador;
	 * @param nome Nome do criador;
	 * @param sexo Sexo do criador;
	 * @param aniversario Aniversário do criador;
	 * @return Retorna true se a operação foi bem sucedida, ou false se não.
	 */
	public boolean cadastrarCriador(String login, String senha, String nome, String sexo, LocalDate aniversario) {
		
		// Verifica se o login já está sendo utilizado por uma Conta existente.
		if (this.contaDAO.buscarLogin(login) != null) {
			System.out.println("Erro: este login já está sendo utilizado!");
			return false;
		}
		
		// Cria a nova Conta e salva no banco de dados.
		Conta novaConta = new Conta(login, senha);
		this.contaDAO.salvar(novaConta);
		
		// Cria um novo Criador associado a essa Conta e salva.
		Criador novoCriador = new Criador(novaConta, nome, sexo, aniversario);
		return this.criadorDAO.salvar(novoCriador);
	}
}