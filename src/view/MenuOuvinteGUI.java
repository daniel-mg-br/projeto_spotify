package view;

import controller.ControllerOuvinte;

import javax.swing.*;
import java.awt.*;

/**
 * Classe responsável pela interface gráfica do menu do Ouvinte.
 * Permite gerenciamento de playlists, músicas e favoritos (álbuns e podcasts).
 * Atua como camada de apresentação chamando o ControllerOuvinte.
 */
public class MenuOuvinteGUI extends JFrame {

    // Controller responsável pelas operações do usuário Ouvinte
    private ControllerOuvinte controller;

    // Campos de entrada para IDs
    private JTextField txtIdPlaylist;
    private JTextField txtIdMusica;
    private JTextField txtIdAlbum;
    private JTextField txtIdPodcast;

    /**
     * Construtor da interface do menu do Ouvinte.
     * Inicializa componentes e configura eventos da UI.
     */
    public MenuOuvinteGUI() {

        // Inicializa controller
        controller = new ControllerOuvinte();

        // Configuração da janela principal
        setTitle("Menu Ouvinte");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela

        // Painel principal com grade (10 linhas, 2 colunas)
        JPanel panel = new JPanel(new GridLayout(10, 2, 5, 5));

        // ================= PLAYLIST =================

        panel.add(new JLabel("Criar Playlist (título fixo demo)"));

        JButton btnCriarPlaylist = new JButton("Criar");
        panel.add(btnCriarPlaylist);

        btnCriarPlaylist.addActionListener(e -> criarPlaylist());

        panel.add(new JLabel("ID Playlist:"));
        txtIdPlaylist = new JTextField();
        panel.add(txtIdPlaylist);

        panel.add(new JLabel("ID Música:"));
        txtIdMusica = new JTextField();
        panel.add(txtIdMusica);

        JButton btnAddMusica = new JButton("Adicionar Música");
        JButton btnRemMusica = new JButton("Remover Música");

        panel.add(btnAddMusica);
        panel.add(btnRemMusica);

        btnAddMusica.addActionListener(e -> adicionarMusica());
        btnRemMusica.addActionListener(e -> removerMusica());

        // ================= OUTRAS AÇÕES DE PLAYLIST =================

        JButton btnCompartilhar = new JButton("Compartilhar Playlist");
        JButton btnDeletarPlaylist = new JButton("Deletar Playlist");

        panel.add(btnCompartilhar);
        panel.add(btnDeletarPlaylist);

        btnCompartilhar.addActionListener(e -> compartilhar());
        btnDeletarPlaylist.addActionListener(e -> deletarPlaylist());

        // ================= FAVORITAR ÁLBUM =================

        panel.add(new JLabel("ID Álbum:"));
        txtIdAlbum = new JTextField();
        panel.add(txtIdAlbum);

        JButton btnFavAlbum = new JButton("Favoritar Álbum");
        JButton btnUnfavAlbum = new JButton("Desfavoritar Álbum");

        panel.add(btnFavAlbum);
        panel.add(btnUnfavAlbum);

        btnFavAlbum.addActionListener(e -> favAlbum());
        btnUnfavAlbum.addActionListener(e -> desfavAlbum());

        // ================= FAVORITAR PODCAST =================

        panel.add(new JLabel("ID Podcast:"));
        txtIdPodcast = new JTextField();
        panel.add(txtIdPodcast);

        JButton btnFavPodcast = new JButton("Favoritar Podcast");
        JButton btnUnfavPodcast = new JButton("Desfavoritar Podcast");

        panel.add(btnFavPodcast);
        panel.add(btnUnfavPodcast);

        btnFavPodcast.addActionListener(e -> favPodcast());
        btnUnfavPodcast.addActionListener(e -> desfavPodcast());

        // Adiciona painel à janela
        add(panel);

        // Exibe a interface
        setVisible(true);
    }

    /**
     * Cria uma playlist com dados fixos (demo).
     */
    private void criarPlaylist() {

        boolean ok = controller.criarPlaylist("Nova Playlist", "Descrição");

        JOptionPane.showMessageDialog(this,
                ok ? "Playlist criada!" : "Erro ao criar playlist");
    }

    /**
     * Adiciona uma música a uma playlist.
     */
    private void adicionarMusica() {

        try {
            int idP = Integer.parseInt(txtIdPlaylist.getText());
            int idM = Integer.parseInt(txtIdMusica.getText());

            boolean ok = controller.adicionarMusicaPlaylist(idP, idM);

            JOptionPane.showMessageDialog(this,
                    ok ? "Música adicionada!" : "Erro ao adicionar");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "IDs inválidos");
        }
    }

    /**
     * Remove uma música de uma playlist.
     */
    private void removerMusica() {

        try {
            int idP = Integer.parseInt(txtIdPlaylist.getText());
            int idM = Integer.parseInt(txtIdMusica.getText());

            boolean ok = controller.removerMusicaPlaylist(idP, idM);

            JOptionPane.showMessageDialog(this,
                    ok ? "Música removida!" : "Erro ao remover");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "IDs inválidos");
        }
    }

    /**
     * Compartilha uma playlist.
     */
    private void compartilhar() {

        try {
            int idP = Integer.parseInt(txtIdPlaylist.getText());

            boolean ok = controller.compartilharPlaylist(idP);

            JOptionPane.showMessageDialog(this,
                    ok ? "Playlist compartilhada!" : "Erro ao compartilhar");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID inválido");
        }
    }

    /**
     * Deleta uma playlist.
     */
    private void deletarPlaylist() {

        try {
            int idP = Integer.parseInt(txtIdPlaylist.getText());

            boolean ok = controller.deletarPlaylist(idP);

            JOptionPane.showMessageDialog(this,
                    ok ? "Playlist deletada!" : "Erro ao deletar");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID inválido");
        }
    }

    /**
     * Marca um álbum como favorito.
     */
    private void favAlbum() {

        try {
            int id = Integer.parseInt(txtIdAlbum.getText());

            boolean ok = controller.favoritarAlbum(id);

            JOptionPane.showMessageDialog(this,
                    ok ? "Álbum favoritado!" : "Erro");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID inválido");
        }
    }

    /**
     * Remove álbum dos favoritos.
     */
    private void desfavAlbum() {

        try {
            int id = Integer.parseInt(txtIdAlbum.getText());

            boolean ok = controller.desfavoritarAlbum(id);

            JOptionPane.showMessageDialog(this,
                    ok ? "Álbum removido!" : "Erro");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID inválido");
        }
    }

    /**
     * Marca podcast como favorito.
     */
    private void favPodcast() {

        try {
            int id = Integer.parseInt(txtIdPodcast.getText());

            boolean ok = controller.favoritarPodcast(id);

            JOptionPane.showMessageDialog(this,
                    ok ? "Podcast favoritado!" : "Erro");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID inválido");
        }
    }

    /**
     * Remove podcast dos favoritos.
     */
    private void desfavPodcast() {

        try {
            int id = Integer.parseInt(txtIdPodcast.getText());

            boolean ok = controller.desfavoritarPodcast(id);

            JOptionPane.showMessageDialog(this,
                    ok ? "Podcast removido!" : "Erro");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID inválido");
        }
    }
}