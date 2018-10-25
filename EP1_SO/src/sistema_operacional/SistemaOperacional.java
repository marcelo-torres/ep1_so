package sistema_operacional;

import computador.processador.Processador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	
	protected final int TEMPO_DE_ESPERA;
	private final int QUANTUM;
	
	private FilaDePrioridade[] filaDePronto;
	private LinkedList<BCP> filaDeBloqueado;
	
	private Relogio relogio;
	private Processador processador;
	private Escalonador escalonador;
	private Despachador despachador;
	private TabelaDeProcessos tabelaDeProcessos;
	
	private int numeroDeProcessosCriados;
	private int numeroDeTrocas;
	private int numeroDeQuantaExecutados;
	private int numeroDeIntrucoesExecutadas;
	
	protected int quantidadeTotalDeProcessos;
	protected int quantidadeDeProcessosProntos;
	protected int quantidadeDeProcessosProntosSemCreditos;
	
	
	
	public SistemaOperacional(String diretorioDeArquivos, int quantum,
							  Relogio relogio, Processador processador)
									  throws FileNotFoundException {
		
		this.diretorioDeArquivos = diretorioDeArquivos;
		this.TEMPO_DE_ESPERA = 2;
		
		if(quantum < 1) {
			throw new IllegalArgumentException("O quantum deve ser positivo");
		} else {
			this.QUANTUM = quantum;
		}
		
		this.filaDePronto = this.criarFilaDePronto();
		this.filaDeBloqueado = new LinkedList<BCP>();
		
		this.relogio = relogio;
		this.processador = processador;
		this.escalonador = new Escalonador(this, this.filaDePronto,
										   this.filaDeBloqueado);
		this.despachador = new Despachador(processador, escalonador);
		this.tabelaDeProcessos = new TabelaDeProcessos(10);
	}
	
	
	
	/* ###################################################################### */
	/* ##################### CRIACAO DA FILA DE PRONTO ###################### */
	/* ###################################################################### */
	
	
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
	
	/**
	 * Retorna a maior prioridade possivel dado o arquivo de prioridades
	 */
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
	
	
	
	public void iniciarSistema() throws FileNotFoundException, Exception {
		GeradorDeLog.iniciar("logs/", this.QUANTUM);
		this.criarProcessosDeInicializacao();
		
		try {
			this.iniciarExecucaoDeProcessos();
		} catch(IOException ioe) {
			throw new Exception("Erro ao escrever no arquivos de log: + "
					+ ioe.getMessage());
		} finally {
			GeradorDeLog.finalizar();
		}
	}
	
	
	
	/* ###################################################################### */
	/* ####################### CRIACAO DOS PROCESSOS ######################## */
	/* ###################################################################### */
	
	
	public void criarProcessosDeInicializacao() throws FileNotFoundException {
		
		File arquivoDePrioridades = new File(this.diretorioDeArquivos + this.nomeDoArquivoDePrioridades);
		
		try(Scanner leitorDePrioridades = new Scanner(arquivoDePrioridades)) {
			for(String nomeDoArquivoDeProcesso : this.vetorDeProcessosInicias) {
				
				int prioridadeDoProcesso = leitorDePrioridades.nextInt();
				BCP bcp = this.criarProcesso(nomeDoArquivoDeProcesso, prioridadeDoProcesso);
				escalonador.inserirOrdenadoNaFilaDePronto(bcp);
			}
		} catch(FileNotFoundException fnfe) {
			throw new FileNotFoundException(fnfe.getMessage());
		}
	}
	
	private BCP criarProcesso(String nomeDoArquivo, int prioridadeDoProcesso) throws FileNotFoundException {
		
		// Cria o processo
		File arquivoDeProcesso = new File(this.diretorioDeArquivos + nomeDoArquivo);
		BCP bcp = new BCP(arquivoDeProcesso);
		this.inserirBcpNaTabelaDeProcessos(bcp);
		
		bcp.prioridadeDoProcesso = prioridadeDoProcesso;
		bcp.creditosDoProcesso = prioridadeDoProcesso;
		bcp.quantitadeDeQuantum = 1;
		bcp.quantumDoProcesso = this.QUANTUM;
		bcp.numeroDoArquivo = this.obterNumeroDoArquivo(nomeDoArquivo);
		
		this.numeroDeProcessosCriados++;
		
		return bcp;
	}
	
	/**
	 *  Devolve o numero do arquivo no qual o processo esta guardado
	 */
	private int obterNumeroDoArquivo(String nomeDoArquivo) {
		
		String extensao = ".txt";
		int limite = nomeDoArquivo.length() - extensao.length();
		
		String aux = nomeDoArquivo.substring(0, limite);
		int numero = Integer.valueOf(aux);
		return numero;
	}
	
	
	
	/* ###################################################################### */
	/* ################ GERENCIAMENTO DA TABELA DE PROCESSOS ################ */
	/* ###################################################################### */
	
	
	public void iniciarExecucaoDeProcessos() throws IOException {
	
		GeradorDeLog.exibirMensagemDeCarregamento(this.filaDePronto);
		
		BCP bcpDoProcessoEscalonado = null;
		
		while(this.existeProcesso()) {
			
			if(escalonador.necessarioRedistribuirCreditos()) {
				this.redistribuirCreditos();
			}
			
			bcpDoProcessoEscalonado = this.escalonador.escalonar();
			
			if(bcpDoProcessoEscalonado != null) {
				this.despachador.restaurarContexto(bcpDoProcessoEscalonado);
				this.relogio.definirLimiteDeCiclos(bcpDoProcessoEscalonado.quantumDoProcesso);
				this.relogio.zerarRelogio();
				
				this.numeroDeTrocas++;
				//this.numeroDeQuantaExecutados += bcpDoProcessoEscalonado.quantitadeDeQuantum();
				
				GeradorDeLog.exibirMensagemDeExecucao(bcpDoProcessoEscalonado.nomeDoProcesso);
				bcpDoProcessoEscalonado.estadoDoProcesso = EstadosDeProcesso.PRONTO;
				try {
					int numeroDeInstrucoesExecutadas = this.processador.executar();
					
					this.numeroDeQuantaExecutados += Math.ceil(numeroDeInstrucoesExecutadas / (double)this.QUANTUM);
					
					// Se nenhuma interrupcao for gerada significa que o processo
					// chegou ao seu final
					this.despachador.salvarContexto(bcpDoProcessoEscalonado);
					
					// TODO remover esse metodo aqui, colocar no SO
					this.liberarEntradaNaTabelaDeProcessos(bcpDoProcessoEscalonado);
					
					this.numeroDeIntrucoesExecutadas += bcpDoProcessoEscalonado.valorDoContadorDePrograma;
					
					GeradorDeLog.exibirMensagemDeFimDeExecucao(bcpDoProcessoEscalonado);
				} catch(InterrupcaoDeRelogio ir) {
					this.numeroDeQuantaExecutados += Math.ceil(ir.quantidadeDeCiclosExecutados() / (double)this.QUANTUM);
					
					boolean inserirNaFrente = this.despachador.salvarContexto(bcpDoProcessoEscalonado);
					this.escalonador.inserirNaFilaDePronto(bcpDoProcessoEscalonado, inserirNaFrente);
					
					GeradorDeLog.exibirMensagemDeInterrupcao(
							bcpDoProcessoEscalonado.nomeDoProcesso,
							ir.quantidadeDeCiclosExecutados());
				} catch(InterrupcaoDeEntradaSaida ies) {
					this.numeroDeQuantaExecutados += Math.ceil(ies.quantidadeDeCiclosExecutados() / (double)this.QUANTUM);
					
					this.despachador.salvarContexto(bcpDoProcessoEscalonado);
					this.escalonador.inserirNaFilaDeBloqueado(bcpDoProcessoEscalonado);
					
					System.out.println("E/S iniciada em " + bcpDoProcessoEscalonado.nomeDoProcesso);
					GeradorDeLog.exibirMensagemDeInterrupcao(
							bcpDoProcessoEscalonado.nomeDoProcesso,
							ies.quantidadeDeCiclosExecutados());
				}
			}
			
			escalonador.decrementarFilaDeBloqueado();
			LinkedList<BCP> desbloqueados = escalonador.obterListaDeDesbloqueados();
			
			for(BCP bcp : desbloqueados) {
				this.escalonador.inserirNaFilaDePronto(bcp, false);
			}
		}
		
		GeradorDeLog.imprimirEstatisticas(this.numeroDeProcessosCriados,
										  this.numeroDeTrocas,
										  this.numeroDeQuantaExecutados,
										  this.numeroDeIntrucoesExecutadas,
										  this.QUANTUM);
	}
	
	protected boolean existeProcesso() {
		return this.quantidadeTotalDeProcessos > 0;
	}
	
	
	
	public boolean redistribuirCreditos() {
		
		if(this.quantidadeTotalDeProcessos != this.quantidadeDeProcessosProntosSemCreditos) {
			return false;
		}
		
		FilaDePrioridade filaDePrioridade = this.filaDePronto[0];
		
		while(filaDePrioridade.tamanho() > 0) {
			BCP bcp = filaDePrioridade.removerPrimeiro();
			this.quantidadeDeProcessosProntosSemCreditos--;
			//this.quantidadeDeProcessosProntos--;
			//this.quantidadeTotalDeProcessos--;
			
			int prioridade = bcp.prioridadeDoProcesso;
			
			FilaDePrioridade filaDePrioridadeCorrespondente = this.escalonador.filaDePrioridadeCorrespondente(bcp);
			
			bcp.creditosDoProcesso = prioridade;
			filaDePrioridadeCorrespondente.inserirOrdenado(bcp);
		}
		
		return true;
	}
	
	
	
	/* ###################################################################### */
	/* ################ GERENCIAMENTO DA TABELA DE PROCESSOS ################ */
	/* ###################################################################### */
	
	public int inserirBcpNaTabelaDeProcessos(BCP bcp) {
		
		if(this.tabelaDeProcessos.filaDeEspacosDisponiveis.size() == 0) {
			return -1;
		}
		
		int indice = this.tabelaDeProcessos.filaDeEspacosDisponiveis.remove();
		this.tabelaDeProcessos.tabela[indice] = bcp;
		
		return indice;
	}
	
	public BCP acessarBcpNoIndiceNaTabela(int indice) {
		
		if(indice < 0 || indice > this.tabelaDeProcessos.tabela.length) {
			throw new IllegalArgumentException("Nao eh possivel acessar o indice "
					+ indice + " posicao invalida");
		}
		
		return this.tabelaDeProcessos.tabela[indice];
	}
	
	public void liberarEntradaNaTabelaDeProcessos(BCP bcp) {
		
		if(bcp == null) {
			throw new IllegalArgumentException("Nao eh possivel acessar um elemento null");
		}
		
		for(int i = 0; i < this.tabelaDeProcessos.tabela.length; i++) {
			if(this.tabelaDeProcessos.tabela[i] == bcp) {
				this.tabelaDeProcessos.tabela[i] = null;
				this.tabelaDeProcessos.filaDeEspacosDisponiveis.add(i);
				break;
			}
		}
	}
}
