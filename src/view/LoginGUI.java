package view;

import controller.ControllerAutenticador; 
import model.actors.Administrador;
import model.actors.Criador;
import model.actors.Ouvinte;
import model.actors.Usuario;

import javax.swing.*;
import java.awt.*;

/**
 * Classe responsável pela interface gráfica de login do sistema.
 * Permite autenticação de usuários e redirecionamento para menus
 * específicos de acordo com o tipo de usuário.
 */
public class LoginGUI extends JFrame {

    // Campos de entrada do login.
    private JTextField txtLogin;
    private JPasswordField txtSenha;

    // Controller responsável pela autenticação.
    private ControllerAutenticador controller;

    /**
     * Construtor da tela de login.
     * Responsável por montar a interface e configurar eventos.
     */
    public LoginGUI() {

        // Inicializa o controller de autenticação.
        controller = new ControllerAutenticador();

        // Configuração da janela.
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela.

        // Painel principal com layout em grade.
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        // Campo de login.
        panel.add(new JLabel("Login:"));
        txtLogin = new JTextField();
        panel.add(txtLogin);

        // Campo de senha.
        panel.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        panel.add(txtSenha);

        // Botão de login.
        JButton btnLogin = new JButton("Entrar");

        // Botão para abrir tela de cadastro.
        JButton btnCadastro = new JButton("Cadastrar");

        panel.add(btnLogin);
        panel.add(btnCadastro);

        // Evento de login.
        btnLogin.addActionListener(e -> fazerLogin());

        // Evento para abrir cadastro.
        btnCadastro.addActionListener(e -> abrirCadastro());

        // Adiciona painel à janela.
        add(panel);

        // Torna a janela visível.
        setVisible(true);
    }

    /**
     * Realiza o processo de autenticação do usuário.
     * Se o login for válido, redireciona para o menu correspondente.
     */
    private void fazerLogin() {

        // Captura credenciais digitadas.
        String login = txtLogin.getText();
        String senha = new String(txtSenha.getPassword());

        if (login.trim().isEmpty() || senha.trim().isEmpty()) {
        	JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Autenticação via controller.
        Usuario u = controller.login(login, senha);

        // Verificação de login inválido.
        if (u == null) {
            JOptionPane.showMessageDialog(this, "Login inválido!");
            return;
        }

        // Fecha tela de login.
        dispose();

        // Redireciona conforme o tipo de usuário (polimorfismo).
        if (u instanceof Administrador) {
            new MenuAdminGUI();
        } else if (u instanceof Criador) {
            new MenuCriadorGUI();
        } else if (u instanceof Ouvinte) {
            new MenuOuvinteGUI();
        }
    }

    /**
     * Abre a tela de cadastro e fecha a tela atual.
     */
    private void abrirCadastro() {
        dispose();
        new CadastroGUI();
    }
}