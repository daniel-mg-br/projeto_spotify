package model.content;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

// Classe Album, que representa os álbuns do Criador e que o Ouvinte ouve
public class Album {
	
	// Atributos privados
    private int id;
    private String titulo;
    private String tipo;
    private boolean status;
    private LocalDate lancamento;
    private int numeroFaixas;
    
    // Lista de faixa(s) que compõem um álbum
    private List<Musica> musicas;
    
    // Método Construtor padrão
    public Album(String titulo, String tipo) {
    	this.id = 0;
    	this.titulo = titulo;
    	this.tipo = tipo;
    	this.lancamento = LocalDate.now();
        this.numeroFaixas = 0;
        this.status = false;
        
        this.musicas = new ArrayList<>();
    }
    
    // Método Construtor com todos os dados (recuperação bd -> objeto)
    public Album(int id, String titulo, String tipo, boolean status, LocalDate lancamento, int numeroFaixas) {
        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.numeroFaixas = numeroFaixas;
        this.status = status;
        this.lancamento = lancamento;
        
        this.musicas = new ArrayList<>();
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

    public LocalDate getLancamento() {return this.lancamento;}
    public void setLancamento(LocalDate lancamento) {this.lancamento = lancamento;}
    
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
        this.status = true;
        this.lancamento = LocalDate.now();

        System.out.println("Álbum lançado com sucesso!");
    }
}