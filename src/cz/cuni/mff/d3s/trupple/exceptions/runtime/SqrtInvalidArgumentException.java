package cz.cuni.mff.d3s.trupple.exceptions.runtime;

public class SqrtInvalidArgumentException extends PascalRuntimeException {

    public SqrtInvalidArgumentException(double value) {
        super("Invalid value for square root function: " + value + ". The value must be greater than 0.");
    }
}
