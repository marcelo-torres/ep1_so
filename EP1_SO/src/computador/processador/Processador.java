package computador.processador;

import computador.Relogio;
import computador.InterrupcaoDeEntradaSaida;
import computador.InterrupcaoDeRelogio;

public class Processador {

	Relogio relogio;
	
	private Registrador pc;
	private Registrador x;
	private Registrador y;
	
	
	public Processador(Relogio relogio) {
		this.relogio = relogio;
		
		this.pc = new Registrador();
		this.x = new Registrador();
		this.y = new Registrador();
	}
	
	
	public void executar() throws InterrupcaoDeRelogio, InterrupcaoDeEntradaSaida {
		
		while(true) {
			this.relogio.gerarCiclo();
			
			/*
			 * faz a interpretacao dos comandos
			 * 
			 */
			
		}
		
	}
}
