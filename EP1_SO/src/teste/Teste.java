package teste;

import computador.Relogio;
import computador.processador.Processador;
import sistema_operacional.Escalonador;

public class Teste {

	public static void exibirLogDeAcerto(String mensagem) {
		System.out.println("OK\t\t\t" + mensagem);
	}
	
	public static void exibirLogDeErro(String mensagem) {
		System.out.println("ERRO\t\t\t" + mensagem);
	}
	
	public static void main(String [] args) {
		
		int numeroDeProblemas = 0;
		
		numeroDeProblemas = Processador.testar()
						  + Relogio.testar()
						  + Escalonador.testar();
		
		System.out.println("------------------ Fim de testes ------------------");
		System.out.println("\n\tQuantidade de erros: " + numeroDeProblemas);
	}
	
}
