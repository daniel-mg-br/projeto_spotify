package model.content;

import java.util.List;
import java.util.ArrayList;

public class Musica extends Conteudo {

    private String genero;
    private String letra;
    private List<String> equipe;
    
    public Musica(int id, String titulo, int duracaoMin,
                   String genero, String letra) {

        super(id, titulo, duracaoMin);

        this.genero = genero;
        this.letra = letra;
        this.equipe = new ArrayList<>();
    }

    
    public Musica() {
        equipe = new ArrayList<>();
    }

	public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public List<String> getEquipe() {
        return equipe;
    }

    public void setEquipe(List<String> equipe) {
        this.equipe = equipe;
    }

    @Override
    public String obterDados() {
        return super.obterDados() +
               "\nGênero: " + genero +
               "\nLetra: " + letra;
    }

    public void mostrarEquipe() {
        for (String pessoa : equipe) {
            System.out.println(pessoa);
        }
    }
}