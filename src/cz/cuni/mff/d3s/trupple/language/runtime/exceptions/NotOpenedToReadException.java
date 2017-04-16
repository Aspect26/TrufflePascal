package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

public class NotOpenedToReadException extends PascalRuntimeException {

    public NotOpenedToReadException() {
        super("This file is not opened to read");
    }

}
