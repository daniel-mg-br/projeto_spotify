package model.content;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe Album, que representa os álbuns do Criador e que o Ouvinte ouve.
 */
public class Album {
	
	// Atributos privados de Album.
    private int id;
    private int criadorId;
    private String titulo;
    private String tipo;
    private boolean status;
    private LocalDate lancamento;
    
    // Lista de faixa(s) que compõem um álbum.
    private List<Musica> musicas;
    
    /**
     * Método Construtor padrão para instanciação.
     * 
     * @param titulo Título do álbum;
     * @param tipo Tipo do álbum (Single, Coletânea, etc);
     * @param criadorId ID do criador que possui o álbum.
     */
    public Album(String titulo, String tipo, int criadorId) {
    	this.id = 0;
    	this.criadorId = criadorId;
    	this.titulo = titulo;
    	this.tipo = tipo;
    	this.lancamento = LocalDate.now();
        this.status = false;
        
        this.musicas = new ArrayList<>();
    }
    
    /**
     * Método Construtor com todos os dados (recuperação bd -> objeto).
     * 
     * @param id ID do álbum recuperado;
     * @param titulo Título do álbum recuperado;
     * @param tipo Tipo do álbum recuperado;
     * @param criadorId ID do criador que possui esse álbum;
     * @param status Status do álbum (rascunho = false, lançado = true);
     * @param lancamento Data de lançamento do álbum recuperado;
     */
    public Album(int id, String titulo, String tipo, int criadorId, boolean status, LocalDate lancamento) {
        this.id = id;
        this.criadorId = criadorId;
        this.titulo = titulo;
        this.tipo = tipo;
        this.status = status;
        this.lancamento = lancamento;
        
        this.musicas = new ArrayList<>();
    }
    
    // Métodos Getter e Setter Padrão.
    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}

    public int getCriadorId() {return this.criadorId;}
    public void setCriadorId(int criadorId) {this.criadorId = criadorId;}
    
    public String getTitulo() {return this.titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}

    public String getTipo() {return this.tipo;}
    public void setTipo(String tipo) {this.tipo = tipo;}

    public boolean isStatus() {return this.status;}
    public void setStatus(boolean status) {this.status = status;}

    public LocalDate getLancamento() {return this.lancamento;}
    public void setLancamento(LocalDate lancamento) {this.lancamento = lancamento;}
    
    public int getNumeroFaixas() {
    	return this.musicas.size();
    }

    public List<Musica> getMusicas() {
        return musicas;
    }
    
    /**
     * Método para mostrar o título das músicas do álbum.
     */
    public void mostrarFaixas() {
        for (Musica m : musicas) {
            System.out.println(m.getTitulo());
        }
    }
    
    /**
     * Método para adicionar músicas ao álbum.
     * 
     * @param m Objeto de Musica com os dados a serem adicionados;
     * @return Retorna true se a operação é bem sucedida, e false se não.
     */
    public boolean adicionarFaixa(Musica m) {
    	if (m == null) return false;
    	
    	// .stream() transforma a lista em uma sequência de elementos;
    	// .anyMatch() compara se a música existe nessa sequência.
        boolean jaExiste = this.musicas.stream().anyMatch(musica -> musica.getId() == m.getId());
        if (jaExiste) return false;
        
        return this.musicas.add(m);
    }
    
    /**
     * Método para remover músicas do álbum.
     * 
     * @param m Objeto de Musica a ser removida do álbum;
     * @return Retorna true se a operação for bem sucedida e false se não.
     */
    public boolean removerFaixa(Musica m) {
    	// .removeIf() remove o(s) elemento(s) que cumpre(m) a condição entre parênteses.
    	return this.musicas.removeIf(musica -> musica.getId() == m.getId());
    }
    
    /**
     * Método para alterar o status de lançamento do álbum (false -> true).
     */
    public void lancarAlbum() {
        this.status = true;
        this.lancamento = LocalDate.now();

        System.out.println("Álbum lançado com sucesso!");
    }
    
    /**
     * Método para recuperar os dados do álbum.
     * 
     * @return Retorna os dados em formato de string.
     */
    public String obterDados() {
        return "ID: " + this.getId() +
        	   "\nID do criador: " + this.getCriadorId() +
               "\nTítulo: " + this.getTitulo() +
               "\nTipo: " + getTipo() +
               "\nStatus: " + this.isStatus() +
               "\nLançamento: " + this.getLancamento() +
               "\nNúmero de Faixas: " + this.getNumeroFaixas();
    }
}