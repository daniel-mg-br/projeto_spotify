package view;

import controller.ControllerAdmin;
import controller.ControllerPerfil;
import model.content.Episodio;
import model.content.Musica;
import model.actors.Criador;
import model.actors.Ouvinte;

import javax.swing.*;
import java.awt.*;

/**
 * Interface gráfica para o menu do Administrador.
 * Organizada em abas, modularizada e com layout travado no topo para uma
 * interface profissional e alinhada.
 */
public class MenuAdminGUI extends JFrame {

    private ControllerAdmin controller;
    private ControllerPerfil controllerPerfil;

    // Componentes visuais dinâmicos para as listagens
    private JComboBox<String> cbOuvintes;
    private JComboBox<String> cbCriadores;
    private JComboBox<String> cbMusicas;
    private JComboBox<String> cbEpisodios;

    public MenuAdminGUI() {
        this.controller = new ControllerAdmin();
        this.controllerPerfil = new ControllerPerfil();

        setTitle("Painel do Administrador (Moderação)");
        setSize(550, 500); // Tamanho padronizado para as abas
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        // Sistema de Abas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Moderação de Usuários", criarPainelUsuarios());
        tabbedPane.addTab("Moderação de Conteúdos", criarPainelConteudos());
        tabbedPane.addTab("Meu Perfil", criarPainelPerfil());

        add(tabbedPane, BorderLayout.CENTER);

        // Rodapé com o botão de Logout seguro
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

        // Carrega os dados nas caixinhas assim que a tela abre
        atualizarListasUsuarios();
        atualizarListasConteudos();

        setVisible(true);
    }

    /**
     * Extrai apenas o número do ID de uma string formatada como "14 - Nome"
     */
    private int extrairId(String textoCombo) {
        if (textoCombo == null) return -1;
        try {
            return Integer.parseInt(textoCombo.split(" - ")[0]);
        } catch (Exception e) {
            return -1;
        }
    }

    // ================== PAINEL DE USUÁRIOS ==================
    private JPanel criarPainelUsuarios() {
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Formulário de Seleção (Grid 2x2)
        JPanel pForm = new JPanel(new GridLayout(2, 2, 10, 10));
        pForm.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
        pForm.setAlignmentX(Component.LEFT_ALIGNMENT);

        cbOuvintes = new JComboBox<>();
        cbCriadores = new JComboBox<>();
        pForm.add(new JLabel("Selecione um Ouvinte:"));
        pForm.add(cbOuvintes);
        pForm.add(new JLabel("Selecione um Criador:"));
        pForm.add(cbCriadores);

        // Botões de Ação
        JPanel pBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotoes.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnSuspenderOuvinte = new JButton("Suspender Ouvinte");
        btnSuspenderOuvinte.addActionListener(e -> suspenderUsuarioSelecionado(cbOuvintes));

        JButton btnSuspenderCriador = new JButton("Suspender Criador");
        btnSuspenderCriador.addActionListener(e -> suspenderUsuarioSelecionado(cbCriadores));

        pBotoes.add(btnSuspenderOuvinte);
        pBotoes.add(Box.createHorizontalStrut(10));
        pBotoes.add(btnSuspenderCriador);

        // Montagem
        panel.add(pForm);
        panel.add(Box.createVerticalStrut(15));
        panel.add(pBotoes);

        wrapper.add(panel, BorderLayout.NORTH);
        return wrapper;
    }

    /**
     * Método auxiliar para centralizar a lógica de suspensão independente de qual combo for usado.
     */
    private void suspenderUsuarioSelecionado(JComboBox<String> combo) {
        if (combo.getSelectedItem() == null) return;
        int id = extrairId(combo.getSelectedItem().toString());
        
        int conf = JOptionPane.showConfirmDialog(this, "Atenção: Tem certeza que deseja SUSPENDER este usuário?", "Aviso", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (conf == JOptionPane.YES_OPTION) {
            if (controller.suspenderUsuario(id)) {
                JOptionPane.showMessageDialog(this, "Conta suspensa com sucesso!");
                // Não precisa atualizar a lista visualmente, pois a conta continua existindo, apenas o status muda para "Suspenso"
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao tentar suspender usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void atualizarListasUsuarios() {
        cbOuvintes.removeAllItems();
        for (Ouvinte o : controller.listarTodosOuvintes()) {
            cbOuvintes.addItem(o.getId() + " - " + o.getNome());
        }

        cbCriadores.removeAllItems();
        for (Criador c : controller.listarTodosCriadores()) {
            cbCriadores.addItem(c.getId() + " - " + c.getNomeArtistico() + " (" + c.getNome() + ")");
        }
    }

    // ================== PAINEL DE CONTEÚDOS ==================
    private JPanel criarPainelConteudos() {
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Formulário de Seleção (Grid 2x2)
        JPanel pForm = new JPanel(new GridLayout(2, 2, 10, 10));
        pForm.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
        pForm.setAlignmentX(Component.LEFT_ALIGNMENT);

        cbMusicas = new JComboBox<>();
        cbEpisodios = new JComboBox<>();
        pForm.add(new JLabel("Músicas da Plataforma:"));
        pForm.add(cbMusicas);
        pForm.add(new JLabel("Episódios de Podcast:"));
        pForm.add(cbEpisodios);

        // Botões de Ação
        JPanel pBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotoes.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnRemoverMusica = new JButton("Deletar Música");
        btnRemoverMusica.addActionListener(e -> deletarConteudoSelecionado(cbMusicas));

        JButton btnRemoverEpisodio = new JButton("Deletar Episódio");
        btnRemoverEpisodio.addActionListener(e -> deletarConteudoSelecionado(cbEpisodios));

        pBotoes.add(btnRemoverMusica);
        pBotoes.add(Box.createHorizontalStrut(10));
        pBotoes.add(btnRemoverEpisodio);

        // Montagem
        panel.add(pForm);
        panel.add(Box.createVerticalStrut(15));
        panel.add(pBotoes);

        wrapper.add(panel, BorderLayout.NORTH);
        return wrapper;
    }

    /**
     * Método auxiliar para centralizar a lógica de exclusão permanente de conteúdo.
     */
    private void deletarConteudoSelecionado(JComboBox<String> combo) {
        if (combo.getSelectedItem() == null) return;
        int id = extrairId(combo.getSelectedItem().toString());
        
        int conf = JOptionPane.showConfirmDialog(this, "Atenção: A remoção de conteúdo é PERMANENTE. Deseja continuar?", "Aviso Crítico", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        if (conf == JOptionPane.YES_OPTION) {
            if (controller.removerConteudo(id)) {
                JOptionPane.showMessageDialog(this, "Conteúdo excluído com sucesso da base de dados!");
                atualizarListasConteudos(); // Recarrega as listas
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao remover conteúdo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void atualizarListasConteudos() {
        cbMusicas.removeAllItems();
        for (Musica m : controller.listarTodasMusicas()) {
            cbMusicas.addItem(m.getId() + " - " + m.getTitulo());
        }

        cbEpisodios.removeAllItems();
        for (Episodio ep : controller.listarTodosEpisodios()) {
            cbEpisodios.addItem(ep.getId() + " - " + ep.getTitulo() + " (Ep. " + ep.getNumEpisodio() + ")");
        }
    }

    // ================== PAINEL DE PERFIL (ADMIN) ==================
    private JPanel criarPainelPerfil() {
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Dados Básicos
        JPanel pFormDados = new JPanel(new GridLayout(2, 2, 10, 10));
        pFormDados.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
        pFormDados.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField txtNome = new JTextField();
        JComboBox<String> cbSexo = new JComboBox<>(new String[]{"M", "F", "Outro", "Prefiro não informar"});
        pFormDados.add(new JLabel("Novo Nome:")); pFormDados.add(txtNome);
        pFormDados.add(new JLabel("Novo Sexo:")); pFormDados.add(cbSexo);

        JPanel pBotDados = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotDados.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnAttDados = new JButton("Atualizar Dados Pessoais");
        btnAttDados.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            if (!nome.isEmpty() && controllerPerfil.atualizarDadosPessoais(nome, (String) cbSexo.getSelectedItem())) {
                JOptionPane.showMessageDialog(panel, "Dados de administrador atualizados!");
            }
        });
        pBotDados.add(btnAttDados);

        // 2. Segurança (Trocar Senha)
        JPanel pFormSenha = new JPanel(new GridLayout(2, 2, 10, 10));
        pFormSenha.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
        pFormSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPasswordField txtSenhaAntiga = new JPasswordField();
        JPasswordField txtSenhaNova = new JPasswordField();
        pFormSenha.add(new JLabel("Senha Antiga:")); pFormSenha.add(txtSenhaAntiga);
        pFormSenha.add(new JLabel("Nova Senha:")); pFormSenha.add(txtSenhaNova);

        JPanel pBotSenha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnTrocarSenha = new JButton("Alterar Senha");
        btnTrocarSenha.addActionListener(e -> {
            if (controllerPerfil.trocarSenha(new String(txtSenhaAntiga.getPassword()), new String(txtSenhaNova.getPassword()))) {
                JOptionPane.showMessageDialog(panel, "Senha do sistema atualizada com segurança.");
                txtSenhaAntiga.setText(""); txtSenhaNova.setText("");
            } else {
                JOptionPane.showMessageDialog(panel, "Erro ao alterar a senha.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        pBotSenha.add(btnTrocarSenha);

        // 3. Credencial Master
        JPanel pFormCred = new JPanel(new GridLayout(1, 2, 10, 10));
        pFormCred.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        pFormCred.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField txtCredencial = new JTextField();
        pFormCred.add(new JLabel("Nova Credencial Master:")); pFormCred.add(txtCredencial);

        JPanel pBotCred = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotCred.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnAttCred = new JButton("Atualizar Credencial");
        btnAttCred.addActionListener(e -> {
            String cred = txtCredencial.getText().trim();
            if (!cred.isEmpty() && controllerPerfil.atualizarCredencialAdmin(cred)) {
                JOptionPane.showMessageDialog(panel, "Atenção: Credencial de acesso alterada!");
                txtCredencial.setText("");
            }
        });
        pBotCred.add(btnAttCred);

        // Montagem do painel com separadores
        panel.add(pFormDados); panel.add(Box.createVerticalStrut(10));
        panel.add(pBotDados); panel.add(Box.createVerticalStrut(15));
        panel.add(new JSeparator()); panel.add(Box.createVerticalStrut(15));

        panel.add(pFormSenha); panel.add(Box.createVerticalStrut(10));
        panel.add(pBotSenha); panel.add(Box.createVerticalStrut(15));
        panel.add(new JSeparator()); panel.add(Box.createVerticalStrut(15));

        panel.add(pFormCred); panel.add(Box.createVerticalStrut(10));
        panel.add(pBotCred);

        wrapper.add(panel, BorderLayout.NORTH);
        return wrapper;
    }
}