package cz.cuni.mff.d3s.trupple.exceptions;

import java.io.IOException;

public class PascalRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 7001598203022655633L;
	
	public PascalRuntimeException(String message) {
		super(message);
	}

	public static class CantReadInputException extends PascalRuntimeException {

		public CantReadInputException(IOException ioException) {
			super("Can't read from input: " + ioException.getMessage());
		}
	}
}
