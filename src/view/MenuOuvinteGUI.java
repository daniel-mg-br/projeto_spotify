package view;

import controller.ControllerOuvinte;
import javax.swing.*;
import java.awt.*;

/**
 * Interface gráfica para o menu do Ouvinte.
 * Organiza as funcionalidades em abas para uma melhor experiência do usuário (UX).
 */
public class MenuOuvinteGUI extends JFrame {

    private ControllerOuvinte controller;

    public MenuOuvinteGUI() {
        this.controller = new ControllerOuvinte();

        // Configurações da Janela Principal
        setTitle("Painel do Ouvinte");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Uso de JTabbedPane para agrupar funcionalidades por contexto (Abas)
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Playlists", criarPainelPlaylist());
        tabbedPane.addTab("Álbuns", criarPainelAlbuns());
        tabbedPane.addTab("Podcasts", criarPainelPodcasts());

        add(tabbedPane, BorderLayout.CENTER);
        
        // Rodapé contendo o botão de Logout para retornar à tela de Login
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogout = new JButton("Sair (Logout)");
        btnLogout.addActionListener(e -> { dispose(); new LoginGUI(); });
        painelRodape.add(btnLogout);
        add(painelRodape, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Monta o painel específico para gerenciamento de Playlists.
     */
    private JPanel criarPainelPlaylist() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField txtIdP = new JTextField(); // Campo para ID da Playlist
        JTextField txtIdM = new JTextField(); // Campo para ID da Música
        
        // Botão para criar uma nova playlist chamando o controller
        panel.add(new JButton("Criar Nova Playlist") {{ addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Nome da Playlist:");
            if(nome != null) controller.criarPlaylist(nome, "Descrição");
        });}});
        
        panel.add(new JLabel("ID Playlist:")); panel.add(txtIdP);
        panel.add(new JLabel("ID Música:")); panel.add(txtIdM);
        
        // Painel interno de botões para operações de música em playlist
        JPanel pBotoes = new JPanel(new GridLayout(1, 2, 5, 5));
        pBotoes.add(new JButton("Adicionar") {{ addActionListener(e -> 
            controller.adicionarMusicaPlaylist(Integer.parseInt(txtIdP.getText()), Integer.parseInt(txtIdM.getText()))); }});
        pBotoes.add(new JButton("Remover") {{ addActionListener(e -> 
            controller.removerMusicaPlaylist(Integer.parseInt(txtIdP.getText()), Integer.parseInt(txtIdM.getText()))); }});
        panel.add(pBotoes);
        
        // Ação de compartilhar, usando o ID da playlist capturado no campo txtIdP
        panel.add(new JButton("Compartilhar/Deletar") {{ addActionListener(e -> {
            int id = Integer.parseInt(txtIdP.getText());
            controller.compartilharPlaylist(id);
            JOptionPane.showMessageDialog(null, "Ação realizada na playlist " + id);
        });}});
        
        return panel;
    }

    /**
     * Monta o painel para gerenciamento de favoritos (Álbuns).
     */
    private JPanel criarPainelAlbuns() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        JTextField txtIdA = new JTextField();
        panel.add(new JLabel("ID Álbum:")); panel.add(txtIdA);
        
        // Botões para manipular a lista de álbuns favoritos do ouvinte
        panel.add(new JButton("Favoritar") {{ addActionListener(e -> controller.favoritarAlbum(Integer.parseInt(txtIdA.getText()))); }});
        panel.add(new JButton("Desfavoritar") {{ addActionListener(e -> controller.desfavoritarAlbum(Integer.parseInt(txtIdA.getText()))); }});
        panel.add(new JButton("Atualizar Gênero Favorito") {{ addActionListener(e -> controller.atualizarGeneroFavorito()); }});
        return panel;
    }

    /**
     * Monta o painel para gerenciamento de favoritos (Podcasts).
     */
    private JPanel criarPainelPodcasts() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        JTextField txtIdPod = new JTextField();
        panel.add(new JLabel("ID Podcast:")); panel.add(txtIdPod);
        
        // Botões para manipular a lista de podcasts favoritos do ouvinte
        panel.add(new JButton("Favoritar Podcast") {{ addActionListener(e -> controller.favoritarPodcast(Integer.parseInt(txtIdPod.getText()))); }});
        panel.add(new JButton("Desfavoritar Podcast") {{ addActionListener(e -> controller.desfavoritarPodcast(Integer.parseInt(txtIdPod.getText()))); }});
        return panel;
    }
}