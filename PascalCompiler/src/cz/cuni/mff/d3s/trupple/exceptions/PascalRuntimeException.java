package cz.cuni.mff.d3s.trupple.exceptions;

public class PascalRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 7001598203022655633L;
	
	public PascalRuntimeException() {
		super();
	}
	
	public PascalRuntimeException(String message) {
		super(message);
	}
}
