package cz.cuni.mff.d3s.trupple.parser.exceptions;

/**
 * Exception thrown during parsing phase when actual argument type does not match formal argument type.
 */
public class ArgumentTypeMismatchException extends LexicalException {

    public ArgumentTypeMismatchException(int argumentNumber) {
        super("Argument number " + argumentNumber + " type mismatch");
    }

}
