package cz.cuni.mff.d3s.trupple.parser.exceptions;

/**
 * Exception thrown during parsing phase when a method call with wrong number of arguments occurs.
 */
public class IncorrectNumberOfArgumentsProvidedException extends LexicalException {

    public IncorrectNumberOfArgumentsProvidedException(int required, int given) {
        super("Incorrect number of parameters provided. Required " + required + ", given: " + given);
    }

}
