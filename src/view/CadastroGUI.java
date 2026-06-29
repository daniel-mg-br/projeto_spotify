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
    private JTextField txtSexo;
    private JTextField txtAno;
    private JTextField txtMes;
    private JTextField txtDia;
    private ControllerAutenticador controller;

    public CadastroGUI() {
        this.controller = new ControllerAutenticador();

        setTitle("Cadastro de Usuário");
        setSize(400, 350); // Aumentei um pouco o tamanho para acomodar o botão de voltar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel Principal
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Painel do Formulário
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        
        formPanel.add(new JLabel("Login:"));
        txtLogin = new JTextField();
        formPanel.add(txtLogin);

        formPanel.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        formPanel.add(txtSenha);

        formPanel.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        formPanel.add(txtNome);

        formPanel.add(new JLabel("Sexo:"));
        txtSexo = new JTextField();
        formPanel.add(txtSexo);

        formPanel.add(new JLabel("Data Nasc (YYYY MM DD):"));
        JPanel dataPanel = new JPanel(new GridLayout(1, 3));
        txtAno = new JTextField();
        txtMes = new JTextField();
        txtDia = new JTextField();
        dataPanel.add(txtAno);
        dataPanel.add(txtMes);
        dataPanel.add(txtDia);
        formPanel.add(dataPanel);

        JButton btnCriarOuvinte = new JButton("Criar Ouvinte");
        JButton btnCriarCriador = new JButton("Criar Criador");
        formPanel.add(btnCriarOuvinte);
        formPanel.add(btnCriarCriador);

        // --- Nova Funcionalidade: Botão Voltar (Logout) ---
        JButton btnVoltar = new JButton("Voltar ao Login");
        btnVoltar.addActionListener(e -> {
            dispose();       // Fecha a janela de cadastro
            new LoginGUI();  // Retorna à tela de login
        });

        // Adicionando tudo ao painel principal
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(btnVoltar, BorderLayout.SOUTH);

        btnCriarOuvinte.addActionListener(e -> cadastrarOuvinte());
        btnCriarCriador.addActionListener(e -> cadastrarCriador());

        add(panel);
        setVisible(true);
    }

    private void cadastrarOuvinte() {
        try {
            int ano = Integer.parseInt(txtAno.getText());
            int mes = Integer.parseInt(txtMes.getText());
            int dia = Integer.parseInt(txtDia.getText());

            boolean ok = controller.cadastrarOuvinte(
                    txtLogin.getText(), new String(txtSenha.getPassword()), 
                    txtNome.getText(), txtSexo.getText(),
                    java.time.LocalDate.of(ano, mes, dia)
            );

            if (ok) {
                JOptionPane.showMessageDialog(this, "Ouvinte cadastrado!");
                dispose();
                new LoginGUI();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar ouvinte");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dados inválidos: Verifique os números da data.");
        }
    }

    private void cadastrarCriador() {
        try {
            int ano = Integer.parseInt(txtAno.getText());
            int mes = Integer.parseInt(txtMes.getText());
            int dia = Integer.parseInt(txtDia.getText());

            boolean ok = controller.cadastrarCriador(
                    txtLogin.getText(), new String(txtSenha.getPassword()), 
                    txtNome.getText(), txtSexo.getText(),
                    java.time.LocalDate.of(ano, mes, dia)
            );

            if (ok) {
                JOptionPane.showMessageDialog(this, "Criador cadastrado!");
                dispose();
                new LoginGUI();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar criador");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dados inválidos: Verifique os números da data.");
        }
    }
}