
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
        
        System.out.println("quantum: " + quantum);
        if (quantum < 1) {
            throw new IllegalArgumentException("O quantum deve ser positivo");
        }

        int instrucoesExecutadas = 0;
        String instrucao;
        
        while (instrucoesExecutadas < quantum ) {

            if (contadorDePrograma == 21) {
                this.fimDoProcesso = true;
            }
            
        /**
         * IGNORA O CODIGO FEIO E NAO DESISTE DE MIM S2
         */     
        
            instrucao = segmentoDeTexto[instrucoesExecutadas];
            System.out.println(instrucao);

            if (instrucao.charAt(0) == 'X'){
                String var = Character.toString(instrucao.charAt(2));
                int x = Integer.parseInt(var);
                this.registradorX = x;

            }

            if (instrucao.charAt(0) == 'Y'){
                String var = Character.toString(instrucao.charAt(2));
                int y = Integer.parseInt(var);
                this.registradorY = y;
            }

            if (instrucao.charAt(0) == 'E'){
                this.interrupcaoDeES = true;

                break;
            }

            if (instrucao.charAt(0) == 'S'){
                this.fimDoProcesso = true;
                break;
            }

                
            this.contadorDePrograma++;
            instrucoesExecutadas++;
            
        }

        
        if (instrucoesExecutadas > 1)
            System.out.println("Interrompido apos " + instrucoesExecutadas + " instrucoes");
        else System.out.println("Interrompido apos " + instrucoesExecutadas + " instrucao");
        
        /*
         * Retorno: o numero de quanta que o processo executou
         */
        
        return instrucoesExecutadas;
    }

    public void salvarContexto(BCP bcp) {

        bcp.definirContadorDePrograma(this.contadorDePrograma);

    }
}
