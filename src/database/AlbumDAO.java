package database;

import java.util.List;
import model.content.Album;

//Classe DAO para manipulação de álbuns do banco
public class AlbumDAO {
	// Salvar um novo álbum no banco
	public boolean salvar(Album novoAlbum) {
		if (novoAlbum == null) {
			return false;
		}
		
		novoAlbum.setId(BancoMemoria.contadorAlbum++);
		BancoMemoria.albuns.add(novoAlbum);
		return true;
	}
	
	// Buscar um álbum pelo ID
	public Album buscarId(int id) {
		for (Album a : BancoMemoria.albuns) {
			if (a.getId() == id) {
				return a;
			}
		}
		return null;
	}
	
	// Retornar a lista de álbuns do banco
	public List <Album> listarAlbuns() {
		return BancoMemoria.albuns;
	}
	
	// Atualizar um álbum existente
	public boolean atualizar(Album albumAtualizado) {
		Album albumAntigo = buscarId(albumAtualizado.getId());
		
		if (albumAntigo != null) {
			int index = BancoMemoria.albuns.indexOf(albumAntigo);
			BancoMemoria.albuns.set(index,albumAtualizado);
			return true;
		}
		
		return false;
	}
	
	// Remover uma álbum existente do banco
	public boolean deletar(int id) {
		Album albumRemover = buscarId(id);
		
		if (albumRemover != null) {
			return BancoMemoria.albuns.remove(albumRemover);
		}
		
		return false;
	}
}
