package classes_antigas;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner; 

public class BCP {
	
	/**
	 * 
	 * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	 * @                                        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * @ REMOVER PARA A ENTREGA -------------------------------->>>>>>>>>>>>. ----->>>>>>>>>>>>,, D239FEF
	 * @
	 * 
	 * @return
	 */
	public static int testar() {
		
		System.out.println("\n\n======== Teste do BCP ========\n");
		
		int numeroDeProblemas = 0;
		
		System.out.print("Criando processo 01: ");
		try {
			System.out.println("OK");
			File arquivo = new File("processos/01.txt");
			BCP bcp = new BCP(arquivo);
			
			System.out.print("Nome: (" + bcp.nomeDoProcesso() +"): ");
			if(bcp.nomeDoProcesso().equals("TESTE-1")) {
				System.out.println("OK");
			} else {
				System.out.println("ERRO");
				numeroDeProblemas--;
			}
			
		} catch(Exception f) {
			System.out.println("ERRO");
			System.out.println("\t" + f.getMessage());
			numeroDeProblemas++;
		}
		
		
		return numeroDeProblemas;
	}
	
	private static int NUMERO_MAXIMO_INSTRUCOES = 21;
	
	private EstadosDeProcesso estadoDoProcesso = EstadosDeProcesso.BLOQUEADO;
	
	private int prioridadeDoProcesso = 0;
	private int creditosDoProcesso = 0;
	
	private String nomeDoProcesso;
	private String[] segmentoDeTexto = new String[NUMERO_MAXIMO_INSTRUCOES];
	
	private int contadorDePrograma = 0;
	private int registradorX = 0;
	private int registradorY = 0;
	
	private int quanta = 1;
	
	
	
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
	public String toString() {
		String s = "(" + this.nomeDoProcesso + ")"
				 + "{C: " + this.creditosDoProcesso + ""
				 + " P: " + this.prioridadeDoProcesso + ""
				 + " PC=" + this.contadorDePrograma + "}";
		
		return s;
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
	
	
	public String nomeDoProcesso() {
		return this.nomeDoProcesso;
	}
	
	public String[] segmentoDeTexto() {
		return this.segmentoDeTexto;
	}
	
	
	public void definirContadorDePrograma(int contadorDePrograma) {
		this.contadorDePrograma = contadorDePrograma;
	}
	
	public int contadorDePrograma() {
		return this.contadorDePrograma;
	}
	
	public int registradorX() {
		return this.registradorX;
	}
	
	public int registradorY() {
		return this.registradorY;
	}
	
	public int quanta() {
		return this.quanta;
	}
	
	public void duplicarQuanta() {
		this.quanta *= 2;
	}
}