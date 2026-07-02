package test;

//Importacoes necessarias
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import model.actors.Criador;

import model.content.Album;
import model.content.Podcast;
import model.actors.Conta;

public class CriadorTest {

    /**
     * Verifica se o método adicionarAlbum() adiciona um álbum
     * corretamente à lista de álbuns do criador.
     */
	@Test
	void deveAdicionarAlbum() {
		
		Conta conta = new Conta("gean", "123");
		
		Criador criador = new Criador(conta, "Gean", "M", LocalDate.of(2000, 01, 01));
		
		Album album = new Album("EVOM", "Trap", criador.getId());
		
		assertTrue(criador.adicionarAlbum(album));
	}

    /**
     * Verifica se o método removerAlbum() remove corretamente
     * um álbum previamente adicionado ao criador.
     */
    @Test
    void deveRemoverAlbum() {
    	
        Conta conta = new Conta("gean", "123");
		
		Criador criador = new Criador(conta, "Gean", "M", LocalDate.of(2000, 01, 01));
		
		Album album = new Album("EVOM", "Trap", criador.getId());
    	
		criador.adicionarAlbum(album);
    	
    	assertTrue(criador.removerAlbum(album));
    }

    /**
     * Verifica se o método adicionarPodcast() adiciona um podcast
     * corretamente à lista de podcasts do criador.
     */
    @Test
    void deveAdicionarPodcast() {
    	
        Conta conta = new Conta("gean", "123");
		
		Criador criador = new Criador(conta, "Gean", "M", LocalDate.of(2000, 01, 01));
		
		Podcast podcast = new Podcast("Evoluir", "Desenvolvimento", criador.getId());
		
		assertTrue(criador.adicionarPodcast(podcast));
    	
    }

    /**
     * Verifica se o método removerPodcast() remove corretamente
     * um podcast previamente adicionado ao criador.
     */
    @Test
    void deveRemoverPodcast() {
    	
        Conta conta = new Conta("gean", "123");
		
		Criador criador = new Criador(conta, "Gean", "M", LocalDate.of(2000, 01, 01));
		
		Podcast podcast = new Podcast("Evoluir", "Desenvolvimento", criador.getId());
		
		criador.adicionarPodcast(podcast);
		
		assertTrue(criador.removerPodcast(podcast));
    	
    }
}

