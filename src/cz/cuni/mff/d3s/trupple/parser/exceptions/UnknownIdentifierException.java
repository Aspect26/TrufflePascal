package cz.cuni.mff.d3s.trupple.parser.exceptions;

public class UnknownIdentifierException extends LexicalException{

    public UnknownIdentifierException(String identifier) {
        super("Unknown identifier: " + identifier);
    }

    @Override
    public String getMessage() {
        return "ssdf";
    }
}
