package classes_antigas;

public enum EstadosDeProcesso {
	PRONTO(0), BLOQUEADO(1), EXECUTANDO(2);
	
	public int codigoDoEstado;
	
	private EstadosDeProcesso(int codigoDoEstado) {
		this.codigoDoEstado = codigoDoEstado;
	}
	
	
}
