package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

/**
 * Exception is thrown when the user tries to read from a file but he has already reached its end.
 */
public class EndOfFileException extends PascalRuntimeException {

    public EndOfFileException() {
        super("Reached the end of file");
    }

}
