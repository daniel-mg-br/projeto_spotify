package view;

import controller.ControllerAutenticador;

import javax.swing.*;
import java.awt.*;

/**
 * Classe responsável pela interface gráfica de cadastro de usuários.
 * Permite cadastrar dois tipos de usuários: Ouvinte e Criador.
 * Utiliza Swing para construção da interface e ControllerAutenticador
 * para realizar a lógica de negócio.
 */
public class CadastroGUI extends JFrame {

    // Campos de entrada do formulário de cadastro
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JTextField txtNome;
    private JTextField txtSexo;
    private JTextField txtAno;
    private JTextField txtMes;
    private JTextField txtDia;

    // Controller responsável pela lógica de autenticação/cadastro
    private ControllerAutenticador controller;

    /**
     * Construtor da interface de cadastro.
     * Responsável por montar toda a UI e configurar eventos.
     */
    public CadastroGUI() {

        // Inicializa o controller
        controller = new ControllerAutenticador();

        // Configuração básica da janela
        setTitle("Cadastro");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Painel principal com layout em grade
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));

        // Campo de login
        panel.add(new JLabel("Login:"));
        txtLogin = new JTextField();
        panel.add(txtLogin);

        // Campo de senha
        panel.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        panel.add(txtSenha);

        // Campo de nome
        panel.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        panel.add(txtNome);

        // Campo de sexo
        panel.add(new JLabel("Sexo:"));
        txtSexo = new JTextField();
        panel.add(txtSexo);

        // Campo de data de nascimento
        panel.add(new JLabel("Data Nasc (YYYY MM DD):"));

        // Painel auxiliar para organizar a data em 3 campos
        JPanel dataPanel = new JPanel(new GridLayout(1, 3));

        txtAno = new JTextField();
        txtMes = new JTextField();
        txtDia = new JTextField();

        dataPanel.add(txtAno);
        dataPanel.add(txtMes);
        dataPanel.add(txtDia);

        panel.add(dataPanel);

        // Botão para cadastrar Ouvinte
        JButton btnCriarOuvinte = new JButton("Criar Ouvinte");

        // Botão para cadastrar Criador
        JButton btnCriarCriador = new JButton("Criar Criador");

        panel.add(btnCriarOuvinte);
        panel.add(btnCriarCriador);

        // Ações dos botões
        btnCriarOuvinte.addActionListener(e -> cadastrarOuvinte());
        btnCriarCriador.addActionListener(e -> cadastrarCriador());

        // Adiciona painel à janela
        add(panel);

        // Torna a janela visível
        setVisible(true);
    }

    /**
     * Método responsável por cadastrar um usuário do tipo Ouvinte.
     * Coleta os dados da interface, valida e envia para o controller.
     */
    private void cadastrarOuvinte() {

        try {
            // Captura dos dados do formulário
            String login = txtLogin.getText();
            String senha = new String(txtSenha.getPassword());
            String nome = txtNome.getText();
            String sexo = txtSexo.getText();

            // Conversão da data de nascimento
            int ano = Integer.parseInt(txtAno.getText());
            int mes = Integer.parseInt(txtMes.getText());
            int dia = Integer.parseInt(txtDia.getText());

            // Chamada do controller para cadastro
            boolean ok = controller.cadastrarOuvinte(
                    login, senha, nome, sexo,
                    java.time.LocalDate.of(ano, mes, dia)
            );

            // Feedback para o usuário
            if (ok) {
                JOptionPane.showMessageDialog(this, "Ouvinte cadastrado!");
                dispose(); // Fecha tela atual
                new LoginGUI(); // Abre tela de login
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar ouvinte");
            }

        } catch (Exception ex) {
            // Tratamento de erro genérico (entrada inválida)
            JOptionPane.showMessageDialog(this, "Dados inválidos");
        }
    }

    /**
     * Método responsável por cadastrar um usuário do tipo Criador.
     * Estrutura semelhante ao cadastro de Ouvinte, mudando apenas o tipo.
     */
    private void cadastrarCriador() {

        try {
            // Captura dos dados do formulário
            String login = txtLogin.getText();
            String senha = new String(txtSenha.getPassword());
            String nome = txtNome.getText();
            String sexo = txtSexo.getText();

            // Conversão da data de nascimento
            int ano = Integer.parseInt(txtAno.getText());
            int mes = Integer.parseInt(txtMes.getText());
            int dia = Integer.parseInt(txtDia.getText());

            // Chamada do controller para cadastro de criador
            boolean ok = controller.cadastrarCriador(
                    login, senha, nome, sexo,
                    java.time.LocalDate.of(ano, mes, dia)
            );

            // Feedback para o usuário
            if (ok) {
                JOptionPane.showMessageDialog(this, "Criador cadastrado!");
                dispose(); // Fecha tela atual
                new LoginGUI(); // Abre tela de login
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar criador");
            }

        } catch (Exception ex) {
            // Tratamento de erro genérico (entrada inválida)
            JOptionPane.showMessageDialog(this, "Dados inválidos");
        }
    }
}