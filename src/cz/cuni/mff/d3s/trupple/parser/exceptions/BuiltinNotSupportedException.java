package cz.cuni.mff.d3s.trupple.parser.exceptions;

public class BuiltinNotSupportedException extends LexicalException {

    public BuiltinNotSupportedException() {
        super("This builtin is not supported");
    }

}
