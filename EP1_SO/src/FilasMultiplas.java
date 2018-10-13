import java.util.Iterator;
import java.util.PriorityQueue;

public class FilasMultiplas {
	
	public static int testar() {
		
		System.out.println("\n\n======== Teste FilasMultiplas ========\n");
		
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
			
			bcp1.definirPrioridadeDoProcesso(5);
			bcp1.definirCreditosDoProcesso(5);
			
			bcp2.definirPrioridadeDoProcesso(5);
			bcp2.definirCreditosDoProcesso(2);
			
			bcp3.definirPrioridadeDoProcesso(5);
			bcp3.definirCreditosDoProcesso(3);
			
			bcp4.definirPrioridadeDoProcesso(5);
			bcp4.definirCreditosDoProcesso(5);
			
			System.out.println("Porcessos a serem usados: ");
			System.out.println("\t" + bcp1);
			System.out.println("\t" + bcp2);
			System.out.println("\t" + bcp3);
			System.out.println("\t" + bcp4 + "\n\n");
			
			FilasMultiplas filasMultiplas = new FilasMultiplas();
			
			filasMultiplas.inserirNaFila(bcp1);
			System.out.println("Inserindo processo 1\n" + filasMultiplas);
			
			filasMultiplas.inserirNaFila(bcp2);
			System.out.println("Inserindo processo 2\n" + filasMultiplas);
			
			filasMultiplas.inserirNaFila(bcp3);
			System.out.println("Inserindo processo 3\n" + filasMultiplas);
			
			filasMultiplas.inserirNaFila(bcp4);
			System.out.println("Inserindo processo 4\n" + filasMultiplas);
			
			BCP removido;
			removido = filasMultiplas.removerProximo();
			System.out.print("Removendo o processo 1: ");
			if(removido == bcp1) System.out.println("OK -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
				numeroDeProblemas++;
			}
			
			removido = filasMultiplas.removerProximo();
			System.out.print("Removendo o processo 4: ");
			if(removido == bcp4) System.out.println("OK -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
				numeroDeProblemas++;
			}
			
			bcp1.definirCreditosDoProcesso(0);
			filasMultiplas.inserirNaFila(bcp1);
			System.out.println("Devolvendo o processo 1 com prioridade 0\n" + filasMultiplas);
			
			bcp4.definirCreditosDoProcesso(2);
			filasMultiplas.inserirNaFila(bcp4);
			System.out.println("Devolvendo o processo 4 com prioridade 2\n" + filasMultiplas);
			
			removido = filasMultiplas.removerProximo();
			System.out.print("Removendo o processo 3: ");
			if(removido == bcp3) System.out.println("OK -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
				numeroDeProblemas++;
			}
			
			removido = filasMultiplas.removerProximo();
			System.out.print("Removendo o processo 2: ");
			if(removido == bcp2) System.out.println("OK -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
				numeroDeProblemas++;
			}
			
			removido = filasMultiplas.removerProximo();
			System.out.print("Removendo o processo 4: ");
			if(removido == bcp4) System.out.println("OK -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
				numeroDeProblemas++;
			}
			
			bcp3.definirCreditosDoProcesso(0);
			filasMultiplas.inserirNaFila(bcp3);
			System.out.println("Devolvendo o processo 3 com prioridade 0\n" + filasMultiplas);
			
			removido = filasMultiplas.removerProximo();
			System.out.print("Removendo o processo 1: ");
			if(removido == bcp1) System.out.println("OK -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
				numeroDeProblemas++;
			}
			
			removido = filasMultiplas.removerProximo();
			System.out.print("Removendo o processo 3: ");
			if(removido == bcp3) System.out.println("OK -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
				numeroDeProblemas++;
			}
			
			bcp2.definirCreditosDoProcesso(0);
			filasMultiplas.inserirNaFila(bcp2);
			System.out.println("Devolvendo o processo 2 com prioridade 0\n" + filasMultiplas);
			
			bcp4.definirCreditosDoProcesso(0);
			filasMultiplas.inserirNaFila(bcp4);
			System.out.println("Devolvendo o processo 4 com prioridade 0\n" + filasMultiplas);
			
			removido = filasMultiplas.removerProximo();
			System.out.print("Removendo o processo 2: ");
			if(removido == bcp2) System.out.println("OK -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
				numeroDeProblemas++;
			}
			
			removido = filasMultiplas.removerProximo();
			System.out.print("Removendo o processo 4: ");
			if(removido == bcp4) System.out.println("OK -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
			else {
				System.out.println("ERRO! -> " + removido.nomeDoProcesso() + "\n" + filasMultiplas + "\n");
				numeroDeProblemas++;
			}
			
		} catch(Exception e) {
			System.out.println("Erro ao carregar o arquivo, nao eh possivel prosseguir");
			numeroDeProblemas++;
			return numeroDeProblemas;
		}
		
		return numeroDeProblemas;
		
	}
	
	
	
	private int quantidadeDeProcessos = 0;
	
	private PriorityQueue<RoundRobin> filasRoundRobin = new PriorityQueue<RoundRobin>();
	private RoundRobin filaDeCreditoZero = new RoundRobin(0);
	
	
	
	@Override
	public String toString() {
		String s = "";
		for(RoundRobin fila : this.filasRoundRobin) {
			s += "(" + (fila.creditos()) + ")--> " + fila.toString() + "\n";
		}
		s += "(0)--> " + filaDeCreditoZero + "\n";
		return s;
	}
	
	
	
	public int quantidadeDeProcessos() {
		return this.quantidadeDeProcessos;
	}
	
	public int quantidadeDeProcessosSemCreditos() {
		return this.filaDeCreditoZero.tamanho();
	}
	
	
	
	/**
	 * Realiza a insercao de um BCP em um das filas de pronto de acordo com
	 * a quantidade de creditos do processo.
	 * 
	 * @param bcp BCP a ser inserido
	 * @return false caso o bcp seja null ou caso contrario
	 */
	public boolean inserirNaFila(BCP bcp) {
		
		if(bcp == null) {
			return false;
		}
		
		if(bcp.creditosDoProcesso() == 0) {
			filaDeCreditoZero.inserirNoFinal(bcp);
		} else {
			RoundRobin filaDeInsercao = null;
			Iterator<RoundRobin> iterador = filasRoundRobin.iterator();
			
			while(iterador.hasNext()) {
				RoundRobin fila = iterador.next();
				
				if(fila.creditos() <= bcp.creditosDoProcesso()) {
					if(fila.creditos() == bcp.creditosDoProcesso()) {
						filaDeInsercao = fila;
					}
					break;
				}
			}
			
			if(filaDeInsercao == null) {
				filaDeInsercao = new RoundRobin(bcp.creditosDoProcesso());
				filasRoundRobin.add(filaDeInsercao);
			}
			
			filaDeInsercao.inserirNoFinal(bcp);
		}
		
		this.quantidadeDeProcessos++;
		return true;
	}

	/**
	 * Devolve o proximo BCP da estrutura com maior prioridade, ou null caso
	 * a estrutura esteja completamente vazia. Caso todos os processos armazenados
	 * nao possuam nenhum credito a fila funcionara por ordem de chegada.
	 * 
	 * @return proximo BCP da estrutura ou null caso nao haja proximo disponivel
	 */
	public BCP removerProximo() {
		
		RoundRobin fila = this.filasRoundRobin.peek();
		
		if(fila == null) {
			if(filaDeCreditoZero.vazia()) {
				return null;
			} else {
				return filaDeCreditoZero.removerPrimeiro();
			}
		}
		
		BCP bcpRemovido = fila.removerPrimeiro();
		
		if(fila.vazia()) {
			this.filasRoundRobin.poll();
		}
		
		this.quantidadeDeProcessos--;
		return bcpRemovido;
	}
		
	public boolean redistribuirCreditos() {
		
		if(this.quantidadeDeProcessos() == this.quantidadeDeProcessosSemCreditos()) {
			return false;
		}
		
		while(this.filaDeCreditoZero.tamanho() > 0) {
			BCP bcp = this.filaDeCreditoZero.removerPrimeiro();
			int prioridade = bcp.prioridadeDoProcesso();
			bcp.definirCreditosDoProcesso(prioridade);
			this.inserirNaFila(bcp);
		}
		
		return true;
	}
		
}