package test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import model.actors.Conta;

import model.actors.Criador;

public class UsuarioTest {

    /**
     * Verifica se um usuário é criado corretamente com os valores
     * informados no construtor.
     */
	@Test
	void deveCriarUsuarioCorretamente() {
		
		Conta conta = new Conta("gean", "123");
		
		LocalDate aniversario = LocalDate.of(2000, 01, 01);
		
		Criador usuario = new Criador(conta, "Gean", "M", aniversario);
		
		assertEquals("Gean", usuario.getNome());
		assertEquals("M", usuario.getSexo());
		assertEquals(aniversario, usuario.getAniversario());
		
	}


    /**
     * Verifica se o método alterarNome() altera corretamente
     * o nome do usuário.
     */
	@Test
	void deveAlterarNome() {
		
		Conta conta = new Conta("gean", "123");
		
		LocalDate aniversario = LocalDate.of(2000, 01, 01);
		
		Criador usuario = new Criador(conta, "Gean", "M", aniversario);
		
		assertTrue(usuario.alterarNome("Jhey"));
		
	}

    /**
     * Verifica se o método calcularIdade() retorna corretamente
     * a idade do usuário com base em sua data de nascimento.
     */
	@Test
	void deveCalcularIdadeCorretamente() {
		
        Conta conta = new Conta("daniel", "123");
		
		LocalDate aniversario = LocalDate.of(2005, 10, 20);
		
		Criador usuario = new Criador(conta, "Daniel", "M", aniversario);
		
		assertEquals(20, usuario.calcularIdade());
	}

    /**
     * Verifica se o método alterarSexo() altera corretamente
     * o sexo do usuário.
     */
	@Test
	void deveAlterarSexo() {
		
        Conta conta = new Conta("maria", "123");
		
		LocalDate aniversario = LocalDate.of(2000, 01, 01);
		
		Criador usuario = new Criador(conta, "Maria", "M", aniversario);
		
		assertTrue(usuario.alterarSexo("F"));
		
	}
}
