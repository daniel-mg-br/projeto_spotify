package model.content;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Episodio extends Conteudo {

    private int numEpisodio;
    private Date lancamento;
    private List<String> convidados;
    
    public Episodio() {
        convidados = new ArrayList<>();
    }

    public Episodio(int id, String titulo, int duracaoMin,
                     int numEpisodio, Date lancamento) {

        super(id, titulo, duracaoMin);

        this.numEpisodio = numEpisodio;
        this.lancamento = lancamento;
        this.convidados = new ArrayList<>();
    }

    public int getNumEpisodio() {
        return numEpisodio;
    }

    public void setNumEpisodio(int episodio) {
        this.numEpisodio = episodio;
    }

    public Date getLancamento() {
        return lancamento;
    }

    public void setLancamento(Date lancamento) {
        this.lancamento = lancamento;
    }

    public List<String> getConvidados() {
        return convidados;
    }

    public void setConvidados(List<String> convidados) {
        this.convidados = convidados;
    }

    @Override
    public String obterDados() {
        return super.obterDados() +
               "\nNúmero do episódio: " + numEpisodio +
               "\nLançamento: " + lancamento;
    }

    public void mostrarConvidados() {
        for (String nome : convidados) {
            System.out.println(nome);
        }
    }

    public boolean adicionarConvidado(String nome) {
        return convidados.add(nome);
    }

    public boolean removerConvidado(String nome) {
        return convidados.remove(nome);
    }
}