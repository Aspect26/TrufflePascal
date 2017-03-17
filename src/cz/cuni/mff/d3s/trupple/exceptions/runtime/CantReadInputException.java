package cz.cuni.mff.d3s.trupple.exceptions.runtime;

import java.io.IOException;

public class CantReadInputException extends PascalRuntimeException {

    public CantReadInputException(IOException ioException) {
        super("Can't read from input: " + ioException.getMessage());
    }
}