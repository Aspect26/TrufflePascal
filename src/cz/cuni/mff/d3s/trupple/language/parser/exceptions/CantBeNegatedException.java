package cz.cuni.mff.d3s.trupple.language.parser.exceptions;

public class CantBeNegatedException extends LexicalException {

    public CantBeNegatedException() {
        super("This type cannot be negated");
    }
}
