package sistema_operacional;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import computador.processador.Registrador;

public class BCP implements Comparable<BCP> {

	protected static int NUMERO_MAXIMO_INSTRUCOES = 21;
	
	
	protected EstadosDeProcesso estadoDoProcesso = EstadosDeProcesso.BLOQUEADO;
	
	protected String nomeDoProcesso;
	protected String[] segmentoDeTexto = new String[NUMERO_MAXIMO_INSTRUCOES];
	
	protected int prioridadeDoProcesso;
	protected int creditosDoProcesso;
	
	protected int valorDoContadorDePrograma;
	protected int valorDoRegistradorX;
	protected int valorDoRegistradorY;
	
	protected int quantitadeDeQuantum;
	protected int quantumDoProcesso;
	protected int tempoDeEspera;
	
	
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
	
	
	@Override
	public int compareTo(BCP outro) {
		
		String nomeDesteProcesso = this.nomeDoProcesso;
		String nomeDoOutroProcesso = outro.nomeDoProcesso;
		
		return nomeDesteProcesso.compareTo(nomeDoOutroProcesso);
		
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
