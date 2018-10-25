package computador;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import computador.processador.Processador;
import sistema_operacional.SistemaOperacional;

public class Computador {

	private String diretorioPrincipal = "processos/";
	private String nomeDoArquivoDoQuantum = diretorioPrincipal + "quantum.txt";
	
	private Relogio relogio;
	private Processador processador;
	private SistemaOperacional sistemaOperacional;
	
	
	
	public Computador() throws Exception {
		int quantum = this.obterQuantum(this.nomeDoArquivoDoQuantum);
		this.relogio = new Relogio();
		this.processador = new Processador(this.relogio);
		this.sistemaOperacional = new SistemaOperacional(diretorioPrincipal, quantum, relogio, processador);
	}
	
	public Computador(int quantum) throws Exception {
		this.relogio = new Relogio();
		this.processador = new Processador(this.relogio);
		this.sistemaOperacional = new SistemaOperacional(diretorioPrincipal, quantum, relogio, processador);
	}
	
	
	
	private int obterQuantum(String nomeDoArquivoDoQuantum) throws Exception {
		int quantum = -1;
		File arquivoDoQuantum = new File(nomeDoArquivoDoQuantum);
		 try (Scanner leitor = new Scanner(arquivoDoQuantum)) {
			quantum = leitor.nextInt();
		} catch (FileNotFoundException fnfe) {
			throw new Exception("[!] - Erro ao ler o quantum: " + fnfe.getMessage());
		}
		
		return quantum;
	}
	
	/**
	 * Liga o computador, isto eh, inicia o sistema operacional
	 */
	public void ligar() {
		
		try {
			this.sistemaOperacional.iniciarSistema();
		} catch(Exception e) {
			System.out.println("Nao foi possivel iniciar o sistema:");
			System.out.println("\t" + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}
}
