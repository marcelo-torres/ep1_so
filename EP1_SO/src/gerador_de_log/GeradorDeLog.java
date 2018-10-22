package gerador_de_log;

import sistema_operacional.BCP;
import sistema_operacional.FilaDePrioridade;

import java.util.Iterator;

public class GeradorDeLog {

	public static void exibirMensagemDeCarregamento(FilaDePrioridade[] filaDePronto) {
		
		for(int prioridade = filaDePronto.length - 1; prioridade >= 0; prioridade--) {
			
			FilaDePrioridade filaDePrioridade = filaDePronto[prioridade];
			Iterator<BCP> iterador = filaDePrioridade.iterador();
			
			while(iterador.hasNext()) {
				BCP bcp = iterador.next();
				System.out.println("Carregando " + bcp.nomeDoProcesso());
			}
		}
		
	}
	
	public static void exibirMensagemDeInterrupcao(
			String nomeDoProcesso,
			int quantidadeDeCiclosExecutados) {
		
		if(quantidadeDeCiclosExecutados == 1) {
			System.out.println("Interrompendo " + nomeDoProcesso
			+ " após " + quantidadeDeCiclosExecutados + " instrução.");
		} else {
			System.out.println("Interrompendo " + nomeDoProcesso
			+ " após " + quantidadeDeCiclosExecutados + " instruções.");
		}
	}
	
	public static void exibirMensagemDeFimDeExecucao(BCP bcp) {
		System.out.println(bcp.nomeDoProcesso() + " terminado. "
						   + "X=" + bcp.valorDoRegistradorX()
						   + ". Y=" + bcp.valorDoRegistradorY());
	}
	
}
