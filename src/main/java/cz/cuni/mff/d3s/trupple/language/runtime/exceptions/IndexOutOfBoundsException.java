package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

/**
 * Exception thrown when user is accessing an array at non-existing index,
 */
public class IndexOutOfBoundsException extends PascalRuntimeException {

    public IndexOutOfBoundsException() {
        super("Index out of bounds");
    }

}
