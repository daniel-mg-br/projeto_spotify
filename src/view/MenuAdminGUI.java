package view;

import controller.ControllerAdmin;
import javax.swing.*;
import java.awt.*;

/**
 * Classe responsável pela interface gráfica do menu do Administrador.
 * Conecta-se ao ControllerAdmin para executar ações de moderação no sistema.
 */
public class MenuAdminGUI extends JFrame {

    // Declaração do Controller que será utilizado pelas ações dos botões
    private ControllerAdmin controller;

    /**
     * Construtor da tela de menu do administrador.
     * Configura a janela e os botões de ação.
     */
    public MenuAdminGUI() {
        // Inicializa o controller
        this.controller = new ControllerAdmin();

        // Configuração básica da janela
        setTitle("Painel do Administrador");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela

        // Painel principal com layout em grade (3 linhas, 1 coluna, com espaçamento)
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Criação dos botões
        JButton btnSuspender = new JButton("Suspender Usuário");
        JButton btnRemoverConteudo = new JButton("Remover Conteúdo (Música/Episódio)");
        JButton btnSair = new JButton("Sair");

        // --- Configuração das Ações (Listeners) ---

        // Ação: Suspender Usuário
        btnSuspender.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(this, "Digite o ID do Usuário (Ouvinte ou Criador) a ser suspenso:");
                if (idStr == null) return; // Cancela se o usuário fechar a janela
                int idUsuario = Integer.parseInt(idStr);

                // Confirmação de segurança para ação crítica
                int confirmacao = JOptionPane.showConfirmDialog(this, 
                        "Tem certeza que deseja suspender o usuário de ID " + idUsuario + "?", 
                        "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (confirmacao == JOptionPane.YES_OPTION) {
                    boolean sucesso = controller.suspenderUsuario(idUsuario);
                    exibirMensagem(sucesso, "Conta do usuário suspensa com sucesso!", "Erro: Usuário não encontrado ou acesso negado.");
                }
            } catch (NumberFormatException ex) {
                exibirErroFormatacao();
            }
        });

        // Ação: Remover Conteúdo
        btnRemoverConteudo.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(this, "Digite o ID do Conteúdo (Música ou Episódio) a ser removido:");
                if (idStr == null) return; // Cancela se o usuário fechar a janela
                int idConteudo = Integer.parseInt(idStr);

                // Confirmação de segurança para ação crítica
                int confirmacao = JOptionPane.showConfirmDialog(this, 
                        "Tem certeza que deseja deletar permanentemente o conteúdo de ID " + idConteudo + "?", 
                        "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (confirmacao == JOptionPane.YES_OPTION) {
                    boolean sucesso = controller.removerConteudo(idConteudo);
                    exibirMensagem(sucesso, "Conteúdo removido com sucesso!", "Erro: Nenhum conteúdo encontrado com esse ID ou acesso negado.");
                }
            } catch (NumberFormatException ex) {
                exibirErroFormatacao();
            }
        });

        // Ação para encerrar o programa
        btnSair.addActionListener(e -> System.exit(0));

        // Adiciona botões ao painel
        panel.add(btnSuspender);
        panel.add(btnRemoverConteudo);
        panel.add(btnSair);

        // Adiciona painel à janela
        add(panel);

        // Torna a janela visível
        setVisible(true);
    }

    /**
     * Método auxiliar para exibir mensagens de sucesso ou erro.
     */
    private void exibirMensagem(boolean sucesso, String msgSucesso, String msgErro) {
        if (sucesso) {
            JOptionPane.showMessageDialog(this, msgSucesso, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, msgErro, "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método auxiliar para exibir erros de formatação (quando o usuário digita letras em vez de números).
     */
    private void exibirErroFormatacao() {
        JOptionPane.showMessageDialog(this, "Entrada inválida. Certifique-se de digitar apenas números inteiros para o ID.", "Erro de Formatação", JOptionPane.WARNING_MESSAGE);
    }
}