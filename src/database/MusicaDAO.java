package database;

import java.util.ArrayList;
import java.util.List;
import model.content.Conteudo;
import model.content.Musica;

public class MusicaDAO {
	// Salvar uma nova música no banco
		public boolean salvar(Musica novaMusica) {
			if (novaMusica == null) {
				return false;
			}
			
			novaMusica.setId(BancoMemoria.contadorConteudo++);
			BancoMemoria.conteudos.add(novaMusica);
			return true;
		}
		
		// Buscar uma música pelo ID por meio de Casting
		public Musica buscarId(int id) {
			for (Conteudo c : BancoMemoria.conteudos) {
				if (c.getId() == id) {
					if (c instanceof Musica) {
						return (Musica) c;
					}
				}
			}
			
			return null;
		}
		
		// Retornar a lista de músicas usando Casting na iteração pela lista de Conteúdos
		public List <Musica> listarMusicas() {
			List <Musica> musicas = new ArrayList<>();
			
			for (Conteudo c : BancoMemoria.conteudos) {
				if (c instanceof Musica) {
					musicas.add((Musica) c);
				}
			}
			
			return musicas;
		}
		
		// Atualizar uma música existente
		public boolean atualizar(Musica musicaAtualizada) {
			Musica musicaAntiga = buscarId(musicaAtualizada.getId());
			
			if (musicaAntiga != null) {
				int index = BancoMemoria.conteudos.indexOf(musicaAntiga);
				BancoMemoria.conteudos.set(index, musicaAtualizada);
				return true;
			}
			
			return false;
		}
		
		// Remover uma música existente da lista
		public boolean deletar(int id) {
			Musica musicaRemover = buscarId(id);
			
			if (musicaRemover != null) {
				return BancoMemoria.conteudos.remove(musicaRemover);
			}
			
			return false;
		}
}
