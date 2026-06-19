package test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import model.actors.Ouvinte;

import model.actors.Conta;

import model.content.Playlist;

public class OuvinteTest {
	
	//Este teste deve testar se o plano foi atualizado corretamente
	@Test
	void deveTestarSeIsPremium() {

		Conta conta = new Conta("gean", "123");
		
		conta.atualizarPlano("Premium");
		
		Ouvinte ouvinte = new Ouvinte(conta, "Gean", "M", LocalDate.of(2000, 01, 01));
		
		assertTrue(ouvinte.isPremium());
		
	}
	//Este teste deve testar se uma playlist é adicionada sem problemas
	@Test
	void deveAdicionarPlaylist() {
		
		Conta conta = new Conta("gean", "123");
		
		Ouvinte ouvinte = new Ouvinte(conta, "Gean", "M", LocalDate.of(2000, 01, 01));
		
		Playlist nova = new Playlist("Estudo", "Playlist para estudar");
		
		assertTrue(ouvinte.adicionarPlaylist(nova));
		
	}
	//Testa se o método remove uma playlist corretamente
	@Test
	void deveRemoverPlaylist() {
		
		Conta conta = new Conta("gean", "123");
		
		Ouvinte ouvinte = new Ouvinte(conta, "Gean", "M", LocalDate.of(2000, 01, 01));
		
		Playlist nova = new Playlist("Estudo", "Playlist para estudar");
		
		ouvinte.adicionarPlaylist(nova);
		
		assertTrue(ouvinte.removerPlaylist(nova));
		
	}

}
