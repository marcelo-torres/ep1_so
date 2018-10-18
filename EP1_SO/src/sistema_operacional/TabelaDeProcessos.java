package sistema_operacional;

import java.util.PriorityQueue;

public class TabelaDeProcessos {

	private final int NUMERO_ESPACOS_DISPONIVEIS;
	
	private final BCP[] tabela;
	private final PriorityQueue<Integer> filaDeEspacosDisponiveis;
	
	public TabelaDeProcessos(int numeroDeEspacosDisponiveis) {
		this.NUMERO_ESPACOS_DISPONIVEIS = numeroDeEspacosDisponiveis;
		
		this.tabela = new BCP[numeroDeEspacosDisponiveis];
		this.filaDeEspacosDisponiveis = new PriorityQueue<Integer>();
		
		for(int indice = 0; indice < numeroDeEspacosDisponiveis; indice++) {
			this.filaDeEspacosDisponiveis.add(indice);
		}
	}
	
	
	public boolean inserirBcp(BCP bcp) {
		
		return false;
	}
	
	public BCP acessarBcpNoIndice(int indice) {
		
		return null;
	}
	
	public boolean liberarIndice(int indice) {
		
		return false;
	}
}
