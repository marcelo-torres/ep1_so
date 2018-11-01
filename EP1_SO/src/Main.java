import java.io.File;

import computador.Computador;

/**
 * Sistemas Operacionais - Professor Clodoaldo - 2 SEM de 2018 - Turma 04
 * 
 * Ana Paula Silva de Souza - nUSP: 10391225
 * Bianca Lima Santos - nUSP: 10346811
 * Marcelo Torres do Ó - nUSP 10414571
 * Mariana Silva Santana - nUSP: 10258897
 * 
 */

public class Main {

	/**
	 * Realiza varios testes com o quantum variando de 1 ate 22, ignorando o
	 * quantum informado no arquivo
	 */
	public static void execucaoDeTesteDeQuantum() {
		
		int quantumMaximo = 25;
		
		for(int quantum = 1; quantum <= quantumMaximo; quantum++) {
			try {
				Computador computador = new Computador(quantum);
				computador.ligar();
			} catch(Exception e) {
				System.out.println("Erro: " + e.getMessage());
				System.exit(1);
			}
		}
	}
	
	/*
	 * Executa normalmente, isto eh, le o quantum do arquivo.
	 */
	public static void execucaoNormal() {
		try {
			Computador computador = new Computador();
			computador.ligar();
		} catch(Exception e) {
			System.out.println("Erro: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
	
		try {
			File diretorioDeLogs = new File("logs/");
			diretorioDeLogs.mkdir();
		} catch(Exception e) {
			System.out.println("Nao foi possivel salvar o diretorio de logs");
		}
		
		execucaoDeTesteDeQuantum();
		//execucaoNormal();
		
	}
}
