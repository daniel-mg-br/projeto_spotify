package model.content;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe Playlist, que representa um conjunto de músicas que o Ouvinte pode criar.
 */
public class Playlist {
	
	// Atributos privados da Playlist.
    private int id;
    private int usuarioId;
    private String titulo;
    private String descricao;
    private boolean compartilhar;
    private LocalDate criacao;
    
    // Lista de músicas que compõem a playlist.
    private List<Musica> musicas;
    
    /**
     * Método Construtor padrão para instanciação.
     * 
     * @param titulo Título da playlist;
     * @param descricao Descrição da playlist;
     * @param usuarioId ID do usuário (ouvinte) que criou a playlist.
     */
    public Playlist(String titulo, String descricao, int usuarioId) {
    	this.id = 0;
    	this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.criacao = LocalDate.now();
        this.compartilhar = false;
        
        this.musicas = new ArrayList<>();
    }

    /**
     * Método Construtor com todos os dados (recuperação bd -> objeto).
     * 
     * @param id ID da playlist recuperada;
     * @param usuarioId ID do usuário (ouvinte) dono da playlist;
     * @param titulo Título da playlist recuperada;
     * @param descricao Descrição da playlist recuperada;
     * @param compartilhar Status de compartilhamento da playlist (true = pública, false = privada);
     * @param criacao Data de criação da playlist recuperada.
     */
    public Playlist(int id, int usuarioId, String titulo, String descricao, boolean compartilhar, LocalDate criacao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.compartilhar = compartilhar;
        this.criacao = criacao;
        
        this.musicas = new ArrayList<>();
    }
    
    // Métodos Getter e Setter padrão.
    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}
    
    public int getUsuarioId() {return this.usuarioId;}
    public void setUsuarioId(int id) {this.usuarioId = id;}
    
    public String getTitulo() {return this.titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}
   
    public String getDescricao() {return this.descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}
    
    public boolean isCompartilhar() {return this.compartilhar;}
	public void setCompartilhar(boolean compartilhar) {this.compartilhar = compartilhar;}
	
	public LocalDate getCriacao() {return this.criacao;}
	public void setCriacao(LocalDate criacao) {this.criacao = criacao;}
	
	public int getTotalMusicas() {
		return this.musicas.size();
	}
	
	public List<Musica> getMusicas() {
		return this.musicas;
	}
    
    /**
     * Método para mostrar o título das músicas na playlist.
     */
    public void mostrarMusicas() {
        for (Musica m : musicas) {
            System.out.println(m.getTitulo());
        }
    }
    
    /**
     * Método para adicionar músicas à playlist, com verifações.
     * 
     * @param m Objeto de Musica com os dados a serem salvos;
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean adicionarMusica(Musica m) {
    	if (m == null) return false;
        
    	// .stream() transforma a lista em uma sequência de elementos;
    	// .anyMatch() compara se a música existe nessa sequência.
        boolean jaExiste = this.musicas.stream().anyMatch(musica -> musica.getId() == m.getId());
        if (jaExiste) return false;
        
        return this.musicas.add(m);
    }
    
    /**
     * Método para remover música da playlist.
     * 
     * @param m Objeto de Musica a ser removida;
     * @return Retorna true se a operação for bem sucedida, e false se não.
     */
    public boolean removerMusica(Musica m) {
    	// .removeIf() remove o(s) elemento(s) que cumpre(m) a condição entre parênteses.
    	return this.musicas.removeIf(musica -> musica.getId() == m.getId());
    }
    
    /**
     * Método para alterar o status de compartilhamento da música (privado -> público).
     */
    public void compartilhar() {
    	if (!this.isCompartilhar()) {
    		setCompartilhar(true);
    		System.out.println("Playlist compartilhada!");
    	}
    }
    
    /**
     * Método para recuperar os dados da Playlist.
     * 
     * @return Retorna os dados em formato de String.
     */
    public String obterDados() {
        return "ID: " + this.getId() +
        	   "\nID do criador: " + this.getUsuarioId() +	
               "\nTítulo: " + this.getTitulo() +
               "\nDescrição: " + this.getDescricao() +
               "\nCriada em: " + this.getCriacao() +
               "\nQuantidade de músicas: " + this.getTotalMusicas() +
               "\nCompartilhar: " + this.isCompartilhar();
    }
}