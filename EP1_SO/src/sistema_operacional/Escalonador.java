package sistema_operacional;

import java.util.Collections;
import java.util.LinkedList;

public class Escalonador {
	
	private final int TEMPO_DE_ESPERA;
	private int quantidadeTotalDeProcessos;
	
	private FilaDePrioridade[] filaDePronto;
	public int quantidadeDeProcessosProntos;
	private int quantidadeDeProcessosProntosSemCreditos;
	
	private LinkedList<BCP> filaDeBloqueado;
	
	
	public Escalonador(FilaDePrioridade[] filaDePronto,
					   LinkedList<BCP> filaDeBloqueado,
					   int tempoDeEspera) {
		
		this.filaDePronto = filaDePronto;
		this.filaDeBloqueado = filaDeBloqueado;
		this.TEMPO_DE_ESPERA = tempoDeEspera;
	}
	
	
	public BCP escalonar() {
		return this.removerProximoDaFilaDePronto();
	}
	
	public boolean existeProcesso() {
		return this.quantidadeTotalDeProcessos > 0;
	}
	
	
	private FilaDePrioridade filaDePrioridadeCorrespondente(BCP bcp) {
		
		if(bcp.creditosDoProcesso >= this.filaDePronto.length) {
			throw new IllegalArgumentException("O processo possui mais creditos do que o valor maximo da fila");
		}
		
		int indiceDaFilaDeInsercao = bcp.creditosDoProcesso;
		return this.filaDePronto[indiceDaFilaDeInsercao];
	}
	
	public void inserirOrdenado(BCP bcp) {
		
		FilaDePrioridade filaDeInsercao = filaDePrioridadeCorrespondente(bcp);
		filaDeInsercao.fila.addLast(bcp);
		Collections.sort(filaDeInsercao.fila);
		
		this.quantidadeTotalDeProcessos++;
		this.quantidadeDeProcessosProntos++;
		if(bcp.creditosDoProcesso == 0) {
			this.quantidadeDeProcessosProntosSemCreditos++;
		}
	}
	
	public boolean inserirNaFilaDePronto(BCP bcp,  boolean insercaoNaFrente) {
		
		bcp.estadoDoProcesso = EstadosDeProcesso.PRONTO;
		FilaDePrioridade filaDeInsercao = filaDePrioridadeCorrespondente(bcp);
		
		if(insercaoNaFrente) {
			filaDeInsercao.fila.addFirst(bcp);
		} else {
			filaDeInsercao.inserirNoFinal(bcp);
		}
		this.quantidadeTotalDeProcessos++;
		this.quantidadeDeProcessosProntos++;
		if(bcp.creditosDoProcesso == 0) {
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
		
		this.quantidadeTotalDeProcessos--;
		this.quantidadeDeProcessosProntos--;
		if(bcp.creditosDoProcesso == 0) {
			this.quantidadeDeProcessosProntosSemCreditos--;
		}
		
		return bcp;	
	}
	
	public boolean necessarioRedistribuirCreditos() {
		return this.quantidadeTotalDeProcessos == this.quantidadeDeProcessosProntosSemCreditos;
	}
	
	public boolean redistribuirCreditos() {
		
		if(this.quantidadeTotalDeProcessos != this.quantidadeDeProcessosProntosSemCreditos) {
			return false;
		}
		
		FilaDePrioridade filaDePrioridade = this.filaDePronto[0];
		
		while(filaDePrioridade.tamanho() > 0) {
			BCP bcp = filaDePrioridade.removerPrimeiro();
			this.quantidadeDeProcessosProntosSemCreditos--;
			//this.quantidadeDeProcessosProntos--;
			//this.quantidadeTotalDeProcessos--;
			
			int prioridade = bcp.prioridadeDoProcesso;
			
			FilaDePrioridade filaDePrioridadeCorrespondente = this.filaDePrioridadeCorrespondente(bcp);
			
			bcp.creditosDoProcesso = prioridade;
			filaDePrioridadeCorrespondente.inserirOrdenado(bcp);
		}
		
		return true;
	}
	
	
	
	
	public void inserirNaFilaDeBloqueado(BCP bcp) {
		
		bcp.estadoDoProcesso = EstadosDeProcesso.BLOQUEADO;
		
		bcp.tempoDeEspera = this.TEMPO_DE_ESPERA;
		this.filaDeBloqueado.addLast(bcp);
		
		this.quantidadeTotalDeProcessos++;
	}
	
	public void decrementarFilaDeBloqueado() {
		
		for(BCP bcp : this.filaDeBloqueado) {
			bcp.tempoDeEspera--;
		}
	}
	
	public LinkedList<BCP> obterListaDeDesbloqueados() {
		
		LinkedList<BCP> listaDeDesbloqueados = new LinkedList<BCP>();
		
		while(this.filaDeBloqueado.size() > 0) {
			BCP bcp = this.filaDeBloqueado.peekFirst();
			
			if(bcp.tempoDeEspera == 0) {
				this.filaDeBloqueado.removeFirst();
				listaDeDesbloqueados.addLast(bcp);
				this.quantidadeTotalDeProcessos--;
			} else {
				break;
			}
		}
		
		return listaDeDesbloqueados;
	}
	
public static int testar() {
		
		System.out.println("\n---------- Iniciando testes com o escalonador ----------");
		
		int numeroDeProblemas = 0;
		
		try {
			java.io.File file1 = new java.io.File("processos/01.txt");
			java.io.File file2 = new java.io.File("processos/02.txt");
			java.io.File file3 = new java.io.File("processos/03.txt");
			java.io.File file4 = new java.io.File("processos/04.txt");
			
			BCP bcp1 = new BCP(file1);
			BCP bcp2 = new BCP(file2);
			BCP bcp3 = new BCP(file3);
			BCP bcp4 = new BCP(file4);
			
			bcp1.prioridadeDoProcesso = 5;
			bcp1.creditosDoProcesso = 5;
			
			bcp2.prioridadeDoProcesso = 5;
			bcp2.creditosDoProcesso =2;
			
			bcp3.prioridadeDoProcesso = 5;
			bcp3.creditosDoProcesso = 3;
			
			bcp4.prioridadeDoProcesso = 5;
			bcp4.creditosDoProcesso = 5;
			
			System.out.println("Porcessos a serem usados: ");
			System.out.println("\t" + bcp1);
			System.out.println("\t" + bcp2);
			System.out.println("\t" + bcp3);
			System.out.println("\t" + bcp4 + "\n\n");
			
			FilaDePrioridade[] filaDePronto = new FilaDePrioridade[10];
			for(int prioridade = 0; prioridade < filaDePronto.length; prioridade++) {
				filaDePronto[prioridade] = new FilaDePrioridade(prioridade);
			}
			
			LinkedList<BCP> filaDeBloqueado = new LinkedList<BCP>();
			Escalonador escalonador = new Escalonador(filaDePronto, filaDeBloqueado, 2);
			
			escalonador.inserirNaFilaDePronto(bcp1, false);
			System.out.println("Inserindo processo 1\n" + toStringFilaDePronto(filaDePronto));
			
			escalonador.inserirNaFilaDePronto(bcp2, false);
			System.out.println("Inserindo processo 2\n" + toStringFilaDePronto(filaDePronto));
			
			escalonador.inserirNaFilaDePronto(bcp3, false);
			System.out.println("Inserindo processo 3\n" + toStringFilaDePronto(filaDePronto));
			
			escalonador.inserirNaFilaDePronto(bcp4, false);
			System.out.println("Inserindo processo 4\n" + toStringFilaDePronto(filaDePronto));
			
			BCP removido;
			removido = escalonador.removerProximoDaFilaDePronto();
			System.out.print("Removendo o processo 1: ");
			if(removido == bcp1) System.out.println("OK -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
				numeroDeProblemas++;
			}
			
			removido = escalonador.removerProximoDaFilaDePronto();
			System.out.print("Removendo o processo 4: ");
			if(removido == bcp4) System.out.println("OK -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
				numeroDeProblemas++;
			}
			
			bcp1.creditosDoProcesso = 0;
			escalonador.inserirNaFilaDePronto(bcp1, false);
			System.out.println("Devolvendo o processo 1 com prioridade 0\n" + toStringFilaDePronto(filaDePronto));
			
			bcp4.creditosDoProcesso = 2;
			escalonador.inserirNaFilaDePronto(bcp4, false);
			System.out.println("Devolvendo o processo 4 com prioridade 2\n" + toStringFilaDePronto(filaDePronto));
			
			removido = escalonador.removerProximoDaFilaDePronto();
			System.out.print("Removendo o processo 3: ");
			if(removido == bcp3) System.out.println("OK -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
				numeroDeProblemas++;
			}
			
			removido = escalonador.removerProximoDaFilaDePronto();
			System.out.print("Removendo o processo 2: ");
			if(removido == bcp2) System.out.println("OK -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
				numeroDeProblemas++;
			}
			
			removido = escalonador.removerProximoDaFilaDePronto();
			System.out.print("Removendo o processo 4: ");
			if(removido == bcp4) System.out.println("OK -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
				numeroDeProblemas++;
			}
			
			bcp3.creditosDoProcesso = 0;
			escalonador.inserirNaFilaDePronto(bcp3, false);
			System.out.println("Devolvendo o processo 3 com prioridade 0\n" + toStringFilaDePronto(filaDePronto));
			
			removido = escalonador.removerProximoDaFilaDePronto();
			System.out.print("Removendo o processo 1: ");
			if(removido == bcp1) System.out.println("OK -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
				numeroDeProblemas++;
			}
			
			removido = escalonador.removerProximoDaFilaDePronto();
			System.out.print("Removendo o processo 3: ");
			if(removido == bcp3) System.out.println("OK -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
				numeroDeProblemas++;
			}
			
			bcp2.creditosDoProcesso = 0;
			escalonador.inserirNaFilaDePronto(bcp2, false);
			System.out.println("Devolvendo o processo 2 com prioridade 0\n" + toStringFilaDePronto(filaDePronto));
			
			bcp4.creditosDoProcesso = 0;
			escalonador.inserirNaFilaDePronto(bcp4, false);
			System.out.println("Devolvendo o processo 4 com prioridade 0\n" + toStringFilaDePronto(filaDePronto));
			
			removido = escalonador.removerProximoDaFilaDePronto();
			System.out.print("Removendo o processo 2: ");
			if(removido == bcp2) System.out.println("OK -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
				numeroDeProblemas++;
			}
			
			removido = escalonador.removerProximoDaFilaDePronto();
			System.out.print("Removendo o processo 4: ");
			if(removido == bcp4) System.out.println("OK -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso + "\n" + toStringFilaDePronto(filaDePronto) + "\n");
				numeroDeProblemas++;
			}
			
		} catch(Exception e) {
			System.out.println("Erro ao carregar o arquivo, nao eh possivel prosseguir");
			numeroDeProblemas++;
			return numeroDeProblemas;
		}
		
		return numeroDeProblemas;
		
	}

	public static String toStringFilaDePronto(FilaDePrioridade[] filaDePronto) {
		
		String s = "";
		
		for(FilaDePrioridade filaDePrioridade : filaDePronto) {
			s += filaDePrioridade + "\n";
		}
		
		return s;
	}
}
