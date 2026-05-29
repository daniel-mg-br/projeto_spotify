package database;

import java.util.ArrayList;
import java.util.List;
import model.actors.Ouvinte;
import model.actors.Usuario;

// Classe DAO para manipulação de ouvintes no banco
public class OuvinteDAO {
	
	// Salvar um novo ouvinte no banco
		public boolean salvar(Ouvinte novoOuvinte) {
			if (novoOuvinte == null) {
				return false;
			}
			
			novoOuvinte.setId(BancoMemoria.contadorUsuario++);
			BancoMemoria.usuarios.add(novoOuvinte);
			return true;
		}
		
		// Buscar um ouvinte pelo ID por meio de Casting
		public Ouvinte buscarId(int id) {
			for (Usuario u : BancoMemoria.usuarios) {
				if (u.getId() == id) {
					if (u instanceof Ouvinte) {
						return (Ouvinte) u;
					}
				}
			}
			
			return null;
		}
		
		// Retornar a lista de ouvintes, usando Casting durante a iteração pela lista de Usuários
		public List <Ouvinte> listarOuvintes() {
			List <Ouvinte> ouvintes = new ArrayList<>();
			
			for (Usuario u : BancoMemoria.usuarios) {
				if (u instanceof Ouvinte) {
					ouvintes.add((Ouvinte) u);
				}
			}
			
			return ouvintes;
		}
		
		// Atualizar um ouvinte existente
		public boolean atualizar(Ouvinte ouvinteAtualizado) {
			Ouvinte ouvinteAntigo = buscarId(ouvinteAtualizado.getId());
			
			if (ouvinteAntigo != null) {
				int index = BancoMemoria.usuarios.indexOf(ouvinteAntigo);
				BancoMemoria.usuarios.set(index, ouvinteAtualizado);
				return true;
			}
			
			return false;
		}
		
		// Remover um ouvinte existente no banco
		public boolean deletar(int id) {
			Ouvinte ouvinteRemover = buscarId(id);
			
			if (ouvinteRemover != null) { 
				return BancoMemoria.usuarios.remove(ouvinteRemover);
			}
			
			return false;
		}
}
