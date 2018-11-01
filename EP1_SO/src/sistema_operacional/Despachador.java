package sistema_operacional;

import java.util.ArrayList;

import computador.processador.Processador;

/**
 * Sistemas Operacionais - Professor Clodoaldo - 2 SEM de 2018 - Turma 04
 * 
 * Ana Paula Silva de Souza - nUSP: 10391225
 * Bianca Lima Santos - nUSP: 10346811
 * Marcelo Torres do Ó - nUSP 10414571
 * Mariana Silva Santana - nUSP: 10258897
 * 
 */

/**
 * Responsavel por fazer salvar e ler dados dos BCPs. Tambem e sua tarefa
 * decrementar a quantidade de creditos e duplicar o quantum do processo.
 */

public class Despachador {

	protected SistemaOperacional sistemaOperacional;
	protected Processador processador;
	protected Escalonador escalonador;
	
	
	
	public Despachador(SistemaOperacional sistemaOperacional,
					   Processador processador, Escalonador escalonador) {
		this.sistemaOperacional = sistemaOperacional;
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
			this.aumentarQuantidadeDeQuantum(bcp);
			this.aumentarQuantumDoProcesso(bcp);
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
		
		ArrayList<String> segmentoDeTexto = bcp.segmentoDeTexto;
		this.processador.definirSegmentoDeTexto(segmentoDeTexto);
	}
	
	
	
	protected void decrementarCreditosDoProcesso(BCP bcp) {
		if(bcp.creditosDoProcesso < 1) {
			throw new RuntimeException("Os creditos do processo nao podem ser menores do que 0");
		}
		
		bcp.creditosDoProcesso--;
	}
	
	protected void aumentarQuantidadeDeQuantum(BCP bcp) {
		bcp.quantitadeDeQuantum++;
	}
	
	protected void aumentarQuantumDoProcesso(BCP bcp) {
		bcp.quantumDoProcesso += this.sistemaOperacional.QUANTUM;
	}
	
	protected void decrementarTempoDeEspera(BCP bcp) {
		if(bcp.tempoDeEspera <= 0) {
			throw new RuntimeException("O tempo de espera nao pode ser menor do que 0");
		}
		
		bcp.tempoDeEspera--;
	}
}
