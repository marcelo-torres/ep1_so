import computador.Computador;

public class Main {

	/**
	 * Realiza varios testes com o quantum variando de 1 ate 22, ignorando o
	 * quantum informado no arquivo
	 */
	public static void execucaoDeTesteDeQuantum() {
		
		int quantumMaximo = 22;
		
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
		
		execucaoDeTesteDeQuantum();
		//execucaoNormal();
		
	}
}
