package sistema_operacional;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Iterator;

public class FilaDePrioridade implements Comparable<FilaDePrioridade> {
	
	private int creditos;
	public LinkedList<BCP> fila = new LinkedList<BCP>();
	
	
	public FilaDePrioridade(int creditos) {
		this.creditos = creditos;
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
	
	public Iterator<BCP> iterador() {
		return this.fila.iterator();
	}
	
	public void inserirOrdenado(BCP bcp) {
		this.fila.addLast(bcp);
		Collections.sort(this.fila);
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