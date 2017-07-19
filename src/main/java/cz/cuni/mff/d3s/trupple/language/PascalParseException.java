package cz.cuni.mff.d3s.trupple.language;

/**
 * Exception thrown if an error occurred while parsing input Pascal source.
 */
public class PascalParseException extends Exception {

    private final String sourceName;

    PascalParseException(String sourceName) {
        this.sourceName = sourceName;
    }

    @Override
    public String getMessage() {
        return "Errors while parsing source " + sourceName + ".";
    }

}
