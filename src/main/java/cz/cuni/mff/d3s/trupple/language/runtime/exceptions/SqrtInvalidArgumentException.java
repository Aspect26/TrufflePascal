package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

/**
 * Exception thrown when a user provides invalid argument for the square root function ({@link cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic.SqrtBuiltinNode}).
 */
public class SqrtInvalidArgumentException extends PascalRuntimeException {

    public SqrtInvalidArgumentException(double value) {
        super("Invalid value for square root function: " + value + ". The value must be greater than 0.");
    }
}
