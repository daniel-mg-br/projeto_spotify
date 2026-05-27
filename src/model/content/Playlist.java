package model.content;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

// Classe Playlist, que representa um conjunto de músicas que o Ouvinte pode criar
public class Playlist {
	// Atributos privados
    private int id;
    private String titulo;
    private String descricao;
    private boolean compartilhar;
    private Date criacao;
    private int totalMusicas;
    
    // Lista de músicas que compõem a playlist
    private List<Musica> musicas;
    
    // Método Construtor com alguns dados padrão preenchidos
    public Playlist() {
        this.musicas = new ArrayList<>();
        this.criacao = new Date();
        this.totalMusicas = 0;
        this.compartilhar = false;
    }

    // Método Construtor com todos os dados da Playlist
    public Playlist(int id, String titulo, String descricao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;

        this.musicas = new ArrayList<>();
        this.criacao = new Date();
        this.totalMusicas = 0;
        this.compartilhar = false;
    }
    
    // Métodos Getter e Setter padrão (id, titulo, descricao, compartilhar criacao, totmusica, list)
    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}
    
    public String getTitulo() {return this.titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}
   
    public String getDescricao() {return this.descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}
    
    public boolean isCompartilhar() {return this.compartilhar;}
	public void setCompartilhar(boolean compartilhar) {this.compartilhar = compartilhar;}
	
	public Date getCriacao() {return this.criacao;}
	public void setCriacao(Date criacao) {this.criacao = criacao;}
	
	public int getTotalMusicas() {return this.totalMusicas;}
	public void setTotalMusicas(int totalMusicas) {this.totalMusicas = totalMusicas;}
	
	public List<Musica> getMusicas() {return this.musicas;}
    
    // Método para recuperar os dados da Playlist
    public String obterDados() {
        return "ID: " + this.getId() +
               "\nTítulo: " + this.getTitulo() +
               "\nDescrição: " + this.getDescricao() +
               "\nCriada em: " + this.getCriacao() +
               "\nQuantidade de músicas: " + this.getTotalMusicas();
    }
    
    // Método para mostrar o título das músicas na playlist
    public void mostrarMusicas() {
        for (Musica m : musicas) {
            System.out.println(m.getTitulo());
        }
    }
    
    // Método para adicionar músicas à playlist, com verificação
    public boolean adicionarMusica(Musica m) {
        boolean adicionou = true;
        
        if (this.getMusicas().contains(m) || m == null) {
        	adicionou = false;
        }

        if (adicionou) {
        	this.getMusicas().add(m);
            this.totalMusicas++;
        }

        return adicionou;
    }
    
    // Método para remover música da playlist
    public boolean removerMusica(Musica m) {
        boolean removeu = musicas.remove(m);

        if (removeu) {
            this.totalMusicas--;
        }

        return removeu;
    }
    
    // Método para alterar o status de compartilhamento da música
    public void compartilhar() {
    	if (!this.isCompartilhar()) {
    		setCompartilhar(true);
    		System.out.println("Playlist compartilhada!");
    	}
    }
}