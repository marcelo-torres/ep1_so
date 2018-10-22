package computador;

public class InterrupcaoDeRelogio extends InterrupcaoDeHardware {

	private int quantidadeDeCiclosExecutados;
	
	
	public InterrupcaoDeRelogio(int quantidadeDeCiclosExecutados, String mensagem) {
		super(mensagem);
		this.quantidadeDeCiclosExecutados = quantidadeDeCiclosExecutados;
	}
	
	
	public int quantidadeDeCiclosExecutados() {
		return this.quantidadeDeCiclosExecutados;
	}
}
