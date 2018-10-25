package sistema_operacional;

import java.util.PriorityQueue;

public class TabelaDeProcessos {
	
	protected final int NUMERO_ESPACOS_DISPONIVEIS;
	
	protected final BCP[] tabela;
	protected final PriorityQueue<Integer> filaDeEspacosDisponiveis;
	
	
	public TabelaDeProcessos(int numeroDeEspacosDisponiveis) {
		this.NUMERO_ESPACOS_DISPONIVEIS = numeroDeEspacosDisponiveis;
		
		this.tabela = new BCP[numeroDeEspacosDisponiveis];
		this.filaDeEspacosDisponiveis = new PriorityQueue<Integer>();
		
		for(int indice = 0; indice < numeroDeEspacosDisponiveis; indice++) {
			this.filaDeEspacosDisponiveis.add(indice);
		}
	}
}
