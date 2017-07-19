package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

/**
 * Generic runtime exception that is thrown when something unexpected has happened. In a hundred percent correct
 * implementation this exception shall never be thrown even if it may appear in a throw statement somewhere.
 */
public class UnexpectedRuntimeException extends PascalRuntimeException {

    public UnexpectedRuntimeException() {
        super("Unexpected exception occurred");
    }

}
