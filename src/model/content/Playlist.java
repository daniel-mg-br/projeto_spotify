package model.content;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Playlist {

    private int id;
    private String titulo;
    private String descricao;
    private boolean compartilhar;
    private Date criacao;
    private int totalMusicas;
    private List<Musica> musicas;
    
    public Playlist() {

        musicas = new ArrayList<>();
        criacao = new Date();
        totalMusicas = 0;
        compartilhar = false;
    }

    public Playlist(int id, String titulo, String descricao) {

        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;

        this.musicas = new ArrayList<>();
        this.criacao = new Date();
        this.totalMusicas = 0;
        this.compartilhar = false;
    }

    public String obterDados() {
        return "ID: " + id +
               "\nTítulo: " + titulo +
               "\nDescrição: " + descricao +
               "\nCriada em: " + criacao +
               "\nQuantidade de músicas: " + totalMusicas;
    }

    public void mostrarMusicas() {
        for (Musica m : musicas) {
            System.out.println(m.getTitulo());
        }
    }

    public boolean adicionarMusica(Musica m) {
        boolean adicionou = musicas.add(m);

        if (adicionou) {
            totalMusicas++;
        }

        return adicionou;
    }

    public boolean removerMusica(Musica m) {
        boolean removeu = musicas.remove(m);

        if (removeu) {
            totalMusicas--;
        }

        return removeu;
    }

    public void compartilhar() {
        setCompartilhar(true);
        System.out.println("Playlist compartilhada!");
    }

	public boolean isCompartilhar() {
		return compartilhar;
	}

	public void setCompartilhar(boolean compartilhar) {
		this.compartilhar = compartilhar;
	}
}