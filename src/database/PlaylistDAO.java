package database;

import java.util.List;
import model.content.Playlist;

//Classe DAO para manipulação de playlists do banco
public class PlaylistDAO {
	
	// Salvar uma nova playlist no banco
	public boolean salvar(Playlist novaPlaylist) {
		if (novaPlaylist == null) {
			return false;
		}
		
		novaPlaylist.setId(BancoMemoria.contadorPlaylist++);
		BancoMemoria.playlists.add(novaPlaylist);
		return true;
	}
	
	// Buscar uma playlist pelo ID
	public Playlist buscarId(int id) {
		for (Playlist p : BancoMemoria.playlists) {
			if (p.getId() == id) {
				return p;
			}
		}
		return null;
	}
	
	// Retornar a lista de playlists do banco
	public List <Playlist> listarPlaylists() {
		return BancoMemoria.playlists;
	}
	
	// Atualizar uma playlist existente
	public boolean atualizar(Playlist playlistAtualizada) {
		Playlist playlistAntiga = buscarId(playlistAtualizada.getId());
		
		if (playlistAntiga != null) {
			int index = BancoMemoria.playlists.indexOf(playlistAntiga);
			BancoMemoria.playlists.set(index, playlistAtualizada);
			return true;
		}
		
		return false;
	}
	
	// Remover uma playlist existente do banco
	public boolean deletar(int id) {
		Playlist playlistRemover = buscarId(id);
		
		if (playlistRemover != null) {
			return BancoMemoria.playlists.remove(playlistRemover);
		}
		
		return false;
	}
}
