package main;

import view.LoginGUI;

public class Principal {

    /**
     * Método principal da aplicação.
     * É o ponto de entrada do programa, responsável por iniciar
     * a interface gráfica de login.
     *
     * @param args argumentos passados pela linha de comando (não utilizados).
     */
    public static void main(String[] args) {

        // Cria e exibe a janela de login da aplicação.
        new LoginGUI();
    }
}