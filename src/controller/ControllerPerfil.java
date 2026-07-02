package controller;

import dao.*;   
import model.actors.*;

/*
 * Classe Controller para alteração de dados envolvendo a Conta e o Usuário.
 */
public class ControllerPerfil {
	// Classes DAO como atributos para manipulação de dados.
	private ContaDAO contaDAO;
	private OuvinteDAO ouvinteDAO;
	private CriadorDAO criadorDAO;
	private AdministradorDAO adminDAO;
	
	/*
	 * Método Construtor do Controller instanciando as classes DAO.
	 */
	public ControllerPerfil() {
		this.contaDAO = new ContaDAO();
		this.ouvinteDAO = new OuvinteDAO();
		this.criadorDAO = new CriadorDAO();
		this.adminDAO = new AdministradorDAO();
	}
	
	/**
	 * Método para recuperar o usuário logado.
	 * 
	 * @return Retorna o objeto da classe filha correspondente da classe Usuario.
	 */
	private Usuario getUsuarioLogado() {
		return ControllerAutenticador.getUsuarioLogado();
	}
	
	/**
	 * Método para atualizar o nome e o sexo do usuário, com verificações de tipo de usuário e valores.
	 * 
	 * @param novoNome Novo nome do usuário (se aplicável);
	 * @param novoSexo Novo sexo do usuário (se aplicável);
	 * @return Retorna true se a operação foi bem sucedida, e false se não.
	 */	
	public boolean atualizarDadosPessoais(String novoNome, String novoSexo) {
		Usuario usuario = this.getUsuarioLogado();
		
		// Validação de usuário nulo.
		if (usuario == null) {
			System.out.println("Erro: nenhum usuário logado!");
			return false;
		}
		
		// Tenta atualizar o nome e o sexo do Objeto do usuário logado.
		boolean alterouNome = usuario.alterarNome(novoNome);
		boolean alterouSexo = usuario.alterarSexo(novoSexo);
		
		// Aplica a atualização no banco de dados, escolhendo o DAO de acordo com o tipo de usuário atualizado.
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
	
	/**
	 * Método para trocar a senha da conta, com validação de valores.
	 * 
	 * @param senhaAntiga Senha antiga do usuário;
	 * @param novaSenha Nova senha do usuário;
	 * @return Retorna true se a operação foi bem sucedida, e false se não.
	 */
	public boolean trocarSenha(String senhaAntiga, String novaSenha) {
		
		// Verificação de usuário nulo.
		Usuario usuario = this.getUsuarioLogado();
		if (usuario == null) return false;
		
		Conta conta = usuario.getConta();
		
		// Verifica se as senha nova é igual à antiga.
		if (!conta.validarSenha(senhaAntiga)) {
			System.out.println("Erro: senha antiga incorreta!");
			return false;
		}
		
		// Tenta alterar a senha no objeto, se der certo, atualiza o banco de dados.
		boolean alterou = conta.alterarSenha(novaSenha);
		
		if (alterou) {
			this.contaDAO.atualizar(conta);
			System.out.println("Senha alterada com sucesso!");
			return true;
		}
		
		System.out.println("Erro: senha nula ou igual a atual!");
		return false;
	}
	
	/**
	 * Método para mudar o plano do usuário (free <---> premium), com verificação.
	 * 
	 * @param novoPlano Novo plano da conta do usuário;
	 * @return Retorna true se a operação foi bem sucedida, e false se não.
	 */
	public boolean mudarPlano(String novoPlano) {
		
		// Verificação de usuário nulo.
		Usuario usuario = this.getUsuarioLogado();
		if (usuario == null) return false;
		
		Conta conta = usuario.getConta();
		
		// Tenta atualizar o status do objeto da Conta recuperada.
		boolean alterou = conta.atualizarPlano(novoPlano);
		
		// Se der certo, atualiza o banco de dados.
		if (alterou) {
			this.contaDAO.atualizar(conta);
			System.out.println("Plano atualizado para: " + novoPlano + "!");
			return true;
		}
		
		System.out.println("Erro: plano informado é inválido!");
		return false;
	}
	
	/**
	 * Método para atualizar os dados do perfil do criador de conteúdo.
	 * 
	 * @param nomeArtistico Novo nome artístico do criador;
	 * @param biografia Nova biografia do Criador;
	 * @return Retorna true se a operação foi bem sucedida, e false se não.
	 */
	public boolean atualizarPerfilCriador(String nomeArtistico, String biografia) {
		Usuario usuario = this.getUsuarioLogado();
			
		if (usuario == null) {
			System.out.println("Erro: nenhum usuário logado!");
			return false;
		}
		
		if (!(usuario instanceof Criador)) {
			System.out.println("Erro: acesso negado! Apenas criadores!");
			return false;
		}
		
		Criador criador = (Criador) usuario;
		if (nomeArtistico == null || biografia == null) {
			System.out.println("Dados inválidos!");
			return false;
		}
		
		criador.setNomeArtistico(nomeArtistico);
		criador.setBiografia(biografia);
		
		boolean atualizou = this.criadorDAO.atualizar(criador);
		if (atualizou) {
			System.out.println("Perfil atualizado com sucesso!");
		} else {
			System.out.println("Erro ao atualizar perfil!");
		}
		
		return atualizou;
	}
	
	/**
	 * Atualiza a credencial do Administrador do sistema.
	 * 
	 * @param novaCredencial String com a nova credencial;
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean atualizarCredencialAdmin(String novaCredencial) {
        Usuario usuario = this.getUsuarioLogado();
        
        if (usuario == null) {
            System.out.println("Erro: nenhum usuário logado!");
            return false;
        }
        
        // Validação de segurança
        if (!(usuario instanceof Administrador)) {
            System.out.println("Erro: Acesso negado. Apenas administradores podem realizar esta ação!");
            return false;
        }
        
        if (novaCredencial == null || novaCredencial.trim().isEmpty()) {
            System.out.println("Erro: Credencial inválida!");
            return false;
        }
        
        Administrador admin = (Administrador) usuario;
        admin.setCredencial(novaCredencial); 
        
        boolean atualizou = this.adminDAO.atualizar(admin);
        
        if (atualizou) {
            System.out.println("Credencial de administrador atualizada com sucesso!");
        } else {
            System.out.println("Erro ao tentar salvar a credencial no banco de dados.");
        }
        
        return atualizou;
    }
}
