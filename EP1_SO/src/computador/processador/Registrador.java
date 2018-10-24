package computador.processador;

public class Registrador {

	private int valorArmazenado;
	
	
	public void definirValorArmazenado(int valor) {
		this.valorArmazenado = valor;
	}
	
	public int valorArmazenado() {
		return this.valorArmazenado;
	}
	
	public void incrementar() {
		this.valorArmazenado++;
	}
	
	public void copiarValorDe(Registrador outro) {
		int valor = outro.valorArmazenado();
		this.definirValorArmazenado(valor);;
	}
}
