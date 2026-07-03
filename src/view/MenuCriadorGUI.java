package view;

import controller.ControllerCriador;
import controller.ControllerPerfil;
import model.content.Album;
import model.content.Episodio;
import model.content.Musica;
import model.content.Podcast;

import javax.swing.*;
import java.awt.*;

/**
 * Interface gráfica para o menu do Criador.
 * Organizada em Abas (JTabbedPane), modularizada e com componentes ancorados
 * ao topo para evitar distorções de tamanho na interface.
 */
public class MenuCriadorGUI extends JFrame {

    private ControllerCriador controller;
    private ControllerPerfil controllerPerfil;

    // Componentes visuais atualizados dinamicamente
    private JComboBox<String> cbMeusAlbuns;
    private JComboBox<String> cbMinhasMusicas;
    private JComboBox<String> cbMeusPodcasts;
    private JComboBox<String> cbMeusEpisodios;
    
    /**
     * Método construtor do menu do criador de conteúdo.
     */
    public MenuCriadorGUI() {
        this.controller = new ControllerCriador();
        this.controllerPerfil = new ControllerPerfil();

        setTitle("Painel do Criador de Conteúdo");
        setSize(550, 680); // Altura aumentada para acomodar o novo campo de plano
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Meus Álbuns", criarPainelAlbuns());
        tabbedPane.addTab("Meus Podcasts", criarPainelPodcasts());
        tabbedPane.addTab("Meu Perfil", criarPainelPerfil());

        add(tabbedPane, BorderLayout.CENTER);

        // Rodapé com o botão de Logout.
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelRodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        JButton btnLogout = new JButton("Sair (Logout)");
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginGUI();
        });
        painelRodape.add(btnLogout);
        add(painelRodape, BorderLayout.SOUTH);

        // Carrega os dados iniciais do banco para as caixas de seleção.
        atualizarListasAlbuns();
        atualizarListasPodcasts();

        setVisible(true);
    }

    /**
     * Extrai apenas o número do ID de uma string formatada como "14 - Nome".
     */
    private int extrairId(String textoCombo) {
        if (textoCombo == null) return -1;
        try {
            return Integer.parseInt(textoCombo.split(" - ")[0]);
        } catch (Exception e) {
            return -1;
        }
    }

    // Painel para gestão de álbuns e músicas.
    private JPanel criarPainelAlbuns() {
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel pForm = new JPanel(new GridLayout(2, 2, 10, 10));
        pForm.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
        pForm.setAlignmentX(Component.LEFT_ALIGNMENT);

        cbMeusAlbuns = new JComboBox<>();
        cbMinhasMusicas = new JComboBox<>();
        pForm.add(new JLabel("Selecione o Álbum:"));
        pForm.add(cbMeusAlbuns);
        pForm.add(new JLabel("Músicas deste Álbum:"));
        pForm.add(cbMinhasMusicas);

        cbMeusAlbuns.addActionListener(e -> atualizarListasMusicas());

        JPanel pBotoesAlbum = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotoesAlbum.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnCriarAlbum = new JButton("Criar Novo Álbum");
        btnCriarAlbum.addActionListener(e -> {
            String titulo = JOptionPane.showInputDialog(panel, "Título do Álbum:");
            if (titulo != null && !titulo.trim().isEmpty()) {
                String tipo = JOptionPane.showInputDialog(panel, "Tipo (ex: Studio, Single, Live):");
                controller.criarAlbum(titulo, tipo != null ? tipo : "Studio");
                atualizarListasAlbuns();
                JOptionPane.showMessageDialog(panel, "Álbum criado com sucesso!");
            }
        });
        
        JButton btnLancar = new JButton("Lançar Álbum");
        btnLancar.addActionListener(e -> {
            if (cbMeusAlbuns.getSelectedItem() == null) return;
            int idA = extrairId(cbMeusAlbuns.getSelectedItem().toString());
            if (controller.lancarAlbum(idA)) {
                JOptionPane.showMessageDialog(panel, "Álbum publicado e disponível para os ouvintes!");
            }
        });

        JButton btnDeletarAlbum = new JButton("Deletar Álbum");
        btnDeletarAlbum.addActionListener(e -> {
            if (cbMeusAlbuns.getSelectedItem() == null) return;
            int idA = extrairId(cbMeusAlbuns.getSelectedItem().toString());
            int conf = JOptionPane.showConfirmDialog(panel, "Excluir este álbum e todas as suas faixas?", "Aviso", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                controller.deletarAlbum(idA);
                atualizarListasAlbuns();
            }
        });
        pBotoesAlbum.add(btnCriarAlbum); pBotoesAlbum.add(Box.createHorizontalStrut(10));
        pBotoesAlbum.add(btnLancar); pBotoesAlbum.add(Box.createHorizontalStrut(10));
        pBotoesAlbum.add(btnDeletarAlbum);

        JPanel pBotoesMusica = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotoesMusica.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnAddMusica = new JButton("Adicionar Música");
        btnAddMusica.addActionListener(e -> {
            if (cbMeusAlbuns.getSelectedItem() == null) return;
            int idA = extrairId(cbMeusAlbuns.getSelectedItem().toString());

            String titulo = JOptionPane.showInputDialog(panel, "Título da Música:");
            if (titulo == null || titulo.trim().isEmpty()) return;

            try {
                int duracao = Integer.parseInt(JOptionPane.showInputDialog(panel, "Duração em minutos:"));
                String genero = JOptionPane.showInputDialog(panel, "Gênero:");
                String letra = JOptionPane.showInputDialog(panel, "Letra da Música (Opcional):");

                if (controller.adicionarMusicaAlbum(idA, titulo, duracao, genero, letra != null ? letra : "")) {
                    atualizarListasMusicas();
                    JOptionPane.showMessageDialog(panel, "Música adicionada ao álbum!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Duração inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnRemoverMusica = new JButton("Remover Música");
        btnRemoverMusica.addActionListener(e -> {
            if (cbMeusAlbuns.getSelectedItem() == null || cbMinhasMusicas.getSelectedItem() == null) return;
            int idA = extrairId(cbMeusAlbuns.getSelectedItem().toString());
            int idM = extrairId(cbMinhasMusicas.getSelectedItem().toString());
            if (controller.removerMusicaAlbum(idA, idM)) {
                atualizarListasMusicas();
                JOptionPane.showMessageDialog(panel, "Música removida e excluída!");
            }
        });
        pBotoesMusica.add(btnAddMusica); pBotoesMusica.add(Box.createHorizontalStrut(10));
        pBotoesMusica.add(btnRemoverMusica);

        JPanel pBotoesEquipe = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotoesEquipe.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnAddEquipe = new JButton("+ Membro Equipe");
        btnAddEquipe.addActionListener(e -> {
            if (cbMinhasMusicas.getSelectedItem() == null) return;
            int idM = extrairId(cbMinhasMusicas.getSelectedItem().toString());
            String nome = JOptionPane.showInputDialog(panel, "Nome do Integrante da Equipe (Ex: Produtor, Guitarrista):");
            if (nome != null && !nome.trim().isEmpty()) {
                controller.adicionarMembroMusica(idM, nome);
            }
        });

        JButton btnRemoverEquipe = new JButton("- Membro Equipe");
        btnRemoverEquipe.addActionListener(e -> {
            if (cbMinhasMusicas.getSelectedItem() == null) return;
            int idM = extrairId(cbMinhasMusicas.getSelectedItem().toString());
            String nome = JOptionPane.showInputDialog(panel, "Nome exato do membro para remover:");
            if (nome != null) controller.removerMembroMusica(idM, nome);
        });
        pBotoesEquipe.add(btnAddEquipe); pBotoesEquipe.add(Box.createHorizontalStrut(10));
        pBotoesEquipe.add(btnRemoverEquipe);

        panel.add(pForm); panel.add(Box.createVerticalStrut(15));
        panel.add(pBotoesAlbum); panel.add(Box.createVerticalStrut(15));
        panel.add(pBotoesMusica); panel.add(Box.createVerticalStrut(15));
        panel.add(pBotoesEquipe);

        wrapper.add(panel, BorderLayout.NORTH);
        return wrapper;
    }

    private void atualizarListasAlbuns() {
        cbMeusAlbuns.removeAllItems();
        for (Album a : controller.listarAlbunsCriador()) {
            cbMeusAlbuns.addItem(a.getId() + " - " + a.getTitulo());
        }
        atualizarListasMusicas();
    }

    private void atualizarListasMusicas() {
        cbMinhasMusicas.removeAllItems();
        if (cbMeusAlbuns.getSelectedItem() == null) return;
        int idAlbum = extrairId(cbMeusAlbuns.getSelectedItem().toString());
        for (Musica m : controller.listarMusicasAlbum(idAlbum)) {
            cbMinhasMusicas.addItem(m.getId() + " - " + m.getTitulo());
        }
    }

    // Painel para gestão de podcasts e episódios.
    private JPanel criarPainelPodcasts() {
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel pForm = new JPanel(new GridLayout(2, 2, 10, 10));
        pForm.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
        pForm.setAlignmentX(Component.LEFT_ALIGNMENT);

        cbMeusPodcasts = new JComboBox<>();
        cbMeusEpisodios = new JComboBox<>();
        pForm.add(new JLabel("Selecione o Podcast:"));
        pForm.add(cbMeusPodcasts);
        pForm.add(new JLabel("Episódios deste Podcast:"));
        pForm.add(cbMeusEpisodios);
        
        cbMeusPodcasts.addActionListener(e -> atualizarListasEpisodios());

        JPanel pBotoesPod = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotoesPod.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnCriarPod = new JButton("Criar Novo Podcast");
        btnCriarPod.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(panel, "Nome do Podcast:");
            if (nome != null && !nome.trim().isEmpty()) {
                String tema = JOptionPane.showInputDialog(panel, "Tema/Gênero do Show:");
                controller.criarPodcast(nome, tema != null ? tema : "Geral");
                atualizarListasPodcasts();
            }
        });

        JButton btnDeletarPod = new JButton("Deletar Podcast");
        btnDeletarPod.addActionListener(e -> {
            if (cbMeusPodcasts.getSelectedItem() == null) return;
            int idP = extrairId(cbMeusPodcasts.getSelectedItem().toString());
            int conf = JOptionPane.showConfirmDialog(panel, "Deletar este show e todos os seus episódios?", "Aviso", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                controller.deletarPodcast(idP);
                atualizarListasPodcasts();
            }
        });
        pBotoesPod.add(btnCriarPod); pBotoesPod.add(Box.createHorizontalStrut(10));
        pBotoesPod.add(btnDeletarPod);

        JPanel pBotoesEp = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotoesEp.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnAddEp = new JButton("Adicionar Episódio");
        btnAddEp.addActionListener(e -> {
            if (cbMeusPodcasts.getSelectedItem() == null) return;
            int idP = extrairId(cbMeusPodcasts.getSelectedItem().toString());

            String titulo = JOptionPane.showInputDialog(panel, "Título do Episódio:");
            if (titulo == null || titulo.trim().isEmpty()) return;

            try {
                int duracao = Integer.parseInt(JOptionPane.showInputDialog(panel, "Duração (min):"));
                int numEp = Integer.parseInt(JOptionPane.showInputDialog(panel, "Número do Episódio:"));

                if (controller.adicionarEpPodcast(idP, titulo, duracao, numEp)) {
                    atualizarListasEpisodios();
                    JOptionPane.showMessageDialog(panel, "Episódio inserido com sucesso!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Dados numéricos inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnRemoverEp = new JButton("Remover Episódio");
        btnRemoverEp.addActionListener(e -> {
            if (cbMeusPodcasts.getSelectedItem() == null || cbMeusEpisodios.getSelectedItem() == null) return;
            int idP = extrairId(cbMeusPodcasts.getSelectedItem().toString());
            int idE = extrairId(cbMeusEpisodios.getSelectedItem().toString());
            if (controller.removerEpPodcast(idP, idE)) {
                atualizarListasEpisodios();
                JOptionPane.showMessageDialog(panel, "Episódio removido!");
            }
        });
        pBotoesEp.add(btnAddEp); pBotoesEp.add(Box.createHorizontalStrut(10));
        pBotoesEp.add(btnRemoverEp);

        JPanel pBotoesConv = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotoesConv.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnAddConv = new JButton("+ Adicionar Convidado");
        btnAddConv.addActionListener(e -> {
            if (cbMeusEpisodios.getSelectedItem() == null) return;
            int idE = extrairId(cbMeusEpisodios.getSelectedItem().toString());
            String nome = JOptionPane.showInputDialog(panel, "Nome do Convidado:");
            if (nome != null && !nome.trim().isEmpty()) {
                controller.adicionarConvidadoEpisodio(idE, nome);
            }
        });

        JButton btnRemoverConv = new JButton("- Remover Convidado");
        btnRemoverConv.addActionListener(e -> {
            if (cbMeusEpisodios.getSelectedItem() == null) return;
            int idE = extrairId(cbMeusEpisodios.getSelectedItem().toString());
            String nome = JOptionPane.showInputDialog(panel, "Nome exato do convidado para remover:");
            if (nome != null) controller.removerConvidadoEpisodio(idE, nome);
        });
        pBotoesConv.add(btnAddConv); pBotoesConv.add(Box.createHorizontalStrut(10));
        pBotoesConv.add(btnRemoverConv);

        panel.add(pForm); panel.add(Box.createVerticalStrut(15));
        panel.add(pBotoesPod); panel.add(Box.createVerticalStrut(15));
        panel.add(pBotoesEp); panel.add(Box.createVerticalStrut(15));
        panel.add(pBotoesConv);

        wrapper.add(panel, BorderLayout.NORTH);
        return wrapper;
    }
    
    private void atualizarListasPodcasts() {
        cbMeusPodcasts.removeAllItems();
        for (Podcast p : controller.listarPodcastCriador()) {
            cbMeusPodcasts.addItem(p.getId() + " - " + p.getNome());
        }
        atualizarListasEpisodios();
    }
    
    private void atualizarListasEpisodios() {
        cbMeusEpisodios.removeAllItems();
        if (cbMeusPodcasts.getSelectedItem() == null) return;
        int idPodcast = extrairId(cbMeusPodcasts.getSelectedItem().toString());
        for (Episodio ep : controller.listarEpisodiosPodcast(idPodcast)) {
            cbMeusEpisodios.addItem(ep.getId() + " - " + ep.getTitulo() + " (Ep. " + ep.getNumEpisodio() + ")");
        }
    }

    // Painel para gestão dos dados do perfil.
    private JPanel criarPainelPerfil() {
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Dados Pessoais Básicos.
        JPanel pFormDados = new JPanel(new GridLayout(2, 2, 10, 10));
        pFormDados.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
        pFormDados.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField txtNome = new JTextField();
        JComboBox<String> cbSexo = new JComboBox<>(new String[]{"M", "F", "Outro", "Prefiro não informar"});
        pFormDados.add(new JLabel("Nome de Perfil:")); pFormDados.add(txtNome);
        pFormDados.add(new JLabel("Sexo:")); pFormDados.add(cbSexo);

        JPanel pBotDados = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotDados.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnSalvarDados = new JButton("Salvar Dados Básicos");
        btnSalvarDados.addActionListener(e -> {
            if (!txtNome.getText().trim().isEmpty()) {
                controllerPerfil.atualizarDadosPessoais(txtNome.getText().trim(), (String) cbSexo.getSelectedItem());
                JOptionPane.showMessageDialog(panel, "Dados básicos atualizados!");
            }
        });
        pBotDados.add(btnSalvarDados);

        // Perfil Artístico.
        JPanel pFormArtista = new JPanel(new GridLayout(2, 2, 10, 10));
        pFormArtista.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
        pFormArtista.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField txtNomeArtistico = new JTextField();
        JTextField txtBiografia = new JTextField(); 
        pFormArtista.add(new JLabel("Nome Artístico:")); pFormArtista.add(txtNomeArtistico);
        pFormArtista.add(new JLabel("Biografia:")); pFormArtista.add(txtBiografia);

        JPanel pBotArtista = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotArtista.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnSalvarArtista = new JButton("Salvar Perfil Artístico");
        btnSalvarArtista.addActionListener(e -> {
            String art = txtNomeArtistico.getText().trim();
            String bio = txtBiografia.getText().trim();
            if (controllerPerfil.atualizarPerfilCriador(art, bio)) {
                JOptionPane.showMessageDialog(panel, "Perfil artístico publicado com sucesso!");
            }
        });
        pBotArtista.add(btnSalvarArtista);

        // Segurança (Trocar Senha).
        JPanel pFormSenha = new JPanel(new GridLayout(2, 2, 10, 10));
        pFormSenha.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
        pFormSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPasswordField txtAntiga = new JPasswordField();
        JPasswordField txtNova = new JPasswordField();
        pFormSenha.add(new JLabel("Senha Antiga:")); pFormSenha.add(txtAntiga);
        pFormSenha.add(new JLabel("Nova Senha:")); pFormSenha.add(txtNova);

        JPanel pBotSenha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnSenha = new JButton("Alterar Senha");
        btnSenha.addActionListener(e -> {
            if (controllerPerfil.trocarSenha(new String(txtAntiga.getPassword()), new String(txtNova.getPassword()))) {
                JOptionPane.showMessageDialog(panel, "Senha modificada de forma segura!");
                txtAntiga.setText(""); txtNova.setText("");
            }
        });
        pBotSenha.add(btnSenha);

        // Mudar Plano.
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
                JOptionPane.showMessageDialog(panel, "A sua conta já possui o plano " + novoPlano + " ou ocorreu um erro.");
            }
        });
        pBotoesPlano.add(btnMudarPlano);

        // Montagem final do painel com separadores.
        panel.add(pFormDados); panel.add(Box.createVerticalStrut(10));
        panel.add(pBotDados); panel.add(Box.createVerticalStrut(15));
        panel.add(new JSeparator()); panel.add(Box.createVerticalStrut(15));

        panel.add(pFormArtista); panel.add(Box.createVerticalStrut(10));
        panel.add(pBotArtista); panel.add(Box.createVerticalStrut(15));
        panel.add(new JSeparator()); panel.add(Box.createVerticalStrut(15));

        panel.add(pFormSenha); panel.add(Box.createVerticalStrut(10));
        panel.add(pBotSenha); panel.add(Box.createVerticalStrut(15));
        panel.add(new JSeparator()); panel.add(Box.createVerticalStrut(15));

        panel.add(pFormPlano); panel.add(Box.createVerticalStrut(10));
        panel.add(pBotoesPlano);

        wrapper.add(panel, BorderLayout.NORTH);
        return wrapper;
    }
}