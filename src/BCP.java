public class BCP {
    private String nome;
    private String memoria;
    private int prioridade;
    private int regX;
    private int regY;
    private int pc;

    public BCP(String nome, int prioridade) {
	this.nome = nome;
	this.prioridade = prioridade;
	this.memoria = "";
	this.regX = 0;
	this.regY = 0;
	this.pc = 0;
    }

    public void upPC() {
        this.pc++;
    }

    public void setRegX(int regX) {
        this.regX = regX;
    }

    public void setRegY(int regY) {
        this.regY = regY;
    }

    public String getNome() {
        return this.nome;
    }

    public String getMemoria() {
        return this.memoria;
    }

    public int getPrioridade() {
        return this.prioridade;
    }

    public int getRegX() {
        return this.regX;
    }

    public int getRegY() {
        return this.regY;
    }

    public int getPC() {
        return this.pc;
    }
}
