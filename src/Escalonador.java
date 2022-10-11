import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class Escalonador {
	public static void main(String[] args) {
		ArrayList<BCP> bcpTabela = new ArrayList<>();
		int quantum = 0;

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
				Scanner ltrPrio = new Scanner(arqPrio);

				int linha = 0;
				while(ltr.hasNextLine()) {
					String data = ltr.nextLine();

					if(linha == 0) {
						for(int k = 1; k < i; k++) {
							ltrPrio.nextLine();
						}

						int valPrio = Integer.parseInt(ltrPrio.nextLine());

						BCP processo = new BCP(data, valPrio);

						bcpTabela.add(processo);
					}

					linha++;
				}
			} catch(FileNotFoundException e) {
				System.out.println("Não foi possível ler o arquivo " + i );
				e.printStackTrace();
			}
		}
		
		File arqQt = new File("../programas/quantum.txt");

		try {
			Scanner ltrQt = new Scanner(arqQt);
			quantum = Integer.parseInt(ltrQt.nextLine());
		
		} catch(FileNotFoundException e) {
			System.out.println("Não foi possível ler o arquivo quantum.");
			e.printStackTrace();
		}

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
	}
}
