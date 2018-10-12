
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class Escalonador {
	
	private TabelaDeProcessos tabelaDeProcessos;
	private CPU cpu;
	private FilasMultiplas filaDePronto;
	private LinkedList<BCP> filaDeBloqueado;
	
	private int quantum;
	
	
	
	public Escalonador(CPU cpu, int quantum) {
		this.tabelaDeProcessos = new TabelaDeProcessos(10);
		this.cpu = cpu;
		this.filaDePronto = new FilasMultiplas();
		this.filaDeBloqueado = new LinkedList<BCP>();
		
		this.quantum = quantum;
	}
	
	
	
	public void iniciar() {
		
		BCP bcpAnterior = null;
		
		while(this.filaDePronto.quantidadeDeProcessos() > 0 
				|| this.filaDeBloqueado.size() > 0) {
		
			if(filaDePronto.quantidadeDeProcessos() > 0) {
				BCP bcp = filaDePronto.removerProximo();
				
				if(bcpAnterior != bcp) {
					this.cpu.salvarContexto(bcpAnterior);
					this.cpu.carregarProcesso(bcp);
				}
				
				// quantum
				this.cpu.executarProcesso(-1);
				
				if(cpu.interrucaoDeES()) {
					
				}
				
				if(cpu.fimDoProcesso()) {
					this.tabelaDeProcessos.removerProcesso(bcp);
				}
			}
			
			// decrementar fila de bloqueado e mover para a de pronto quem merece
		}
	}
	
	public void iniciarProcessos(String[] nomeDosArquivosDosProcesso, 
			String nomeDoArquivoDePrioridades) throws Exception {
		
		BCP[] processosCriados = 
				tabelaDeProcessos.criarProcessos(
					nomeDosArquivosDosProcesso, nomeDoArquivoDePrioridades);
		
		for(BCP bcp : processosCriados) {
			int prioridade = bcp.prioridadeDoProcesso();
			bcp.definirCreditosDoProcesso(prioridade);
			this.inserirNaFilaDePronto(bcp);
		}
	}

	private void inserirNaFilaDePronto(BCP bcp) {
		bcp.definirProcessoComoPronto();
		this.filaDePronto.inserirNaFila(bcp);
	}
	
	private void inserirNaFilaDeBloqueado(BCP bcp) {
		
	}
	
	public BCP escalonar() {
		
		// Verificar situacoes adversar e retorna
		
		return null;
	}
	

	public static void main(String[] args) {
		
		File arquivoDoQuantum = new File("processos/quantum.txt");
		int quantum = -1;
		
		try(Scanner leitor = new Scanner(arquivoDoQuantum)){
			quantum = leitor.nextInt();
		} catch(Exception e) {
			System.out.println("[!] - Erro ao ler o quantum.");
			System.out.println("\t" + e.getMessage());
			System.exit(1);
		}
		
		CPU cpu = new CPU();
		Escalonador escalonador = new Escalonador(cpu, quantum);
		
		String[] nomeDosArquivosDosProcesso = {
				"processos/01.txt",
				"processos/02.txt",
				"processos/03.txt",
				"processos/04.txt",
				"processos/05.txt",
				"processos/06.txt",
				"processos/07.txt",
				"processos/08.txt",
				"processos/09.txt",
				"processos/10.txt"
		};
		
		try {
			escalonador.iniciarProcessos(nomeDosArquivosDosProcesso, 
										"processos/prioridades.txt");
			
		} catch(Exception e) {
			System.out.println("[!] - Erro ao carregar os processos:");
			System.out.println("\t" + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
}
