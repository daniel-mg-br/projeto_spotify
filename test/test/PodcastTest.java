package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import model.content.Podcast;

import model.content.Episodio;

public class PodcastTest {
	
	@Test
	void deveCriarPodcastCorretamente() {
		
		LocalDate data = LocalDate.of(2026, 06, 18);
		
		Podcast novo = new Podcast(344, "Saude", "Podcast sobre saude", data, 13);
		
		assertEquals(344, novo.getId());
		assertEquals("Saude", novo.getNome());
		assertEquals("Podcast sobre saude", novo.getTema());
		assertEquals(data, novo.getCriacao());
		assertEquals(13, novo.getNumEpisodios());
	}
	
	@Test
	void deveAdicionarEpsodioCorretamente() {
		
		LocalDate data = LocalDate.of(2026, 06, 18);
		
		Podcast novo = new Podcast(344, "Saude", "Podcast sobre saude", data, 13);
		
		Episodio nov = new Episodio("Alimentacao", 15);
		
		assertTrue(novo.adicionarEp(nov));
		
	}
	
	@Test
	void naoDeveAdicionarEpsodioInvalido() {
		
		LocalDate data = LocalDate.of(2026, 06, 18);
		
		Podcast novo = new Podcast(344, "Saude", "Podcast sobre saude", data, 13);
		
		Episodio nov = new Episodio("Alimentacao", 15);
		
		novo.adicionarEp(nov);
		
		assertFalse(novo.adicionarEp(nov));
		assertFalse(novo.adicionarEp(null));
		
	}
	
	@Test
	void deveRemoverEpsodioCorretamente() {
				
		LocalDate data = LocalDate.of(2026, 06, 18);
		
		Podcast novo = new Podcast(344, "Saude", "Podcast sobre saude", data, 13);
		
		Episodio nov = new Episodio("Alimentacao", 15);
		
		novo.adicionarEp(nov);
		
		assertTrue(novo.removerEp(nov));
		
	}

}
