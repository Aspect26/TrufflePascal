package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

/**
 * Exception thrown when a user tries to write to a file that is not opened for writing.
 */
public class NotOpenedToWriteException extends PascalRuntimeException {

    public NotOpenedToWriteException() {
        super("This file is not opened to write");
    }

}
