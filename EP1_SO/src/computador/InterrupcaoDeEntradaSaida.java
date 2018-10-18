package computador;

public class InterrupcaoDeEntradaSaida extends InterrupcaoDeHardware {

	private int contadorDeCiclos;
	
	
	public InterrupcaoDeEntradaSaida(int contadorDeCiclos, String mensagem) {
		super(mensagem);
		this.contadorDeCiclos = contadorDeCiclos;
	}
	
	
	public int contadorDeCiclos() {
		return this.contadorDeCiclos;
	}
}
