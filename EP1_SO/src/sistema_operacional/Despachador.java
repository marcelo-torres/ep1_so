package sistema_operacional;

import computador.processador.Processador;

public class Despachador {

	protected Processador processador;
	protected Escalonador escalonador;
	
	
	
	public Despachador(Processador processador, Escalonador escalonador) {
		this.processador = processador;
		this.escalonador = escalonador;
	}
	
	
	
	protected boolean salvarContexto(BCP bcp) {
		
		boolean drecrementouOsCreditos = false;
		
		if(bcp != null) {
			int valorDoContadorDePrograma = this.processador.valorDoContadorDePrograma();
			int valorDoRegistradorX = this.processador.valorDoRegistradorX();
			int valorDoRegistradorY = this.processador.valorDoRegistradorY();
			
			bcp.valorDoContadorDePrograma = valorDoContadorDePrograma;
			bcp.valorDoRegistradorX =  valorDoRegistradorX;
			bcp.valorDoRegistradorY = valorDoRegistradorY;
			
			if(bcp.creditosDoProcesso > 0) {
				this.decrementarCreditosDoProcesso(bcp);
				drecrementouOsCreditos = true;
			}
			this.duplicarQuantidadeDeQuantum(bcp);
			this.duplicarQuantumDoProcesso(bcp);
		}
		
		return drecrementouOsCreditos;
	}
	
	protected void restaurarContexto(BCP bcp) {
		
		if(bcp == null) return;
		
		int valorDoContadorDePrograma = bcp.valorDoContadorDePrograma;
		int valorDoRegistradorX = bcp.valorDoRegistradorX;
		int valorDoRegistradorY = bcp.valorDoRegistradorY;
		
		this.processador.definirValorDoContadorDePrograma(valorDoContadorDePrograma);
		this.processador.definirValorDoRegistradorX(valorDoRegistradorX);
		this.processador.definirValorDoRegistradorY(valorDoRegistradorY);
		
		String[] segmentoDeTexto = bcp.segmentoDeTexto;
		this.processador.definirSegmentoDeTexto(segmentoDeTexto);
	}
	
	
	
	protected void decrementarCreditosDoProcesso(BCP bcp) {
		if(bcp.creditosDoProcesso < 1) {
			throw new RuntimeException("Os creditos do processo nao podem ser menores do que 0");
		}
		
		bcp.creditosDoProcesso--;
	}
	
	protected void duplicarQuantidadeDeQuantum(BCP bcp) {
		bcp.quantitadeDeQuantum *= 2;
	}
	
	protected void duplicarQuantumDoProcesso(BCP bcp) {
		bcp.quantumDoProcesso *= 2;
	}
	
	protected void decrementarTempoDeEspera(BCP bcp) {
		if(bcp.tempoDeEspera <= 0) {
			throw new RuntimeException("O tempo de espera nao pode ser menor do que 0");
		}
		
		bcp.tempoDeEspera--;
	}
}
