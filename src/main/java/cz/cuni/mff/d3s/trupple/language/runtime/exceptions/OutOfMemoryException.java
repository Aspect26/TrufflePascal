package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

/**
 * This exception is thrown when the heap memory is full and the interpreter needs to allocate space for another object.
 * {@link cz.cuni.mff.d3s.trupple.language.runtime.heap.PascalHeap}
 */
public class OutOfMemoryException extends PascalRuntimeException {

    public OutOfMemoryException() {
        super("Out of memory.");
    }
}
