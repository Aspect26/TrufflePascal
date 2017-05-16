package cz.cuni.mff.d3s.trupple.parser.exceptions;

public class ArgumentTypeMismatchException extends LexicalException {

    public ArgumentTypeMismatchException(int argumentNumber) {
        super("Argument number " + argumentNumber + " type mismatch");
    }

}
