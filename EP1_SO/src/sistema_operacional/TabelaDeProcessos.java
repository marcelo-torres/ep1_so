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
	
	
	public int inserirBcp(BCP bcp) {
		
		if(this.filaDeEspacosDisponiveis.size() == 0) {
			return -1;
		}
		
		int indice = this.filaDeEspacosDisponiveis.remove();
		this.tabela[indice] = bcp;
		
		return indice;
	}
	
	public BCP acessarBcpNoIndice(int indice) {
		
		if(indice < 0 || indice > this.tabela.length) {
			throw new IllegalArgumentException("Nao eh possivel acessar o indice "
					+ indice + " posicao invalida");
		}
		
		return this.tabela[indice];
	}
	
	public void liberarEntrada(BCP bcp) {
		
		if(bcp == null) {
			throw new IllegalArgumentException("Nao eh possivel acessar um elemento null");
		}
		
		for(int i = 0; i < this.tabela.length; i++) {
			if(this.tabela[i] == bcp) {
				this.tabela[i] = null;
				this.filaDeEspacosDisponiveis.add(i);
				break;
			}
		}
	}
	
	public void liberarEntrada(int indice) {
		
		if(indice < 0 || indice > this.tabela.length) {
			throw new IllegalArgumentException("Nao eh possivel acessar o indice "
					+ indice + " posicao invalida");
		}
		
		this.tabela[indice] = null;
		this.filaDeEspacosDisponiveis.add(indice);
	}
}
