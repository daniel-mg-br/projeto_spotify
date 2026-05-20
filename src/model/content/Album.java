package model.content;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Album {

    private int id;
    private String titulo;
    private String tipo;
    private boolean status;
    private Date lancamento;
    private int numeroFaixas;
    private List<Musica> musicas;
    
    public Album() {

        musicas = new ArrayList<>();
        numeroFaixas = 0;
        status = false;
    }

    public Album(int id, String titulo, String tipo) {

        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;

        this.musicas = new ArrayList<>();
        this.numeroFaixas = 0;
        this.status = false;
    }

   

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getLancamento() {
        return lancamento;
    }

    public void setLancamento(Date lancamento) {
        this.lancamento = lancamento;
    }

    public int getNumeroFaixas() {
        return numeroFaixas;
    }

    public void setNumeroFaixas(int numeroFaixas) {
        this.numeroFaixas = numeroFaixas;
    }

    public List<Musica> getMusicas() {
        return musicas;
    }

    public void setMusicas(List<Musica> musicas) {
        this.musicas = musicas;
    }

    

    public String obterDados() {
        return "ID: " + id +
               "\nTítulo: " + titulo +
               "\nTipo: " + tipo +
               "\nStatus: " + status +
               "\nLançamento: " + lancamento +
               "\nNúmero de Faixas: " + numeroFaixas;
    }

    public void mostrarFaixas() {

        for (Musica m : musicas) {
            System.out.println(m.getTitulo());
        }
    }

    public boolean adicionarFaixa(Musica m) {

        boolean adicionou = musicas.add(m);

        if (adicionou) {
            numeroFaixas++;
        }

        return adicionou;
    }

    public boolean removerFaixa(Musica m) {

        boolean removeu = musicas.remove(m);

        if (removeu) {
            numeroFaixas--;
        }

        return removeu;
    }

    public void lancarAlbum() {

        status = true;
        lancamento = new Date();

        System.out.println("Álbum lançado com sucesso!");
    }
}