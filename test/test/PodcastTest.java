package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import model.content.Podcast;
import model.actors.Conta;
import model.actors.Ouvinte;
import model.actors.Criador;
import model.content.Episodio;

public class PodcastTest {

    /**
     * Verifica se um podcast é criado corretamente com os valores
     * informados no construtor.
     */
	@Test
	void deveCriarPodcastCorretamente() {
		
		LocalDate data = LocalDate.of(2026, 06, 18);
		
		Conta conta = new Conta("gean", "123");
		
		Criador criador = new Criador(conta, "Gean", "M", LocalDate.of(2000, 12, 23));
		
		Podcast novo = new Podcast(344, "Saude", "Podcast sobre saude", criador.getId() ,data);
		
		assertEquals(344, novo.getId());
		assertEquals("Saude", novo.getNome());
		assertEquals("Podcast sobre saude", novo.getTema());
		assertEquals(data, novo.getCriacao());
	}

    /**
     * Verifica se o método adicionarEp() adiciona corretamente
     * um episódio ao podcast.
     */
	@Test
	void deveAdicionarEpisodioCorretamente() {
		
		LocalDate data = LocalDate.of(2026, 06, 18);
		
		Conta conta = new Conta("gean", "123");
		
		Criador criador = new Criador(conta, "Gean", "M", LocalDate.of(2000, 12, 23));
		
		Podcast novo = new Podcast(344, "Saude", "Podcast sobre saude", criador.getId(), data);
		
		Episodio nov = new Episodio("Alimentacao", 15);
		
		assertTrue(novo.adicionarEp(nov));
		
	}
	
    /**
     * Verifica se o método adicionarEp() impede a adição
     * de episódios inválidos, como episódios duplicados ou nulos.
     */
	@Test
	void naoDeveAdicionarEpisodioInvalido() {
		
		LocalDate data = LocalDate.of(2026, 06, 18);
		
		Conta conta = new Conta("gean", "123");
		
		Criador criador = new Criador(conta, "Gean", "M", LocalDate.of(2000, 12, 23));
		
		Podcast novo = new Podcast(344, "Saude", "Podcast sobre saude", criador.getId(), data);
		
		Episodio nov = new Episodio("Alimentacao", 15);
		
		novo.adicionarEp(nov);
		
		assertFalse(novo.adicionarEp(nov));
		assertFalse(novo.adicionarEp(null));
		
	}

    /**
     * Verifica se o método removerEp() remove corretamente
     * um episódio previamente adicionado ao podcast.
     */
	@Test
	void deveRemoverEpisodioCorretamente() {
				
		LocalDate data = LocalDate.of(2026, 06, 18);
		
		Conta conta = new Conta("gean", "123");
		
		Criador criador = new Criador(conta, "Gean", "M", LocalDate.of(2000, 12, 23));
		
		Podcast novo = new Podcast(344, "Saude", "Podcast sobre saude", criador.getId(), data);
		
		Episodio nov = new Episodio("Alimentacao", 15);
		
		novo.adicionarEp(nov);
		
		assertTrue(novo.removerEp(nov));
		
	}

}
