package database;

import java.util.List;
import model.actors.Conta;

// Classe DAO para manipulação de contas dos usuários
public class ContaDAO {
	// Salvar uma conta nova no banco
	public boolean salvar(Conta novaConta) {
		if (novaConta == null) {
			return false;
		}
		
		novaConta.setId(BancoMemoria.contadorConta++);
		BancoMemoria.contas.add(novaConta);
		return true;
	}
	
	// Buscar um conta pelo ID
	public Conta buscarId(int id) {
		for (Conta c : BancoMemoria.contas) {
			if (c.getId() == id) {
				return c;
			}
		}
		
		return null;
	}
	
	// Buscar uma conta pelo login
	public Conta buscarLogin(String login) {
		for (Conta c : BancoMemoria.contas) {
			if (c.getLogin().equalsIgnoreCase(login)) {
				return c;
			}
		}
		
		return null;
	}
	
	// Retornar a lista de contas do banco
	public List <Conta> listarContas() {
		return BancoMemoria.contas;
	}
	
	// Atualizar uma conta já existente
	public boolean atualizar(Conta contaAtualizada) {
		Conta contaAntiga = buscarId(contaAtualizada.getId());
		
		if (contaAntiga != null) {
			int index =	BancoMemoria.contas.indexOf(contaAntiga);
			BancoMemoria.contas.set(index, contaAtualizada);
			return true;
		}
		
		return false;
	}
	
	// Remover uma conta do banco
	public boolean delete(int id) {
		Conta contaRemover = buscarId(id);
		
		if (contaRemover != null) {
			return BancoMemoria.contas.remove(contaRemover);
		}
		
		return false;
	}
}
