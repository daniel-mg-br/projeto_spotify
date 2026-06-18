package test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import model.actors.Conta;

import model.actors.Criador;

import model.actors.Administrador;

import model.content.Episodio;

public class AdministradorTest {
	
	//Testa se a consulta ao usuario é efetuada com sucesso
	@Test
	void deveConsultarUsuario() {
		
		Conta conta = new Conta("gean", "123");
		
		Criador usuario = new Criador(conta, "Daniel", "M", LocalDate.of(2000, 1, 1));
		
		Administrador administrador = new Administrador(conta, "Gean", "M", LocalDate.of(2000, 1, 1), "333");
		
		assertSame(usuario, administrador.consultarUsuario(usuario));
	}
	//Testa se a consulta ao conteudo é efetuada com sucesso
	@Test
	void deveConsultarConteudo() {
		
		Conta conta = new Conta("gean", "123");
		
		Administrador administrador = new Administrador(conta, "Gean", "M", LocalDate.of(2000, 1, 1), "333");
		
		Episodio conteudo = new Episodio("Sintonia", 23);
		
		assertSame(conteudo, administrador.consultarConteudo(conteudo));
		
	}
	//Testa se os dados são obtidos corretamente
	@Test
	void deveObterDados() {
		
		Conta conta = new Conta("gean", "123");
		
		Administrador administrador = new Administrador(conta, "Gean", "M", LocalDate.of(2000, 1, 1), "333");
		
		assertTrue(administrador.obterDados().contains("Gean"));
		assertTrue(administrador.obterDados().contains("M"));
		assertTrue(administrador.obterDados().contains("333"));
		
	}

}
