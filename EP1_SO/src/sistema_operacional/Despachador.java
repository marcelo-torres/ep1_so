package sistema_operacional;

import computador.processador.Processador;

public class Despachador {

	private Processador processador;
	private Escalonador escalonador;
	
	
	public Despachador(Processador processador, Escalonador escalonador) {
		this.processador = processador;
		this.escalonador = escalonador;
	}
	
	
	public BCP despachar(BCP bcpAnterior) {
		
		BCP bcpAtual = this.escalonador.escalonar();
		
		if(bcpAtual != bcpAnterior) {
			this.salvarContexto(bcpAnterior);
			this.restaurarContexto(bcpAtual);
		}
		
		return bcpAtual;
	}
	
	private void salvarContexto(BCP bcp) {
		if(bcp == null) return;
	}
	
	private void restaurarContexto(BCP bcp) {
		if(bcp == null) return;
	}
}
