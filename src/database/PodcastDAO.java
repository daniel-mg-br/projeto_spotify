package database;

import java.util.List;
import model.content.Podcast;

// Classe DAO para manipulação de podcasts do banco
public class PodcastDAO {
	
	// Salvar um novo podcast no banco
		public boolean salvar(Podcast novoPodcast) {
			if (novoPodcast == null) {
				return false;
			}
			
			novoPodcast.setId(BancoMemoria.contadorPodcast++);
			BancoMemoria.podcasts.add(novoPodcast);
			return true;
		}
		
		// Buscar um podcast pelo ID
		public Podcast buscarId(int id) {
			for (Podcast p : BancoMemoria.podcasts) {
				if (p.getId() == id) {
					return p;
				}
			}
			
			return null;
		}
		
		// Retornar a lista de podcasts do banco
		public List <Podcast> listarPodcasts() {
			return BancoMemoria.podcasts;
		}
		
		// Atualizar um podcast existente
		public boolean atualizar(Podcast podcastAtualizado) {
			Podcast podcastAntigo = buscarId(podcastAtualizado.getId());
			
			if (podcastAntigo != null) {
				int index = BancoMemoria.podcasts.indexOf(podcastAntigo);
				BancoMemoria.podcasts.set(index, podcastAtualizado);
				return true;
			}
			
			return false;
		}
		
		// Remover um podcast existente do banco
		public boolean deletar(int id) {
			Podcast podcastRemover = buscarId(id);
			
			if (podcastRemover != null) {
				return BancoMemoria.podcasts.remove(podcastRemover);
			}
			
			return false;
		}
}
