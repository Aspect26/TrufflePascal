package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

public class FileNotAssignedPathException extends PascalRuntimeException {

    public FileNotAssignedPathException() {
        super("This file has no assigned path, yet");
    }

}
