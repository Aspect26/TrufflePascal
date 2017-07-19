package cz.cuni.mff.d3s.trupple.parser.exceptions;

/**
 * Exception thrown during parsing phase when negation of a value that can not be negated occurs in Pascal source.
 */
public class CantBeNegatedException extends LexicalException {

    public CantBeNegatedException() {
        super("This type cannot be negated");
    }
}
