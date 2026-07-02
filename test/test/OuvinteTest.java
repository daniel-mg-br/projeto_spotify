package test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import model.actors.Ouvinte;

import model.actors.Conta;

import model.content.Playlist;

public class OuvinteTest {
	
	/**
     * Verifica se o método isPremium() retorna verdadeiro
     * quando a conta do ouvinte possui o plano Premium.
     */
	@Test
	void deveTestarSeIsPremium() {

		Conta conta = new Conta("gean", "123");
		
		conta.atualizarPlano("Premium");
		
		Ouvinte ouvinte = new Ouvinte(conta, "Gean", "M", LocalDate.of(2000, 01, 01));
		
		assertTrue(ouvinte.isPremium());
		
	}

    /**
     * Verifica se o método adicionarPlaylist() adiciona
     * corretamente uma playlist ao ouvinte.
     */
	@Test
	void deveAdicionarPlaylist() {
		
		Conta conta = new Conta("gean", "123");
		
		Ouvinte ouvinte = new Ouvinte(conta, "Gean", "M", LocalDate.of(2000, 01, 01));
		
		Playlist nova = new Playlist("Estudo", "Playlist para estudar", ouvinte.getId());
		
		assertTrue(ouvinte.adicionarPlaylist(nova));
		
	}

    /**
     * Verifica se o método removerPlaylist() remove corretamente
     * uma playlist previamente adicionada ao ouvinte.
     */
	@Test
	void deveRemoverPlaylist() {
		
		Conta conta = new Conta("gean", "123");
		
		Ouvinte ouvinte = new Ouvinte(conta, "Gean", "M", LocalDate.of(2000, 01, 01));
		
		Playlist nova = new Playlist("Estudo", "Playlist para estudar", ouvinte.getId());
		
		ouvinte.adicionarPlaylist(nova);
		
		assertTrue(ouvinte.removerPlaylist(nova));
		
	}

}
