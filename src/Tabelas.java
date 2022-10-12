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

	public void addNovo(BCP processo) {
		todos.add(processo);

		if(processo.getEstado() == 0) {
			prontos.add(processo);
		}
	}

	public void terminou(BCP atual) {
		int indexTodos = todos.indexOf(atual);
		int indexProntos = prontos.indexOf(atual);

		todos.get(indexTodos).setEstado(2);
		prontos.remove(indexProntos);
	}

	public void novoX(BCP atual) {
		int novoX = atual.getXCmd();

		todos.get(todos.indexOf(atual)).setRegX(novoX);
	}

	public void novoY(BCP atual) {
		int novoY = atual.getYCmd();

		todos.get(todos.indexOf(atual)).setRegY(novoY);
	}

	public void novaPrioridade(int id, int prioridade) {
		for(BCP alvo : todos) {
			if(alvo.getId() == id) {
				alvo.setPrioridade(prioridade);
				
				int index = prontos.indexOf(alvo);
				prontos.get(index).setPrioridade(prioridade);
			}
		}
	}

	public void sort() {
		Collections.sort(todos);
		Collections.sort(prontos);
		Collections.sort(bloqueados);
	}

	public boolean acabou() {
		for(BCP alvo : todos) {
			if(alvo.getEstado() != 2) {
				return false;
			}
		}

		return true;
	}
}
