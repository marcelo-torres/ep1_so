 package computador.processador;

import computador.Relogio;
import computador.InterrupcaoDeEntradaSaida;
import computador.InterrupcaoDeRelogio;

public class Processador {

	private Relogio relogio;
	
	private Registrador contadorDePrograma;
	private Registrador registradorX;
	private Registrador registradorY;
	
	private String[] segmentoDeTexto;
	
	
	public Processador(Relogio relogio) {
		this.relogio = relogio;
		
		this.contadorDePrograma = new Registrador();
		this.registradorX = new Registrador();
		this.registradorY = new Registrador();
	}
	
	
	public int executar() throws InterrupcaoDeRelogio, InterrupcaoDeEntradaSaida {
		
		int numeroDeInstrucoesExecutadas = 0;
		
		while(true) {
			
			String instrucao = this.segmentoDeTexto[this.contadorDePrograma.valorArmazenado()];
			
			this.contadorDePrograma.incrementar();
			numeroDeInstrucoesExecutadas++;
			
			char operacao = instrucao.charAt(0);
			String var;
			
			switch(operacao) {
			
			case 'X':
				var = instrucao.substring(2);
				int x = Integer.parseInt(var);
				this.registradorX.definirValorArmazenado(x);
				break;
			
			case 'Y':
				var = instrucao.substring(2);
				int y = Integer.parseInt(var);
				this.registradorY.definirValorArmazenado(y);
				break;
			
			case 'E':
				// TODO: escrever algo
				throw new InterrupcaoDeEntradaSaida(numeroDeInstrucoesExecutadas, "");
			
			case 'S':
				return numeroDeInstrucoesExecutadas;
			
			default:
				// Instrucao comum
			}
			
			this.relogio.gerarCiclo();
		}
	}
	
	public void definirValorDoContadorDePrograma(int valor) {
		this.contadorDePrograma.definirValorArmazenado(valor);
	}
	
	public int valorDoContadorDePrograma() {
		return this.contadorDePrograma.valorArmazenado();
	}
	
	public void definirValorDoRegistradorX(int valor) {
		this.registradorX.definirValorArmazenado(valor);
	}
	
	public int valorDoRegistradorX() {
		return this.registradorX.valorArmazenado();
	}
	
	public void definirValorDoRegistradorY(int valor) {
		this.registradorY.definirValorArmazenado(valor);
	}
	
	public int valorDoRegistradorY() {
		return this.registradorY.valorArmazenado();
	}
	
	public void definirSegmentoDeTexto(String[] segmentoDeTexto) {
		this.segmentoDeTexto = segmentoDeTexto;
	}
	
	public String[] segmentoDeTexto() {
		return this.segmentoDeTexto;
	}
}
