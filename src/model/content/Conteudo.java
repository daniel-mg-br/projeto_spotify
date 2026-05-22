package model.content;

// Classe abstrata Conteudo, representando a mídia geral do sistema
public abstract class Conteudo {
	// Atributos privados
    private int id;
    private String titulo;
    private int duracaoMin;
    
    // Método Construtor
    public Conteudo(int id, String titulo, int duracaoMin) {
        this.id = id;
        this.titulo = titulo;
        this.duracaoMin = duracaoMin;
    }
    
    // Métodos Getter e Setter padrão
    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}

    public String getTitulo() {return this.titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}

    public int getDuracaoMin() {return this.duracaoMin;}
    public void setDuracaoMin(int duracaoMin) {this.duracaoMin = duracaoMin;}
    
    // Método para recuperar os dados do conteúdo
    public String obterDados() {
        return "ID: " + this.getId() +
               "\nTítulo: " + this.getTitulo() +
               "\nDuração: " + this.getDuracaoMin() + " min";
    }
}