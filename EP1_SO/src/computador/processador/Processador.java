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
	
	public static int testar() {
		
		int numeroDeProblemas = 0;
		
		Relogio relogio = new Relogio();
		Processador processador = new Processador(relogio);
		
		System.out.println("---------- Iniciando testes com o Processador ----------\n");
		
		{ // Teste 01
			System.out.print("\nTeste 01: executando programa de tamanho 6 com quantum 6\n-> ");
			
			String[] seg1 = {
				"COM",
				"COM",
				"COM",
				"COM",
				"COM",
				"SAIDA"
			};
			
			processador.definirValorDoContadorDePrograma(0);
			processador.definirSegmentoDeTexto(seg1);
			
			relogio.definirLimiteDeCiclos(6);
			int numeroDeInstrucoesExecutadas;
			try {
				numeroDeInstrucoesExecutadas = processador.executar();
				
				if(numeroDeInstrucoesExecutadas == 6) {
					teste.Teste.exibirLogDeAcerto("");
				} else {
					teste.Teste.exibirLogDeErro("");
					numeroDeProblemas++;
				}
			} catch(Exception e) {
				// Nenhuma exception deveria ser lancada.
				teste.Teste.exibirLogDeErro("Interrupcao inesperada: " + e.getMessage());
				numeroDeProblemas++;
				e.printStackTrace();
			}
			relogio.zerarRelogio();
		}
		
		{ // Teste 02
			System.out.print("\nTeste 02: executando programa de tamanho 6 com quantum 3\n-> ");
			
			String[] seg2 = {
				"COM",
				"COM",
				"COM",
				"COM",
				"COM",
				"SAIDA"
			};
			
			processador.definirValorDoContadorDePrograma(0);
			processador.definirSegmentoDeTexto(seg2);
			
			relogio.definirLimiteDeCiclos(3);
			int numeroDeInstrucoesExecutadas;
			try {
				processador.executar();
			} catch(InterrupcaoDeEntradaSaida es) {
				teste.Teste.exibirLogDeErro("Interrupcao inesperada: " + es.getMessage());
				numeroDeProblemas++;
				es.printStackTrace();
			} catch(InterrupcaoDeRelogio r) {
				try {
					
					relogio.zerarRelogio();
					numeroDeInstrucoesExecutadas = processador.executar();
					
					if(numeroDeInstrucoesExecutadas == 3) {
						teste.Teste.exibirLogDeAcerto("");
					} else {
						teste.Teste.exibirLogDeErro("");
						numeroDeProblemas++;
					}
				} catch(Exception e) {
					// Nenhuma exception deveria ser lancada.
					teste.Teste.exibirLogDeErro("Interrupcao inesperada: " + e.getMessage());
					numeroDeProblemas++;
					e.printStackTrace();
				}
			}
			relogio.zerarRelogio();
		}
		
		{ // Teste 03
			System.out.print("\nTeste 03: executando programa de tamanho 7 com quantum 3\n-> ");
			
			String[] seg3 = {
				"COM",
				"COM",
				"COM",
				"COM",
				"COM",
				"COM",
				"SAIDA"
			};
			
			processador.definirValorDoContadorDePrograma(0);
			processador.definirSegmentoDeTexto(seg3);
			relogio.definirLimiteDeCiclos(3);
			int numeroDeInstrucoesExecutadas;
			try {
				relogio.zerarRelogio();
				processador.executar();
			} catch(InterrupcaoDeEntradaSaida es) {
				teste.Teste.exibirLogDeErro("Interrupcao inesperada: " + es.getMessage());
				numeroDeProblemas++;
				es.printStackTrace();
			} catch(InterrupcaoDeRelogio r) {
				try {
					relogio.zerarRelogio();
					processador.executar();
				} catch(InterrupcaoDeEntradaSaida es) {
					teste.Teste.exibirLogDeErro("Interrupcao inesperada: " + es.getMessage());
					numeroDeProblemas++;
					es.printStackTrace();
				} catch(InterrupcaoDeRelogio r2) {
					try {
						
						relogio.zerarRelogio();
						numeroDeInstrucoesExecutadas = processador.executar();
						
						if(numeroDeInstrucoesExecutadas == 1) {
							teste.Teste.exibirLogDeAcerto("");
						} else {
							teste.Teste.exibirLogDeErro("");
							numeroDeProblemas++;
						}
					} catch(Exception e) {
						// Nenhuma exception deveria ser lancada.
						teste.Teste.exibirLogDeErro("Interrupcao inesperada: " + e.getMessage());
						numeroDeProblemas++;
						e.printStackTrace();
					}
				}
			}
			
			relogio.zerarRelogio();
		}
		
		{ // Teste 04
			System.out.print("\nTeste 04: executando programa de tamanho 1 com quantum 3\n-> ");
			
			String[] seg4 = {
				"SAIDA"
			};
			
			processador.definirValorDoContadorDePrograma(0);
			processador.definirSegmentoDeTexto(seg4);
			relogio.definirLimiteDeCiclos(3);
			int numeroDeInstrucoesExecutadas;
			
			try {
				relogio.zerarRelogio();
				processador.executar();
				teste.Teste.exibirLogDeAcerto("");
			} catch(Exception e) {
				teste.Teste.exibirLogDeErro("Interrupcao inesperada: " + e.getMessage());
				numeroDeProblemas++;
				e.printStackTrace();
			}
		}
		
		{ // Teste 05
			System.out.print("\nTeste 05: executando programa de tamanho 2 com quantum 3\n-> ");
			
			String[] seg5 = {
				"COM",
				"SAIDA"
			};
			
			processador.definirValorDoContadorDePrograma(0);
			processador.definirSegmentoDeTexto(seg5);
			relogio.definirLimiteDeCiclos(3);
			int numeroDeInstrucoesExecutadas;
			
			try {
				relogio.zerarRelogio();
				processador.executar();
				teste.Teste.exibirLogDeAcerto("");
			} catch(Exception e) {
				teste.Teste.exibirLogDeErro("Interrupcao inesperada: " + e.getMessage());
				numeroDeProblemas++;
				e.printStackTrace();
			}
		}
		
		{ // Teste 06
			System.out.print("\nTeste 06: executando programa de tamanho 5 com quantum 3\n-> ");
			String[] seg6 = {
					"Y=12",
					"X=123456",
					"X=66",
					"Y=999",
					"SAIDA"
				};
			
			processador.definirValorDoContadorDePrograma(0);
			processador.definirSegmentoDeTexto(seg6);
			relogio.definirLimiteDeCiclos(3);
			
			try {
				relogio.zerarRelogio();
				processador.executar();
			} catch(InterrupcaoDeEntradaSaida es) {
				teste.Teste.exibirLogDeErro("Interrupcao inesperada: " + es.getMessage());
				numeroDeProblemas++;
				es.printStackTrace();
			} catch(InterrupcaoDeRelogio r) {
				try {
					relogio.zerarRelogio();
					processador.executar();
					if(processador.valorDoRegistradorX() == 66 && processador.valorDoRegistradorY() == 999) {
						teste.Teste.exibirLogDeAcerto("");
					} else {
						teste.Teste.exibirLogDeErro("Os valores dos registradores diferem do valor correto");
						numeroDeProblemas++;
					}
				} catch(InterrupcaoDeEntradaSaida es) {
					teste.Teste.exibirLogDeErro("Interrupcao inesperada: " + es.getMessage());
					numeroDeProblemas++;
					es.printStackTrace();
				} catch(InterrupcaoDeRelogio r2) {
					teste.Teste.exibirLogDeErro("Interrupcao inesperada: " + r2.getMessage());
					numeroDeProblemas++;
					r2.printStackTrace();
					
				}
			}
		}
		
		{ // Teste 07
			System.out.print("\nTeste 07: executando programa de tamanho 2 com E/S no segundo comando com quantum 3\n-> ");
			String[] seg7 = {
					"E/S",
					"SAIDA"
				};
			
			processador.definirValorDoContadorDePrograma(0);
			processador.definirSegmentoDeTexto(seg7);
			relogio.definirLimiteDeCiclos(3);
			
			try {
				relogio.zerarRelogio();
				processador.executar();
			} catch(InterrupcaoDeEntradaSaida es) {
				try {
					relogio.zerarRelogio();
					int numeroDeComandosExecutados = processador.executar();
					if(numeroDeComandosExecutados == 1) {
						teste.Teste.exibirLogDeAcerto("");
					} else {
						teste.Teste.exibirLogDeErro("Numero errado de instrucoes executadas");
						numeroDeProblemas++;
					}
				} catch(Exception e) {
					teste.Teste.exibirLogDeErro("Interrupcao inesperada: " + e.getMessage());
					numeroDeProblemas++;
					e.printStackTrace();
				}
			} catch(InterrupcaoDeRelogio r) {
				teste.Teste.exibirLogDeErro("Interrupcao inesperada: " + r.getMessage());
				numeroDeProblemas++;
				r.printStackTrace();
			}
			
		}
		
		{ // Teste 07
			System.out.print("\nTeste 07: executando programa de tamanho 6 com E/S no terceiro comando com quantum 3\n-> ");
			String[] seg7 = {
					"COM",
					"COM",
					"E/S",
					"COM",
					"COM",
					"SAIDA"
				};
			
			processador.definirValorDoContadorDePrograma(0);
			processador.definirSegmentoDeTexto(seg7);
			relogio.definirLimiteDeCiclos(3);
			
			try {
				relogio.zerarRelogio();
				processador.executar();
			} catch(InterrupcaoDeEntradaSaida es) {
				try {
					relogio.zerarRelogio();
					int numeroDeComandosExecutados = processador.executar();
					if(numeroDeComandosExecutados == 3) {
						teste.Teste.exibirLogDeAcerto("");
					} else {
						teste.Teste.exibirLogDeErro("Numero errado de instrucoes executadas");
						numeroDeProblemas++;
					}
				} catch(Exception e) {
					teste.Teste.exibirLogDeErro("Interrupcao inesperada: " + e.getMessage());
					numeroDeProblemas++;
					e.printStackTrace();
				}
			} catch(InterrupcaoDeRelogio r) {
				teste.Teste.exibirLogDeErro("Interrupcao inesperada: " + r.getMessage());
				numeroDeProblemas++;
				r.printStackTrace();
			}
			
		}
		
		System.out.println("Erros na classe Processador: " + numeroDeProblemas);
		
		return numeroDeProblemas;
	}
}
