package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

/**
 * Exception thrown when a file with specified path can not be found.
 */
public class FileNotFoundException extends PascalRuntimeException {

    public FileNotFoundException(String filePath) {
        super("File " + filePath + " not found");
    }

}
