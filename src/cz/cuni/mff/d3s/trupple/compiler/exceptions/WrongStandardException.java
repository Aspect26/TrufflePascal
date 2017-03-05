package cz.cuni.mff.d3s.trupple.compiler.exceptions;

public class WrongStandardException extends CompilerException {

    public WrongStandardException(String standard) {
        super("Wrong standard chosen: " + standard, ExitCodesEnum.EXIT_CODE_WRONG_STANDARD);
    }
}
