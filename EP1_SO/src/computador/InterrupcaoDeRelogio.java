package computador;

/*
 * Simula uma interrupcao de relogio, sendo capaz de armazenar a quantidade de
 * ciclos executados, invocando o sistema operacional em forma de mensagem.
 */

public class InterrupcaoDeRelogio extends Exception {

	private int quantidadeDeCiclosExecutados;
	
	
	public InterrupcaoDeRelogio(int quantidadeDeCiclosExecutados, String mensagem) {
		super(mensagem);
		this.quantidadeDeCiclosExecutados = quantidadeDeCiclosExecutados;
	}
	
	
	public int quantidadeDeCiclosExecutados() {
		return this.quantidadeDeCiclosExecutados;
	}
}
