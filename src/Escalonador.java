import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class Escalonador {
	public static void main(String[] args) {
		Tabelas processos = new Tabelas();

		int quantum = 0;

		// Lê o arquivo do quantum e atribui a variavel
		File arqQt = new File("../programas/quantum.txt");

		try {
			Scanner ltrQt = new Scanner(arqQt);
			quantum = Integer.parseInt(ltrQt.nextLine());
				
		} catch(FileNotFoundException e) {
			System.out.println("Não foi possível ler o arquivo quantum.");
			e.printStackTrace();
		}
		
		// Cria o arquivo LogXX.txt 
		String pathLog = "../programas/";
		
		if(quantum < 10) {
			pathLog += "Log0" + quantum + ".txt";
		} else {
			pathLog += "Log" + quantum + ".txt";
		}
		
		File arqLog = new File(pathLog);
		
		try {
			arqLog.createNewFile();
		} catch(IOException e) {
			System.out.println("Não foi possível criar o arquivo LogXX");
			e.printStackTrace();
		}
		
		// Lê os processos nos arquivos e cria os BCPs apropriados
		for(int i = 1; i < 11; i++) {
			String path = "../programas/";
			
			if(i < 10) {
				path += "0" + i + ".txt";
			} else {
				path += i + ".txt";
			}

			File arq = new File(path);
			File arqPrio = new File("../programas/prioridades.txt");

			try {
				Scanner ltr = new Scanner(arq);

				BCP processo = new BCP(ltr.nextLine(), i);

				while(ltr.hasNextLine()) {
					processo.addCmd(ltr.nextLine() + "\n");
				}
				
				processos.addNovo(processo);

			} catch(FileNotFoundException e) {
				System.out.println("Não foi possível ler o arquivo " + i );
				e.printStackTrace();
			}
		}

		// Lẽ o arquivo prioridades e atribui as variaveis
		File arqPrio = new File("../programas/prioridades.txt");

		try {
			Scanner ltrPrio = new Scanner(arqPrio);

			int nmbProc = 1;

			while(ltrPrio.hasNextLine()) {
				processos.novaPrioridade(nmbProc, Integer.parseInt(ltrPrio.nextLine()));
				
				nmbProc++;
			}
		
		} catch(FileNotFoundException e) {
			System.out.println("Não foi possível ler o arquivo prioridades.");
			e.printStackTrace();
		}
		
		processos.sort();

		while(!processos.acabou()) {
			BCP atual = processos.executar();

			for(int i = 0; i < quantum; i++) {
				atual.upPC();

				if(atual.getCmd() == "E/S") {
					processos.bloquear(atual);
					processos.updateCreditos(atual);
					break;
				} else if(atual.getCmd() == "X=") {
					processos.novoX(atual);
				} else if(atual.getCmd() == "Y=") {
					processos.novoY(atual);
				} else if(atual.getCmd() == "SAIDA") {
					processos.terminou(atual);
					processos.updateCreditos(atual);
					break;
				}

				processos.updateCreditos(atual);
			}

			processos.downTimeout();
		}
	}
}
