package cz.cuni.mff.d3s.trupple.parser.exceptions;

/**
 * Exception thrown during parsing phase when a declaration of an identifier such that was declared previously in the
 * same scope occurs.
 */
public class DuplicitIdentifierException extends LexicalException {

    public DuplicitIdentifierException(String identifier) {
        super("Duplicit identifier: " + identifier);
    }
}