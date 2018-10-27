package computador;

/*
 * Simula uma interrupcao de entrada e saida, sendo capaz de armazenar a
 * quantidade de ciclos executados, invocando o sistema operacional em forma de 
 * mensagem.
 */

public class InterrupcaoDeEntradaSaida extends Exception {

	private int quantidadeDeCiclosExecutados;
	
	
	public InterrupcaoDeEntradaSaida(int quantidadeDeCiclosExecutados, String mensagem) {
		super(mensagem);
		this.quantidadeDeCiclosExecutados = quantidadeDeCiclosExecutados;
	}
	
	
	public int quantidadeDeCiclosExecutados() {
		return this.quantidadeDeCiclosExecutados;
	}
}
