package database;

import java.util.ArrayList;
import java.util.List;
import model.actors.Administrador;
import model.actors.Usuario;

// Classe DAO para manipulação do administrador no banco
public class AdministradorDAO {
	
	// Salvar novo administrador no banco (caso necessário)
	public boolean salvar(Administrador novoAdmin) {
		if (novoAdmin == null) {
			return false;
		}
		
		novoAdmin.setId(BancoMemoria.contadorUsuario++);
		BancoMemoria.usuarios.add(novoAdmin);
		return true;
	}
	
	// Buscar um administrador pelo ID por meio de Casting
	public Administrador buscarId(int id) {
		for (Usuario u : BancoMemoria.usuarios) {
			if (u.getId() == id) {
				if (u instanceof Administrador) {
					return (Administrador) u;
				}	
			}
		}
		
		return null;
	}
	
	// Retornar a lista de administradores do banco (por hora apenas 1)
	public List <Administrador> listarAdministradores() {
		List <Administrador> administradores = new ArrayList<>();
		
		for (Usuario u : BancoMemoria.usuarios) {
			if (u instanceof Administrador) {
				administradores.add((Administrador) u);
			}
		}
		
		return administradores;
	}
	
	// Atualizar um administrador existente
	public boolean atualizar(Administrador adminAtualizado) {
		Administrador adminAntigo = buscarId(adminAtualizado.getId());
		
		if (adminAntigo != null) {
			int index = BancoMemoria.usuarios.indexOf(adminAntigo);
			BancoMemoria.usuarios.set(index, adminAtualizado);
			return true;
		}
		
		return false;
	}
	
	// Remover um admin existente no banco
	public boolean deletar(int id) {
		Administrador adminRemover = buscarId(id);
		
		if (adminRemover != null) {
			return BancoMemoria.usuarios.remove(adminRemover);
		}
		
		return false;
	}
}
