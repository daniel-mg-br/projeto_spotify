package database;

import java.util.ArrayList;
import java.util.List;
import model.actors.Criador;
import model.actors.Usuario;

// Classe DAO para manipulação de criadores no banco
public class CriadorDAO {
	
	// Salvar um novo criador no banco
	public boolean salvar(Criador novoCriador) {
		if (novoCriador == null) {
			return false;
		}
		
		novoCriador.setId(BancoMemoria.contadorUsuario++);
		BancoMemoria.usuarios.add(novoCriador);
		return true;
	}
	
	// Buscar um criador pelo ID por meio de Casting
	public Criador buscarId(int id) {
		for (Usuario u : BancoMemoria.usuarios) {
			if (u.getId() == id) {
				if (u instanceof Criador) {
					return (Criador) u;
				}
			}
		}
		
		return null;
	}
	
	// Retornar a lista de criadores, usando Casting durante a iteração pela lista de Usuários
	public List <Criador> listarCriadores() {
		List <Criador> criadores = new ArrayList<>();
		
		for (Usuario u : BancoMemoria.usuarios) {
			if (u instanceof Criador) {
				criadores.add((Criador) u);
			}
		}
		
		return criadores;
	}
	
	// Atualizar um criador existente
	public boolean atualizar(Criador criadorAtualizado) {
		Criador criadorAntigo = buscarId(criadorAtualizado.getId());
		
		if (criadorAntigo != null) {
			int index = BancoMemoria.usuarios.indexOf(criadorAntigo);
			BancoMemoria.usuarios.set(index, criadorAtualizado);
			return true;
		}
		
		return false;
	}
	
	// Remover um criador existente no banco
	public boolean deletar(int id) {
		Criador criadorRemover = buscarId(id);
		
		if (criadorRemover != null) { 
			return BancoMemoria.usuarios.remove(criadorRemover);
		}
		
		return false;
	}
}
