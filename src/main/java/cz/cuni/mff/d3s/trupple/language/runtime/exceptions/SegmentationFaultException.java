package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

/**
 * This exception is thrown when the interpreter Pascal source tries to access invalid address in the heap.
 * {@link cz.cuni.mff.d3s.trupple.language.runtime.heap.PascalHeap}
 */
public class SegmentationFaultException extends PascalRuntimeException {

    public SegmentationFaultException() {
        super("Segmentation fault.");
    }
}
