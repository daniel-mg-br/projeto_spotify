package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import model.content.Playlist;

import model.content.Musica;


public class PlaylistTest {
	//Testa se o método está criando playlist corretamente e com os dados corretos
	@Test
	void deveCriarPlaylistCorretamente() {
		
		Playlist nova = new Playlist(900, "Estudo", "Playlist para estudar", false, LocalDate.of(2026, 07, 23), 12);
		
		assertEquals(900, nova.getId());
		assertEquals("Estudo", nova.getTitulo());
		assertEquals("Playlist para estudar", nova.getDescricao());
		assertFalse(nova.isCompartilhar());
		assertEquals(LocalDate.of(2026, 07, 23), nova.getCriacao());
		assertEquals(12, nova.getTotalMusicas());
	}
	//Testa se o método está adicionando musica corretamente
	@Test
	void deveAdicionarMusicaCorretamente() {
		
		Musica nova = new Musica("Superstar", 3);
		
		Playlist nov = new Playlist("neon", "Playlist para curtir");
		
		assertTrue(nov.adicionarMusica(nova));
		
	}
	//Testa se o método impede que se adicione a mesma música duas vezes, ou uma música null
	@Test
	void naoDeveAdicionarMusicaInvalida() {
		
		Musica nova = new Musica("Superstar", 3);
		
		Playlist nov = new Playlist("neon", "Playlist para curtir");
		
		nov.adicionarMusica(nova);
		
		assertFalse(nov.adicionarMusica(nova));
		assertFalse(nov.adicionarMusica(null));		
	}
	//Testa se o método remove uma música corretamente
	@Test
	void deveRemoverMusicaCorretamente() {
		
		Musica nova = new Musica("Superstar", 3);
		
		Playlist nov = new Playlist("neon", "Playlist para curtir");
		
		nov.adicionarMusica(nova);
		
		assertTrue(nov.removerMusica(nova));
		
	}

}
