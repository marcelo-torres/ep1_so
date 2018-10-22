package teste;

import computador.Relogio;
import computador.processador.Processador;
import sistema_operacional.Escalonador;

import java.util.PriorityQueue;

public class Teste {
	
	public static void exibirLogDeAcerto(String mensagem) {
		System.out.println("OK\t\t\t" + mensagem);
	}
	
	public static void exibirLogDeErro(String mensagem) {
		System.out.println("ERRO\t\t\t" + mensagem);
	}
	
	public static void main(String [] args) {
		
		PriorityQueue<Integer> lista = new PriorityQueue(20);
		
		int[] elementos = {1, 2, 3, 4, 5, 0};
		for(int e : elementos) lista.offer(e);
		for(int e : lista) System.out.print(e + "--> ");
		
		int numeroDeProblemas = 0;
		
		/*numeroDeProblemas = Processador.testar()
						  + Relogio.testar()
						  + Escalonador.testar();
		
		System.out.println("------------------ Fim de testes ------------------");
		System.out.println("\n\tQuantidade de erros: " + numeroDeProblemas);*/
	}
	
}
