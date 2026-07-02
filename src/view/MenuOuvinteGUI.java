package view;

import controller.ControllerOuvinte;
import controller.ControllerPerfil;
import model.content.Album;
import model.content.Musica;
import model.content.Playlist;
import model.content.Podcast;

import javax.swing.*;
import java.awt.*;

/**
 * Interface gráfica para o menu do Ouvinte.
 * Utiliza BorderLayout.NORTH para "ancorar" os componentes no topo
 * e evitar que o Swing estique os formulários verticalmente.
 */
public class MenuOuvinteGUI extends JFrame {

    private ControllerOuvinte controller;
    private ControllerPerfil controllerPerfil;

    // Componentes visuais que precisam ser atualizados dinamicamente
    private JComboBox<String> cbMinhasPlaylists;
    private JComboBox<String> cbMusicasDaPlaylist;
    private JComboBox<String> cbTodasMusicas;
    private JComboBox<String> cbTodosAlbuns;
    private JComboBox<String> cbAlbunsFavoritos;
    private JComboBox<String> cbTodosPodcasts;
    private JComboBox<String> cbPodcastsFavoritos;

    public MenuOuvinteGUI() {
        this.controller = new ControllerOuvinte();
        this.controllerPerfil = new ControllerPerfil();

        setTitle("Painel do Ouvinte");
        setSize(550, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Playlists", criarPainelPlaylist());
        tabbedPane.addTab("Álbuns", criarPainelAlbuns());
        tabbedPane.addTab("Podcasts", criarPainelPodcasts());
        tabbedPane.addTab("Meu Perfil", criarPainelPerfil()); 

        add(tabbedPane, BorderLayout.CENTER);
        
        // Rodapé com o botão de Logout
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelRodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY)); 
        JButton btnLogout = new JButton("Sair (Logout)");
        btnLogout.addActionListener(e -> { 
            controllerPerfil = null; 
            dispose(); 
            new LoginGUI(); 
        });
        painelRodape.add(btnLogout);
        add(painelRodape, BorderLayout.SOUTH);

        // Carrega os dados iniciais
        atualizarListasPlaylists();
        atualizarListasAlbuns();
        atualizarListasPodcasts();

        setVisible(true);
    }

    private int extrairId(String textoCombo) {
        try {
            return Integer.parseInt(textoCombo.split(" - ")[0]);
        } catch (Exception e) {
            return -1;
        }
    }

    // ================== PAINEL DE PLAYLISTS ==================
    private JPanel criarPainelPlaylist() {
        // O Wrapper (embrulho) trava o formulário no topo (NORTH)
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 1. Topo: Botão de Criar (Alinhado à esquerda)
        JPanel pTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
        pTopo.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnCriar = new JButton("Criar Nova Playlist");
        btnCriar.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(panel, "Nome da Playlist:");
            if (nome != null && !nome.trim().isEmpty()) {
                controller.criarPlaylist(nome, "Descrição");
                atualizarListasPlaylists(); 
                JOptionPane.showMessageDialog(panel, "Playlist criada com sucesso!");
            }
        });
        pTopo.add(btnCriar);
        
        // 2. Meio: Formulário 
        JPanel pForm = new JPanel(new GridLayout(3, 2, 10, 10));
        pForm.setMaximumSize(new Dimension(Short.MAX_VALUE, 90)); 
        pForm.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        cbMinhasPlaylists = new JComboBox<>();
        cbTodasMusicas = new JComboBox<>();
        cbMusicasDaPlaylist = new JComboBox<>();
        
        pForm.add(new JLabel("Selecione sua Playlist:")); 
        pForm.add(cbMinhasPlaylists);
        
        pForm.add(new JLabel("Músicas já nesta Playlist:")); 
        pForm.add(cbMusicasDaPlaylist);
        
        pForm.add(new JLabel("Selecione uma Música:")); 
        pForm.add(cbTodasMusicas);
        
        cbMinhasPlaylists.addActionListener(e -> atualizarMusicasDaPlaylistSelecionada());
        
        // 3. Fundo: Botões (Grid 2x2 para manter as proporções)
        JPanel pBotoes = new JPanel(new GridLayout(2, 2, 10, 10));
        pBotoes.setMaximumSize(new Dimension(400, 70)); // Trava a largura para não ficarem gigantes
        pBotoes.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton btnAddMusica = new JButton("Adicionar Música");
        btnAddMusica.addActionListener(e -> {
            if (cbMinhasPlaylists.getSelectedItem() == null || cbTodasMusicas.getSelectedItem() == null) return;
            int idP = extrairId(cbMinhasPlaylists.getSelectedItem().toString());
            int idM = extrairId(cbTodasMusicas.getSelectedItem().toString());
            if (controller.adicionarMusicaPlaylist(idP, idM)) {
                JOptionPane.showMessageDialog(panel, "Música adicionada!");
            } else {
                JOptionPane.showMessageDialog(panel, "Erro ou música já existe na playlist.");
            }
        });
        
        JButton btnRemoverMusica = new JButton("Remover Música");
        btnRemoverMusica.addActionListener(e -> {
            if (cbMinhasPlaylists.getSelectedItem() == null || cbTodasMusicas.getSelectedItem() == null) return;
            int idP = extrairId(cbMinhasPlaylists.getSelectedItem().toString());
            int idM = extrairId(cbTodasMusicas.getSelectedItem().toString());
            if (controller.removerMusicaPlaylist(idP, idM)) {
                JOptionPane.showMessageDialog(panel, "Música removida!");
            } else {
                JOptionPane.showMessageDialog(panel, "A música não está nesta playlist.");
            }
        });
        
        JButton btnCompartilhar = new JButton("Compartilhar Playlist");
        btnCompartilhar.addActionListener(e -> {
            if (cbMinhasPlaylists.getSelectedItem() == null) return;
            int idP = extrairId(cbMinhasPlaylists.getSelectedItem().toString());
            if (controller.compartilharPlaylist(idP)) {
                JOptionPane.showMessageDialog(panel, "Playlist agora é pública!");
            } else {
                JOptionPane.showMessageDialog(panel, "A playlist já é pública ou ocorreu um erro.");
            }
        });
        
        JButton btnDeletar = new JButton("Deletar Playlist");
        btnDeletar.addActionListener(e -> {
            if (cbMinhasPlaylists.getSelectedItem() == null) return;
            int idP = extrairId(cbMinhasPlaylists.getSelectedItem().toString());
            if (controller.deletarPlaylist(idP)) {
                atualizarListasPlaylists();
                JOptionPane.showMessageDialog(panel, "Playlist excluída!");
            }
        });

        // Adiciona na ordem do Grid 2x2
        pBotoes.add(btnAddMusica);
        pBotoes.add(btnRemoverMusica);
        pBotoes.add(btnCompartilhar);
        pBotoes.add(btnDeletar);

        // Montagem
        panel.add(pTopo);
        panel.add(Box.createVerticalStrut(15));
        panel.add(pForm);
        panel.add(Box.createVerticalStrut(15));
        panel.add(pBotoes);
        
        wrapper.add(panel, BorderLayout.NORTH); // Fixa no topo
        return wrapper;
    }

    private void atualizarListasPlaylists() {
        cbMinhasPlaylists.removeAllItems();
        for (Playlist p : controller.listarPlaylistsOuvinte()) {
            cbMinhasPlaylists.addItem(p.getId() + " - " + p.getTitulo());
        }
        
        cbTodasMusicas.removeAllItems();
        for (Musica m : controller.listarTodasMusicas()) {
            cbTodasMusicas.addItem(m.getId() + " - " + m.getTitulo() + " (" + m.getDuracaoMin() + "m)");
        }
        
        atualizarMusicasDaPlaylistSelecionada();
    }
    
    /**
     * Atualiza o combo que mostra as músicas pertencentes à playlist atualmente selecionada.
     */
    private void atualizarMusicasDaPlaylistSelecionada() {
        cbMusicasDaPlaylist.removeAllItems();
        
        // Se não houver nenhuma playlist selecionada (ou a lista estiver vazia), para por aqui
        if (cbMinhasPlaylists.getSelectedItem() == null) {
            return;
        }
        
        int idPlaylist = extrairId(cbMinhasPlaylists.getSelectedItem().toString());
        
        // Solicita as músicas ao controller e preenche o combo
        for (Musica m : controller.listarMusicasPlaylist(idPlaylist)) {
            cbMusicasDaPlaylist.addItem(m.getId() + " - " + m.getTitulo());
        }
    }

    // ================== PAINEL DE ÁLBUNS ==================
    private JPanel criarPainelAlbuns() {
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel pForm = new JPanel(new GridLayout(2, 2, 10, 10));
        pForm.setMaximumSize(new Dimension(Short.MAX_VALUE, 60)); 
        pForm.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        cbTodosAlbuns = new JComboBox<>();
        cbAlbunsFavoritos = new JComboBox<>();
        pForm.add(new JLabel("Todos os Álbuns Disponíveis:")); 
        pForm.add(cbTodosAlbuns);
        pForm.add(new JLabel("Meus Álbuns Favoritos:")); 
        pForm.add(cbAlbunsFavoritos);
        
        // Botões enfileirados à esquerda
        JPanel pBot1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBot1.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnFavoritar = new JButton("Favoritar Álbum");
        btnFavoritar.addActionListener(e -> {
            if (cbTodosAlbuns.getSelectedItem() == null) return;
            int id = extrairId(cbTodosAlbuns.getSelectedItem().toString());
            if (controller.favoritarAlbum(id)) {
                atualizarListasAlbuns();
                JOptionPane.showMessageDialog(panel, "Álbum adicionado aos favoritos!");
            } else {
                JOptionPane.showMessageDialog(panel, "Álbum já está nos favoritos.");
            }
        });
        pBot1.add(btnFavoritar);
        
        JPanel pBot2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBot2.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnDesfavoritar = new JButton("Desfavoritar Álbum");
        btnDesfavoritar.addActionListener(e -> {
            if (cbAlbunsFavoritos.getSelectedItem() == null) return;
            int id = extrairId(cbAlbunsFavoritos.getSelectedItem().toString());
            if (controller.desfavoritarAlbum(id)) {
                atualizarListasAlbuns();
                JOptionPane.showMessageDialog(panel, "Álbum removido dos favoritos!");
            }
        });
        pBot2.add(btnDesfavoritar);
        
        JPanel pBot3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBot3.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnGenero = new JButton("Calcular Gênero Fav.");
        btnGenero.addActionListener(e -> {
            controller.atualizarGeneroFavorito();
            JOptionPane.showMessageDialog(panel, "Gênero favorito atualizado com base no seu histórico!");
        });
        pBot3.add(btnGenero);

        panel.add(pForm);
        panel.add(Box.createVerticalStrut(15));
        panel.add(pBot1);
        panel.add(Box.createVerticalStrut(5)); // Espaçamento pequeno entre os botões
        panel.add(pBot2);
        panel.add(Box.createVerticalStrut(5));
        panel.add(pBot3);
        
        wrapper.add(panel, BorderLayout.NORTH); // Fixa no topo
        return wrapper;
    }

    private void atualizarListasAlbuns() {
        cbTodosAlbuns.removeAllItems();
        for (Album a : controller.listarTodosAlbuns()) {
            cbTodosAlbuns.addItem(a.getId() + " - " + a.getTitulo());
        }
        cbAlbunsFavoritos.removeAllItems();
        for (Album a : controller.listarAlbunsFavoritos()) {
            cbAlbunsFavoritos.addItem(a.getId() + " - " + a.getTitulo());
        }
    }

    // ================== PAINEL DE PODCASTS ==================
    private JPanel criarPainelPodcasts() {
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel pForm = new JPanel(new GridLayout(2, 2, 10, 10));
        pForm.setMaximumSize(new Dimension(Short.MAX_VALUE, 60)); 
        pForm.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        cbTodosPodcasts = new JComboBox<>();
        cbPodcastsFavoritos = new JComboBox<>();
        pForm.add(new JLabel("Todos os Podcasts:")); 
        pForm.add(cbTodosPodcasts);
        pForm.add(new JLabel("Meus Podcasts Favoritos:")); 
        pForm.add(cbPodcastsFavoritos);
        
        JPanel pBot1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
        pBot1.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnFavoritar = new JButton("Favoritar Podcast");
        btnFavoritar.addActionListener(e -> {
            if (cbTodosPodcasts.getSelectedItem() == null) return;
            int id = extrairId(cbTodosPodcasts.getSelectedItem().toString());
            if (controller.favoritarPodcast(id)) {
                atualizarListasPodcasts();
                JOptionPane.showMessageDialog(panel, "Podcast adicionado aos favoritos!");
            } else {
                JOptionPane.showMessageDialog(panel, "Podcast já está nos favoritos.");
            }
        });
        pBot1.add(btnFavoritar);
        
        JPanel pBot2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBot2.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnDesfavoritar = new JButton("Desfavoritar Podcast");
        btnDesfavoritar.addActionListener(e -> {
            if (cbPodcastsFavoritos.getSelectedItem() == null) return;
            int id = extrairId(cbPodcastsFavoritos.getSelectedItem().toString());
            if (controller.desfavoritarPodcast(id)) {
                atualizarListasPodcasts();
                JOptionPane.showMessageDialog(panel, "Podcast removido dos favoritos!");
            }
        });
        pBot2.add(btnDesfavoritar);

        panel.add(pForm);
        panel.add(Box.createVerticalStrut(15));
        panel.add(pBot1);
        panel.add(Box.createVerticalStrut(5));
        panel.add(pBot2);
        
        wrapper.add(panel, BorderLayout.NORTH); // Fixa no topo
        return wrapper;
    }

    private void atualizarListasPodcasts() {
        cbTodosPodcasts.removeAllItems();
        for (Podcast p : controller.listarTodosPodcasts()) {
            cbTodosPodcasts.addItem(p.getId() + " - " + p.getNome());
        }
        cbPodcastsFavoritos.removeAllItems();
        for (Podcast p : controller.listarPodcastsFavoritos()) {
            cbPodcastsFavoritos.addItem(p.getId() + " - " + p.getNome());
        }
    }

    // ================== PAINEL DE PERFIL ==================
    private JPanel criarPainelPerfil() {
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. Dados Pessoais
        JPanel pFormDados = new JPanel(new GridLayout(2, 2, 10, 10));
        pFormDados.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
        pFormDados.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField txtNome = new JTextField();
        JComboBox<String> cbSexo = new JComboBox<>(new String[]{"M", "F", "Outro", "Prefiro não informar"});
        pFormDados.add(new JLabel("Novo Nome:")); 
        pFormDados.add(txtNome);
        pFormDados.add(new JLabel("Novo Sexo:")); 
        pFormDados.add(cbSexo);

        JPanel pBotoesDados = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
        pBotoesDados.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnAttDados = new JButton("Atualizar Dados");
        btnAttDados.addActionListener(e -> {
            String novoNome = txtNome.getText().trim();
            String novoSexo = (String) cbSexo.getSelectedItem();
            
            if (novoNome.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "O campo de nome não pode estar vazio.");
                return;
            }

            if (controllerPerfil.atualizarDadosPessoais(novoNome, novoSexo)) {
                JOptionPane.showMessageDialog(panel, "Dados atualizados com sucesso!");
            } else {
                JOptionPane.showMessageDialog(panel, "Nenhuma alteração foi realizada.");
            }
        });
        pBotoesDados.add(btnAttDados);

        // 2. Trocar Senha
        JPanel pFormSenha = new JPanel(new GridLayout(2, 2, 10, 10));
        pFormSenha.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
        pFormSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPasswordField txtSenhaAntiga = new JPasswordField();
        JPasswordField txtSenhaNova = new JPasswordField();
        pFormSenha.add(new JLabel("Senha Antiga:")); 
        pFormSenha.add(txtSenhaAntiga);
        pFormSenha.add(new JLabel("Nova Senha:")); 
        pFormSenha.add(txtSenhaNova);

        JPanel pBotoesSenha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
        pBotoesSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnTrocarSenha = new JButton("Trocar Senha");
        btnTrocarSenha.addActionListener(e -> {
            String antiga = new String(txtSenhaAntiga.getPassword());
            String nova = new String(txtSenhaNova.getPassword());
            
            if (antiga.isEmpty() || nova.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Preencha ambas as senhas.");
                return;
            }

            if (controllerPerfil.trocarSenha(antiga, nova)) {
                JOptionPane.showMessageDialog(panel, "Senha alterada com sucesso!");
                txtSenhaAntiga.setText("");
                txtSenhaNova.setText("");
            } else {
                JOptionPane.showMessageDialog(panel, "Erro! Senha antiga incorreta ou nova senha é igual à atual.");
            }
        });
        pBotoesSenha.add(btnTrocarSenha);

        // 3. Mudar Plano
        JPanel pFormPlano = new JPanel(new GridLayout(1, 2, 10, 10));
        pFormPlano.setMaximumSize(new Dimension(Short.MAX_VALUE, 30)); 
        pFormPlano.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JComboBox<String> cbPlano = new JComboBox<>(new String[]{"Free", "Premium"});
        pFormPlano.add(new JLabel("Plano da Conta:")); 
        pFormPlano.add(cbPlano);

        JPanel pBotoesPlano = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
        pBotoesPlano.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnMudarPlano = new JButton("Atualizar Plano");
        btnMudarPlano.addActionListener(e -> {
            String novoPlano = (String) cbPlano.getSelectedItem();
            if (controllerPerfil.mudarPlano(novoPlano)) {
                JOptionPane.showMessageDialog(panel, "Plano atualizado para " + novoPlano + " com sucesso!");
            } else {
                JOptionPane.showMessageDialog(panel, "A sua conta já possui o plano " + novoPlano + ".");
            }
        });
        pBotoesPlano.add(btnMudarPlano);

        // Montagem
        panel.add(pFormDados);
        panel.add(Box.createVerticalStrut(10));
        panel.add(pBotoesDados);
        panel.add(Box.createVerticalStrut(15));
        panel.add(new JSeparator());
        panel.add(Box.createVerticalStrut(15));

        panel.add(pFormSenha);
        panel.add(Box.createVerticalStrut(10));
        panel.add(pBotoesSenha);
        panel.add(Box.createVerticalStrut(15));
        panel.add(new JSeparator());
        panel.add(Box.createVerticalStrut(15));

        panel.add(pFormPlano);
        panel.add(Box.createVerticalStrut(10));
        panel.add(pBotoesPlano);

        wrapper.add(panel, BorderLayout.NORTH); // Fixa no topo
        return wrapper;
    }
}