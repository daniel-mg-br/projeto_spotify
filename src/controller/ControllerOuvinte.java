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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe ControllerOuvinte referente à interação do Ouvinte com o sistema.
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
	 * Método Construtor do Controller instanciando as classes DAO.
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
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean criarPlaylist(String titulo, String descricao) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		
		if (ouvinte == null) {
			System.out.println("Erro: nenhum ouvinte logado!");
			return false;
		}
		
		// Instancia a nova playlist e atualiza o banco de dados.
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
		
		// Verifica se os objetos recuperados são nulos.
		if (ouvinte == null || playlist == null) return false;
		
		// Verifica se a playlist pertence ao ouvinte.
		if (playlist.getUsuarioId() != ouvinte.getId()) {
			System.out.println("Erro: playlist inválida!");
			return false;
		}
		
		// Remove a playlist e atualiza o banco de dados.
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
		
		// Verifica se os objetos recuperados são nulos.
		if (ouvinte == null || playlist == null || musica == null) {
			return false;
		}
		
		// Verifica se a playlist pertence ao ouvinte.
		if (playlist.getUsuarioId() != ouvinte.getId()) {
			System.out.println("Erro: playlist inválida!");
			return false;
		}
		
		// Atualiza o banco de dados com a playlist nova.
		boolean adicionou = playlist.adicionarMusica(musica);
		
		if (adicionou) {
			this.playlistDAO.atualizar(playlist);
			System.out.println("Música: " + musica.getTitulo() + " adicionada à playlist!");
			
			// Atualiza os minutos ouvidos do ouvinte
			ouvinte.adicionarTempo(musica.getDuracaoMin());
			this.ouvinteDAO.atualizar(ouvinte);
			
			// Busca a música adicionada na bibliografia do autor.
			for (Album album : this.albumDAO.listarAlbuns()) {
				boolean musicaNoAlbum = album.getMusicas().stream().anyMatch(m -> m.getId() == idMusica);
				
				// Se a música for encontrada, atualiza os ouvintes mensais do criador.
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
	 * Método para remover a música da playlist, verificando objetos nulos e veracidade de relacionamentos.
	 * 
	 * @param idPlaylist ID da playlist a perder a música;
	 * @param idMusica ID da música a ser removida;
	 * @return Retorna true se a operação for bem sucedida, e false se não.
	 */
	public boolean removerMusicaPlaylist(int idPlaylist, int idMusica) {
		Ouvinte ouvinte = this.getOuvinteLogado();
		Playlist playlist = this.playlistDAO.buscarId(idPlaylist);
		Musica musica = this.musicaDAO.buscarId(idMusica);
		
		// Verifica se os objetos são nulos.
		if (ouvinte == null || playlist == null || musica == null) return false;
		
		// Verifica se a playlist pertence ao ouvinte.
		if (playlist.getUsuarioId() != ouvinte.getId()) {
			System.out.println("Erro: você não tem permissão para alterar a playlist!");
			return false;
		}
		
		// Remove a música da playlist e atualiza o banco de dados.
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
		
		// Verifica se os objetos recuperados são nulos.
		if (ouvinte == null || playlist == null) {
			return false;
		}
		
		// Verifica se a playlist pertence ao ouvinte.
		if (playlist.getUsuarioId() != ouvinte.getId()) {
			System.out.println("Erro: playlist inválida!");
			return false;
		}
		
		// Verifica se a playlist já é pública.
		if (playlist.isCompartilhar()) {
			System.out.println("Aviso: a playlist " + playlist.getTitulo() + " já é pública!");
			return false;
		}
		
		// Atualiza o status da playlist e salva no banco de dados.
		playlist.compartilhar();
		boolean atualizou = this.playlistDAO.atualizar(playlist);
		
		if (atualizou) {
			System.out.println("A playlist agora é pública!");
		}
		
		return atualizou;
	}
	
	/**
	 * Recupera os dados de todas as playlists criadas pelo ouvinte.
	 * 
	 * @return Retorna a lista de playlists recuperadas.
	 */
	public List <Playlist> listarPlaylistsOuvinte() {
		Ouvinte ouvinte = this.getOuvinteLogado();
		if (ouvinte == null) return new ArrayList<>();
		return ouvinte.getPlaylists();
	}
	
	/**
	 * Retorna os dados da música de uma playlist específica.
	 * 
	 * @param idPlaylist ID da playlist solicitada.
	 * @return Retorna a lista de músicas da playlist recuperada.
	 */
	public List<Musica> listarMusicasPlaylist(int idPlaylist) {
        Ouvinte ouvinte = this.getOuvinteLogado();
        if (ouvinte == null) {
            return new ArrayList<>();
        }

        // Busca a playlist correspondente dentro da lista do ouvinte logado.
        return ouvinte.getPlaylists().stream()
            .filter(p -> p.getId() == idPlaylist)
            .findFirst()
            .map(Playlist::getMusicas) // Se achar a playlist, extrai a lista de músicas dela.
            .orElse(new ArrayList<>()); // Se não achar, devolve uma lista vazia.
    }
	
	/**
	 * Busca os dados de todas as músicas cadastradas, assim o ouvinte
	 * pode escolher mais facilmente qual música colocar na playlist.
	 * 
	 * @return Retorna a lista com todas as músicas recuperadas.
	 */
	public List <Musica> listarTodasMusicas() {
		Ouvinte ouvinte = this.getOuvinteLogado();
		if (ouvinte == null) return new ArrayList<>();
		return this.musicaDAO.listarMusicas();
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
		
		// Verificação de objetos nulos.
		if (ouvinte == null || album == null) return false;
		
		// Verifica se o álbum já está favoritado.
		boolean jaExiste = ouvinte.getAlbunsFavoritos().stream().anyMatch(a -> a.getId() == idAlbum);
		if (jaExiste) {
			System.out.println("O álbum já consta nos favoritos!");
			return false;
		}
		
		// Adiciona os álbuns aos favoritos e atualiza o banco de dados.
		ouvinte.adicionarAlbumFav(album);
		this.ouvinteDAO.atualizar(ouvinte); 
		System.out.println("Álbum: " + album.getTitulo() + " adicionado aos favoritos!");
		
		// Adiciona ao tempo ouvido do ouvinte.
		int duracaoTotal = 0;
		for (Musica m : album.getMusicas()) {
			duracaoTotal += m.getDuracaoMin();
		}
		
		// Atualiza o banco de dados com os minutos ouvintos.
		ouvinte.adicionarTempo(duracaoTotal);
		this.ouvinteDAO.atualizar(ouvinte);
		
		// Atualiza os ouvintes mensais do criador e atualiza o banco de dados.
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
		
		// Verifica se os objetos recuperados são nulos.
		if (ouvinte == null || album == null) return false;
		
		// Tenta remover o álbum favoritado da lista.
		boolean removeu = ouvinte.getAlbunsFavoritos().removeIf(a -> a.getId() == idAlbum);
		
		// Se der certo, atualiza o banco de dados.
		if (removeu) {
			this.ouvinteDAO.atualizar(ouvinte);
			System.out.println("Álbum removido dos favoritos!");
		} 
		
		return removeu;
	}
	
	/**
	 * Busca os dados dos álbuns favoritados pelo ouvinte.
	 * 
	 * @return Retorna a lista com todos os álbuns recuperados.
	 */
	public List<Album> listarAlbunsFavoritos() {
	    Ouvinte ouvinte = this.getOuvinteLogado();
	    if (ouvinte == null) return new ArrayList<>();
	    return ouvinte.getAlbunsFavoritos();
	}
	
	/**
	 * Recupera todos os álbuns registrados para o ouvinte;
	 * 
	 * @return Retorna a lista de álbuns recuperados.
	 */
	public List<Album> listarTodosAlbuns() {
        return this.albumDAO.listarAlbuns();
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
		
		// Verifica se os objetos recuperados são nulos.
		if (ouvinte == null || podcast == null) return false;
		
		// Verifica se o podcasts já estão favoritados.
		boolean jaExiste = ouvinte.getPodcastsFavoritos().stream().anyMatch(p -> p.getId() == idPodcast);
		if (jaExiste) {
			System.out.println("Este podcast já está nos favoritos!");
			return false;
		}
		
		// Adiciona o podcast favorito e salva o no banco de dados.
		ouvinte.adicionarPodcastFav(podcast);
		this.ouvinteDAO.atualizar(ouvinte);
		System.out.println("Podcast: " + podcast.getNome() + " adicionado aos favoritos!");
		
		// Atualiza o tempo ouvido pelo ouvinte.
		int duracaoTotal = 0;
		for (Episodio ep : podcast.getEpisodios()) {
			duracaoTotal += ep.getDuracaoMin();
		}
		
		// Atualiza os minutos ouvidos no banco de dados.
		ouvinte.adicionarTempo(duracaoTotal);
		this.ouvinteDAO.atualizar(ouvinte);
		
		// Incrementa os ouvintes mensais do criador e atualiza o banco de dados.
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
		
		// Verifica se os objetos recuperados são nulos.
		if (ouvinte == null || podcast == null) return false;
		
		// Tenta remover o podcast da lista de favoritos e salva no banco de dados se der certo.
		boolean removeu = ouvinte.getPodcastsFavoritos().removeIf(p -> p.getId() == idPodcast);		
		if (removeu) {
			this.ouvinteDAO.atualizar(ouvinte);
			System.out.println("Podcast removido dos favoritos!");
		}
		
		return removeu;
	}
	
	/**
	 * Recupera os dados dos podcasts registrados.
	 * 
	 * @return Retorna a lista com os podcasts recuperados.
	 */
	public List<Podcast> listarTodosPodcasts() {
        return this.podcastDAO.listarPodcasts();
    }
	
	/**
	 * Busca os dados dos podcasts favoritados pelo ouvinte.
	 * 
	 * @return Retorna a lista com todos os podcasts recuperados.
	 */
	public List<Podcast> listarPodcastsFavoritos() {
	    Ouvinte ouvinte = this.getOuvinteLogado();
	    if (ouvinte == null) return new ArrayList<>();
	    return ouvinte.getPodcastsFavoritos();
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

        // Contabiliza os gêneros das músicas nas Playlists
        for (Playlist p : ouvinte.getPlaylists()) {
            for (Musica m : p.getMusicas()) {
                String genero = m.getGenero();
                // Pega o valor atual daquele gênero e soma +1 (se não existir, começa do 0)
                contagemGeneros.put(genero, contagemGeneros.getOrDefault(genero, 0) + 1);
            }
        }

        // Contabiliza os gêneros das músicas nos Álbuns Favoritos
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

        // Descobre qual gênero teve a maior pontuação
        String generoFavorito = "";
        int maiorContagem = 0;

        for (Map.Entry<String, Integer> entrada : contagemGeneros.entrySet()) {
            if (entrada.getValue() > maiorContagem) {
                maiorContagem = entrada.getValue();
                generoFavorito = entrada.getKey();
            }
        }

        // Salva o resultado no banco
        ouvinte.setGeneroFavorito(generoFavorito);
        this.ouvinteDAO.atualizar(ouvinte);
        System.out.println("Novo gênero favorito calculado: " + generoFavorito + " (" + maiorContagem + " músicas)");
    }
}