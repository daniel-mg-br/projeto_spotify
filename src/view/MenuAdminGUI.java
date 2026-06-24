package view;

import javax.swing.*;
import java.awt.*;

/**
 * Classe responsável pela interface gráfica do menu do Administrador.
 * Apresenta opções básicas de navegação para o usuário administrador.
 */
public class MenuAdminGUI extends JFrame {

    /**
     * Construtor da tela de menu do administrador.
     * Configura a janela e os botões de ação.
     */
    public MenuAdminGUI() {

        // Configuração básica da janela
        setTitle("Menu Admin");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela

        // Painel principal com layout em grade (2 linhas, 1 coluna)
        JPanel panel = new JPanel(new GridLayout(2, 1));

        // Botão que futuramente pode abrir o painel administrativo
        JButton btn1 = new JButton("Painel Admin");

        // Botão para encerrar a aplicação
        JButton btn2 = new JButton("Sair");

        // Ação para encerrar o programa
        btn2.addActionListener(e -> System.exit(0));

        // Adiciona botões ao painel
        panel.add(btn1);
        panel.add(btn2);

        // Adiciona painel à janela
        add(panel);

        // Torna a janela visível
        setVisible(true);
    }
}