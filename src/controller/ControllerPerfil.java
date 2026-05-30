package controller;

import database.*;
import model.actors.*;

// Classe Controller para alteração de dados envolvendo a Conta e o Usuário
public class ControllerPerfil {
	// Classes DAO para manipulação
	private ContaDAO contaDAO;
	private OuvinteDAO ouvinteDAO;
	private CriadorDAO criadorDAO;
	private AdministradorDAO adminDAO;
	
	// Método Construtor
	public ControllerPerfil() {
		this.contaDAO = new ContaDAO();
		this.ouvinteDAO = new OuvinteDAO();
		this.criadorDAO = new CriadorDAO();
		this.adminDAO = new AdministradorDAO();
	}
	
	// Método para recuperar o usuário logado
	private Usuario getUsuarioLogado() {
		return ControllerAutenticador.getUsuarioLogado();
	}
	
	// Método para atualizar o nome e o sexo do usuário, com verificações de tipo de usuário e valores
	public boolean atualizarDadosPessoais(String novoNome, String novoSexo) {
		Usuario usuario = this.getUsuarioLogado();
		
		if (usuario == null) {
			System.out.println("Erro: nenhum usuário logado!");
			return false;
		}
		
		boolean alterouNome = usuario.alterarNome(novoNome);
		boolean alterouSexo = usuario.alterarSexo(novoSexo);
		
		if (alterouNome || alterouSexo) {
			if (usuario instanceof Ouvinte) 
			{
				return this.ouvinteDAO.atualizar((Ouvinte) usuario);
			} 
			else if (usuario instanceof Criador) 
			{
				return this.criadorDAO.atualizar((Criador) usuario);
			}
			else if (usuario instanceof Administrador)
			{
				return this.adminDAO.atualizar((Administrador) usuario);
			}
		}
		
		System.out.println("Aviso: nenhuma alteração realizada!");
		return false;
	}
	
	// Método para trocar a senha da conta, com validação de valores
	public boolean trocarSenha(String senhaAntiga, String novaSenha) {
		Usuario usuario = this.getUsuarioLogado();
		if (usuario == null) return false;
		Conta conta = usuario.getConta();
		
		if (!conta.validarSenha(senhaAntiga)) {
			System.out.println("Erro: senha antiga incorreta!");
			return false;
		}
		
		boolean alterou = conta.alterarSenha(novaSenha);
		
		if (alterou) {
			System.out.println("Senha alterada com sucesso!");
			return this.contaDAO.atualizar(conta);
		}
		
		System.out.println("Erro: senha nula ou igual a atual!");
		return false;
	}
	
	// Método para mudar o plano do usuário (free <---> premium), com verificação
	public boolean mudarPlano(String novoPlano) {
		Usuario usuario = this.getUsuarioLogado();
		if (usuario == null) return false;
		
		Conta conta = usuario.getConta();
		
		boolean alterou = conta.atualizarPlano(novoPlano);
		
		if (alterou) {
			System.out.println("Plano atualizado para: " + novoPlano + "!");
			return this.contaDAO.atualizar(conta);
		}
		
		System.out.println("Erro: plano informado é inválido!");
		return false;
		
	}
}
