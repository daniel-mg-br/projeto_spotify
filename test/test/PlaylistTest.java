package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import model.content.Playlist;

import model.content.Musica;

import model.actors.Conta;

import model.actors.Ouvinte;


public class PlaylistTest {
	
    /**
     * Verifica se uma playlist é criada corretamente com os valores
     * informados no construtor.
     */
	@Test
	void deveCriarPlaylistCorretamente() {
		
		Conta conta = new Conta("gean", "123");
		
		Ouvinte ouvinte = new Ouvinte(conta, "Gean", "M", LocalDate.of(2000, 12, 23));
		
		Playlist nova = new Playlist(900, ouvinte.getId(), "Estudo", "Playlist para estudar", false, LocalDate.of(2026, 07, 23));
		
		assertEquals(900, nova.getId());
		assertEquals("Estudo", nova.getTitulo());
		assertEquals("Playlist para estudar", nova.getDescricao());
		assertFalse(nova.isCompartilhar());
		assertEquals(LocalDate.of(2026, 07, 23), nova.getCriacao());
		
	}

    /**
     * Verifica se o método adicionarMusica() adiciona corretamente
     * uma música à playlist.
     */
	@Test
	void deveAdicionarMusicaCorretamente() {
		
		
		Conta conta = new Conta("gean", "123");
		
		Ouvinte ouvinte = new Ouvinte(conta, "Gean", "M", LocalDate.of(2000, 12, 23));
		
		Musica nova = new Musica("Superstar", 3);
		
		Playlist nov = new Playlist("neon", "Playlist para curtir", ouvinte.getId());
		
		assertTrue(nov.adicionarMusica(nova));
		
	}
	
    /**
     * Verifica se o método adicionarMusica() impede a adição
     * de músicas inválidas, como músicas duplicadas ou nulas.
     */a null
	@Test
	void naoDeveAdicionarMusicaInvalida() {
		
		Musica nova = new Musica("Superstar", 3);
		
		Conta conta = new Conta("gean", "123");
		
		Ouvinte ouvinte = new Ouvinte(conta, "Gean", "M", LocalDate.of(2000, 12, 23));
		
		Playlist nov = new Playlist("neon", "Playlist para curtir", ouvinte.getId());
		
		nov.adicionarMusica(nova);
		
		assertFalse(nov.adicionarMusica(nova));
		assertFalse(nov.adicionarMusica(null));		
	}

     /**
      * Verifica se o método removerMusica() remove corretamente
      * uma música previamente adicionada à playlist.
      */
	@Test
	void deveRemoverMusicaCorretamente() {
		
		Musica nova = new Musica("Superstar", 3);
		
		Conta conta = new Conta("gean", "123");
		
		Ouvinte ouvinte = new Ouvinte(conta, "Gean", "M", LocalDate.of(2000, 12, 23));
		
		Playlist nov = new Playlist("neon", "Playlist para curtir", ouvinte.getId());
		
		nov.adicionarMusica(nova);
		
		assertTrue(nov.removerMusica(nova));
		
	}

}
