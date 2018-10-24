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
			
			bcp.definirValorDoContadorDePrograma(valorDoContadorDePrograma);
			bcp.definirValorDoRegistradorX(valorDoRegistradorX);
			bcp.definirValorDoRegistradorY(valorDoRegistradorY);
			
			if(bcp.creditosDoProcesso() > 0) {
				bcp.decrementarCreditosDoProcesso();
				drecrementouOsCreditos = true;
			}
			bcp.duplicarQuantidadeDeQuantum();
			bcp.duplicarQuantumDoProcesso();
		}
		
		return drecrementouOsCreditos;
	}
	
	public void restaurarContexto(BCP bcp) {
		
		if(bcp == null) return;
		
		int valorDoContadorDePrograma = bcp.valorDoContadorDePrograma();
		int valorDoRegistradorX = bcp.valorDoRegistradorX();
		int valorDoRegistradorY = bcp.valorDoRegistradorY();
		
		this.processador.definirValorDoContadorDePrograma(valorDoContadorDePrograma);
		this.processador.definirValorDoRegistradorX(valorDoRegistradorX);
		this.processador.definirValorDoRegistradorY(valorDoRegistradorY);
		
		String[] segmentoDeTexto = bcp.segmentoDeTexto();
		this.processador.definirSegmentoDeTexto(segmentoDeTexto);
	}
}
