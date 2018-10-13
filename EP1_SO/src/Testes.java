
public class Testes {

	public static int testeGeral() {
		
		int numeroDeProblemas = 0;
		
		numeroDeProblemas = BCP.testar()
						  + RoundRobin.testar()
						  + FilasMultiplas.testar()
						  + FilaDeEspera.testar();
		
		return numeroDeProblemas;
	}
	
	public static void main(String[] args) {
	
		System.out.println("------ Testes Gerais ------");
		
		
		int numeroDeProblemas = 0;
		
		numeroDeProblemas += testeGeral();
		
		System.out.println("----------== FIM DOS TESTES ==----------\n");
		
		if(numeroDeProblemas == 0) {
			System.out.println("\tNENHUM erro foi encontrado");
		} else {
			System.out.println("\t[!] - " + numeroDeProblemas + " PROBLEMAS FORAM ENCONTRADOS!!!");
		}
		
	}
}
