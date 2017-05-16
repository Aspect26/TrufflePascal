package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

public class EndOfFileException extends PascalRuntimeException {

    public EndOfFileException() {
        super("Reached the end of file");
    }

}
