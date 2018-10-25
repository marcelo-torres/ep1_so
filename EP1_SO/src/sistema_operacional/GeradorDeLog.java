package sistema_operacional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class GeradorDeLog {

	private static String nomeDoArquivoDeEstatisticas = "estatisticas.csv";
	private static String diretorioDosArquivo;
	
	private static FileWriter escridorDoLog = null;
	
	public static void iniciar(String diretorioDosArquivo, int quantum) throws IOException {
		GeradorDeLog.diretorioDosArquivo = diretorioDosArquivo;
		
		String nome = String.format("log%02d.txt", quantum);
		
		File arquivoDeLog = new File(diretorioDosArquivo + nome);
		GeradorDeLog.escridorDoLog = new FileWriter(arquivoDeLog, true);
		
		File arquivoDeEstatisticas = new File(diretorioDosArquivo + nomeDoArquivoDeEstatisticas);
		if(!arquivoDeEstatisticas.exists()) {
			FileWriter escritorDeEstatisticas = new FileWriter(arquivoDeEstatisticas, true);
			String linha = "Quantum,"
						 + "Média de Instruções,"
						 + "Média de trocas,"
						 + "Total de Instruções Executada,\n";
			
			escritorDeEstatisticas.write(linha);
			escritorDeEstatisticas.close();
		}
	}
	
	public static void finalizar() {
		try {
			GeradorDeLog.escridorDoLog.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static void exibirMensagemDeCarregamento(FilaDePrioridade[] filaDePronto) throws IOException {
		
		for(int prioridade = filaDePronto.length - 1; prioridade >= 0; prioridade--) {
			
			FilaDePrioridade filaDePrioridade = filaDePronto[prioridade];
			Iterator<BCP> iterador = filaDePrioridade.iterador();
			
			while(iterador.hasNext()) {
				BCP bcp = iterador.next();
				System.out.println("Carregando " + bcp.nomeDoProcesso);
				
				GeradorDeLog.escridorDoLog.write("Carregando " + bcp.nomeDoProcesso + "\n");
			}
		}
		
	}
	
	public static void exibirMensagemDeExecucao(String nomeDoProcesso) throws IOException {
		System.out.println("Executando " + nomeDoProcesso);
	}
		
	public static void exibirMensagemDeInterrupcao(String nomeDoProcesso,
				int quantidadeDeCiclosExecutados) throws IOException {
		
		if(quantidadeDeCiclosExecutados == 1) {
			System.out.println("Interrompendo " + nomeDoProcesso
			+ " após " + quantidadeDeCiclosExecutados + " instrução.");
			
			GeradorDeLog.escridorDoLog.write("Interrompendo " + nomeDoProcesso
					+ " após " + quantidadeDeCiclosExecutados + " instrução.\n");
		} else {
			System.out.println("Interrompendo " + nomeDoProcesso
			+ " após " + quantidadeDeCiclosExecutados + " instruções.");
			
			GeradorDeLog.escridorDoLog.write("Interrompendo " + nomeDoProcesso
					+ " após " + quantidadeDeCiclosExecutados + " instruções.\n");
		}
	}
	
	public static void exibirMensagemDeFimDeExecucao(BCP bcp)  throws IOException {
		System.out.println(bcp.nomeDoProcesso + " terminado. "
						   + "X=" + bcp.valorDoRegistradorX
						   + ". Y=" + bcp.valorDoRegistradorY);
		
		GeradorDeLog.escridorDoLog.write(bcp.nomeDoProcesso + " terminado. "
										+ "X=" + bcp.valorDoRegistradorX
										+ ". Y=" + bcp.valorDoRegistradorY + "\n");
	}
	
	public static void imprimirEstatisticas(int numeroDeProcessosCriados,
											int numeroDeTrocas,
											int numeroDeQuantaExecutados,
											int numeroDeIntrucoesExecutadas,
											int quantum) throws IOException {
		
		double mediaDeTrocas = numeroDeTrocas / (double)numeroDeProcessosCriados;
		double mediaDeInstrucoes = numeroDeIntrucoesExecutadas / (double) numeroDeQuantaExecutados;
		
		System.out.println("MEDIA DE TROCAS: " + mediaDeTrocas);
		System.out.println("MEDIA DE INSTRUCOES: " + mediaDeInstrucoes);
		System.out.println("QUANTUM: " + quantum);
		
		GeradorDeLog.escridorDoLog.write("MEDIA DE TROCAS: " + mediaDeTrocas + "\n"
										 + "MEDIA DE INSTRUCOES: " + mediaDeInstrucoes + "\n"
										 + "QUANTUM: " + quantum + "\n");
		
		salvarEstatisticas(quantum, mediaDeTrocas, mediaDeInstrucoes, numeroDeIntrucoesExecutadas);
	}
	
	public static void salvarEstatisticas(int quantum,
										  double mediaDeTrocas,
										  double mediaDeInstrucoes,
										  int numeroDeIntrucoesExecutadas) throws IOException {
		
		File arquivoDeEstatisticas = new File(diretorioDosArquivo + nomeDoArquivoDeEstatisticas);
		FileWriter escritor = new FileWriter(arquivoDeEstatisticas, true);
		
		String linha = quantum + ";"
					 + mediaDeInstrucoes + ";"
					 + mediaDeTrocas + ";"
					 + numeroDeIntrucoesExecutadas + ";\n";
		
		escritor.write(linha);
		escritor.close();
	}
}
