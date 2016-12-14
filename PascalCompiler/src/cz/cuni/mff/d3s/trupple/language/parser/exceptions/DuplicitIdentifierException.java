package cz.cuni.mff.d3s.trupple.language.parser.exceptions;

public class DuplicitIdentifierException extends LexicalException {

    public DuplicitIdentifierException(String identifier) {
        super("Duplicit identifier: " + identifier);
    }
}