package sistema_operacional;
import java.util.LinkedList;

public class RoundRobin implements Comparable<RoundRobin> {
	
	/**
	 * 
	 * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	 * @                                        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * @ REMOVER PARA A ENTREGA -------------------------------->>>>>>>>>>>>. ----->>>>>>>>>>>>,, D239FEF
	 * @
	 * 
	 * @return
	 */
	public static int testar() {
		
		System.out.println("\n\n======== Teste do RoundRobin ========\n");
		
		int numeroDeProblemas = 0;
		
		RoundRobin fila = new RoundRobin(5);
		
		System.out.print("Testando a integridade dos creditos (5 == 5): ");
		if(fila.creditos() == 5) {
			System.out.println("OK");
		} else {
			System.out.println("ERRO");
			numeroDeProblemas++;
		}
		
		try {
			java.io.File file1 = new java.io.File("processos/01.txt");
			java.io.File file2 = new java.io.File("processos/02.txt");
			java.io.File file3 = new java.io.File("processos/03.txt");
			java.io.File file4 = new java.io.File("processos/04.txt");
			
			BCP bcp1 = new BCP(file1);
			BCP bcp2 = new BCP(file2);
			BCP bcp3 = new BCP(file3);
			BCP bcp4 = new BCP(file4);
			
			fila.inserirNoFinal(bcp1);
			System.out.println("Inserindo processo 1\n\n" + fila);
			
			fila.inserirNoFinal(bcp2);
			System.out.println("Inserindo processo 2\n\n" + fila);
			
			fila.inserirNoFinal(bcp3);
			System.out.println("Inserindo processo 3\n\n" + fila);
			
			fila.inserirNoFinal(bcp4);
			System.out.println("Inserindo processo 4\n\n" + fila);
			
			BCP removido;
			
			removido = fila.removerPrimeiro();
			System.out.println("Removendo o primeiro (processo 1)\n" + fila);
			if(removido == bcp1) System.out.println("Status: OK ->" + removido.nomeDoProcesso() + "\n");
			else {
				System.out.println("ERRO! ->" + removido.nomeDoProcesso() + "\n");
				numeroDeProblemas++;
			}
			
			removido = fila.removerPrimeiro();
			System.out.println("Removendo o primeiro (processo 2)\n" + fila);
			if(removido == bcp2) System.out.println("Status: OK ->" + removido.nomeDoProcesso() + "\n");
			else {
				System.out.println("ERRO! ->" + removido.nomeDoProcesso() + "\n");
				numeroDeProblemas++;
			}
			
			removido = fila.removerPrimeiro();
			System.out.println("Removendo o primeiro (processo 3)\n" + fila);
			if(removido == bcp3) System.out.println("Status: OK ->" + removido.nomeDoProcesso() + "\n");
			else {
				System.out.println("ERRO! ->" + removido.nomeDoProcesso() + "\n");
				numeroDeProblemas++;
			}
			
			removido = fila.removerPrimeiro();
			System.out.println("Removendo o primeiro (processo 4)\n" + fila);
			if(removido == bcp4) System.out.println("Status: OK ->" + removido.nomeDoProcesso() + "\n");
			else {
				System.out.println("ERRO! ->" + removido.nomeDoProcesso() + "\n");
				numeroDeProblemas++;
			}
			
		} catch(Exception e) {
			System.out.println("Erro ao carregar o arquivo, nao eh possivel prosseguir");
			numeroDeProblemas++;
			return numeroDeProblemas;
		}
		
		return numeroDeProblemas;
	}
	
	
	private int creditos;
	
	private LinkedList<BCP> fila = new LinkedList<BCP>();
	
	
	public RoundRobin(int creditos) {
		this.creditos = creditos;
	}
	
	
	@Override
	public String toString() {
		String s = Integer.toString(this.creditos);
		
		Object[] o = this.fila.toArray();
		
		for(Object p : o) {
			s += " " + ((BCP)p).toString();
		}
		
		return s;
	}
	
	@Override
	public int compareTo(RoundRobin outro) {
		if(outro == null) {
			return 0;
		}
		
		return outro.creditos() - this.creditos();
	}
	
	
	public int creditos() {
		return this.creditos;
	}
	
	public void inserirNoFinal(BCP bcp) {
		this.fila.addLast(bcp);
	}
	
	public BCP removerPrimeiro() {
		return this.fila.removeFirst(); 
	}
	
	public boolean vazia() {
		return this.fila.size() == 0;
	}
	
	public int tamanho() {
		return this.fila.size();
	}
}