public class BCP implements Comparable<BCP>{
	private String nome;
	private String comandos;
	
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
	this.comandos = "";
	this.timeoutBloqueado = 0;

	this.creditos = 0;
    }

	public void upPC() {
		this.pc++;
	}

    public void addCmd(String cmd) {
    	comandos += cmd;
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

	// A fazer
	public String getCmd() {
		return "";
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

    public int compareTo(BCP comparado) {
    	return comparado.getPrioridade() - this.getPrioridade();
    }
}
