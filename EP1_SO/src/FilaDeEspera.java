import java.util.LinkedList;

public class FilaEspera {

	public static class NoFila {
		
		private NoFila proximoElemento;
		private int tempoEspera;
		private BCP bcp;
		
		
		
		public NoFila(int tempoEspera, BCP bcp) {
			this.proximoElemento = null;
			this.tempoEspera = tempoEspera;
			this.bcp = bcp;
		}
		
		
		
		public void definirProximo(NoFila proximoElemento) {
			this.proximoElemento = proximoElemento;
		}
		
		public NoFila proximoElemento() {
			return this.proximoElemento;
		}
		
		public void decrementarTempoEspera() {
			if(tempoEspera < 1) {
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
	
	private NoFila noCabeca;
	private NoFila fim;
	
	
	public FilaEspera(int tempoEspera) {
		
		if(tempoEspera < 0) {
			throw new IllegalArgumentException("O tempo de espera nao pode ser negativo!");
		}
		
		this.TEMPO_ESPERA = tempoEspera;
		this.noCabeca = new NoFila(0, null);
	}
	
	public void inserirNaFila(BCP bcp) {
		NoFila no = new NoFila(TEMPO_ESPERA, bcp);
		
	}
	
	public BCP[] decrementarTempoEspera() {
		
		LinkedList<BCP> ListaDedesbloqueados = new LinkedList<BCP>();
		
		Iterator<NoFila> iterador = this.fila.iterator();
		
		
		
	}
}
