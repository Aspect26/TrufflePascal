package cz.cuni.mff.d3s.trupple.parser.exceptions;

/**
 * Exception thrown during parsing phase when user uses a built-in subroutine that is not supported in Trupple.
 */
public class BuiltinNotSupportedException extends LexicalException {

    public BuiltinNotSupportedException() {
        super("This builtin is not supported");
    }

}
