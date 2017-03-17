package cz.cuni.mff.d3s.trupple.exceptions.runtime;

public class LogarithmInvalidArgumentException extends PascalRuntimeException {

    public LogarithmInvalidArgumentException(double value) {
        super("Invalid value for logarithm function: " + value + ". The value must be greater than 0.");
    }
}
