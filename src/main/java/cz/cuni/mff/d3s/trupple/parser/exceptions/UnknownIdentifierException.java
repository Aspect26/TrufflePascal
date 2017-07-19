package cz.cuni.mff.d3s.trupple.parser.exceptions;

/**
 * Exception thrown during parsing phase when usage of an undefined identifier occurs.
 */
public class UnknownIdentifierException extends LexicalException{

    public UnknownIdentifierException(String identifier) {
        super("Unknown identifier: " + identifier);
    }

}
