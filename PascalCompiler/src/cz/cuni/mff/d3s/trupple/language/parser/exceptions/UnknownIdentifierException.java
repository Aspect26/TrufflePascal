package cz.cuni.mff.d3s.trupple.language.parser.exceptions;

public class UnknownIdentifierException extends LexicalException{

    public UnknownIdentifierException(String identifier) {
        super("Unknown identifier: " + identifier);
    }
}
