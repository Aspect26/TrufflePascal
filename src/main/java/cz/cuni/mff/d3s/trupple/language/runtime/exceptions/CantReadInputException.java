package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

import java.io.IOException;

/**
 * Exception thrown when we are unable to read from the input.
 */
public class CantReadInputException extends PascalRuntimeException {

    public CantReadInputException(IOException ioException) {
        super("Can't read from input: " + ioException.getMessage());
    }
}