package controller;

import dao.*;    
import model.actors.Ouvinte;
import model.actors.Criador;
import model.actors.Usuario;
import model.content.Musica;
import model.content.Playlist;
import model.content.Album;
import model.content.Podcast;
import model.content.Episodio;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe Controller referente à interação do Ouvinte com o sistema.
 */
public class ControllerOuvinte {
	// Classes DAO como atributos, responsáveis pela manipulção de dados
	private OuvinteDAO ouvinteDAO;
	private PlaylistDAO playlistDAO;
	private AlbumDAO albumDAO;
	private PodcastDAO podcastDAO;
	private MusicaDAO musicaDAO;
	private CriadorDAO criadorDAO;
	
	/**
	 * Método Construtor do Controller.
	 */
	public ControllerOuvinte() {
		this.ouvinteDAO = new OuvinteDAO();
		this.playlistDAO = new PlaylistDAO();
		this.albumDAO = new AlbumDAO();
		this.podcastDAO = new PodcastDAO();
		this.musicaDAO = new MusicaDAO();
		this.criadorDAO = new CriadorDAO();
	}
	
	/**
	 * Retornar o ouvinte logado na sessão.
	 * 
	 * @return Retorna o objeto do Ouvinte logado, com seus dados.
	 */
	private Ouvinte getOuvinteLogado() {
		Usuario usuarioLogado = ControllerAutenticador.getUsuarioLogado();
		
		if (usuarioLogado != null && usuarioLogado instanceof Ouvinte) {
			return (Ouvinte) usuarioLogado;
		}
		
		return null;
	}
	
	/**
	 * Método para criar uma playlist nova, com verificações de usuário.
	 * 
	 * @param titulo Título da nova playlist;
	 * @param descricao Descrição da playlist criada;
	 * @param criadorId ID do ouvinte criador da playlist;
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean criarPlaylist(String titulo, String descricao) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		
		if (ouvinte == null) {
			System.out.println("Erro: nenhum ouvinte logado!");
			return false;
		}
		
		Playlist novaPlaylist = new Playlist(titulo, descricao, ouvinte.getId());
		this.playlistDAO.salvar(novaPlaylist);
		
		ouvinte.adicionarPlaylist(novaPlaylist);
		return true;
	}
	
	/**
	 * Método para remover uma playlist existente, com verificações de objetos vazios e ids válidos.
	 * 
	 * @param idPlaylist ID da playlist a ser removida;
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean deletarPlaylist(int idPlaylist) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Playlist playlist = this.playlistDAO.buscarId(idPlaylist);
		
		if (ouvinte == null || playlist == null) return false;
		
		if (playlist.getUsuarioId() != ouvinte.getId()) {
			System.out.println("Erro: playlist inválida!");
			return false;
		}
		
		ouvinte.removerPlaylist(playlist);
		
		boolean deletou = this.playlistDAO.deletar(idPlaylist);
		if (deletou) {
			System.out.println("Playlist excluída com sucesso!");
		}
		
		return deletou;
	}
	
	/**
	 * Método para adicionar uma música na playlist.
	 * Verificações: ouvinte, playlist e música vazios; playlist válida
	 * 
	 * @param idPlaylist ID da playlist a receber a música;
	 * @param idMusica ID da música nova;
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean adicionarMusicaPlaylist(int idPlaylist, int idMusica) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Playlist playlist = this.playlistDAO.buscarId(idPlaylist);
		Musica musica = this.musicaDAO.buscarId(idMusica);
		
		if (ouvinte == null || playlist == null || musica == null) {
			return false;
		}
		
		if (playlist.getUsuarioId() != ouvinte.getId()) {
			System.out.println("Erro: playlist inválida!");
			return false;
		}
		
		boolean adicionou = playlist.adicionarMusica(musica);
		
		if (adicionou) {
			this.playlistDAO.atualizar(playlist);
			System.out.println("Música: " + musica.getTitulo() + " adicionada à playlist!");
			
			ouvinte.adicionarTempo(musica.getDuracaoMin());
			this.ouvinteDAO.atualizar(ouvinte);
			
			for (Album album : this.albumDAO.listarAlbuns()) {
				boolean musicaNoAlbum = album.getMusicas().stream().anyMatch(m -> m.getId() == idMusica);
				
				if (musicaNoAlbum) {
					Criador criador = this.criadorDAO.buscarId(album.getCriadorId());
					
					if (criador != null) {
						criador.adicionarEngajamento();
						this.criadorDAO.atualizar(criador);
					}
					break;
				}
			}
		}
		
		return adicionou;
	}
	
	/**
	 * Método para remover a música da playlist, verificando objetos nulos e veracidade de relacionamentos
	 * @param idPlaylist ID da playlist a perder a música;
	 * @param idMusica ID da música a ser removida;
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean removerMusicaPlaylist(int idPlaylist, int idMusica) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Playlist playlist = this.playlistDAO.buscarId(idPlaylist);
		Musica musica = this.musicaDAO.buscarId(idMusica);
		
		if (ouvinte == null || playlist == null || musica == null) return false;
		
		if (playlist.getUsuarioId() != ouvinte.getId()) {
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
	
	/**
	 * Método para mudar o status da playlist para pública, com suas verificações de estado.
	 * 
	 * @param idPlaylist ID da playlist a ser compartilhada;
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean compartilharPlaylist(int idPlaylist) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Playlist playlist = this.playlistDAO.buscarId(idPlaylist);
		
		if (ouvinte == null || playlist == null) {
			return false;
		}
		
		if (playlist.getUsuarioId() != ouvinte.getId()) {
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
	
	/**
	 * Método para favoritar um álbum, com verificação de duplicata e objetos nulos.
	 * 
	 * @param idAlbum ID do álbum a ser 'favoritado'.
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean favoritarAlbum(int idAlbum) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Album album = this.albumDAO.buscarId(idAlbum);
		
		if (ouvinte == null || album == null) return false;
		
		boolean jaExiste = ouvinte.getAlbunsFavoritos().stream().anyMatch(a -> a.getId() == idAlbum);
		if (jaExiste) {
			System.out.println("O álbum já consta nos favoritos!");
			return false;
		}
		
		ouvinte.adicionarAlbumFav(album);
		this.ouvinteDAO.atualizar(ouvinte); 
		System.out.println("Álbum: " + album.getTitulo() + " adicionado aos favoritos!");
		
		int duracaoTotal = 0;
		for (Musica m : album.getMusicas()) {
			duracaoTotal += m.getDuracaoMin();
		}
		
		ouvinte.adicionarTempo(duracaoTotal);
		this.ouvinteDAO.atualizar(ouvinte);
		
		Criador criador = this.criadorDAO.buscarId(album.getCriadorId());
		if (criador != null) {
			criador.adicionarEngajamento();
			this.criadorDAO.atualizar(criador);
		}
		
		return true;
	}
	
	/**
	 * Método para remover um álbum dos favoritos, com verificação dos objetos.
	 * 
	 * @param idAlbum ID do álbum a ser 'desfavoritado'.
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean desfavoritarAlbum(int idAlbum) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Album album = this.albumDAO.buscarId(idAlbum);
		
		if (ouvinte == null || album == null) return false;
		
		boolean removeu = ouvinte.getAlbunsFavoritos().removeIf(a -> a.getId() == idAlbum);
		
		if (removeu) {
			this.ouvinteDAO.atualizar(ouvinte);
			System.out.println("Álbum removido dos favoritos!");
		} 
		
		return removeu;
	}
	
	/**
	 * Método para favoritar um podcast, com verificação de duplicata e objetos nulos.
	 * 
	 * @param idPodcast ID do podcast a ser 'favoritado';
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean favoritarPodcast(int idPodcast) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Podcast podcast = this.podcastDAO.buscarId(idPodcast);
		
		if (ouvinte == null || podcast == null) return false;
		
		boolean jaExiste = ouvinte.getPodcastsFavoritos().stream().anyMatch(p -> p.getId() == idPodcast);
		if (jaExiste) {
			System.out.println("Este podcast já está nos favoritos!");
			return false;
		}
		
		ouvinte.adicionarPodcastFav(podcast);
		this.ouvinteDAO.atualizar(ouvinte);
		System.out.println("Podcast: " + podcast.getNome() + " adicionado aos favoritos!");
		
		int duracaoTotal = 0;
		for (Episodio ep : podcast.getEpisodios()) {
			duracaoTotal += ep.getDuracaoMin();
		}
		
		ouvinte.adicionarTempo(duracaoTotal);
		this.ouvinteDAO.atualizar(ouvinte);
		
		Criador criador = this.criadorDAO.buscarId(podcast.getCriadorId());
		if (criador != null) {
			criador.adicionarEngajamento();
			this.criadorDAO.atualizar(criador);
		}
		
		return true;
	}
	
	/**
	 * Método para remover um podcast dos favoritos, com verificação.
	 *  
	 * @param idPodcast ID do podcast a ser 'desfavoritado'.
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean desfavoritarPodcast(int idPodcast) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Podcast podcast = this.podcastDAO.buscarId(idPodcast);
		
		if (ouvinte == null || podcast == null) return false;
		
		boolean removeu = ouvinte.getPodcastsFavoritos().removeIf(p -> p.getId() == idPodcast);		
		if (removeu) {
			this.ouvinteDAO.atualizar(ouvinte);
			System.out.println("Podcast removido dos favoritos!");
		}
		
		return removeu;
	}
	
	/**
	 * Método para calcular o gênero favorito do ouvinte.
	 * Uso de contagem de frequências por meio de Hash Map.
	 */
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