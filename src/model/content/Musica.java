package model.content;
import java.util.List;
import java.util.ArrayList;

// Classe Música, filha de Conteúdo, representando uma música de um criador
public class Musica extends Conteudo {
	// Atributos privados
    private String genero;
    private String letra;
    
    // Lista com os membros da equipe por trás da música
    private List<String> equipe;
    
    // Método Construtor com todos os dados da música
    public Musica(int id, String titulo, int duracaoMin, String genero, String letra) {
        super(id, titulo, duracaoMin);

        this.genero = genero;
        this.letra = letra;
        this.equipe = new ArrayList<>();
    }
    
    // Método Construtor com os dados apenas do Conteúdo
    public Musica(int id, String titulo, int duracaoMin) {
    	super(id, titulo, duracaoMin);
        equipe = new ArrayList<>();
    }
    
    // Métodos Getter e Setter padrão
	public String getGenero() {return this.genero;}
    public void setGenero(String genero) {this.genero = genero;}
    
    public String getLetra() {return this.letra;}
    public void setLetra(String letra) {this.letra = letra;}

    public List<String> getEquipe() {
    	return this.equipe;
    }
    
    // Método para ecuperar os dados da música
    @Override
    public String obterDados() {
        return super.obterDados() +
               "\nGênero: " + this.getGenero() +
               "\nLetra: " + this.getLetra();
    }
    
    // Método para mostrar a equipe de produção da música
    public void mostrarEquipe() {
        for (String pessoa : equipe) {
            System.out.println(pessoa);
        }
    }
    
    // Método para adicionar um membro à equipe da música, com verificação
    public boolean adicionarMembroEquipe(String nomeMembro) {
    	if (this.equipe.contains(nomeMembro) || nomeMembro == null) {
    		return false;
    	}
    	
    	return this.equipe.add(nomeMembro);
    }
    
    // Método para removere um membro da equipe da música
    public boolean removerMembroEquipe(String nomeMembro) {
    	return this.equipe.remove(nomeMembro);
    }
}