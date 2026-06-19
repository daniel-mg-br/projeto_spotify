package test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import model.actors.Conta;

import model.actors.Criador;

public class UsuarioTest {

	//Deve testar se o método está criando usuário corretamente
	@Test
	void deveCriarUsuarioCorretamente() {
		
		Conta conta = new Conta("gean", "123");
		
		LocalDate aniversario = LocalDate.of(2000, 01, 01);
		
		Criador usuario = new Criador(conta, "Gean", "M", aniversario);
		
		assertEquals("Gean", usuario.getNome());
		assertEquals("M", usuario.getSexo());
		assertEquals(aniversario, usuario.getAniversario());
		
	}
	//Deve testar se o nome é alterado corretamente
	@Test
	void deveAlterarNome() {
		
		Conta conta = new Conta("gean", "123");
		
		LocalDate aniversario = LocalDate.of(2000, 01, 01);
		
		Criador usuario = new Criador(conta, "Gean", "M", aniversario);
		
		assertTrue(usuario.alterarNome("Jhey"));
		
	}
	//Este teste deve calcular a idade corretamente com base na data de nascimento
	@Test
	void deveCalcularIdadeCorretamente() {
		
        Conta conta = new Conta("daniel", "123");
		
		LocalDate aniversario = LocalDate.of(2005, 10, 20);
		
		Criador usuario = new Criador(conta, "Daniel", "M", aniversario);
		
		assertEquals(20, usuario.calcularIdade());
	}
	//Este teste deve testar se o sexo é alterado corretamente
	@Test
	void deveAlterarSexo() {
		
        Conta conta = new Conta("maria", "123");
		
		LocalDate aniversario = LocalDate.of(2000, 01, 01);
		
		Criador usuario = new Criador(conta, "Maria", "M", aniversario);
		
		assertTrue(usuario.alterarSexo("F"));
		
	}
}
