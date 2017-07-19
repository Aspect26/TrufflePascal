package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

/**
 * Exception thrown when user tries to do I/O operation on a file variable but it has not assigned a path to a file.
 */
public class FileNotAssignedPathException extends PascalRuntimeException {

    public FileNotAssignedPathException() {
        super("This file has no assigned path, yet");
    }

}
