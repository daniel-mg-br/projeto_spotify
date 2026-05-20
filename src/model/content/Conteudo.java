package model.content;

public abstract class Conteudo {
    private int id;
    private String titulo;
    private int duracaoMin;
    
    public Conteudo() {
    	
    }
    
    public Conteudo(int id, String titulo, int duracaoMin) {
        this.id = id;
        this.titulo = titulo;
        this.duracaoMin = duracaoMin;
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

    public int getDuracaoMin() {
        return duracaoMin;
    }

    public void setDuracaoMin(int duracaoMin) {
        this.duracaoMin = duracaoMin;
    }

    public String obterDados() {
        return "ID: " + id +
               "\nTítulo: " + titulo +
               "\nDuração: " + duracaoMin + " min";
    }
}