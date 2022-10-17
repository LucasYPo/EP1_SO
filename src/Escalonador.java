import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Scanner;

public class Escalonador {
	public static void main(String[] args) {
		Tabelas processos = new Tabelas();

		float mediaTrocas = 0;
		float mediaInstru = 0;

		float qntInstru = 0;
		float qntInterrup = 0;
		float qntTrocas = 0;
		float tempo = 0;

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

		try {
			FileWriter ecrt = new FileWriter(arqLog, true);
			for(BCP atual : processos.getTodos()) {
				ecrt.write("Carregando " + atual.getNome() + "\n");
			}
			ecrt.close();
		} catch(IOException e) {
			System.out.println("Não foi possível escrever no arquivo Log.");
			e.printStackTrace();
		}

		while(!processos.acabou()) {
			if(processos.espera()) {
				processos.downTimeout();
				tempo++;
				continue;
			}
			BCP atual = processos.executar();
			qntInstru = 0;
			try {
				FileWriter scrt = new FileWriter(arqLog, true);

				scrt.write("Executando" + atual.getNome() + "\n");

				scrt.close();
			} catch(IOException e) {
				System.out.println("Não foi possível escrever no arquivo Log.");
				e.printStackTrace();
			}

			atual.downCreditos();

			for (int i = 0; i < quantum; i++) {
				qntInstru++;
				tempo++;
				String comando = atual.getCmd();
				int len = comando.length();
		
				Reader reader = new StringReader(comando);
		
				char[] instrucao = new char[len];
		
				try {
					reader.read(instrucao);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				//System.out.println("Linha " + i + ": " + instrucao[0] + " Alvo: " + atual.getId());
		
				atual.upPC();

				if (instrucao[0] == 'E') {
					qntTrocas++;
					mediaInstru += qntInstru;
					qntInterrup++;
				  try {
					FileWriter scrt = new FileWriter(arqLog, true);
					
					scrt.write("E/S iniciada em TESTE-" + atual.getId() + "\n");
					scrt.write("Interrompendo TESTE-" + atual.getId() + " após " + (i + 1) + " instruções\n");
		
					scrt.close();
				  } catch (IOException e) {
					System.out.println("Não foi possivel escrever no arquivo Log.");
					e.printStackTrace();
				  }
		
				  atual.setEstado(1);
				  atual.setTimeout(quantum);
				  break;
				} else if (instrucao[0] == 'X') {
				  String sValX;
				  StringBuilder sb = new StringBuilder();

				  int count = 0;
				  for(char a : instrucao) {
					if(count >= 2) {
						sb.append(a);
					}
					count++;
				  }

				  sValX = sb.toString();

				  int valX = Integer.parseInt(sValX);

				  atual.setRegX(valX);

				  atual.setEstado(0);
				} else if (instrucao[0] == 'Y') {
					String sValY;
					StringBuilder sb = new StringBuilder();
  
					int count = 0;
					for(char a : instrucao) {
					  if(count >= 2) {
						  sb.append(a);
					  }
					  count++;
					}

					sValY = sb.toString();
  
					int valY = Integer.parseInt(sValY);
  
					atual.setRegY(valY);

					atual.setEstado(0);
				} else if (instrucao[0] == 'S') {
					qntTrocas++;
					mediaInstru += qntInstru;
					qntInterrup++;
				  	try{
						FileWriter ecrt = new FileWriter(arqLog, true);
						
						ecrt.write(atual.getNome() + " terminado. X=" + atual.getRegX() + ". Y=" + atual.getRegY() + "\n");

						ecrt.close();
					} catch(IOException e) {

					}
					atual.setEstado(2);
				  	break;
				} else {
					atual.setEstado(0);
				}
			  }

			  if(atual.getEstado() == 0) {
				  qntTrocas++;
				  mediaInstru += qntInstru;
				  qntInterrup++;
				try{
					FileWriter ecrt = new FileWriter(arqLog, true);

					ecrt.write("Interrompendo " + atual.getNome() + " após " + quantum + " instruções\n");

					ecrt.close();
				} catch(IOException e) {
					System.out.println("Nao foi possível escrever no arquivo Log.");
					e.printStackTrace();
				}
			  }

			  processos.downTimeout();
			  processos.add(atual);
		
			  processos.updateCreditos();
			  processos.sort();
		}

		mediaTrocas = qntTrocas / tempo;
		mediaInstru = mediaInstru / qntInterrup;

		System.out.println("A media de trocas é " + mediaTrocas);
		System.out.println("A media de instruções é: " + mediaInstru);
	}
}
