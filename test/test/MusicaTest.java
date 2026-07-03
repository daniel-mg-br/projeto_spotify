package test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import model.content.Musica;

public class MusicaTest {

    /**
     * Verifica se uma música é criada corretamente com os valores
     * informados no construtor.
     */
	@Test
	void deveCriarMusicaCorretamente() {
		
		Musica nova = new Musica(800, "Sinonimos", 4, "Sertanejo", "Quanto tempo o coracao...");
		
		assertEquals(800, nova.getId());
		assertEquals("Sinonimos", nova.getTitulo());
		assertEquals(4, nova.getDuracaoMin());
		assertEquals("Sertanejo", nova.getGenero());
		assertEquals("Quanto tempo o coracao...", nova.getLetra());
	}

    /**
     * Verifica se o método adicionarMembroEquipe() adiciona
     * corretamente um novo membro à equipe da música.
     */
	@Test
	void deveAdicionarMembroNaEquipe() {
		
		Musica nova = new Musica("Sinonimos", 4);
		
		assertTrue(nova.adicionarMembroEquipe("Chitaozinho"));	
		
	}

    /**
     * Verifica se o método adicionarMembroEquipe() impede a adição
     * de membros inválidos, como nomes repetidos ou valores nulos.
     */
	@Test
	void naoDeveAdicionarMebroInvalido() {
		
		Musica nova = new Musica("Sinonimos", 4);
		
		nova.adicionarMembroEquipe("Chitaozinho");
		
		assertFalse(nova.adicionarMembroEquipe("Chitaozinho"));	
		
		assertFalse(nova.adicionarMembroEquipe(null));
			
	}

    /**
     * Verifica se o método removerMembroEquipe() remove corretamente
     * um membro previamente adicionado à equipe da música.
     */
	@Test
	void deveRemoverMembro() {
		
		Musica nova = new Musica("Sinonimos", 4);
		
		nova.adicionarMembroEquipe("Chitaozinho");
		
		assertTrue(nova.removerMembroEquipe("Chitaozinho"));
	}

}
