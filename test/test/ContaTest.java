package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.actors.Conta;

public class ContaTest {
    
	//Testa se esta criando a conta com os valores corretos e correspondentes
    @Test
    void deveCriarContaComValoresPadrao() {

        Conta conta = new Conta("gean", "123");

        assertEquals("gean", conta.getLogin());
        assertEquals("123", conta.getSenha());
        assertEquals("Free", conta.getPlano());
        assertEquals("Ativa", conta.getStatus());
        assertNotNull(conta.getDataCriacao());
    }
    //Esse teste verifica se o metodo funciona corretamente para uma senha valida
    @Test
    void deveValidarSenhaCorretamente() {

        Conta conta = new Conta("gean", "123");

        assertTrue(conta.validarSenha("123"));
    }
    // Esse teste verifica se o metodo funciona corretamente para uma senha invalida
    @Test
    void naoDeveValidarSenhaIncorreta() {

        Conta conta = new Conta("gean", "123");

        assertFalse(conta.validarSenha("456"));
    }
    /* Esse teste faz duas coisas Primeiro confirmamos que alterarSenha() retorna true,
    indicando sucesso, e depois verificamos que o atributo senha foi atualizado para 
    o novo valor." */
    @Test
    void deveAlterarSenha() {

        Conta conta = new Conta("gean", "123");

        assertTrue(conta.alterarSenha("novaSenha"));

        assertEquals("novaSenha", conta.getSenha());
    }
    //Esse teste verifica se o sistema impede corretamente a troca da senha para o mesmo valor já utilizado
    @Test
    void naoDeveAlterarParaMesmaSenha() {

        Conta conta = new Conta("gean", "123");

        assertFalse(conta.alterarSenha("123"));
    }

}