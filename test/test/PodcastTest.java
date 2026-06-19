package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import model.content.Podcast;

import model.content.Episodio;

public class PodcastTest {
	//Testa se o método está criando um podcast corretamente e com os dados corretos
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
	//Testa se o método está adicionando um episódio corretamente
	@Test
	void deveAdicionarEpisodioCorretamente() {
		
		LocalDate data = LocalDate.of(2026, 06, 18);
		
		Podcast novo = new Podcast(344, "Saude", "Podcast sobre saude", data, 13);
		
		Episodio nov = new Episodio("Alimentacao", 15);
		
		assertTrue(novo.adicionarEp(nov));
		
	}
	//Testa se o método impede que se adicione um episódio null, ou repetido
	@Test
	void naoDeveAdicionarEpisodioInvalido() {
		
		LocalDate data = LocalDate.of(2026, 06, 18);
		
		Podcast novo = new Podcast(344, "Saude", "Podcast sobre saude", data, 13);
		
		Episodio nov = new Episodio("Alimentacao", 15);
		
		novo.adicionarEp(nov);
		
		assertFalse(novo.adicionarEp(nov));
		assertFalse(novo.adicionarEp(null));
		
	}
	//Testa se o método remove um episódio corretamente
	@Test
	void deveRemoverEpisodioCorretamente() {
				
		LocalDate data = LocalDate.of(2026, 06, 18);
		
		Podcast novo = new Podcast(344, "Saude", "Podcast sobre saude", data, 13);
		
		Episodio nov = new Episodio("Alimentacao", 15);
		
		novo.adicionarEp(nov);
		
		assertTrue(novo.removerEp(nov));
		
	}

}
