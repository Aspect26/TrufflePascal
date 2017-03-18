package cz.cuni.mff.d3s.trupple.language.parser.exceptions;

import cz.cuni.mff.d3s.trupple.exceptions.runtime.PascalRuntimeException;

public class OutOfMemoryException extends PascalRuntimeException {

    public OutOfMemoryException() {
        super("Out of memory.");
    }
}
