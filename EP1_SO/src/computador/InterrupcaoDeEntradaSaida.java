package computador;

public class InterrupcaoDeEntradaSaida extends InterrupcaoDeHardware {

	private int quantidadeDeCiclosExecutados;
	
	
	public InterrupcaoDeEntradaSaida(int quantidadeDeCiclosExecutados, String mensagem) {
		super(mensagem);
		this.quantidadeDeCiclosExecutados = quantidadeDeCiclosExecutados;
	}
	
	
	public int quantidadeDeCiclosExecutados() {
		return this.quantidadeDeCiclosExecutados;
	}
}
