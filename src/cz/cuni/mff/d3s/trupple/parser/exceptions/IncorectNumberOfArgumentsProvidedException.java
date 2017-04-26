package cz.cuni.mff.d3s.trupple.parser.exceptions;

public class IncorectNumberOfArgumentsProvidedException extends LexicalException {

    public IncorectNumberOfArgumentsProvidedException(int required, int given) {
        super("Incorrect number of parameters provided. Required " + required + ", given: " + given);
    }

}
