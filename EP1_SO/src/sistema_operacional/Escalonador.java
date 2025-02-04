package sistema_operacional;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Sistemas Operacionais - Professor Clodoaldo - 2 SEM de 2018 - Turma 04
 * 
 * Ana Paula Silva de Souza - nUSP: 10391225
 * Bianca Lima Santos - nUSP: 10346811
 * Marcelo Torres do Ó - nUSP 10414571
 * Mariana Silva Santana - nUSP: 10258897
 * 
 */

/*
 * Responsavel por gerenciar as filas e definir qual processo sera o proximo a
 * ser executado.
 */

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
	protected FilaDePrioridade filaDePrioridadeCorrespondente(int creditos) {
		
		if(creditos >= this.filaDePronto.length) {
			throw new IllegalArgumentException("O processo possui mais creditos do que o valor maximo da fila");
		}
		
		int indiceDaFilaDeInsercao = creditos;
		return this.filaDePronto[indiceDaFilaDeInsercao];
	}
	
	
	/**
	 * Insere na fila de pronto usando como criterio para a ordenacao em cada
	 * fila de prioridade o numero do arquivo usado para criar o bcp
	 */
	protected void inserirOrdenadoNaFilaDePronto(BCP bcp) {
		
		FilaDePrioridade filaDeInsercao = filaDePrioridadeCorrespondente(bcp.creditosDoProcesso);
		filaDeInsercao.fila.addLast(bcp);
		Collections.sort(filaDeInsercao.fila);
		
		this.sistemaOperacional.quantidadeTotalDeProcessos++;
		this.sistemaOperacional.quantidadeDeProcessosProntos++;
		if(bcp.creditosDoProcesso == 0) {
			this.sistemaOperacional.quantidadeDeProcessosSemCreditos++;
		}
	}
	
	protected void inserirNaFilaDePronto(BCP bcp) {
		
		bcp.estadoDoProcesso = EstadosDeProcesso.PRONTO;
		FilaDePrioridade filaDeInsercao = filaDePrioridadeCorrespondente(bcp.creditosDoProcesso);
		
		if(bcp.creditosDoProcesso != 0) {
			filaDeInsercao.fila.addFirst(bcp);
		} else {
			filaDeInsercao.fila.addLast(bcp);
			this.sistemaOperacional.quantidadeDeProcessosSemCreditos++;
		}
		
		this.sistemaOperacional.quantidadeTotalDeProcessos++;
		this.sistemaOperacional.quantidadeDeProcessosProntos++;
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
			this.sistemaOperacional.quantidadeDeProcessosSemCreditos--;
		}
		
		return bcp;	
	}
	
	protected void inserirNaFilaDeBloqueado(BCP bcp) {
		
		bcp.estadoDoProcesso = EstadosDeProcesso.BLOQUEADO;
		
		bcp.tempoDeEspera = this.sistemaOperacional.TEMPO_DE_ESPERA;
		this.filaDeBloqueado.addLast(bcp);
		
		if(bcp.creditosDoProcesso == 0) {
			this.sistemaOperacional.quantidadeDeProcessosSemCreditos++;
		}
		
		this.sistemaOperacional.quantidadeTotalDeProcessos++;
	}
	
	protected LinkedList<BCP> obterListaDeDesbloqueados() {
		
		LinkedList<BCP> listaDeDesbloqueados = new LinkedList<BCP>();
		
		while(this.filaDeBloqueado.size() > 0) {
			BCP bcp = this.filaDeBloqueado.peekFirst();
			
			if(bcp.tempoDeEspera == 0) {
				this.filaDeBloqueado.removeFirst();
				listaDeDesbloqueados.addLast(bcp);
				this.sistemaOperacional.quantidadeTotalDeProcessos--;
				
				if(bcp.creditosDoProcesso == 0) {
					this.sistemaOperacional.quantidadeDeProcessosSemCreditos--;
				}
			} else {
				break;
			}
		}
		
		return listaDeDesbloqueados;
	}
}