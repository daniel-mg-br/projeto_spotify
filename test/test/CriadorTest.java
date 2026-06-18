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
    // Teste para ver se esta adicionando o album com sucesso
	@Test
	void deveAdicionarAlbum() {
		
		Conta conta = new Conta("gean", "123");
		
		Criador criador = new Criador(conta, "Gean", "M", LocalDate.of(2000, 01, 01));
		
		Album album = new Album("EVOM", "Trap");
		
		assertTrue(criador.adicionarAlbum(album));
	}
	// Teste para ver se esta removendo o album corretamente
    @Test
    void deveRemoverAlbum() {
    	
        Conta conta = new Conta("gean", "123");
		
		Criador criador = new Criador(conta, "Gean", "M", LocalDate.of(2000, 01, 01));
		
		Album album = new Album("EVOM", "Trap");
    	
		criador.adicionarAlbum(album);
    	
    	assertTrue(criador.removerAlbum(album));
    }
    // Teste para ver se esta adicionando podcasts com sucesso
    @Test
    void deveAdicionarPodcast() {
    	
        Conta conta = new Conta("gean", "123");
		
		Criador criador = new Criador(conta, "Gean", "M", LocalDate.of(2000, 01, 01));
		
		Podcast podcast = new Podcast("Evoluir", "Desenvolvimento");
		
		assertTrue(criador.adicionarPodcast(podcast));
    	
    }
    //Testa se esta removendo podcasts corretamente
    @Test
    void deveRemoverPodcast() {
    	
        Conta conta = new Conta("gean", "123");
		
		Criador criador = new Criador(conta, "Gean", "M", LocalDate.of(2000, 01, 01));
		
		Podcast podcast = new Podcast("Evoluir", "Desenvolvimento");
		
		criador.adicionarPodcast(podcast);
		
		assertTrue(criador.removerPodcast(podcast));
    	
    }
}

