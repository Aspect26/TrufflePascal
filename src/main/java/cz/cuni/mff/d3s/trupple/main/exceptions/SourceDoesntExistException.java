package cz.cuni.mff.d3s.trupple.main.exceptions;

public class SourceDoesntExistException extends CompilerException {

    public SourceDoesntExistException(String path) {
        super("The specified source doesn't exist: " + path + ".", ExitCodesEnum.EXIT_CODE_SOURCE_DOESNT_EXIST);
    }
}
