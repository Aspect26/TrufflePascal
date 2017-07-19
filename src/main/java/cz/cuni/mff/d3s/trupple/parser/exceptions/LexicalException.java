package cz.cuni.mff.d3s.trupple.parser.exceptions;

/**
 * A generic exception for lexical errors. They are thrown during parsing phase, caught in the {@link cz.cuni.mff.d3s.trupple.parser.NodeFactory}
 * and their message is printed to the error output stream.
 */
public class LexicalException extends Exception {

    private String message;

    public LexicalException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}