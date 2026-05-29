package model.actors;

import model.content.*;
import java.time.LocalDate;

// Classe Administrador, filha de Usuario, representa um moderador do sistema operando sobre os dados
public class Administrador extends Usuario {
	// Atributo privado
    private String credencial;
    
    // Métodos Getter e Setter padrão
    public String getCredencial() {return this.credencial;}
    public void setCredencial(String credencial) {this.credencial = credencial;}
    
    // Método Construtor padrão
    public Administrador(Conta conta, String nome, String sexo, LocalDate aniversario, String credencial) {
    	super(conta, nome, sexo, aniversario);
        this.credencial = credencial;
    }
    
    // Método Construtor com todos os dados (recuperação bd -> objeto)
    public Administrador(Conta conta, int id, String nome, String sexo, LocalDate aniversario, String credencial) {
    	super(conta, id, nome, sexo, aniversario);
    	this.credencial = credencial;
    }
    
    // Método temporário / incompleto: operação de suspenão de usuários
    public void suspenderUsuario(Usuario usuario) {
        System.out.println("Usuário suspenso: " + usuario.obterDados());
    }
    
    // Método temporário / incompleto: operação de remoção de conteúdos
    public void removerConteudo(Conteudo conteudo) {
    	System.out.println("Conteúdo removido: " + conteudo.obterDados());
    }
    
    // Método temporário / incompleto: operação de consulta de usuários
    public Usuario consultarUsuario(Usuario usuario) {
        return usuario;
    }
    
    // Método temporário / incompleto: operação de consulta de conteúdo
    public Conteudo consultarConteudo(Conteudo conteudo) {
        return conteudo;
    }
    
    // Método para recuperar os dados do Administrador
    @Override
    public String obterDados() {
    	String superDados = super.obterDados();
    	return superDados + "\n" +
    		   "Credencial: " + this.getCredencial();	
    }
}

