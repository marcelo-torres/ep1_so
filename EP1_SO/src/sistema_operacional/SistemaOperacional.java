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

/**
 * Sistemas Operacionais - Professor Clodoaldo - 2 SEM de 2018 - Turma 04
 * 
 * Ana Paula Silva de Souza - nUSP: 10391225
 * Bianca Lima Santos - nUSP: 10346811
 * Marcelo Torres do Ó - nUSP 10414571
 * Mariana Silva Santana - nUSP: 10258897
 * 
 */

/*
 * Recebe o vetor inicial de processos e o arquivo de prioridades. Eh responsavel
 * criar e inicializar as filas e os processos, gerenciar a tabela de processos
 * e as prioridades.
 */

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
	protected final int QUANTUM;
	
	private FilaDePrioridade[] filaDePronto;
	private LinkedList<BCP> filaDeBloqueado;
	
	private Relogio relogio;
	private Processador processador;
	private Escalonador escalonador;
	private Despachador despachador;
	private TabelaDeProcessos tabelaDeProcessos;
	
	private int numeroDeProcessosCriados;
	private int numeroDeTrocas = -1;
	private int numeroDeQuantaExecutados;
	private int numeroDeIntrucoesExecutadas;
	
	protected int quantidadeTotalDeProcessos;
	protected int quantidadeDeProcessosProntos;
	protected int quantidadeDeProcessosSemCreditos;
	
	
	
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
	
	
	protected FilaDePrioridade[] criarFilaDePronto() throws FileNotFoundException {
		
		int maiorPrioridade = this.obterMaiorPrioridade();
		
		FilaDePrioridade[] filaDePrioridade = new FilaDePrioridade[maiorPrioridade + 1];
		this.inicializarFilaDePronto(filaDePrioridade);
		return filaDePrioridade;
	}
	
	protected void inicializarFilaDePronto(FilaDePrioridade[] filaDePronto) {
		for(int prioridade = 0; prioridade < filaDePronto.length; prioridade++) {
			filaDePronto[prioridade] = new FilaDePrioridade(prioridade);
		}
	}
	
	/**
	 * Retorna a maior prioridade possivel dado o arquivo de prioridades
	 */
	protected int obterMaiorPrioridade() throws FileNotFoundException {
		
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
	
	
	protected void criarProcessosDeInicializacao() throws FileNotFoundException {
		
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
	
	protected BCP criarProcesso(String nomeDoArquivo, int prioridadeDoProcesso) throws FileNotFoundException {
		
		// Cria o processo
		File arquivoDeProcesso = new File(this.diretorioDeArquivos + nomeDoArquivo);
		BCP bcp = new BCP(arquivoDeProcesso);
		this.inserirBcpNaTabelaDeProcessos(bcp);
		
		bcp.prioridadeDoProcesso = prioridadeDoProcesso;
		bcp.numeroDoArquivo = this.obterNumeroDoArquivo(nomeDoArquivo);
		this.redefinirCreditos(bcp);
		
		this.numeroDeProcessosCriados++;
		
		return bcp;
	}
	
	/**
	 *  Devolve o numero do arquivo no qual o processo esta guardado
	 */
	protected int obterNumeroDoArquivo(String nomeDoArquivo) {
		
		String extensao = ".txt";
		int limite = nomeDoArquivo.length() - extensao.length();
		
		String aux = nomeDoArquivo.substring(0, limite);
		int numero = Integer.valueOf(aux);
		return numero;
	}
	
	/*
	 * Reseta os creditos, a quantidade de quantum e o quantum para os valores
	 * iniciais
	 */
	protected void redefinirCreditos(BCP bcp) {
		bcp.creditosDoProcesso = bcp.prioridadeDoProcesso;
		bcp.quantitadeDeQuantum = 1;
		bcp.quantumDoProcesso = this.QUANTUM;
	}
	
	
	
	/* ###################################################################### */
	/* ################ GERENCIAMENTO DA TABELA DE PROCESSOS ################ */
	/* ###################################################################### */
	
	// TODO remover
	public void debugar() {
		
		for(int i = this.filaDePronto.length - 1; i >= 0; i--) {
			
			FilaDePrioridade f = this.filaDePronto[i];
			
			System.out.print(i + " -> ");
			Object[] b = f.fila.toArray();
			for(Object o : b) {
				BCP bcp = (BCP)o;
				System.out.print("["+bcp.nomeDoProcesso+" c=" + bcp.creditosDoProcesso + "] ");
			}
			System.out.println();
		}
		
	}
	
	protected void iniciarExecucaoDeProcessos() throws IOException {
	
		GeradorDeLog.escreverMensagemDeCarregamento(this.filaDePronto);
		
		BCP bcpDoProcessoEscalonado = null;
		
		while(this.existeProcesso()) {
			
			if(this.necessarioRedistribuirCreditos()) {
				this.redistribuirCreditos();
			}
			
			bcpDoProcessoEscalonado = this.escalonador.escalonar();
			
			this.decrementarFilaDeBloqueado();
			LinkedList<BCP> desbloqueados = this.escalonador.obterListaDeDesbloqueados();
			
			if(bcpDoProcessoEscalonado != null) {
				
				//System.out.println("\nEscalonado: " + bcpDoProcessoEscalonado.nomeDoProcesso);
				//debugar();
				//System.out.println();
				
				this.despachador.restaurarContexto(bcpDoProcessoEscalonado);
				this.relogio.definirLimiteDeCiclos(bcpDoProcessoEscalonado.quantumDoProcesso);
				this.relogio.zerarRelogio();
				this.numeroDeTrocas++;
				
				//this.numeroDeQuantaExecutados += bcpDoProcessoEscalonado.quantitadeDeQuantum;
				
				GeradorDeLog.escreverMensagemDeExecucao(bcpDoProcessoEscalonado.nomeDoProcesso);
				bcpDoProcessoEscalonado.estadoDoProcesso = EstadosDeProcesso.PRONTO;
				int numeroDeInstrucoesExecutadas = 0;
				try {
					numeroDeInstrucoesExecutadas = this.processador.executar();
					
					//this.contabilizarQuantaUtilizado(numeroDeInstrucoesExecutadas);
					// Se nenhuma interrupcao for gerada significa que o processo chegou ao seu final
					this.despachador.salvarContexto(bcpDoProcessoEscalonado);
					this.liberarEntradaNaTabelaDeProcessos(bcpDoProcessoEscalonado);
					this.numeroDeIntrucoesExecutadas += bcpDoProcessoEscalonado.valorDoContadorDePrograma;
					
					GeradorDeLog.escreverMensagemDeFimDeExecucao(bcpDoProcessoEscalonado);
				} catch(InterrupcaoDeRelogio ir) {
					numeroDeInstrucoesExecutadas = ir.quantidadeDeCiclosExecutados();
					//this.contabilizarQuantaUtilizado(ir.quantidadeDeCiclosExecutados());
					
					boolean inserirNaFrente = this.despachador.salvarContexto(bcpDoProcessoEscalonado);
					this.escalonador.inserirNaFilaDePronto(bcpDoProcessoEscalonado);
					
					GeradorDeLog.escreverMensagemDeInterrupcao(
							bcpDoProcessoEscalonado.nomeDoProcesso,
							ir.quantidadeDeCiclosExecutados());
				} catch(InterrupcaoDeEntradaSaida ies) {
					numeroDeInstrucoesExecutadas = ies.quantidadeDeCiclosExecutados();
					//this.contabilizarQuantaUtilizado(ies.quantidadeDeCiclosExecutados());
					
					this.despachador.salvarContexto(bcpDoProcessoEscalonado);
					this.escalonador.inserirNaFilaDeBloqueado(bcpDoProcessoEscalonado);
					
					GeradorDeLog.escreverMensagemDeEntradaSaida(bcpDoProcessoEscalonado.nomeDoProcesso);
					GeradorDeLog.escreverMensagemDeInterrupcao(
							bcpDoProcessoEscalonado.nomeDoProcesso,
							ies.quantidadeDeCiclosExecutados());
				}
				
				this.contabilizarQuantaUtilizado(numeroDeInstrucoesExecutadas);
			}
			
			for(BCP bcp : desbloqueados) {
				this.escalonador.inserirNaFilaDePronto(bcp);
			}
		}
		
		GeradorDeLog.escreverEstatisticas(this.numeroDeProcessosCriados,
										  this.numeroDeTrocas,
										  this.numeroDeQuantaExecutados,
										  this.numeroDeIntrucoesExecutadas,
										  this.QUANTUM);
	}
	
	protected void contabilizarQuantaUtilizado(int numeroDeInstrucoesExecutadas) {
		this.numeroDeQuantaExecutados += Math.ceil(numeroDeInstrucoesExecutadas / (double)this.QUANTUM);
	}
	
	protected boolean existeProcesso() {
		return this.quantidadeTotalDeProcessos > 0;
	}
	
	protected boolean necessarioRedistribuirCreditos() {
		return this.quantidadeTotalDeProcessos == this.quantidadeDeProcessosSemCreditos;
	}
	
	protected boolean redistribuirCreditos() {
		
		if(this.quantidadeTotalDeProcessos != this.quantidadeDeProcessosSemCreditos) {
			return false;
		}
		
		FilaDePrioridade filaDeCreditoZero = this.filaDePronto[0];
		
		while(filaDeCreditoZero.tamanho() > 0) {
			BCP bcp = filaDeCreditoZero.removerPrimeiro();
			this.quantidadeDeProcessosSemCreditos--;
			
			FilaDePrioridade filaDePrioridadeCorrespondente =this.escalonador.filaDePrioridadeCorrespondente(bcp.creditosDoProcesso);
			
			this.redefinirCreditos(bcp);
			filaDePrioridadeCorrespondente.inserirOrdenado(bcp);
		}
		
		for(BCP bcp : this.filaDeBloqueado) {
			this.redefinirCreditos(bcp);
		}
		
		return true;
	}
	
	protected void decrementarFilaDeBloqueado() {
		for(BCP bcp : this.filaDeBloqueado) {
			bcp.tempoDeEspera--;
		}
	}
	
	
	
	/* ###################################################################### */
	/* ################ GERENCIAMENTO DA TABELA DE PROCESSOS ################ */
	/* ###################################################################### */
	
	protected int inserirBcpNaTabelaDeProcessos(BCP bcp) {
		
		if(this.tabelaDeProcessos.filaDeEspacosDisponiveis.size() == 0) {
			return -1;
		}
		
		int indice = this.tabelaDeProcessos.filaDeEspacosDisponiveis.remove();
		this.tabelaDeProcessos.tabela[indice] = bcp;
		
		return indice;
	}
	
	protected BCP acessarBcpNoIndiceNaTabela(int indice) {
		
		if(indice < 0 || indice > this.tabelaDeProcessos.tabela.length) {
			throw new IllegalArgumentException("Nao eh possivel acessar o indice "
					+ indice + " posicao invalida");
		}
		
		return this.tabelaDeProcessos.tabela[indice];
	}
	
	protected void liberarEntradaNaTabelaDeProcessos(BCP bcp) {
		
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
