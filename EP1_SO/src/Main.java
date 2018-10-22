import computador.Computador;

public class Main {

	public static void main(String[] args) {
		
		try {
			Computador computador = new Computador();
			computador.ligar();
			System.out.println("classe main");
		} catch(Exception e) {
			System.out.println("Erro: " + e.getMessage());
			System.exit(1);
		}
	}
}
