package computador;

public class Relogio {

	private int contadorDeCiclos;
	private int limiteDeCiclos;
	
	
	public void definirLimiteDeCiclos(int limiteDeCiclos) {
		
		if(limiteDeCiclos < 1) {
			throw new IllegalArgumentException("O limite de ciclos do Relogio deve ser maior do que 0");
		}
		
		this.limiteDeCiclos = limiteDeCiclos;
	}
	
	public void gerarCiclo() throws InterrupcaoDeRelogio {
		
		this.contadorDeCiclos++;
		
		if(this.contadorDeCiclos == this.limiteDeCiclos) {
			int numeroDeCiclos = this.contadorDeCiclos;
			this.contadorDeCiclos = 0;
			throw new InterrupcaoDeRelogio(numeroDeCiclos, "O tempo acabou");
		}
	}
	
	public void zerarRelogio() {
		this.contadorDeCiclos = 0;
	}
	
	public static int testar() {
		
		int numeroDeProblemas = 0;
		Relogio relogio = new Relogio();
		
		System.out.println("---------- Iniciando testes com o Processador ----------\n");
		
		{ // Teste 01
			System.out.print("\nTeste 01: 3 ciclos com limite 3\n-> ");
			
			relogio.definirLimiteDeCiclos(3);
			
			try {
				for(int x = 0; x < 3; x++) {
					relogio.gerarCiclo();
				}
			} catch(InterrupcaoDeRelogio r) {
				if(r.quantidadeDeCiclosExecutados() == 3) {
					teste.Teste.exibirLogDeAcerto("");
				} else {
					teste.Teste.exibirLogDeErro("Numero errados de ciclos");
					numeroDeProblemas++;
				}
			}
		}
		
		return numeroDeProblemas;
	}
}
