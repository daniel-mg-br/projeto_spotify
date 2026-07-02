package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.actors.Conta;

public class ContaTest {
    
    /**
     * Verifica se uma conta é criada corretamente com os valores
     * informados no construtor e com os valores padrão definidos
     * para plano, status e data de criação.
     */
    @Test
    void deveCriarContaComValoresPadrao() {

        Conta conta = new Conta("gean", "123");

        assertEquals("gean", conta.getLogin());
        assertEquals("123", conta.getSenha());
        assertEquals("Free", conta.getPlano());
        assertEquals("Ativa", conta.getStatus());
        assertNotNull(conta.getDataCriacao());
    }   

    /**
     * Verifica se o método validarSenha() retorna verdadeiro
     * quando a senha informada corresponde à senha da conta.
     */
    @Test
    void deveValidarSenhaCorretamente() {

        Conta conta = new Conta("gean", "123");

        assertTrue(conta.validarSenha("123"));
    }

    /**
     * Verifica se o método validarSenha() retorna falso
     * quando a senha informada é diferente da senha cadastrada.
     */
    @Test
    void naoDeveValidarSenhaIncorreta() {

        Conta conta = new Conta("gean", "123");

        assertFalse(conta.validarSenha("456"));
    }
    
    /**
     * Verifica se o método alterarSenha() altera a senha com sucesso
     * quando é informado um novo valor diferente da senha atual.
     */
    @Test
    void deveAlterarSenha() {

        Conta conta = new Conta("gean", "123");

        assertTrue(conta.alterarSenha("novaSenha"));

        assertEquals("novaSenha", conta.getSenha());
    }

    /**
     * Verifica se o método alterarSenha() impede a alteração
     * quando a nova senha é igual à senha já cadastrada.
     */
    @Test
    void naoDeveAlterarParaMesmaSenha() {

        Conta conta = new Conta("gean", "123");

        assertFalse(conta.alterarSenha("123"));
    }

}