package cz.cuni.mff.d3s.trupple.parser.exceptions;

import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;

public class OutOfMemoryException extends PascalRuntimeException {

    public OutOfMemoryException() {
        super("Out of memory.");
    }
}
