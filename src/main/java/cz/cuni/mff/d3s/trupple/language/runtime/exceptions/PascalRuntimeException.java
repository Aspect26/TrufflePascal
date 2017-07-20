package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

/**
 * A generic runtime exception with arbitrary message.
 */
public class PascalRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 7001598203022655633L;
	
	public PascalRuntimeException(String message) {
		super(message);
	}

}
