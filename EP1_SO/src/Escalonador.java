
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class Escalonador {

    public static final int TEMPO_DE_ESPERA = 2;

    private TabelaDeProcessos tabelaDeProcessos;
    private CPU cpu;
    private FilasMultiplas filaDePronto;
    private FilaDeEspera filaDeBloqueado;

    private int quantum;

    public Escalonador(CPU cpu, int quantum) {
        this.tabelaDeProcessos = new TabelaDeProcessos(10);
        this.cpu = cpu;
        this.filaDePronto = new FilasMultiplas();
        this.filaDeBloqueado = new FilaDeEspera(TEMPO_DE_ESPERA);

        this.quantum = quantum;
    }

    public void iniciar() {

        BCP bcp = null;
        BCP bcpAnterior = null;

        while (this.filaDePronto.quantidadeDeProcessos() > 0
                || this.filaDeBloqueado.tamanho() > 0) {

            if (filaDePronto.quantidadeDeProcessos()
                    == filaDePronto.quantidadeDeProcessosSemCreditos()
                    && this.filaDeBloqueado.tamanho() == 0) {

                this.filaDePronto.redistribuirCreditos();
            }

            if (filaDePronto.quantidadeDeProcessos() > 0) {
                bcp = this.escalonar();

                if (bcpAnterior != bcp) {
                    if (bcpAnterior != null) {
                        this.cpu.salvarContexto(bcpAnterior);
                    }
                    this.cpu.carregarProcesso(bcp);
                }

                bcp.definirProcessoComoExecutando();
                this.cpu.executarProcesso(this.quantum * bcp.quanta());
                bcp.duplicarQuanta();
                System.out.println("Executando: " + bcp.nomeDoProcesso()); // TODO remover isso

                BCP[] bcpsProntos = this.filaDeBloqueado.decrementarTempoEspera();
                for (BCP bcpPronto : bcpsProntos) {
                    this.inserirNaFilaDePronto(bcpPronto);
                }

                if (cpu.interrucaoDeES()) {
                    System.out.println("E/S iniciado em " + bcp.nomeDoProcesso());

                    this.cpu.resetarInterrupcaoDeES();
                    this.inserirNaFilaDeBloqueado(bcp);
                } else if (cpu.fimDoProcesso()) {
                    this.tabelaDeProcessos.removerProcesso(bcp);
                    System.out.println("Removendo: " + bcp.nomeDoProcesso() + " " + bcp.contadorDePrograma());
                } else {
                    System.out.println("Interrompendo " + bcp.nomeDoProcesso() + " apos " + cpu.getInstrucoesExecutadas() + " instrucoes");
                    this.inserirNaFilaDePronto(bcp);

                }

                bcpAnterior = bcp;

            } else {
                BCP[] bcpsProntos = this.filaDeBloqueado.decrementarTempoEspera();
                for (BCP bcpPronto : bcpsProntos) {
                    this.inserirNaFilaDePronto(bcpPronto);
                }
            }

        }
    }

    public void iniciarProcessos(String[] nomeDosArquivosDosProcesso,
            String nomeDoArquivoDePrioridades) throws Exception {

        BCP[] processosCriados
                = tabelaDeProcessos.criarProcessos(
                        nomeDosArquivosDosProcesso, nomeDoArquivoDePrioridades);

        for (BCP bcp : processosCriados) {
            int prioridade = bcp.prioridadeDoProcesso();
            bcp.definirCreditosDoProcesso(prioridade);
            this.inserirNaFilaDePronto(bcp);
        }
    }

    public BCP escalonar() {
        //return this.filaDePronto.removerProximo();
        BCP bcp = this.filaDePronto.removerProximo();
        System.out.println("Escalonado: " + bcp.nomeDoProcesso());
        return bcp;
    }

    private void inserirNaFilaDePronto(BCP bcp) {
        bcp.definirProcessoComoPronto();
        System.out.println("Inserido na fila de pronto: " + bcp.nomeDoProcesso());
        this.filaDePronto.inserirNaFila(bcp);
    }

    private void inserirNaFilaDeBloqueado(BCP bcp) {
        bcp.definirProcessoComoBloqueado();
        System.out.println("Inserido na fila de bloqueado: " + bcp.nomeDoProcesso());
        this.filaDeBloqueado.inserirNaFila(bcp);
    }

    public static void main(String[] args) {

        File arquivoDoQuantum = new File("processos/quantum.txt");
        int quantum = -1;

        try (Scanner leitor = new Scanner(arquivoDoQuantum)) {
            quantum = leitor.nextInt();
        } catch (Exception e) {
            System.out.println("[!] - Erro ao ler o quantum.");
            System.out.println("\t" + e.getMessage());
            System.exit(1);
        }

        CPU cpu = new CPU();
        Escalonador escalonador = new Escalonador(cpu, quantum);

        String[] nomeDosArquivosDosProcesso = {
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

        try {
            escalonador.iniciarProcessos(nomeDosArquivosDosProcesso,
                    "processos/prioridades.txt");

        } catch (Exception e) {
            System.out.println("[!] - Erro ao carregar os processos:");
            System.out.println("\t" + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        escalonador.iniciar();
    }

}
