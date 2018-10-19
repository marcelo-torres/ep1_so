package sistema_operacional;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import computador.processador.Registrador;

public class BCP {

	private static int NUMERO_MAXIMO_INSTRUCOES = 21;
	
	
	private EstadosDeProcesso estadoDoProcesso = EstadosDeProcesso.BLOQUEADO;
	
	private String nomeDoProcesso;
	private String[] segmentoDeTexto = new String[NUMERO_MAXIMO_INSTRUCOES];
	
	private int prioridadeDoProcesso;
	private int creditosDoProcesso;
	
	private int valorDoContadorDePrograma;
	private int valorDoRegistradorX;
	private int valorDoRegistradorY;
	
	private int quantidadeDeQuantum;
	private int quantumDoProcesso;
	
	private int tempoDeEspera;
	
	
	/**
	 * Cria um objeto BCP informacoes de um processo armazenado em um arquivo .txt
	 * 
	 * @param arquivo Arquivo .txt com as informacoes do processo
	 */
	public BCP(File arquivo) throws FileNotFoundException {
		
		Scanner leitor = new Scanner(arquivo);
		
		this.nomeDoProcesso = leitor.nextLine();
		
		int i = 0;
		while(leitor.hasNextLine()) {
			this.segmentoDeTexto[i] = leitor.nextLine();
			i++;
		}
		
		leitor.close();
	}
	
	
	public void definirProcessoComoPronto() {
		this.estadoDoProcesso = EstadosDeProcesso.PRONTO;
	}
	
	public void definirProcessoComoBloqueado() {
		this.estadoDoProcesso = EstadosDeProcesso.BLOQUEADO;
	}
	
	public void definirProcessoComoExecutando() {
		this.estadoDoProcesso = EstadosDeProcesso.EXECUTANDO;
	}
	
	
	public String nomeDoProcesso() {
		return this.nomeDoProcesso;
	}
	
	public String[] segmentoDeTexto() {
		return this.segmentoDeTexto;
	}
	
	
	public void definirPrioridadeDoProcesso(int prioridadeDoProcesso) {
		this.prioridadeDoProcesso = prioridadeDoProcesso;
	}
	
	public int prioridadeDoProcesso() {
		return prioridadeDoProcesso;
	}
	
	public void definirCreditosDoProcesso(int creditosDoProcesso) {
		this.creditosDoProcesso = creditosDoProcesso;
	}
	
	public int creditosDoProcesso() {
		return this.creditosDoProcesso;
	}
	
	
	public void definirValorDoContadorDePrograma(int valor) {
		this.valorDoContadorDePrograma = valor;
	}
	
	public int valorDoContadorDePrograma() {
		return this.valorDoContadorDePrograma;
	}
	
	public int definirValorDoRegistradorX() {
		return this.valorDoRegistradorX;
	}
	
	public int definirValorDoRegistradorY() {
		return this.valorDoRegistradorY;
	}
	
	
	public int quantidadeDeQuantum() {
		return this.quantidadeDeQuantum;
	}
	
	public void definirQuantidadeDeQuantum(int quantidadeDeQuantum) {
		if(quantidadeDeQuantum < 1) {
			throw new IllegalArgumentException("A quantidade de quantum deve ser positiva");
		}
	}
	
	public int quantumDoProcesso() {
		return this.quantumDoProcesso;
	}
	
	
	private void definirTempoDeEspera(int tempoDeEspera) {
		this.tempoDeEspera = tempoDeEspera;
	}
	
	private int tempoDeEspera() {
		return this.tempoDeEspera;
	}
}
