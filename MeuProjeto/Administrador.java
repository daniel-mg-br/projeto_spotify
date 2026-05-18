package MeuProjeto;

public class Administrador {

    private String credencial;

    public Administrador(String credencial) {
        this.credencial = credencial;
    }

    public void suspenderUsuario(Usuario usuario) {

        System.out.println(
                "Usuário suspenso: "
                + usuario.obterDados()
        );
    }

    public void removerConteudo() {

    }

    public Usuario consultarUsuario(Usuario usuario) {
        return usuario;
    }

    public Conteudo consultarConteudo(Conteudo conteudo) {
        return conteudo;
    }
}

public abstract class Administrador {

}
