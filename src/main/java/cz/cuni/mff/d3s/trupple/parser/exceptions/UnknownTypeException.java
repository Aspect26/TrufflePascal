package cz.cuni.mff.d3s.trupple.parser.exceptions;

public class UnknownTypeException extends LexicalException {

    public UnknownTypeException(String typeIdentifier) {
        super("Unknown type: " + typeIdentifier);
    }
}
