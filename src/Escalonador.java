import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Escalonador {
	public static void main(String[] args) {
		Tabelas processos = new Tabelas();

		int quantum = 0;

		// Lê o arquivo do quantum e atribui a variavel
		File arqQt = new File("../programas/quantum.txt");

		try {
			Scanner ltrQt = new Scanner(arqQt);
			quantum = Integer.parseInt(ltrQt.nextLine());
				
			ltrQt.close();
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

			try {
				Scanner ltr = new Scanner(arq);

				BCP processo = new BCP(ltr.nextLine(), i);

				int pos = 0;
				while(ltr.hasNextLine()) {
					processo.addCmd(ltr.nextLine(), pos);

					pos++;
				}
				
				processos.add(processo);

				try {
					FileWriter ecrt = new FileWriter(arqLog, true);
					ecrt.write("Carregando TESTE-" + i + "\n");
					ecrt.close();
				} catch(IOException e) {
					System.out.println("Não foi possível escrever no arquivo Log.");
					e.printStackTrace();
				}

				ltr.close();
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
		
			ltrPrio.close();
		} catch(FileNotFoundException e) {
			System.out.println("Não foi possível ler o arquivo prioridades.");
			e.printStackTrace();
		}
		
		processos.updateCreditos();
		processos.sort();

		while(!processos.acabou()) {
			BCP atual = processos.executar();
			try {
				FileWriter scrt = new FileWriter(arqLog, true);

				scrt.write("Executando TESTE-" + atual.getId() + "\n");

				scrt.close();
			} catch(IOException e) {
				System.out.println("Não foi possível escrever no arquivo Log.");
				e.printStackTrace();
			}

			atual.downCreditos();

			for(int i = 0; i < quantum; i++) {
				atual.upPC();

				String comando = atual.getCmd();

				if(comando == "E/S") {
					atual.setEstado(1);
					break;
				} else if(comando == "X=") {
					//atual.setRegX();
				} else if(comando == "Y=") {
					//atual.setRegY();
				} else if(comando == "SAIDA") {
					atual.setEstado(2);
					break;
				}
			}

			processos.add(atual);

			processos.updateCreditos();
			processos.downTimeout();
			processos.sort();
		}
	}
}
