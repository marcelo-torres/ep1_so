package sistema_operacional;

import computador.processador.Processador;

import java.io.File;
import java.io.FileNotFoundException;

import computador.InterrupcaoDeEntradaSaida;
import computador.InterrupcaoDeRelogio;
import computador.Relogio;

public class SistemaOperacional {
	
	private String[] vetorDeProcessosInicias = {
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
	
	private final int QUANTUM;
	
	private Relogio relogio;
	private Processador processador;
	private Escalonador escalonador;
	private Despachador despachador;
	private TabelaDeProcessos tabelaDeProcessos;
	
	public SistemaOperacional(int quantum, Relogio relogio, Processador processador) {
		if(quantum < 1) {
			throw new IllegalArgumentException("O quantum deve ser positivo");
		} else {
			this.QUANTUM = quantum;
		}
		
		this.relogio = relogio;
		this.processador = processador;
		this.escalonador = new Escalonador();
		this.despachador = new Despachador(processador, escalonador);
		this.tabelaDeProcessos = new TabelaDeProcessos(10);
	}
	
	
	public void iniciarSistema() throws FileNotFoundException {
		this.criarProcessosDeInicializacao();
		this.iniciarProcessos();
	}
	
	public void criarProcessosDeInicializacao() throws FileNotFoundException {
		
		for(String nomeDeArquivoDeProcesso : this.vetorDeProcessosInicias) {
			File arquivoDeProcesso = new File(nomeDeArquivoDeProcesso);
			
			BCP bcp = new BCP(arquivoDeProcesso);
			
			int indiceDeInsercao = this.tabelaDeProcessos.inserirBcp(bcp);
			//escalonador.escalonar(indiceDeInsercao);
		}
		
	}
	
	public void iniciarProcessos() {
	
		BCP bcpAnterior = null;
		BCP bcpAtual = null;
		
		while(escalonador.existeProcesso()) {
			
			// Verificar necessidade de redistribuir os creditos
			
			bcpAtual = despachador.despachar(bcpAnterior);
			
			if(bcpAtual != null) {
				this.relogio.definirLimiteDeCiclos(bcpAtual.quantumDoProcesso());
				
				try {
					this.relogio.zerarRelogio();
					int numeroDeInstrucoesExecutadas = this.processador.executar();
					
					this.tabelaDeProcessos.liberarIndice(-1);
				} catch(InterrupcaoDeRelogio ir) {
					this.escalonador.inserirNaFilaDePronto(bcpAtual);
					// Dobrar o quantum aqui?
				} catch(InterrupcaoDeEntradaSaida ies) {
					this.escalonador.inserirNaFilaDeBloqueado(bcpAtual);
					// Dobrar o quantum aqui?
				}
			}
			
			// Derementar tempo da fila de bloqueados
			
			bcpAnterior = bcpAtual;
		}
	}
}
