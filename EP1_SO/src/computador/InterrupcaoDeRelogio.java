package computador;

public class InterrupcaoDeRelogio extends InterrupcaoDeHardware {

	private int contadorDeCiclos;
	
	
	public InterrupcaoDeRelogio(int contadorDeCiclos, String mensagem) {
		super(mensagem);
		this.contadorDeCiclos = contadorDeCiclos;
	}
	
	
	public int contadorDeCiclos() {
		return this.contadorDeCiclos;
	}
}
