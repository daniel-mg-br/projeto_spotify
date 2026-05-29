package database;

import java.util.ArrayList;
import java.util.List;
import model.actors.Conta;
import model.actors.Usuario;
import model.content.Album;
import model.content.Playlist;
import model.content.Podcast;
import model.content.Conteudo;

// Classe para simular o banco de dados que roda em tempo de execução do programa
public class BancoMemoria {
	// Lista para armazenar as contas dos usuários
	public static List <Conta> contas = new ArrayList<>();
	
	// Lista para armazenar usuários diversos
	public static List <Usuario> usuarios = new ArrayList<>();
	
	// Lista para armazenar os dois tipos de conteúdo
	public static List <Conteudo> conteudos = new ArrayList<>();
	
	// Listas simulando as tabelas para os álbuns, playlists e podcasts
	public static List <Album> albuns = new ArrayList<>();
	public static List <Playlist> playlists = new ArrayList<>();
	public static List <Podcast> podcasts = new ArrayList<>();
	
	// Contadores de ID simulando o AUTOINCREMENT do SQL
	public static int contadorConta = 1;
	public static int contadorUsuario = 1;
	public static int contadorConteudo = 1;
	public static int contadorAlbum = 1;
	public static int contadorPlaylist = 1;
	public static int contadorPodcast = 1;
}
