package sistema_operacional;

import java.util.Collections;
import java.util.LinkedList;

public class Escalonador {
	
	protected SistemaOperacional sistemaOperacional;
	protected FilaDePrioridade[] filaDePronto;
	protected LinkedList<BCP> filaDeBloqueado;
	
	
	
	protected Escalonador(SistemaOperacional sistemaOperacional,
					   FilaDePrioridade[] filaDePronto,
					   LinkedList<BCP> filaDeBloqueado) {
		
		this.sistemaOperacional = sistemaOperacional;
		this.filaDePronto = filaDePronto;
		this.filaDeBloqueado = filaDeBloqueado;
	}
	
	
	
	protected BCP escalonar() {
		return this.removerProximoDaFilaDePronto();
	}
	
	
	
	/**
	 * Devolve a fila de prioridade da fila de pronto correspondente ao bcp
	 * passado por parametro
	 */
	protected FilaDePrioridade filaDePrioridadeCorrespondente(BCP bcp) {
		
		if(bcp.creditosDoProcesso >= this.filaDePronto.length) {
			throw new IllegalArgumentException("O processo possui mais creditos do que o valor maximo da fila");
		}
		
		int indiceDaFilaDeInsercao = bcp.creditosDoProcesso;
		return this.filaDePronto[indiceDaFilaDeInsercao];
	}
	
	
	/**
	 * Insere na fila de pronto usando como criterio para a ordenacao em cada
	 * fila de prioridade o numero do arquivo usado para criar o bcp
	 */
	protected void inserirOrdenadoNaFilaDePronto(BCP bcp) {
		
		FilaDePrioridade filaDeInsercao = filaDePrioridadeCorrespondente(bcp);
		filaDeInsercao.fila.addLast(bcp);
		Collections.sort(filaDeInsercao.fila);
		
		this.sistemaOperacional.quantidadeTotalDeProcessos++;
		this.sistemaOperacional.quantidadeDeProcessosProntos++;
		if(bcp.creditosDoProcesso == 0) {
			this.sistemaOperacional.quantidadeDeProcessosProntosSemCreditos++;
		}
	}
	
	protected boolean inserirNaFilaDePronto(BCP bcp,  boolean insercaoNaFrente) {
		
		bcp.estadoDoProcesso = EstadosDeProcesso.PRONTO;
		FilaDePrioridade filaDeInsercao = filaDePrioridadeCorrespondente(bcp);
		
		if(insercaoNaFrente) {
			filaDeInsercao.fila.addFirst(bcp);
		} else {
			filaDeInsercao.inserirNoFinal(bcp);
		}
		this.sistemaOperacional.quantidadeTotalDeProcessos++;
		this.sistemaOperacional.quantidadeDeProcessosProntos++;
		if(bcp.creditosDoProcesso == 0) {
			this.sistemaOperacional.quantidadeDeProcessosProntosSemCreditos++;
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
	protected BCP removerProximoDaFilaDePronto() {
		
		if(this.sistemaOperacional.quantidadeDeProcessosProntos == 0) {
			return null;
		}
		
		FilaDePrioridade filaDeRemocao = null;
		
		int indiceDaFila = this.filaDePronto.length - 1;
		while(filaDeRemocao == null && indiceDaFila >= 0) {
			FilaDePrioridade fila = this.filaDePronto[indiceDaFila];
			if(!fila.vazia()) {
				filaDeRemocao = fila;
			} else {
				indiceDaFila--;
			}
		}
		
		// O proximo elemento eh o primeiro da fila de prioridade mais alta
		BCP bcp = filaDeRemocao.removerPrimeiro();
		
		this.sistemaOperacional.quantidadeTotalDeProcessos--;
		this.sistemaOperacional.quantidadeDeProcessosProntos--;
		if(bcp.creditosDoProcesso == 0) {
			this.sistemaOperacional.quantidadeDeProcessosProntosSemCreditos--;
		}
		
		return bcp;	
	}
	
	protected boolean necessarioRedistribuirCreditos() {
		return this.sistemaOperacional.quantidadeTotalDeProcessos == this.sistemaOperacional.quantidadeDeProcessosProntosSemCreditos;
	}
	
	
	protected void inserirNaFilaDeBloqueado(BCP bcp) {
		
		bcp.estadoDoProcesso = EstadosDeProcesso.BLOQUEADO;
		
		bcp.tempoDeEspera = this.sistemaOperacional.TEMPO_DE_ESPERA;
		this.filaDeBloqueado.addLast(bcp);
		
		this.sistemaOperacional.quantidadeTotalDeProcessos++;
	}
	
	protected void decrementarFilaDeBloqueado() {
		
		for(BCP bcp : this.filaDeBloqueado) {
			bcp.tempoDeEspera--;
		}
	}
	
	protected LinkedList<BCP> obterListaDeDesbloqueados() {
		
		LinkedList<BCP> listaDeDesbloqueados = new LinkedList<BCP>();
		
		while(this.filaDeBloqueado.size() > 0) {
			BCP bcp = this.filaDeBloqueado.peekFirst();
			
			if(bcp.tempoDeEspera == 0) {
				this.filaDeBloqueado.removeFirst();
				listaDeDesbloqueados.addLast(bcp);
				this.sistemaOperacional.quantidadeTotalDeProcessos--;
			} else {
				break;
			}
		}
		
		return listaDeDesbloqueados;
	}
}