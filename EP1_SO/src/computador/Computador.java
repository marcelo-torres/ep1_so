package computador;

import java.io.File;
import java.util.PriorityQueue;
import java.util.Scanner;

import computador.processador.Processador;
import sistema_operacional.SistemaOperacional;

public class Computador {

	private String diretorioPrincipal = "processos/";
	private String nomeDoArquivoDoQuantum = diretorioPrincipal + "quantum.txt";
	
	private Relogio relogio;
	private Processador processador;
	private SistemaOperacional sistemaOperacional;
	
	public Computador() {
		
		int quantum = -1;
		File arquivoDoQuantum = new File(nomeDoArquivoDoQuantum);
		 try (Scanner leitor = new Scanner(arquivoDoQuantum)) {
			quantum = leitor.nextInt();
		} catch (Exception e) {
			System.out.println("[!] - Erro ao ler o quantum.");
			System.out.println("\t" + e.getMessage());
			System.exit(1);
		}
		
		this.relogio = new Relogio();
		this.processador = new Processador(this.relogio);
		this.sistemaOperacional = new SistemaOperacional(quantum, relogio, processador);
	}
	
	
	public void ligar() {
		
		/**
		 * TODO perguntar para o professor quis sao os valores que os registradores
		 *  x e y podem assim, tipo 0 < x < 9, ou mais?
		 */
		
		try {
			this.sistemaOperacional.iniciarSistema();
		} catch(Exception e) {
			System.out.println("Nao foi possivel iniciar o sistema:");
			System.out.println("\t" + e.getMessage());
			System.exit(1);
		}
	}
}
