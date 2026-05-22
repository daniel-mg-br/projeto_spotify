package model.content;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

// Classe Album, que representa os álbuns do Criador e que o Ouvinte ouve
public class Album {
	
	// Atributos privados
    private int id;
    private String titulo;
    private String tipo;
    private boolean status;
    private Date lancamento;
    private int numeroFaixas;
    
    // Lista de faixa(s) que compõem um álbum
    private List<Musica> musicas;
    
    // Método Construtor com alguns atributos padrão definidos
    public Album() {
        this.musicas = new ArrayList<>();
        this.numeroFaixas = 0;
        this.status = false;
    }
    
    // Método Construtor com todos os dados do Álbum
    public Album(int id, String titulo, String tipo) {
        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;

        this.musicas = new ArrayList<>();
        this.numeroFaixas = 0;
        this.status = false;
    }
    
    // Métodos Getter e Setter Padrão
    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}

    public String getTitulo() {return this.titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}

    public String getTipo() {return this.tipo;}
    public void setTipo(String tipo) {this.tipo = tipo;}

    public boolean isStatus() {return this.status;}
    public void setStatus(boolean status) {this.status = status;}

    public Date getLancamento() {return this.lancamento;}
    public void setLancamento(Date lancamento) {this.lancamento = lancamento;}
    
    public int getNumeroFaixas() {return this.numeroFaixas;}
    public void setNumeroFaixas(int numeroFaixas) {this.numeroFaixas = numeroFaixas;}

    public List<Musica> getMusicas() {
        return musicas;
    }
    
    // Método para recuperar os dados do album
    public String obterDados() {
        return "ID: " + this.getId() +
               "\nTítulo: " + this.getTitulo() +
               "\nTipo: " + getTipo() +
               "\nStatus: " + this.isStatus() +
               "\nLançamento: " + this.getLancamento() +
               "\nNúmero de Faixas: " + this.getNumeroFaixas();
    }
    
    // Método para mostrar o título das músicas no álbum
    public void mostrarFaixas() {
        for (Musica m : musicas) {
            System.out.println(m.getTitulo());
        }
    }
    
    // Método para adicionar músicas ao álbum
    public boolean adicionarFaixa(Musica m) {
        boolean adicionou = true;

        if (this.musicas.contains(m) || m == null) {
        	adicionou = false;
            
        }
        
        if (adicionou) {
        	this.musicas.add(m);
            numeroFaixas++;
        }
        
        return adicionou;
    }
    
    // Método para remover músicas do álbum
    public boolean removerFaixa(Musica m) {

        boolean removeu = musicas.remove(m);

        if (removeu) {
            this.numeroFaixas--;
        }

        return removeu;
    }
    
    // Método para alterar o status de lançamento do álbum
    public void lancarAlbum() {

        status = true;
        lancamento = new Date();

        System.out.println("Álbum lançado com sucesso!");
    }
}