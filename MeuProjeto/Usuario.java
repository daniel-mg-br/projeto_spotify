package MeuProjeto;

import java.util.Calendar;
import java.util.Date;

public class Usuario {

    private Conta conta;
    private int id;
    private String nome;
    private String sexo;
    private Date aniversario;

    public Usuario(Conta conta, int id,
                    String nome, String sexo,
                    Date aniversario) {

        this.conta = conta;
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.aniversario = aniversario;
    }

    public void alterarNome(String novoNome) {
        this.nome = novoNome;
    }

    public void alterarSexo(String novoSexo) {
        this.sexo = novoSexo;
    }

    public int calcularIdade() {

        Calendar hoje = Calendar.getInstance();
        Calendar nascimento = Calendar.getInstance();

        nascimento.setTime(aniversario);

        int idade = hoje.get(Calendar.YEAR) - nascimento.get(Calendar.YEAR);

        return idade;
    }

    public String obterDados() {

        return "Id: " + id +
               "\nNome: " + nome +
               "\nSexo: " + sexo +
               "\nIdade: " + calcularIdade();
    }
}
