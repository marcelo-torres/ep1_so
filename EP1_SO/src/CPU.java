
public class CPU {
	
	private String[] segmentoDeTexto;
	
	private int contadorDePrograma;
	private int registradorX;
	private int registradorY;
	
	private boolean interrupcaoDeES;
	private boolean fimDoProcesso;
	
	
	
	public CPU() {
		this.segmentoDeTexto = null;
		
		this.contadorDePrograma = -1;
		this.registradorX = 0;
		this.registradorY = 0;
		
		this.interrupcaoDeES = false;
		this.fimDoProcesso = false;
	}
	
	
	
	public boolean interrucaoDeES() {
		return this.interrupcaoDeES;
	}
	
	public boolean fimDoProcesso() {
		return this.fimDoProcesso;
	}
	
	
	
	public void carregarProcesso(BCP bcp) {
		
		this.segmentoDeTexto = bcp.segmentoDeTexto();
		
		this.contadorDePrograma = bcp.contadorDePrograma();
		this.registradorX = bcp.registradorX();
		this.registradorY = bcp.registradorY();
		
		this.interrupcaoDeES = false;
		this.fimDoProcesso = false;
	}
	
	public int executarProcesso(int quantum) {
		
		if(quantum < 1) {
			throw new IllegalArgumentException("O quantum deve ser positivo");
		}
		
		/*
		 * Retorno: o numero de quanta que o processo executou
		 */
		
		return -1;
	}
	
	public void salvarContexto(BCP bcp) {
		
	}
}
