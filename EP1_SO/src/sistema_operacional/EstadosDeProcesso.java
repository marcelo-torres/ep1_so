package sistema_operacional;

/*
 * Informa o estado de um processo. Este pode ser “pronto”, “bloqueado” ou
 * “executando”.
 */

public enum EstadosDeProcesso {
	PRONTO(0), BLOQUEADO(1), EXECUTANDO(2);
	
	public int codigoDoEstado;
	
	
	private EstadosDeProcesso(int codigoDoEstado) {
		this.codigoDoEstado = codigoDoEstado;
	}
}
