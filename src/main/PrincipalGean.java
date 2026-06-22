package main;

import java.util.Scanner;

import controller.*;
import model.actors.*;
import java.time.LocalDate;

public class PrincipalGean {

		public static void main(String[] args) {
			
			Scanner sc = new Scanner(System.in);
			
			ControllerAutenticador auth = new ControllerAutenticador();
			ControllerAdmin admin = new ControllerAdmin();
			ControllerCriador criador = new ControllerCriador();
			ControllerOuvinte ouvinte = new ControllerOuvinte();
			ControllerPerfil perfil = new ControllerPerfil();
			
			boolean executando = true;
			
			while (executando) {
				
				System.out.println("\n===== SOUNDIFY =====");
				System.out.println("1 - Login");
				System.out.println("2 - Cadastrar Ouvinte");
				System.out.println("3 - Cadastrar Criador");
				System.out.println("0 - Sair");
				
				int opcao = Integer.parseInt(sc.nextLine());
				
				switch (opcao) {
				
				case 1:
					
					System.out.print("Login: ");
					String login = sc.nextLine();
					
					System.out.print("Senha: ");
					String senha = sc.nextLine();
					
					Usuario usuario = auth.login(login, senha);
					
					if(usuario == null) {
						System.out.println("Falha no login");
					}
					
					else if(usuario instanceof Administrador) {
						
						boolean menuAdmin = true;

						while (menuAdmin) {

						    System.out.println("\n=== MENU ADMINISTRADOR ===");

						    System.out.println("1 - Suspender usuário");
						    System.out.println("2 - Remover conteúdo");

						    System.out.println("3 - Alterar dados pessoais");
						    System.out.println("4 - Trocar senha");
						    System.out.println("5 - Alterar plano");

						    System.out.println("0 - Logout");

						    int opAdmin = Integer.parseInt(sc.nextLine());

						    switch (opAdmin) {

						    case 1:

						        System.out.print("ID do usuário: ");
						        int idUsuario = Integer.parseInt(sc.nextLine());

						        admin.suspenderUsuario(idUsuario);

						        break;

						    case 2:

						        System.out.print("ID do conteúdo: ");
						        int idConteudo = Integer.parseInt(sc.nextLine());

						        admin.removerConteudo(idConteudo);

						        break;

						    case 3:

						        System.out.print("Novo nome: ");
						        String novoNome = sc.nextLine();

						        System.out.print("Novo sexo: ");
						        String novoSexo = sc.nextLine();

						        perfil.atualizarDadosPessoais(novoNome, novoSexo);

						        break;

						    case 4:

						        System.out.print("Senha atual: ");
						        String senhaAtual = sc.nextLine();

						        System.out.print("Nova senha: ");
						        String novaSenha = sc.nextLine();

						        perfil.trocarSenha(senhaAtual, novaSenha);

						        break;

						    case 5:

						        System.out.print("Novo plano (Free/Premium): ");
						        String novoPlano = sc.nextLine();

						        perfil.mudarPlano(novoPlano);

						        break;

						    case 0:

						        auth.logout();
						        menuAdmin = false;

						        break;

						    default:

						        System.out.println("Opção inválida!");
						    }
						}
					}
					
					else if (usuario instanceof Criador) {
						
						boolean menuCriador = true;

						while (menuCriador) {
							
						    System.out.println("\n=== MENU CRIADOR ===");
						    System.out.println("1 - Criar álbum");
						    System.out.println("2 - Adicionar música ao álbum");
						    System.out.println("3 - Remover música do álbum");
						    System.out.println("4 - Lançar álbum");
						    System.out.println("5 - Deletar álbum");

						    System.out.println("6 - Criar podcast");
						    System.out.println("7 - Adicionar episódio");
						    System.out.println("8 - Remover episódio");
						    System.out.println("9 - Deletar podcast");

						    System.out.println("10 - Alterar dados pessoais");
						    System.out.println("11 - Trocar senha");
						    System.out.println("12 - Alterar plano");

						    System.out.println("0 - Logout");

						    int opCriador = Integer.parseInt(sc.nextLine());

						    switch (opCriador) {

						    case 1:

						        System.out.print("Título do álbum: ");
						        String tituloAlbum = sc.nextLine();

						        System.out.print("Tipo do álbum: ");
						        String tipoAlbum = sc.nextLine();

						        criador.criarAlbum(tituloAlbum, tipoAlbum);
						        break;

						    case 2:

						        System.out.print("ID do álbum: ");
						        int idAlbumMusica = Integer.parseInt(sc.nextLine());

						        System.out.print("Título da música: ");
						        String tituloMusica = sc.nextLine();

						        System.out.print("Duração (segundos): ");
						        int duracaoMusica = Integer.parseInt(sc.nextLine());

						        System.out.print("Gênero: ");
						        String genero = sc.nextLine();

						        System.out.print("Letra: ");
						        String letra = sc.nextLine();

						        criador.adicionarMusicaAlbum(idAlbumMusica, tituloMusica, duracaoMusica, genero, letra);

						        break;

						    case 3:

						        System.out.print("ID do álbum: ");
						        int idAlbumRemover = Integer.parseInt(sc.nextLine());

						        System.out.print("ID da música: ");
						        int idMusicaRemover = Integer.parseInt(sc.nextLine());

						        criador.removerMusicaAlbum(idAlbumRemover, idMusicaRemover);

						        break;

						    case 4:

						        System.out.print("ID do álbum: ");
						        int idAlbumLancar = Integer.parseInt(sc.nextLine());

						        criador.lancarAlbum(idAlbumLancar);

						        break;

						    case 5:

						        System.out.print("ID do álbum: ");
						        int idAlbumExcluir = Integer.parseInt(sc.nextLine());

						        criador.deletarALbum(idAlbumExcluir);

						        break;

						    case 6:

						        System.out.print("Nome do podcast: ");
						        String nomePodcast = sc.nextLine();

						        System.out.print("Tema do podcast: ");
						        String temaPodcast = sc.nextLine();

						        criador.criarPodcast(nomePodcast, temaPodcast);

						        break;

						    case 7:

						        System.out.print("ID do podcast: ");
						        int idPodcastAdicionar = Integer.parseInt(sc.nextLine());

						        System.out.print("Título do episódio: ");
						        String tituloEp = sc.nextLine();

						        System.out.print("Duração do episódio: ");
						        int duracaoEp = Integer.parseInt(sc.nextLine());

						        System.out.print("Número do episódio: ");
						        int numeroEp = Integer.parseInt(sc.nextLine());

						        criador.adicionarEpPodcast( idPodcastAdicionar, tituloEp, duracaoEp, numeroEp);

						        break;

						    case 8:

						        System.out.print("ID do podcast: ");
						        int idPodcastRemover = Integer.parseInt(sc.nextLine());

						        System.out.print("ID do episódio: ");
						        int idEpRemover = Integer.parseInt(sc.nextLine());

						        criador.removerEpPodcast(idPodcastRemover, idEpRemover);

						        break;

						    case 9:

						        System.out.print("ID do podcast: ");
						        int idPodcastExcluir = Integer.parseInt(sc.nextLine());

						        criador.deletarPodcast(idPodcastExcluir);

						        break;

						    case 10:

						        System.out.print("Novo nome: ");
						        String novoNome = sc.nextLine();

						        System.out.print("Novo sexo: ");
						        String novoSexo = sc.nextLine();

						        perfil.atualizarDadosPessoais(novoNome, novoSexo);

						        break;

						    case 11:

						        System.out.print("Senha atual: ");
						        String senhaAtual = sc.nextLine();

						        System.out.print("Nova senha: ");
						        String novaSenha = sc.nextLine();

						        perfil.trocarSenha(senhaAtual, novaSenha);

						        break;

						    case 12:

						        System.out.print("Novo plano (Free/Premium): ");
						        String plano = sc.nextLine();

						        perfil.mudarPlano(plano);

						        break;

						    case 0:

						        auth.logout();
						        menuCriador = false;

						        break;

						    default:

						        System.out.println("Opção inválida!");
							}
						}
					}
					
					else if (usuario instanceof Ouvinte) {
						
						boolean menuOuvinte = true;

						while (menuOuvinte) {

						    System.out.println("\n=== MENU OUVINTE ===");

						    System.out.println("1 - Criar playlist");
						    System.out.println("2 - Deletar playlist");
						    System.out.println("3 - Adicionar música à playlist");
						    System.out.println("4 - Remover música da playlist");
						    System.out.println("5 - Compartilhar playlist");

						    System.out.println("6 - Favoritar álbum");
						    System.out.println("7 - Desfavoritar álbum");

						    System.out.println("8 - Favoritar podcast");
						    System.out.println("9 - Desfavoritar podcast");

						    System.out.println("10 - Atualizar gênero favorito");

						    System.out.println("11 - Alterar dados pessoais");
						    System.out.println("12 - Trocar senha");
						    System.out.println("13 - Alterar plano");

						    System.out.println("0 - Logout");

						    int opOuvinte = Integer.parseInt(sc.nextLine());

						    switch (opOuvinte) {

						    case 1:

						        System.out.print("Título da playlist: ");
						        String titulo = sc.nextLine();

						        System.out.print("Descrição da playlist: ");
						        String descricao = sc.nextLine();

						        ouvinte.criarPlaylist(titulo, descricao);

						        break;

						    case 2:

						        System.out.print("ID da playlist: ");
						        int idPlaylistExcluir = Integer.parseInt(sc.nextLine());

						        ouvinte.deletarPlaylist(idPlaylistExcluir);

						        break;

						    case 3:

						        System.out.print("ID da playlist: ");
						        int idPlaylistAdd = Integer.parseInt(sc.nextLine());

						        System.out.print("ID da música: ");
						        int idMusicaAdd = Integer.parseInt(sc.nextLine());

						        ouvinte.adicionarMusicaPlaylist(idPlaylistAdd, idMusicaAdd);

						        break;

						    case 4:

						        System.out.print("ID da playlist: ");
						        int idPlaylistRem = Integer.parseInt(sc.nextLine());

						        System.out.print("ID da música: ");
						        int idMusicaRem = Integer.parseInt(sc.nextLine());

						        ouvinte.removerMusicaPlaylist(idPlaylistRem, idMusicaRem);

						        break;

						    case 5:

						        System.out.print("ID da playlist: ");
						        int idPlaylistCompartilhar = Integer.parseInt(sc.nextLine());

						        ouvinte.compartilharPlaylist(idPlaylistCompartilhar);

						        break;

						    case 6:

						        System.out.print("ID do álbum: ");
						        int idAlbumFav = Integer.parseInt(sc.nextLine());

						        ouvinte.favoritarAlbum(idAlbumFav);

						        break;

						    case 7:

						        System.out.print("ID do álbum: ");
						        int idAlbumDesfav = Integer.parseInt(sc.nextLine());

						        ouvinte.desfavoritarAlbum(idAlbumDesfav);

						        break;

						    case 8:

						        System.out.print("ID do podcast: ");
						        int idPodcastFav = Integer.parseInt(sc.nextLine());

						        ouvinte.favoritarPodcast(idPodcastFav);

						        break;

						    case 9:

						        System.out.print("ID do podcast: ");
						        int idPodcastDesfav = Integer.parseInt(sc.nextLine());

						        ouvinte.desfavoritarPodcast(idPodcastDesfav);

						        break;

						    case 10:

						        ouvinte.atualizarGeneroFavorito();

						        break;

						    case 11:

						        System.out.print("Novo nome: ");
						        String novoNome = sc.nextLine();

						        System.out.print("Novo sexo: ");
						        String novoSexo = sc.nextLine();

						        perfil.atualizarDadosPessoais(novoNome, novoSexo);

						        break;

						    case 12:

						        System.out.print("Senha atual: ");
						        String senhaAtual = sc.nextLine();

						        System.out.print("Nova senha: ");
						        String novaSenha = sc.nextLine();

						        perfil.trocarSenha(senhaAtual, novaSenha);

						        break;

						    case 13:

						        System.out.print("Novo plano (Free/Premium): ");
						        String novoPlano = sc.nextLine();

						        perfil.mudarPlano(novoPlano);

						        break;

						    case 0:

						        auth.logout();
						        menuOuvinte = false;

						        break;

						    default:

						        System.out.println("Opção inválida!");
						    }
						}
					}
					
					break;
					
				case 2:
					

	                System.out.print("Login: ");
	                String loginOuvinte = sc.nextLine();

	                System.out.print("Senha: ");
	                String senhaOuvinte = sc.nextLine();

	                System.out.print("Nome: ");
	                String nomeOuvinte = sc.nextLine();

	                System.out.print("Sexo: ");
	                String sexoOuvinte = sc.nextLine();

	                System.out.print("Ano nascimento: ");
	                int ano = Integer.parseInt(sc.nextLine());

	                System.out.print("Mês nascimento: ");
	                int mes = Integer.parseInt(sc.nextLine());

	                System.out.print("Dia nascimento: ");
	                int dia = Integer.parseInt(sc.nextLine());

	                auth.cadastrarOuvinte(loginOuvinte, senhaOuvinte, nomeOuvinte, sexoOuvinte, LocalDate.of(ano, mes, dia));
	                
	                break;
	                
				case 3:
					

	                System.out.print("Login: ");
	                String loginCriador = sc.nextLine();

	                System.out.print("Senha: ");
	                String senhaCriador = sc.nextLine();

	                System.out.print("Nome: ");
	                String nomeCriador = sc.nextLine();

	                System.out.print("Sexo: ");
	                String sexoCriador = sc.nextLine();

	                System.out.print("Ano nascimento: ");
	                int anoCriador = Integer.parseInt(sc.nextLine());

	                System.out.print("Mês nascimento: ");
	                int mesCriador = Integer.parseInt(sc.nextLine());

	                System.out.print("Dia nascimento: ");
	                int diaCriador = Integer.parseInt(sc.nextLine());

	                auth.cadastrarCriador(loginCriador, senhaCriador, nomeCriador, sexoCriador, LocalDate.of(anoCriador, mesCriador, diaCriador));

	                break;
	                
				case 0:
					
					executando = false;
					break;
					
					default:
						
						System.out.println("Opção inválida!");

				}
			}
			
			sc.close(); //Serve para fechar o objeto Scanner e liberar o recurso que ele está utilizando.
			
		}

}
