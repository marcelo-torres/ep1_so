package sistema_operacional;

import computador.processador.Processador;

public class Despachador {

	private Processador processador;
	private Escalonador escalonador;
	
	
	public Despachador(Processador processador, Escalonador escalonador) {
		this.processador = processador;
		this.escalonador = escalonador;
	}
	
	
	
	public boolean salvarContexto(BCP bcp) {
		
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
			duplicarQuantidadeDeQuantum(bcp);
			duplicarQuantumDoProcesso(bcp);
		}
		
		return drecrementouOsCreditos;
	}
	
	public void restaurarContexto(BCP bcp) {
		
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
	
	private void decrementarCreditosDoProcesso(BCP bcp) {
		if(bcp.creditosDoProcesso < 1) {
			throw new RuntimeException("Os creditos do processo nao podem ser menores do que 0");
		}
		
		bcp.creditosDoProcesso--;
	}
	
	private void duplicarQuantidadeDeQuantum(BCP bcp) {
		bcp.quantitadeDeQuantum *= 2;
	}
	
	private void duplicarQuantumDoProcesso(BCP bcp) {
		bcp.quantumDoProcesso *= 2;
	}
	
	private void decrementarTempoDeEspera(BCP bcp) {
		if(bcp.tempoDeEspera <= 0) {
			throw new RuntimeException("O tempo de espera nao pode ser menor do que 0");
		}
		
		bcp.tempoDeEspera--;
	}
}
