package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

public class FileNotFoundException extends PascalRuntimeException {

    public FileNotFoundException(String filePath) {
        super("File " + filePath + " not found");
    }

}
