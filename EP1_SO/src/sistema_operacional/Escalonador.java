package sistema_operacional;

public class Escalonador {

	public Escalonador() {
		
	}
	
	
	public BCP escalonar() {
		
		return null;
	}
	
	public boolean existeProcesso() {
		
		return false;
	}
	
	public void inserirNaFilaDePronto(BCP bcp) {
		bcp.definirProcessoComoPronto();
	}
	
	public void inserirNaFilaDeBloqueado(BCP bcp) {
		bcp.definirProcessoComoBloqueado();
	}
}
