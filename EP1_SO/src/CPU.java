public class CPU {

    private String[] segmentoDeTexto;

    private int instrucoesExecutadas;
    private int contadorDePrograma;
    private int registradorX;
    private int registradorY;

    private boolean interrupcaoDeES;
    private boolean fimDoProcesso;

    public CPU() {
        this.segmentoDeTexto = null;

        this.instrucoesExecutadas = 0;
        this.contadorDePrograma = -1;
        this.registradorX = 0;
        this.registradorY = 0;

        this.interrupcaoDeES = false;
        this.fimDoProcesso = false;
    }

    public void resetarInterrupcaoDeES() {
    	this.interrupcaoDeES = false;
    }
    
    public boolean interrucaoDeES() {
        return this.interrupcaoDeES;
    }

    public boolean fimDoProcesso() {
        return this.fimDoProcesso;
    }

    public int getInstrucoesExecutadas (){
        return this.instrucoesExecutadas;
    }
    
    public int getRegistradorX (){
        return this.registradorX;
    }
    
    public int getRegistradorY (){
        return this.registradorY;
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
        
        instrucoesExecutadas = 0;
        if (quantum < 1) {
            throw new IllegalArgumentException("O quantum deve ser positivo");
        }

        //int instrucoesExecutadas = 0;
        String instrucao;
        
        while (instrucoesExecutadas < quantum) {
  
        
            instrucao = this.segmentoDeTexto[this.contadorDePrograma];
            //System.out.println(instrucao);
            
            this.contadorDePrograma++;
            instrucoesExecutadas++;
            
            if (instrucao.charAt(0) == 'X'){
                String var = Character.toString(instrucao.charAt(2));
                int x = Integer.parseInt(var);
                this.registradorX = x;
            }

            else if (instrucao.charAt(0) == 'Y'){
                String var = Character.toString(instrucao.charAt(2));
                int y = Integer.parseInt(var);
                this.registradorY = y;
            }

            else if (instrucao.charAt(0) == 'E'){
                this.interrupcaoDeES = true;
                break;
            }

            else if (instrucao.charAt(0) == 'S'){
                this.fimDoProcesso = true;
                break;
            }
            
        }

        
        
        
        /*
         * Retorno: o numero de quanta que o processo executou
         */
        
        return instrucoesExecutadas;
    }

    public void salvarContexto(BCP bcp) {

        bcp.definirContadorDePrograma(this.contadorDePrograma);

    }
    
    
}