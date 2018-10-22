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
	
	public void decrementarCreditosDoProcesso() {
		
		if(this.creditosDoProcesso() < 1) {
			throw new RuntimeException("Os creditos do processo nao podem ser menores do que 0");
		}
		
		this.creditosDoProcesso--;
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
	
	public void definirValorDoRegistradorX(int valor) {
		this.valorDoRegistradorX = valor;
	}
	
	public int valorDoRegistradorX() {
		return this.valorDoRegistradorX;
	}
	
	public void definirValorDoRegistradorY(int valor) {
		this.valorDoRegistradorY = valor;
	}
	
	public int valorDoRegistradorY() {
		return this.valorDoRegistradorY;
	}
	
	
	public void definirQuantumDoProcesso(int quantum) {
		this.quantumDoProcesso = quantum;
	}
	
	public void duplicarQuantumDoProcesso() {
		this.quantumDoProcesso *= 2;
	}
	
	public int quantumDoProcesso() {
		return this.quantumDoProcesso;
	}
	
	
	public void definirTempoDeEspera(int tempoDeEspera) {
		this.tempoDeEspera = tempoDeEspera;
	}
	
	public void decrementarTempoDeEspera() {
		
		if(this.tempoDeEspera <= 0) {
			throw new RuntimeException("O tempo de espera nao pode ser menor do que 0");
		}
		
		this.tempoDeEspera--;
	}
	
	public int tempoDeEspera() {
		return this.tempoDeEspera;
	}
	
	@Override
	public String toString() {
		String s = "[" + this.nomeDoProcesso
				 + " C=" + this.creditosDoProcesso
				 + " P=" + this.prioridadeDoProcesso
				 +"]";
				 
		return s;
	}
}
