package computador;

public class Relogio {

	private int contadorDeCiclos;
	private int limiteDeCiclos;
	
	
	public void definirLimiteDeCiclos(int limiteDeCiclos) {
		
		if(limiteDeCiclos < 1) {
			throw new IllegalArgumentException("O limite de ciclos do Relogio deve ser maior do que 0");
		}
		
		this.limiteDeCiclos = limiteDeCiclos;
	}
	
	public void gerarCiclo() throws InterrupcaoDeRelogio {
		
		if(this.contadorDeCiclos + 1 > this.limiteDeCiclos) {
			int numeroDeCiclos = this.contadorDeCiclos;
			this.contadorDeCiclos = 0;
			throw new InterrupcaoDeRelogio(numeroDeCiclos, "O tempo acabou");
		} else {
			this.contadorDeCiclos++;
		}
	}
	
	public void zerarRelogio() {
		this.contadorDeCiclos = 0;
	}
}
