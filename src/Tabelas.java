import java.util.ArrayList;
import java.util.Collections;

public class Tabelas {
	private ArrayList<BCP> todos;
	private ArrayList<BCP> prontos;
	private ArrayList<BCP> bloqueados;

	public Tabelas() {
		todos      = new ArrayList<>();
		prontos    = new ArrayList<>();
		bloqueados = new ArrayList<>();
	}

	// Adiciona um novo processo as filas, dependendo do Estado atual.
	public void add(BCP processo) {
		if(!todos.contains(processo)) {
			todos.add(processo);
		}

		if(processo.getEstado() == 0) {
			prontos.add(processo);
		} else if(processo.getEstado() == 1) {
			bloqueados.add(processo);
		}
	}

	// Reduz o timeoutBloqueado de todos os elementos da tabela bloqueados.
	// Se o timeoutBloqueado chegar a 0, retira da fila de bloqueados e adiciona ao de prontos.
	public void downTimeout() {
		for(BCP atual : bloqueados) {
			atual.setTimeout(atual.getTimeout() - 1);
		}

		for(int i = 0; i < bloqueados.size(); i++) {
			BCP atual = bloqueados.get(i);
			if(atual.getTimeout() <= 0) {
				bloqueados.remove(atual);
				atual.setEstado(0);
				prontos.add(atual);
			}
		}
	}

	// Remove o primeito elemento da lista prontos e muda e estado para executando.
	public BCP executar() {
		BCP rtn = prontos.get(0);
		prontos.remove(0);

		todos.get(todos.indexOf(rtn)).setEstado(3);
		rtn.setEstado(3);

		return rtn;
	}

	// Verifica se os creditos de todos os processos esta zerado.
	// Caso verdade atualiza os creditos de todos.
	public void updateCreditos() {
		boolean zerado = true;

		for(BCP atual : todos) {
			if(atual.getCreditos() != 0) {
				zerado = false;
			}
		}

		if(zerado) {
			for(BCP atual : todos) {
				atual.setCreditos();
			}

			for(BCP atual : prontos) {
				atual.setCreditos();
			}

			for(BCP atual : bloqueados) {
				atual.setCreditos();
			}
		}
	}

	// Atualiza a prioridade de um processo com o id expecificado
	public void novaPrioridade(int id, int prioridade) {
		for(BCP alvo : todos) {
			if(alvo.getId() == id) {
				alvo.setPrioridade(prioridade);
				
				int index = prontos.indexOf(alvo);
				prontos.get(index).setPrioridade(prioridade);
			}
		}
	}

	// Rearranja as listas
	public void sort() {
		Collections.sort(todos);
		Collections.sort(prontos);
		Collections.sort(bloqueados);
	}

	// Retorna verdade caso todo os processo tiverem terminado
	public boolean acabou() {
		for(BCP alvo : todos) {
			if(alvo.getEstado() != 2) {
				return false;
			}
		}

		return true;
	}

	public boolean espera() {
		if (prontos.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}
