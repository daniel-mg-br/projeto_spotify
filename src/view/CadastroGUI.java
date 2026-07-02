package view;

import controller.ControllerAutenticador; 

import javax.swing.*;
import java.awt.*;

/**
 * Classe responsável pela interface gráfica de cadastro de usuários.
 */
public class CadastroGUI extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JTextField txtNome;
    
    // Transformados em JComboBox para evitar erros de digitação e regras do banco
    private JComboBox<String> cbSexo;
    private JComboBox<String> cbTipoUsuario;
    
    private JTextField txtAno;
    private JTextField txtMes;
    private JTextField txtDia;
    
    private ControllerAutenticador controller;

    public CadastroGUI() {
        this.controller = new ControllerAutenticador();

        setTitle("Cadastro de Usuário");
        setSize(400, 400); // Aumentei um pouco para caber os novos componentes
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Painel do Formulário agora com 8 linhas
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        
        formPanel.add(new JLabel("Login:"));
        txtLogin = new JTextField();
        formPanel.add(txtLogin);

        formPanel.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        formPanel.add(txtSenha);

        formPanel.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        formPanel.add(txtNome);

        // JComboBox para o Sexo (Garante que bate com a restrição do BD)
        formPanel.add(new JLabel("Sexo:"));
        String[] opcoesSexo = {"M", "F", "Outro", "Prefiro não informar"};
        cbSexo = new JComboBox<>(opcoesSexo);
        formPanel.add(cbSexo);

        formPanel.add(new JLabel("Data Nasc (YYYY MM DD):"));
        JPanel dataPanel = new JPanel(new GridLayout(1, 3));
        txtAno = new JTextField();
        txtMes = new JTextField();
        txtDia = new JTextField();
        dataPanel.add(txtAno);
        dataPanel.add(txtMes);
        dataPanel.add(txtDia);
        formPanel.add(dataPanel);

        // JComboBox para escolher o tipo de conta
        formPanel.add(new JLabel("Tipo de Conta:"));
        String[] tipos = {"Ouvinte", "Criador de Conteúdo"};
        cbTipoUsuario = new JComboBox<>(tipos);
        formPanel.add(cbTipoUsuario);

        // Apenas um botão de cadastro agora
        JButton btnCadastrar = new JButton("Cadastrar");
        formPanel.add(new JLabel("")); // Célula vazia para alinhar o botão à direita
        formPanel.add(btnCadastrar);

        // Botão Voltar (Logout)
        JButton btnVoltar = new JButton("Voltar ao Login");
        btnVoltar.addActionListener(e -> {
            dispose();       
            new LoginGUI();  
        });

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(btnVoltar, BorderLayout.SOUTH);

        // Único evento de cadastro
        btnCadastrar.addActionListener(e -> realizarCadastro());

        add(panel);
        setVisible(true);
    }

    /**
     * Método unificado para processar o cadastro, ler os JComboBox e chamar o Controller correto.
     */
    private void realizarCadastro() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();
        String nome = txtNome.getText().trim();
        String anoStr = txtAno.getText().trim();
        String mesStr = txtMes.getText().trim();
        String diaStr = txtDia.getText().trim();

        // 1. Validação de campos vazios
        if (login.isEmpty() || senha.isEmpty() || nome.isEmpty() || anoStr.isEmpty() || mesStr.isEmpty() || diaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int ano = Integer.parseInt(anoStr);
            int mes = Integer.parseInt(mesStr);
            int dia = Integer.parseInt(diaStr);
            
            // Pegando os valores selecionados nos menus suspensos
            String sexoSelecionado = (String) cbSexo.getSelectedItem();
            String tipoUsuario = (String) cbTipoUsuario.getSelectedItem();

            boolean ok = false;

            // 2. Decide qual método do Controller chamar com base na escolha
            if (tipoUsuario.equals("Ouvinte")) {
                ok = controller.cadastrarOuvinte(login, senha, nome, sexoSelecionado, java.time.LocalDate.of(ano, mes, dia));
            } else {
                ok = controller.cadastrarCriador(login, senha, nome, sexoSelecionado, java.time.LocalDate.of(ano, mes, dia));
            }

            // 3. Feedback final
            if (ok) {
                JOptionPane.showMessageDialog(this, tipoUsuario + " cadastrado com sucesso!");
                dispose();
                new LoginGUI();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar. O login já pode estar em uso.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dados inválidos: Verifique se a data contém apenas números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}