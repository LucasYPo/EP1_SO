import java.util.ArrayList;

public class BCP implements Comparable<BCP>{
	private String nome;
	private ArrayList<String> comandos;
	
	private int pc;
	private int estado;
	private int prioridade;
	private int creditos;

	private int regX;
	private int regY;

	private int timeoutBloqueado;
	private int id;

    public BCP(String nome, int id) {
	this.nome = nome;
	this.prioridade = 0;
	this.id = id;

	this.pc = 0;
	this.regX = 0;
	this.regY = 0;

	this.estado = 0;
	this.comandos = new ArrayList<>();
	this.timeoutBloqueado = 0;

	this.creditos = 0;
    }

	public void upPC() {
		this.pc++;
	}

    public void addCmd(String cmd, int pos) {
    	comandos.add(pos, cmd);
    }

	public void downCreditos() {
		this.creditos--;
	}

	public void setCreditos() {
		this.creditos = this.prioridade;
	}

    public void setEstado(int estado) {
    	this.estado = estado;
    }

    public void setPrioridade(int prioridade) {
    	this.prioridade = prioridade;
    }

	public void setRegX(int novoX) {
		this.regX = novoX;
	}

	public void setRegY(int novoY) {
		this.regY = novoY;
	}

	public void setTimeout(int quantum) {
		this.timeoutBloqueado = quantum;
	}

	// A fazer
	public String getCmd() {
		String rtn = comandos.get(this.pc);
		return rtn;
	}

	public int getCreditos() {
		return this.creditos;
	}

    public int getEstado() {
    	return this.estado;
    }

    public int getPrioridade() {
    	return this.prioridade;
    }

    public int getId() {
    	return this.id;
    }

	public int getTimeout() {
		return this.timeoutBloqueado;
	}

	public int getRegX() {
		return this.regX;
	}

	public int getRegY() {
		return this.regY;
	}

	public String getNome() {
		return this.nome;
	}

    public int compareTo(BCP comparado) {
    	return comparado.getCreditos() - this.getCreditos();
    }
}
