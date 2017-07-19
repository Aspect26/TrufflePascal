package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

/**
 * Exception thrown when a user tries to read from a file that is not opened for reading.
 */
public class NotOpenedToReadException extends PascalRuntimeException {

    public NotOpenedToReadException() {
        super("This file is not opened to read");
    }

}
