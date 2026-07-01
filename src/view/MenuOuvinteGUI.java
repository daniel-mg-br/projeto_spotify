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
        // Usar 0 no número de linhas faz o Grid se ajustar dinamicamente à quantidade de itens
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField txtIdP = new JTextField(); // Campo para ID da Playlist
        JTextField txtIdM = new JTextField(); // Campo para ID da Música
        
        // Botão para criar uma nova playlist
        JButton btnCriar = new JButton("Criar Nova Playlist");
        btnCriar.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Nome da Playlist:");
            if(nome != null && !nome.trim().isEmpty()) {
                boolean sucesso = controller.criarPlaylist(nome, "Descrição");
                if (sucesso) JOptionPane.showMessageDialog(this, "Playlist criada com sucesso!");
                else JOptionPane.showMessageDialog(this, "Erro ao criar playlist.");
            }
        });
        panel.add(btnCriar);
        
        // Campos de texto e rótulos
        panel.add(new JLabel("ID Playlist:")); 
        panel.add(txtIdP);
        panel.add(new JLabel("ID Música:")); 
        panel.add(txtIdM);
        
        // --- 1. Painel de Operações com Músicas (Adicionar / Remover) ---
        JPanel pBotoesMusica = new JPanel(new GridLayout(1, 2, 5, 5));
        
        JButton btnAdicionar = new JButton("Adicionar Música");
        btnAdicionar.addActionListener(e -> {
            try {
                int idPlaylist = Integer.parseInt(txtIdP.getText());
                int idMusica = Integer.parseInt(txtIdM.getText());
                
                boolean sucesso = controller.adicionarMusicaPlaylist(idPlaylist, idMusica);
                if(sucesso) JOptionPane.showMessageDialog(this, "Música adicionada com sucesso!");
                else JOptionPane.showMessageDialog(this, "Erro ao adicionar música.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Entrada inválida! Digite apenas números nos IDs.");
            }
        });
        
        JButton btnRemover = new JButton("Remover Música");
        btnRemover.addActionListener(e -> {
            try {
                int idPlaylist = Integer.parseInt(txtIdP.getText());
                int idMusica = Integer.parseInt(txtIdM.getText());
                
                boolean sucesso = controller.removerMusicaPlaylist(idPlaylist, idMusica);
                if(sucesso) JOptionPane.showMessageDialog(this, "Música removida com sucesso!");
                else JOptionPane.showMessageDialog(this, "Erro ao remover música.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Entrada inválida! Digite apenas números nos IDs.");
            }
        });
        
        pBotoesMusica.add(btnAdicionar);
        pBotoesMusica.add(btnRemover);
        panel.add(pBotoesMusica);
        
        // --- 2. Painel de Operações com a Playlist (Compartilhar / Deletar) ---
        JPanel pBotoesPlaylist = new JPanel(new GridLayout(1, 2, 5, 5));
        
        JButton btnCompartilhar = new JButton("Compartilhar Playlist");
        btnCompartilhar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtIdP.getText());
                
                boolean sucesso = controller.compartilharPlaylist(id);
                if(sucesso) JOptionPane.showMessageDialog(this, "Playlist compartilhada com sucesso!");
                else JOptionPane.showMessageDialog(this, "Erro ao compartilhar playlist.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um ID de Playlist válido para compartilhar.");
            }
        });
        
        JButton btnDeletar = new JButton("Deletar Playlist");
        btnDeletar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtIdP.getText());
                
                // Caixa de confirmação antes de apagar os dados do usuário
                int confirmacao = JOptionPane.showConfirmDialog(this, 
                        "Tem certeza que deseja deletar a playlist " + id + "?", 
                        "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (confirmacao == JOptionPane.YES_OPTION) {
                    boolean sucesso = controller.deletarPlaylist(id);
                    if(sucesso) JOptionPane.showMessageDialog(this, "Playlist deletada com sucesso!");
                    else JOptionPane.showMessageDialog(this, "Erro ao deletar playlist.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um ID de Playlist válido para deletar.");
            }
        });
        
        pBotoesPlaylist.add(btnCompartilhar);
        pBotoesPlaylist.add(btnDeletar);
        panel.add(pBotoesPlaylist);
        
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