package sistema_operacional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Escalonador {
	
	private int quantidadeTotalDeProcessos;
	
	private PriorityQueue<FilaDePrioridade> filaDePronto;
	private int quantidadeDeProcessosProntos;
	private int quantidadeDeProcessosProntosSemCreditos;
	
	private LinkedList<BCP> filaDeBloqueado;
	
	
	public Escalonador(PriorityQueue<FilaDePrioridade> filaDePronto,
					   LinkedList<BCP> filaDeBloqueado) {
		
		this.filaDePronto = filaDePronto;
		this.filaDeBloqueado = filaDeBloqueado;
	}
	
	
	public BCP escalonar() {
		
		return null;
	}
	
	public boolean existeProcesso() {
		
		return false;
	}
	
	public void inserirNaFilaDeBloqueado(BCP bcp) {
		bcp.definirProcessoComoBloqueado();
	}
	
	
	public boolean inserirNaFilaDePronto(BCP bcp) {
		
		if(bcp == null) {
			return false;
		}
		
		bcp.definirProcessoComoPronto();
		
		FilaDePrioridade filaDeInsercao = null;
		Iterator<FilaDePrioridade> iterador = this.filaDePronto.iterator();
		
		while(iterador.hasNext()) {
			FilaDePrioridade fila = iterador.next();
			
			if(fila.creditos() <= bcp.creditosDoProcesso()) {
				if(fila.creditos() == bcp.creditosDoProcesso()) {
					filaDeInsercao = fila;
				}
				break;
			}
		}
		
		if(filaDeInsercao == null) {
			filaDeInsercao = new FilaDePrioridade(bcp.creditosDoProcesso());
			this.filaDePronto.add(filaDeInsercao);
		}
		
		filaDeInsercao.inserirNoFinal(bcp);
		this.quantidadeTotalDeProcessos++;
		this.quantidadeDeProcessosProntos++;
		if(bcp.creditosDoProcesso() == 0) {
			this.quantidadeDeProcessosProntosSemCreditos++;
		}
		
		return true;
	}
	
	/**
	 * Devolve o proximo BCP da estrutura com maior prioridade, ou null caso
	 * a estrutura esteja completamente vazia. Caso todos os processos armazenados
	 * nao possuam nenhum credito a fila funcionara por ordem de chegada.
	 * 
	 * @return proximo BCP da estrutura ou null caso nao haja proximo disponivel
	 */
	public BCP removerProximoDaFilaDePronto() {
		
		if(this.quantidadeDeProcessosProntos == 0) {
			return null;
		}
		
		FilaDePrioridade filaDePrioridade = this.filaDePronto.peek();
			
		// O proximo elemento eh o primeiro da da fila de prioridade mais alta
		BCP bcp = filaDePrioridade.removerPrimeiro();
		
		if(filaDePrioridade.vazia()) {
			// Se a fila de prioridade ficar vazia eh necessario remove-la
			// da fila de pronto
			this.filaDePronto.poll();
		}
		
		this.quantidadeTotalDeProcessos--;
		this.quantidadeDeProcessosProntos--;
		if(bcp.creditosDoProcesso() == 0) {
			this.quantidadeDeProcessosProntosSemCreditos--;
		}
		
		return bcp;	
	}
	
	public boolean redistribuirCreditos() {
		
		if(this.quantidadeDeProcessosProntos != this.quantidadeDeProcessosProntosSemCreditos) {
			return false;
		}
		
		FilaDePrioridade filaDePrioridade = this.filaDePronto.poll();
		
		while(filaDePrioridade.tamanho() > 0) {
			BCP bcp = filaDePrioridade.removerPrimeiro();
			int prioridade = bcp.prioridadeDoProcesso();
			bcp.definirCreditosDoProcesso(prioridade);
			this.inserirNaFilaDePronto(bcp);
		}
		
		return true;
	}
	
	
}
