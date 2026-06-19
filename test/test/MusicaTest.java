package test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import model.content.Musica;

public class MusicaTest {
	//Testa se o método está criando música corretamente
	@Test
	void deveCriarMusicaCorretamente() {
		
		Musica nova = new Musica(800, "Sinonimos", 4, "Sertanejo", "Quanto tempo o coracao...");
		
		assertEquals(800, nova.getId());
		assertEquals("Sinonimos", nova.getTitulo());
		assertEquals(4, nova.getDuracaoMin());
		assertEquals("Sertanejo", nova.getGenero());
		assertEquals("Quanto tempo o coracao...", nova.getLetra());
	}
	//Testa se o método está adicionando um membro a equipe corretamente
	@Test
	void deveAdicionarMembroNaEquipe() {
		
		Musica nova = new Musica("Sinonimos", 4);
		
		assertTrue(nova.adicionarMembroEquipe("Chitaozinho"));	
		
	}
	//Testa se o método impede que se adicione um membro null ou repitido
	@Test
	void naoDeveAdicionarMebroInvalido() {
		
		Musica nova = new Musica("Sinonimos", 4);
		
		nova.adicionarMembroEquipe("Chitaozinho");
		
		assertFalse(nova.adicionarMembroEquipe("Chitaozinho"));	
		
		assertFalse(nova.adicionarMembroEquipe(null));
			
	}
	//Testa se o método remove um membro corretamente
	@Test
	void deveRemoverMembro() {
		
		Musica nova = new Musica("Sinonimos", 4);
		
		nova.adicionarMembroEquipe("Chitaozinho");
		
		assertTrue(nova.removerMembroEquipe("Chitaozinho"));
	}

}
