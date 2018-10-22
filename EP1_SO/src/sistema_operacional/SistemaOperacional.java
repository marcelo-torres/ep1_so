package sistema_operacional;

import gerador_de_log.GeradorDeLog;

import computador.processador.Processador;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

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
	
	private FilaDePrioridade[] filaDePronto;
	private LinkedList<BCP> filaDeBloqueado;
	
	private Relogio relogio;
	private Processador processador;
	private Escalonador escalonador;
	private Despachador despachador;
	private TabelaDeProcessos tabelaDeProcessos;
	
	private int numeroDeProcessosCraidos;
	private int numeroDeTrocas;
	//private int numeroDe
	
	public SistemaOperacional(String diretorioDeArquivos, int quantum,
							  Relogio relogio, Processador processador)
									  throws FileNotFoundException {
		
		this.diretorioDeArquivos = diretorioDeArquivos;
		this.TEMPO_ESPERA = 2;
		
		if(quantum < 1) {
			throw new IllegalArgumentException("O quantum deve ser positivo");
		} else {
			this.QUANTUM = quantum;
		}
		
		this.filaDePronto = this.criarFilaDePronto();
		this.filaDeBloqueado = new LinkedList<BCP>();
		
		this.relogio = relogio;
		this.processador = processador;
		this.escalonador = new Escalonador(this.filaDePronto, this.filaDeBloqueado, 
										   this.TEMPO_ESPERA);
		this.despachador = new Despachador(processador, escalonador);
		this.tabelaDeProcessos = new TabelaDeProcessos(10);
	}
	
	
	private FilaDePrioridade[] criarFilaDePronto() throws FileNotFoundException {
		
		int maiorPrioridade = this.obterMaiorPrioridade();
		
		FilaDePrioridade[] filaDePrioridade = new FilaDePrioridade[maiorPrioridade + 1];
		this.inicializarFilaDePronto(filaDePrioridade);
		return filaDePrioridade;
	}
	
	private void inicializarFilaDePronto(FilaDePrioridade[] filaDePronto) {
		for(int prioridade = 0; prioridade < filaDePronto.length; prioridade++) {
			filaDePronto[prioridade] = new FilaDePrioridade(prioridade);
		}
	}
	
	private int obterMaiorPrioridade() throws FileNotFoundException {
		
		int maiorPrioridade = 0;
		
		File arquivoDePrioridades = new File(this.diretorioDeArquivos + this.nomeDoArquivoDePrioridades);
		
		try(Scanner leitorDePrioridades = new Scanner(arquivoDePrioridades)) {
			
			while(leitorDePrioridades.hasNext()) {
				int prioridade = leitorDePrioridades.nextInt();
				if(prioridade > maiorPrioridade) {
					maiorPrioridade = prioridade;
				}
			}
			
		} catch(FileNotFoundException fnfe) {
			throw new FileNotFoundException(fnfe.getMessage());
		}
			
		return maiorPrioridade;
	}
	
	
	public void iniciarSistema() throws FileNotFoundException {
		this.criarProcessosDeInicializacao();	
		this.iniciarExecucaoDeProcessos();
	}
	
	
	public void criarProcessosDeInicializacao() throws FileNotFoundException {
		
		File arquivoDePrioridades = new File(this.diretorioDeArquivos + this.nomeDoArquivoDePrioridades);
		
		try(Scanner leitorDePrioridades = new Scanner(arquivoDePrioridades)) {
			for(String nomeDoArquivoDeProcesso : this.vetorDeProcessosInicias) {
				
				int prioridadeDoProcesso = leitorDePrioridades.nextInt();
				BCP bcp = this.criarProcesso(nomeDoArquivoDeProcesso, prioridadeDoProcesso);
				escalonador.inserirNaFilaDePronto(bcp);
			}
		} catch(FileNotFoundException fnfe) {
			throw new FileNotFoundException(fnfe.getMessage());
		}
	}
	
	private BCP criarProcesso(String nomeDoArquivo, int prioridadeDoProcesso) throws FileNotFoundException {
		
		// Cria o processo
		File arquivoDeProcesso = new File(this.diretorioDeArquivos + nomeDoArquivo);
		BCP bcp = new BCP(arquivoDeProcesso);
		this.tabelaDeProcessos.inserirBcp(bcp);
		
		bcp.definirPrioridadeDoProcesso(prioridadeDoProcesso);
		bcp.definirCreditosDoProcesso(prioridadeDoProcesso);
		bcp.definirQuantumDoProcesso(this.QUANTUM);
		
		return bcp;
	}
	
	
	public void iniciarExecucaoDeProcessos() {
	
		GeradorDeLog.exibirMensagemDeCarregamento(this.filaDePronto);
		
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
					
					GeradorDeLog.exibirMensagemDeFimDeExecucao(bcpDoProcessoEscalonado);
				} catch(InterrupcaoDeRelogio ir) {
					this.despachador.salvarContexto(bcpDoProcessoEscalonado);
					this.escalonador.inserirNaFilaDePronto(bcpDoProcessoEscalonado);
					
					GeradorDeLog.exibirMensagemDeInterrupcao(
							bcpDoProcessoEscalonado.nomeDoProcesso(),
							ir.quantidadeDeCiclosExecutados());
				} catch(InterrupcaoDeEntradaSaida ies) {
					this.despachador.salvarContexto(bcpDoProcessoEscalonado);
					this.escalonador.inserirNaFilaDeBloqueado(bcpDoProcessoEscalonado);
					
					System.out.println("E/S iniciada em " + bcpDoProcessoEscalonado.nomeDoProcesso());
					GeradorDeLog.exibirMensagemDeInterrupcao(
							bcpDoProcessoEscalonado.nomeDoProcesso(),
							ies.quantidadeDeCiclosExecutados());
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
