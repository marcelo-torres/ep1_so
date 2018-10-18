package computador;

import computador.processador.Processador;
import sistema_operacional.SistemaOperacional;

public class Computador {

	private Relogio relogio;
	private Processador processador;
	private SistemaOperacional sistemaOperacional;
	
	public Computador() {
		this.relogio = new Relogio();
		this.processador = new Processador(this.relogio);
		this.sistemaOperacional = new SistemaOperacional(relogio, processador);
	}
	
}
