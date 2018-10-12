import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class TabelaDeProcessos {

	private ArrayList<BCP> tabelaDeProcessos;
	
	
	
	public TabelaDeProcessos(int tamanhoInicial) {
		this.tabelaDeProcessos = new ArrayList<BCP>(tamanhoInicial);
	}
	
	
	
	public BCP criarProcesso(String nomeDoArquivoDoProcesso, int prioridadeDoProcesso) throws FileNotFoundException {
		
		File arquivoDoProcesso = new File(nomeDoArquivoDoProcesso);
		
		BCP bcp = new BCP(arquivoDoProcesso);
		bcp.definirPrioridadeDoProcesso(prioridadeDoProcesso);
		this.tabelaDeProcessos.add(bcp);
		
		return bcp;
	}
	
	public BCP[] criarProcessos(String[] nomeDosArquivoDosProcesso, String nomeDoArquivoDePrioridades) throws Exception {
		
		LinkedList<BCP> processosCriados = new LinkedList<BCP>();
		
		File arquivoDePrioridades = new File(nomeDoArquivoDePrioridades);
		
		try(Scanner leitorPrioridades = new Scanner(arquivoDePrioridades)) {
		
			for(String nomeDoArquivoDoProcesso : nomeDosArquivoDosProcesso) {
				
				int prioridade = leitorPrioridades.nextInt();
				
				try {
					BCP bcp = this.criarProcesso(nomeDoArquivoDoProcesso, prioridade);
					processosCriados.add(bcp);
				} catch(FileNotFoundException fnfe) {
					System.out.println("[!] - Erro ao tentar carregar o processo do arquivo" + nomeDoArquivoDoProcesso);
					System.out.println("\t" + fnfe.getMessage());
				}
			}
			
		} catch (FileNotFoundException fnfe) {
			throw new Exception("Nao foi possivel ler o arquivo de prioridades:\n\t" 
								+ fnfe.getMessage());
		}
		
		Object[] vetorAuxiliar = processosCriados.toArray();
		BCP[] bcps = new BCP[vetorAuxiliar.length];
		
		for(int i = 0; i < bcps.length; i++) {
			bcps[i] = (BCP)vetorAuxiliar[i];
		}
		
		return bcps;
	}
	
	public boolean removerProcesso(BCP bcp) {
		return this.tabelaDeProcessos.remove(bcp);
	}
	
}
