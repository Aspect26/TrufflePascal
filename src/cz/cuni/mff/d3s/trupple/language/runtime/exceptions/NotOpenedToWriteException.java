package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

public class NotOpenedToWriteException extends PascalRuntimeException {

    public NotOpenedToWriteException() {
        super("This file is not opened to write");
    }

}
