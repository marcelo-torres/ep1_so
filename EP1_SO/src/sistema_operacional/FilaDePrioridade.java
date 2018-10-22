package sistema_operacional;
import java.util.LinkedList;

public class FilaDePrioridade implements Comparable<FilaDePrioridade> {
	
	private int creditos;
	private LinkedList<BCP> fila = new LinkedList<BCP>();
	
	
	public FilaDePrioridade(int creditos) {
		this.creditos = creditos;
	}
	
	
	@Override
	public String toString() {
		String s = Integer.toString(this.creditos) + "-->";
		
		Object[] o = this.fila.toArray();
		
		for(Object p : o) {
			s += " " + ((BCP)p).toString();
		}
		
		return s;
	}
	
	@Override
	public int compareTo(FilaDePrioridade outro) {
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