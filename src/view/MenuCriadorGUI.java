package view;

import javax.swing.*;
import java.awt.*;

/**
 * Classe responsável pela interface gráfica do menu do Criador.
 * Exibe opções básicas de navegação para o usuário do tipo Criador.
 */
public class MenuCriadorGUI extends JFrame {

    /**
     * Construtor da tela de menu do Criador.
     * Configura a janela e os botões disponíveis.
     */
    public MenuCriadorGUI() {

        // Configuração da janela principal
        setTitle("Menu Criador");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Painel principal com layout em grade (2 linhas, 1 coluna)
        JPanel panel = new JPanel(new GridLayout(2, 1));

        // Botão que futuramente abrirá o painel do Criador
        JButton btn1 = new JButton("Painel Criador");

        // Botão para encerrar o sistema
        JButton btn2 = new JButton("Sair");

        // Ação de encerramento da aplicação
        btn2.addActionListener(e -> System.exit(0));

        // Adiciona botões ao painel
        panel.add(btn1);
        panel.add(btn2);

        // Adiciona painel à janela principal
        add(panel);

        // Exibe a janela
        setVisible(true);
    }
}