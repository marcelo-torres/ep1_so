package sistema_operacional;

import computador.processador.Processador;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeSet;

import computador.InterrupcaoDeEntradaSaida;
import computador.InterrupcaoDeRelogio;
import computador.Relogio;

public class SistemaOperacional {
	
	private String[] vetorDeProcessosInicias = {
			"01.txt",
			"02.txt",
			"03.txt",
			"04.txt",
			"05.txt",
			"06.txt",
			"07.txt",
			"08.txt",
			"09.txt",
			"10.txt"
	};
	
	private String nomeDoArquivoDePrioridades = "prioridades.txt";
	private String diretorioDeArquivos;
	
	private final int TEMPO_ESPERA;
	private final int QUANTUM;
	
	private TreeSet<FilaDePrioridade> filaDePronto;
	private LinkedList<BCP> filaDeBloqueado;
	
	private Relogio relogio;
	private Processador processador;
	private Escalonador escalonador;
	private Despachador despachador;
	private TabelaDeProcessos tabelaDeProcessos;
	
	public SistemaOperacional(String diretorioDeArquivos, int quantum,
							  Relogio relogio, Processador processador) {
		
		this.diretorioDeArquivos = diretorioDeArquivos;
		this.TEMPO_ESPERA = 2;
		
		if(quantum < 1) {
			throw new IllegalArgumentException("O quantum deve ser positivo");
		} else {
			this.QUANTUM = quantum;
		}
		
		this.filaDePronto = new TreeSet<FilaDePrioridade>();
		this.filaDeBloqueado = new LinkedList<BCP>();
		
		this.relogio = relogio;
		this.processador = processador;
		this.escalonador = new Escalonador(this.filaDePronto, this.filaDeBloqueado, this.TEMPO_ESPERA);
		this.despachador = new Despachador(processador, escalonador);
		this.tabelaDeProcessos = new TabelaDeProcessos(10);
	}
	
	
	public void iniciarSistema() throws FileNotFoundException {
		this.criarProcessosDeInicializacao();	
		this.iniciarExecucaoDeProcessos();
	}
	
	public void criarProcessosDeInicializacao() throws FileNotFoundException {
		
		File arquivoDePrioridades = new File(diretorioDeArquivos + this.nomeDoArquivoDePrioridades);
		
		try(Scanner leitorDePrioridades = new Scanner(arquivoDePrioridades)) {
			for(String nomeDeArquivoDeProcesso : this.vetorDeProcessosInicias) {
				File arquivoDeProcesso = new File(diretorioDeArquivos + nomeDeArquivoDeProcesso);
				
				BCP bcp = new BCP(arquivoDeProcesso);
				
				this.tabelaDeProcessos.inserirBcp(bcp);
				
				int prioridadeDoProcesso = leitorDePrioridades.nextInt();
				bcp.definirPrioridadeDoProcesso(prioridadeDoProcesso);
				bcp.definirCreditosDoProcesso(prioridadeDoProcesso);
				bcp.definirQuantumDoProcesso(this.QUANTUM);
				
				System.out.println("criando " + bcp);
				System.out.println(Escalonador.toStringFilaDePronto(this.filaDePronto));
				
				escalonador.inserirNaFilaDePronto(bcp);
			}
		} catch(FileNotFoundException fnfe) {
			throw new FileNotFoundException(fnfe.getMessage());
		}
	}
	
	public void iniciarExecucaoDeProcessos() {
	
		BCP bcpDoProcessoEscalonado = null;
		
		while(escalonador.existeProcesso()) {
			
			//System.out.println("\n" + Escalonador.toStringFilaDePronto(this.filaDePronto) + "\n");
			
			if(escalonador.necessarioRedistribuirCreditos()) {
				this.escalonador.redistribuirCreditos();
			}
			
			bcpDoProcessoEscalonado = this.escalonador.escalonar();
			
			if(bcpDoProcessoEscalonado != null) {
				this.despachador.restaurarContexto(bcpDoProcessoEscalonado);
				this.relogio.definirLimiteDeCiclos(bcpDoProcessoEscalonado.quantumDoProcesso());
				this.relogio.zerarRelogio();
				
				System.out.println("Executando " + bcpDoProcessoEscalonado.nomeDoProcesso());
				bcpDoProcessoEscalonado.definirProcessoComoExecutando();
				try {
					int numeroDeInstrucoesExecutadas = this.processador.executar();
					
					// Se nenhuma interrupcao for gerada significa que o processo
					// chegou ao seu final
					this.despachador.salvarContexto(bcpDoProcessoEscalonado);
					this.tabelaDeProcessos.liberarEntrada(bcpDoProcessoEscalonado);
					
					System.out.println(bcpDoProcessoEscalonado.nomeDoProcesso() + " terminado. "
						+ "X=" + bcpDoProcessoEscalonado.valorDoRegistradorX() + ". Y=" + bcpDoProcessoEscalonado.valorDoRegistradorY());
				} catch(InterrupcaoDeRelogio ir) {
					this.despachador.salvarContexto(bcpDoProcessoEscalonado);
					this.escalonador.inserirNaFilaDePronto(bcpDoProcessoEscalonado);
					
					if(ir.quantidadeDeCiclosExecutados() == 1) {
						System.out.println("Interrompendo " + bcpDoProcessoEscalonado.nomeDoProcesso()
						+ " após " + ir.quantidadeDeCiclosExecutados() + " instrução.");
					} else {
						System.out.println("Interrompendo " + bcpDoProcessoEscalonado.nomeDoProcesso()
						+ " após " + ir.quantidadeDeCiclosExecutados() + " instruções.");
					}
				} catch(InterrupcaoDeEntradaSaida ies) {
					this.despachador.salvarContexto(bcpDoProcessoEscalonado);
					this.escalonador.inserirNaFilaDeBloqueado(bcpDoProcessoEscalonado);
					
					System.out.println("E/S iniciada em " + bcpDoProcessoEscalonado.nomeDoProcesso());
					if(ies.quantidadeDeCiclosExecutados() == 1) {
						System.out.println("Interrompendo " + bcpDoProcessoEscalonado.nomeDoProcesso()
						+ " após " + ies.quantidadeDeCiclosExecutados() + " instrução.");
					} else {
						System.out.println("Interrompendo " + bcpDoProcessoEscalonado.nomeDoProcesso()
						+ " após " + ies.quantidadeDeCiclosExecutados() + " instruções.");
					}
				}
			}
			
			escalonador.decrementarFilaDeBloqueado();
			LinkedList<BCP> desbloqueados = escalonador.obterListaDeDesbloqueados();
			
			for(BCP bcp : desbloqueados) {
				this.escalonador.inserirNaFilaDePronto(bcp);
			}
		}
	}
}
