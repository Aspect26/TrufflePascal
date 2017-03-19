package cz.cuni.mff.d3s.trupple.exceptions.runtime;

public class NoBinaryRepresentationException extends PascalRuntimeException {

    public NoBinaryRepresentationException() {
        super("This type has no binary representation.");
    }
}
