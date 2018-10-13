
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Iterator;

public class FilaDeEspera {

	public static int testar() {
		
		int numeroDeProblemas = 0;
		
		System.out.println("\n\n======== Teste da Fila de Espera ========\n");
		
		FilaDeEspera filaDeEspera = new FilaDeEspera(2);
		
		try {
			java.io.File file1 = new java.io.File("processos/01.txt");
			java.io.File file2 = new java.io.File("processos/02.txt");
			java.io.File file3 = new java.io.File("processos/03.txt");
			java.io.File file4 = new java.io.File("processos/04.txt");
			
			BCP bcp1 = new BCP(file1);
			BCP bcp2 = new BCP(file2);
			BCP bcp3 = new BCP(file3);
			BCP bcp4 = new BCP(file4);
			
			System.out.println("Porcessos a serem usados: ");
			System.out.println("\t" + bcp1);
			System.out.println("\t" + bcp2);
			System.out.println("\t" + bcp3);
			System.out.println("\t" + bcp4 + "\n\n");
			
			filaDeEspera.inserirNaFila(bcp1);
			System.out.println("(01) Colocando o processo 1 na fila:\n\t" + filaDeEspera + "\n");
			
			BCP[] retorno;
			
			retorno = filaDeEspera.decrementarTempoEspera();
			System.out.print("(02) Decrementando o tempo dos processos: ");
			if(retorno.length == 0) {
				System.out.println("OK");
			} else {
				System.out.println("ERRO!");
				numeroDeProblemas++;
			}
			System.out.println("\t" + filaDeEspera + "\n");
			
			filaDeEspera.inserirNaFila(bcp2);
			System.out.println("(03) Colocando o processo 2 na fila:\n\t" + filaDeEspera + "\n");
			
			filaDeEspera.inserirNaFila(bcp3);
			System.out.println("(04) Colocando o processo 3 na fila:\n\t" + filaDeEspera + "\n");
			
			retorno = filaDeEspera.decrementarTempoEspera();
			System.out.print("(05) Decrementando o tempo dos processos: ");
			if(retorno.length == 1 && retorno[0] == bcp1) {
				System.out.println("OK - processo 1 removido com sucesso");
			} else {
				System.out.println("ERRO! - processo 1 nao foi removido");
				numeroDeProblemas++;
			}
			System.out.println("\t" + filaDeEspera + "\n");
			
			retorno = filaDeEspera.decrementarTempoEspera();
			System.out.print("(06) Decrementando o tempo dos processos: ");
			if(retorno.length == 2 && retorno[0] == bcp2 && retorno[1] == bcp3) {
				System.out.println("OK - processos 2 e 3 removidos com sucesso");
			} else {
				System.out.println("ERRO! - processos 2 e 3 nao foram removidos");
				numeroDeProblemas++;
			}
			System.out.println("\t" + filaDeEspera + "\n");
			
			filaDeEspera.inserirNaFila(bcp4);
			System.out.println("(07) Colocando o processo 4 na fila:\n\t" + filaDeEspera + "\n");
			
			filaDeEspera.inserirNaFila(bcp1);
			System.out.println("(08) Colocando o processo 1 na fila:\n\t" + filaDeEspera + "\n");
			
			retorno = filaDeEspera.decrementarTempoEspera();
			System.out.print("(09) Decrementando o tempo dos processos: ");
			if(retorno.length == 0) {
				System.out.println("OK");
			} else {
				System.out.println("ERRO!");
				numeroDeProblemas++;
			}
			System.out.println("\t" + filaDeEspera + "\n");
			
			filaDeEspera.inserirNaFila(bcp2);
			System.out.println("(10) Colocando o processo 2 na fila:\n\t" + filaDeEspera + "\n");
			
			retorno = filaDeEspera.decrementarTempoEspera();
			System.out.print("(11) Decrementando o tempo dos processos: ");
			if(retorno.length == 2 && retorno[0] == bcp4 && retorno[1] == bcp1) {
				System.out.println("OK - processos 4 e 1 removidos com sucesso");
			} else {
				System.out.println("ERRO! - processos 1 e 4 nao foram removidos");
				numeroDeProblemas++;
			}
			System.out.println("\t" + filaDeEspera + "\n");
			
			retorno = filaDeEspera.decrementarTempoEspera();
			System.out.print("(12) Decrementando o tempo dos processos: ");
			if(retorno.length == 1 && retorno[0] == bcp2) {
				System.out.println("OK - processo 2 removido com sucesso");
			} else {
				System.out.println("ERRO! - processo 2 nao foi removido");
				numeroDeProblemas++;
			}
			System.out.println("\t" + filaDeEspera + "\n");
			
		} catch(java.io.FileNotFoundException e) {
			System.out.println("Erro ao carregar o arquivo, nao eh possivel prosseguir");
			numeroDeProblemas++;
			return numeroDeProblemas;
		}
		
		return numeroDeProblemas;
	}
	
	public static class NoFila implements Comparable<NoFila> {
		
		private static int contadorDeObjetos = 0;
		
		public final int ID;
		private int tempoEspera;
		private BCP bcp;
		
		
		
		public NoFila(int tempoEspera, BCP bcp) {
			this.ID = contadorDeObjetos;
			contadorDeObjetos++;
			
			this.tempoEspera = tempoEspera;
			this.bcp = bcp;
		}
		
		
		
		@Override
		public String toString() {
			String s = "[ID="
						+ this.ID
						+ " T=" + this.tempoEspera
						+" BCP=" + bcp.nomeDoProcesso() + "]";
			
			return s;
		}
		
		
		
		@Override
		public int compareTo(NoFila outro) {
			return this.ID - outro.ID;
		}
		
		
		
		public void decrementarTempoEspera() {
			if(this.tempoEspera < 1) {
				throw new IllegalArgumentException("O tempo de espera nao pode ser negativo");
			}
			
			this.tempoEspera--;
		}
		
		public int tempoEspera() {
			return tempoEspera;
		}
		
		public BCP bcp() {
			return this.bcp;
		}
	}
	
	
	
	private int TEMPO_ESPERA;
	
	private LinkedList<NoFila> fila = new LinkedList<NoFila>();
	
	
	
	public FilaDeEspera(int tempoEspera) {
		
		if(tempoEspera < 0) {
			throw new IllegalArgumentException("O tempo de espera nao pode ser negativo!");
		}
		
		this.TEMPO_ESPERA = tempoEspera;
	}
	
	
	
	@Override
	public String toString() {
		
		Iterator<NoFila> iterador = this.fila.iterator();
		
		String s = "<";
		
		while(iterador.hasNext()) {
			NoFila noFila = iterador.next();
		
			s += "--" + noFila.toString();
		}
		
		return s;
	}
	
	public void inserirNaFila(BCP bcp) {
		NoFila noFila = new NoFila(TEMPO_ESPERA, bcp);
		this.fila.addLast(noFila);
	}
	
	public BCP[] decrementarTempoEspera() {
		
		LinkedList<BCP> listaDeDesbloqueados = new LinkedList<BCP>();
		
		while(this.fila.size() > 0) {
			NoFila noFila = this.fila.peekFirst();
			
			if(noFila.tempoEspera() == 1) {
				noFila.decrementarTempoEspera();
				this.fila.removeFirst();
				listaDeDesbloqueados.add(noFila.bcp());
			} else {
				break;
			}
		}
		
		ListIterator<NoFila> iterador = this.fila.listIterator();
		
		while(iterador.hasNext()) {
			NoFila noFila = iterador.next();
			noFila.decrementarTempoEspera();
		}
		
		Object[] vetorAuxiliar = listaDeDesbloqueados.toArray();
		BCP[] vetorDeDesbloqueados = new BCP[vetorAuxiliar.length];
		
		for(int i = 0; i < vetorDeDesbloqueados.length; i++) {
			vetorDeDesbloqueados[i] = (BCP)vetorAuxiliar[i];
		}
		
		return vetorDeDesbloqueados;
	}
	
	public int tamanho() {
		return this.fila.size();
	}
}
