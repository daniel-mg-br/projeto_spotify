package controller;

import database.*;
import model.actors.Ouvinte;
import model.actors.Usuario;
import model.content.Musica;
import model.content.Playlist;
import model.content.Album;
import model.content.Podcast;
import java.util.HashMap;
import java.util.Map;

// Classe Controller referente à interação do Ouvinte com o sistema
public class ControllerOuvinte {
	// Classes DAO responsáveis pela manipulção de dados
	private OuvinteDAO ouvinteDAO;
	private PlaylistDAO playlistDAO;
	private AlbumDAO albumDAO;
	private PodcastDAO podcastDAO;
	private MusicaDAO musicaDAO;
	
	// Método Construtor
	public ControllerOuvinte() {
		this.ouvinteDAO = new OuvinteDAO();
		this.playlistDAO = new PlaylistDAO();
		this.albumDAO = new AlbumDAO();
		this.podcastDAO = new PodcastDAO();
		this.musicaDAO = new MusicaDAO();
	}
	
	// Retornar o ouvinte logado na sessão
	private Ouvinte getOuvinteLogado() {
		Usuario usuarioLogado = ControllerAutenticador.getUsuarioLogado();
		
		if (usuarioLogado != null && usuarioLogado instanceof Ouvinte) {
			return (Ouvinte) usuarioLogado;
		}
		
		return null;
	}
	
	// Método para criar uma playlist nova, com verificações de usuário
	public boolean criarPlaylist(String titulo, String descricao) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		
		if (ouvinte == null) {
			System.out.println("Erro: nenhum ouvinte logado!");
			return false;
		}
		
		Playlist novaPlaylist = new Playlist(titulo, descricao);
		this.playlistDAO.salvar(novaPlaylist);
		
		ouvinte.adicionarPlaylist(novaPlaylist);
		return this.ouvinteDAO.atualizar(ouvinte);
	}
	
	// Método para remover uma playlist existente, com verificações de objetos vazios e ids válidos
	public boolean deletarPlaylist(int idPlaylist) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Playlist playlist = this.playlistDAO.buscarId(idPlaylist);
		
		if (ouvinte == null || playlist == null) return false;
		
		if (!ouvinte.getPlaylists().contains(playlist)) {
			System.out.println("Erro: playlist inválida!");
			return false;
		}
		
		ouvinte.removerPlaylist(playlist);
		this.ouvinteDAO.atualizar(ouvinte);
		
		boolean deletou = this.playlistDAO.deletar(idPlaylist);
		
		if (deletou) {
			System.out.println("Playlist excluída com sucesso!");
		}
		
		return deletou;
	}
	
	// Método para adicionar uma música na playlist
	// Verificações: ouvinte, playlist e música vazios; playlist válida
	public boolean adicionarMusicaPlaylist(int idPlaylist, int idMusica) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		
		Playlist playlist = this.playlistDAO.buscarId(idPlaylist);
		Musica musica = (Musica) this.musicaDAO.buscarId(idMusica);
		
		if (ouvinte == null || playlist == null || musica == null) {
			return false;
		}
		
		if (!ouvinte.getPlaylists().contains(playlist)) {
			System.out.println("Erro: playlist inválida!");
			return false;
		}
		
		boolean adicionou = playlist.adicionarMusica(musica);
		
		if (adicionou) {
			this.playlistDAO.atualizar(playlist);
			System.out.println("Música: " + musica.getTitulo() + " adicionada à playlist!");
		}
		
		return adicionou;
	}
	
	// Método para remover a música da playlist, verificando objetos nulos e veracidade de relacionamentos
	public boolean removerMusicaPlaylist(int idPlaylist, int idMusica) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Playlist playlist = this.playlistDAO.buscarId(idPlaylist);
		Musica musica = (Musica) this.musicaDAO.buscarId(idMusica);
		
		if (ouvinte == null || playlist == null || musica == null) return false;
		
		if (!ouvinte.getPlaylists().contains(playlist)) {
			System.out.println("Erro: você não tem permissão para alterar a playlist!");
			return false;
		}
		
		boolean removeu = playlist.removerMusica(musica);
		
		if (removeu) {
			this.playlistDAO.atualizar(playlist);
			System.out.println("Música removida da playlist!");
		} else {
			System.out.println("Esta música não está na playlist!");
		}
		
		return removeu;
	}
	
	// Método para mudar o status da playlist para pública, com suas verificações de estado
	public boolean compartilharPlaylist(int idPlaylist) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Playlist playlist = this.playlistDAO.buscarId(idPlaylist);
		
		if (ouvinte == null || playlist == null) {
			return false;
		}
		
		if (!ouvinte.getPlaylists().contains(playlist)) {
			System.out.println("Erro: playlist inválida!");
			return false;
		}
		
		if (playlist.isCompartilhar()) {
			System.out.println("Aviso: a playlist " + playlist.getTitulo() + " já é pública!");
			return false;
		}
		
		playlist.compartilhar();
		boolean atualizou = this.playlistDAO.atualizar(playlist);
		
		if (atualizou) {
			System.out.println("A playlist agora é pública!");
		}
		
		return atualizou;
	}
	
	// Método para favoritar um álbum, com verificação de duplicata e objetos nulos
	public boolean favoritarAlbum(int idAlbum) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Album album = this.albumDAO.buscarId(idAlbum);
		
		if (ouvinte == null || album == null) return false;
		
		boolean adicionou = ouvinte.adicionarAlbumFav(album);
		
		if (adicionou) {
			this.ouvinteDAO.atualizar(ouvinte);
			System.out.println("Álbum: " + album.getTitulo() + " adicionado aos favoritos!");
		} else {
			System.out.println("Este álbum já está nos favoritos!");
		}
		
		return adicionou;
	}
	
	// Método para remover um álbum dos favoritos, com verificação dos objetos 
	public boolean desfavoritarAlbum(int idAlbum) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Album album = this.albumDAO.buscarId(idAlbum);
		
		if (ouvinte == null || album == null) return false;
		
		boolean removeu = ouvinte.removerAlbumFav(album);
		
		if (removeu) {
			this.ouvinteDAO.atualizar(ouvinte);
			System.out.println("Álbum removido dos favoritos!");
		} 
		
		return removeu;
	}
	
	// Método para favoritar um podcast, com verificação de duplicata e objetos nulos
	public boolean favoritarPodcast(int idPodcast) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Podcast podcast = this.podcastDAO.buscarId(idPodcast);
		
		if (ouvinte == null || podcast == null) return false;
		
		boolean adicionou = ouvinte.adicionarPodcastFav(podcast);
		
		if (adicionou) {
			this.ouvinteDAO.atualizar(ouvinte);
			System.out.println("Podcast: " + podcast.getNome() + " adicionado aos favoritos!");
		} else {
			System.out.println("Este podcast já está nos favoritos!");
		}
		
		return adicionou;
	}
	
	// Método para remover um podcast dos favoritos, com verificação 
	public boolean desfavoritarPodcast(int idPodcast) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Podcast podcast = this.podcastDAO.buscarId(idPodcast);
		
		if (ouvinte == null || podcast == null) return false;
		
		boolean removeu = ouvinte.removerPodcastFav(podcast);
		
		if (removeu) {
			this.ouvinteDAO.atualizar(ouvinte);
			System.out.println("Podcast removido dos favoritos!");
		}
		
		return removeu;
	}
	
	// Método para calcular o gênero favorito do ouvinte por meio de Hash Map
	public void atualizarGeneroFavorito() {
		Ouvinte ouvinte = this.getOuvinteLogado();
        if (ouvinte == null) return;

        // O HashMap vai guardar o nome do Gênero (String) e quantas vezes ele apareceu (Integer)
        Map<String, Integer> contagemGeneros = new HashMap<>();

        // 1. Contabiliza os gêneros das músicas nas Playlists
        for (Playlist p : ouvinte.getPlaylists()) {
            for (Musica m : p.getMusicas()) {
                String genero = m.getGenero();
                // Pega o valor atual daquele gênero e soma +1 (se não existir, começa do 0)
                contagemGeneros.put(genero, contagemGeneros.getOrDefault(genero, 0) + 1);
            }
        }

        // 2. Contabiliza os gêneros das músicas nos Álbuns Favoritos
        for (Album a : ouvinte.getAlbunsFavoritos()) {
            for (Musica m : a.getMusicas()) {
                String genero = m.getGenero();
                contagemGeneros.put(genero, contagemGeneros.getOrDefault(genero, 0) + 1);
            }
        }

        // Se o usuário não tem músicas, encerra o método
        if (contagemGeneros.isEmpty()) {
            ouvinte.setGeneroFavorito("Ainda não definido");
            this.ouvinteDAO.atualizar(ouvinte);
            return;
        }

        // 3. Descobre qual gênero teve a maior pontuação
        String generoFavorito = "";
        int maiorContagem = 0;

        for (Map.Entry<String, Integer> entrada : contagemGeneros.entrySet()) {
            if (entrada.getValue() > maiorContagem) {
                maiorContagem = entrada.getValue();
                generoFavorito = entrada.getKey();
            }
        }

        // 4. Salva o resultado no banco
        ouvinte.setGeneroFavorito(generoFavorito);
        this.ouvinteDAO.atualizar(ouvinte);
        System.out.println("Novo gênero favorito calculado: " + generoFavorito + " (" + maiorContagem + " músicas)");
    }
}