package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

/**
 * Exception thrown when a user provides invalid argument for the logarithm function ({@link cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic.LnBuiltinNode}).
 */
public class LogarithmInvalidArgumentException extends PascalRuntimeException {

    public LogarithmInvalidArgumentException(double value) {
        super("Invalid value for logarithm function: " + value + ". The value must be greater than 0.");
    }
}
