package cz.cuni.mff.d3s.trupple.language.parser;

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