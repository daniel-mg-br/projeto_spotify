package view;

import controller.ControllerCriador;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Classe responsável pela interface gráfica do menu do Criador.
 * Conecta-se ao ControllerCriador para executar todas as ações do sistema.
 */
public class MenuCriadorGUI extends JFrame {

    private ControllerCriador controller;

    public MenuCriadorGUI() {
        this.controller = new ControllerCriador();

        // Configuração da janela principal
        setTitle("Painel do Criador");
        setSize(650, 450); // Tamanho ajustado para o novo layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        // Painel principal usando BorderLayout
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- 1. Painel de Gerenciamento de Álbuns ---
        JPanel painelAlbuns = new JPanel(new GridLayout(3, 2, 10, 10));
        painelAlbuns.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Gerenciamento de Álbuns", 
                TitledBorder.LEFT, TitledBorder.TOP));

        JButton btnCriarAlbum = new JButton("Criar Novo Álbum");
        JButton btnLancarAlbum = new JButton("Lançar Álbum");
        JButton btnAddMusica = new JButton("Adicionar Música");
        JButton btnRemoverMusica = new JButton("Remover Música");
        JButton btnDeletarAlbum = new JButton("Deletar Álbum");

        painelAlbuns.add(btnCriarAlbum);
        painelAlbuns.add(btnLancarAlbum);
        painelAlbuns.add(btnAddMusica);
        painelAlbuns.add(btnRemoverMusica);
        painelAlbuns.add(btnDeletarAlbum);
        painelAlbuns.add(new JLabel("")); // Espaço vazio para manter o grid simétrico

        // --- 2. Painel de Gerenciamento de Podcasts ---
        JPanel painelPodcasts = new JPanel(new GridLayout(2, 2, 10, 10));
        painelPodcasts.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Gerenciamento de Podcasts", 
                TitledBorder.LEFT, TitledBorder.TOP));

        JButton btnCriarPodcast = new JButton("Criar Novo Podcast");
        JButton btnAddEpisodio = new JButton("Adicionar Episódio");
        JButton btnRemoverEpisodio = new JButton("Remover Episódio");
        JButton btnDeletarPodcast = new JButton("Deletar Podcast");

        painelPodcasts.add(btnCriarPodcast);
        painelPodcasts.add(btnAddEpisodio);
        painelPodcasts.add(btnRemoverEpisodio);
        painelPodcasts.add(btnDeletarPodcast);

        // --- 3. Painel de Ações do Sistema (Rodapé) ---
        JPanel painelSistema = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogout = new JButton("Sair (Logout)");
        painelSistema.add(btnLogout);

        // --- 4. Configuração das Ações (Listeners) ---

        // Ações de Álbum
        btnCriarAlbum.addActionListener(e -> {
            String titulo = JOptionPane.showInputDialog(this, "Digite o título do Álbum:");
            if (titulo != null && !titulo.trim().isEmpty()) {
                String tipo = JOptionPane.showInputDialog(this, "Digite o tipo do Álbum (ex: EP, Studio):");
                boolean sucesso = controller.criarAlbum(titulo, tipo);
                exibirMensagem(sucesso, "Álbum criado com sucesso!", "Erro ao criar álbum.");
            }
        });

        btnLancarAlbum.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(this, "Digite o ID do Álbum a ser lançado:");
                if (idStr == null) return;
                int idAlbum = Integer.parseInt(idStr);
                
                boolean sucesso = controller.lancarAlbum(idAlbum);
                exibirMensagem(sucesso, "Álbum lançado com sucesso!", "Erro: Álbum não encontrado ou acesso negado.");
            } catch (NumberFormatException ex) {
                exibirErroFormatacao();
            }
        });

        btnAddMusica.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(this, "Digite o ID do Álbum:");
                if (idStr == null) return;
                int idAlbum = Integer.parseInt(idStr);

                String titulo = JOptionPane.showInputDialog(this, "Digite o título da Música:");
                int duracao = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite a duração (em minutos):"));
                String genero = JOptionPane.showInputDialog(this, "Digite o gênero:");
                String letra = JOptionPane.showInputDialog(this, "Digite a letra (opcional):");

                boolean sucesso = controller.adicionarMusicaAlbum(idAlbum, titulo, duracao, genero, letra);
                exibirMensagem(sucesso, "Música adicionada!", "Erro ao adicionar música.");
            } catch (NumberFormatException ex) {
                exibirErroFormatacao();
            }
        });

        btnRemoverMusica.addActionListener(e -> {
            try {
                String idAlbumStr = JOptionPane.showInputDialog(this, "Digite o ID do Álbum:");
                if (idAlbumStr == null) return;
                int idAlbum = Integer.parseInt(idAlbumStr);

                int idMusica = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite o ID da Música a remover:"));

                boolean sucesso = controller.removerMusicaAlbum(idAlbum, idMusica);
                exibirMensagem(sucesso, "Música removida com sucesso!", "Erro ao remover música.");
            } catch (NumberFormatException ex) {
                exibirErroFormatacao();
            }
        });

        btnDeletarAlbum.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(this, "Digite o ID do Álbum a ser deletado:");
                if (idStr == null) return;
                int idAlbum = Integer.parseInt(idStr);
                
                int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar este álbum e todas as suas músicas?", "Atenção", JOptionPane.YES_NO_OPTION);
                if (confirmacao == JOptionPane.YES_OPTION) {
                    boolean sucesso = controller.deletarAlbum(idAlbum);
                    exibirMensagem(sucesso, "Álbum deletado com sucesso!", "Erro ao deletar álbum.");
                }
            } catch (NumberFormatException ex) {
                exibirErroFormatacao();
            }
        });

        // Ações de Podcast
        btnCriarPodcast.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Digite o nome do Podcast:");
            if (nome != null && !nome.trim().isEmpty()) {
                String tema = JOptionPane.showInputDialog(this, "Digite o tema do Podcast:");
                boolean sucesso = controller.criarPodcast(nome, tema);
                exibirMensagem(sucesso, "Podcast criado com sucesso!", "Erro ao criar podcast.");
            }
        });

        btnAddEpisodio.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(this, "Digite o ID do Podcast:");
                if (idStr == null) return;
                int idPodcast = Integer.parseInt(idStr);

                String titulo = JOptionPane.showInputDialog(this, "Digite o título do Episódio:");
                int duracao = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite a duração (em minutos):"));
                int numEpisodio = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite o número do episódio:"));

                boolean sucesso = controller.adicionarEpPodcast(idPodcast, titulo, duracao, numEpisodio);
                exibirMensagem(sucesso, "Episódio adicionado!", "Erro ao adicionar episódio.");
            } catch (NumberFormatException ex) {
                exibirErroFormatacao();
            }
        });

        btnRemoverEpisodio.addActionListener(e -> {
            try {
                String idPodStr = JOptionPane.showInputDialog(this, "Digite o ID do Podcast:");
                if (idPodStr == null) return;
                int idPodcast = Integer.parseInt(idPodStr);

                int idEpisodio = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite o ID do Episódio a remover:"));

                boolean sucesso = controller.removerEpPodcast(idPodcast, idEpisodio);
                exibirMensagem(sucesso, "Episódio removido com sucesso!", "Erro ao remover episódio.");
            } catch (NumberFormatException ex) {
                exibirErroFormatacao();
            }
        });

        btnDeletarPodcast.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(this, "Digite o ID do Podcast a ser deletado:");
                if (idStr == null) return;
                int idPodcast = Integer.parseInt(idStr);
                
                int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar este podcast e todos os seus episódios?", "Atenção", JOptionPane.YES_NO_OPTION);
                if (confirmacao == JOptionPane.YES_OPTION) {
                    boolean sucesso = controller.deletarPodcast(idPodcast);
                    exibirMensagem(sucesso, "Podcast deletado com sucesso!", "Erro ao deletar podcast.");
                }
            } catch (NumberFormatException ex) {
                exibirErroFormatacao();
            }
        });

        // Ação de Logout (Volta para a tela de Login)
        btnLogout.addActionListener(e -> {
            dispose();       // Fecha o Menu do Criador
            new LoginGUI();  // Abre a tela de Login novamente
        });

        // --- 5. Montagem Final da Janela ---
        JPanel painelCentral = new JPanel(new BorderLayout(10, 10));
        painelCentral.add(painelAlbuns, BorderLayout.NORTH);
        painelCentral.add(painelPodcasts, BorderLayout.CENTER);

        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        painelPrincipal.add(painelSistema, BorderLayout.SOUTH);

        add(painelPrincipal);
        setVisible(true);
    }

    /**
     * Método auxiliar para exibir mensagens de sucesso ou erro.
     */
    private void exibirMensagem(boolean sucesso, String msgSucesso, String msgErro) {
        if (sucesso) {
            JOptionPane.showMessageDialog(this, msgSucesso, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, msgErro, "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método auxiliar para exibir erros de formatação de números.
     */
    private void exibirErroFormatacao() {
        JOptionPane.showMessageDialog(this, "Entrada inválida. Certifique-se de digitar apenas números (IDs, duração, etc).", "Erro de Formatação", JOptionPane.WARNING_MESSAGE);
    }
}