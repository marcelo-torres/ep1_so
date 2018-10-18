package sistema_operacional;

import computador.processador.Processador;
import computador.InterrupcaoDeEntradaSaida;
import computador.InterrupcaoDeRelogio;
import computador.Relogio;

public class SistemaOperacional {

	private Relogio relogio;
	private Processador processador;
	private Escalonador escalonador;
	private Despachador despachador;
	
	public SistemaOperacional(Relogio relogio, Processador processador) {
		this.relogio = relogio;
		this.processador = processador;
		this.escalonador = new Escalonador();
		this.despachador = new Despachador(processador, escalonador);
	}
	
	public void iniciar() {
		
		BCP bcpAnterior = null;
		BCP bcpAtual = null;
		
		while(escalonador.existeProcesso()) {
			
			bcpAtual = despachador.despachar(bcpAnterior);
			
			if(bcpAtual != null) {
				this.relogio.definirLimiteDeCiclos(bcpAtual.quantumDoProcesso());
				
				try {
					this.processador.executar();
				} catch(InterrupcaoDeRelogio ir) {
					
				} catch(InterrupcaoDeEntradaSaida ies) {
					
				}
			}
			
			bcpAnterior = bcpAtual;
		}
		
	}
}
