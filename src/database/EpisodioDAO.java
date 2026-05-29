package database;

import java.util.ArrayList;
import java.util.List;
import model.content.Conteudo;
import model.content.Episodio;

// Classe DAO para manipulação de episódios de podcast
public class EpisodioDAO {
	
	// Salvar um novo episódio no banco
	public boolean salvar(Episodio novoEpisodio) {
		if (novoEpisodio == null) {
			return false;
		}
		
		novoEpisodio.setId(BancoMemoria.contadorConteudo++);
		BancoMemoria.conteudos.add(novoEpisodio);
		return true;
	}
	
	// Buscar um episódio pelo ID por meio de Casting
	public Episodio buscarId(int id) {
		for (Conteudo c : BancoMemoria.conteudos) {
			if (c.getId() == id) {
				if (c instanceof Episodio) {
					return (Episodio) c;
				}
			}
		}
		
		return null;
	}
	
	// Retornar a lista de episódios usando Casting na iteração pela lista de Conteúdos
	public List <Episodio> listarEpisodios() {
		List <Episodio> episodios = new ArrayList<>();
		
		for (Conteudo c : BancoMemoria.conteudos) {
			if (c instanceof Episodio) {
				episodios.add((Episodio) c);
			}
		}
		
		return episodios;
	}
	
	// Atualizar um episódio existente
	public boolean atualizar(Episodio episodioAtualizado) {
		Episodio episodioAntigo = buscarId(episodioAtualizado.getId());
		
		if (episodioAntigo != null) {
			int index = BancoMemoria.conteudos.indexOf(episodioAntigo);
			BancoMemoria.conteudos.set(index, episodioAtualizado);
			return true;
		}
		
		return false;
	}
	
	// Remover um episódio existente da lista
	public boolean deletar(int id) {
		Episodio episodioRemover = buscarId(id);
		
		if (episodioRemover != null) {
			return BancoMemoria.conteudos.remove(episodioRemover);
		}
		
		return false;
	}
}
