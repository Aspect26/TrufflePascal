package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import java.io.IOException;
import java.util.regex.Pattern;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "read")
@NodeChild(value = "arguments", type = ExpressionNode[].class)
public abstract class ReadBuiltinNode extends BuiltinNode {

    protected static final String NEW_LINE = System.getProperty("line.separator");

    public ReadBuiltinNode(PascalContext context) {
        super(context);
    }

    protected PascalContext getContext() {
        return this.context;
    }

	@Specialization
    public Object read(Object[] arguments) {
        if (arguments.length == 0) {
            return readOne();
        }

        for (Object argument : arguments) {
            Reference reference = (Reference)argument;
            this.readOneToReference(reference);
        }

        return new Object();
    }

    private String readOne() {
	    try {
	        return this.readString();
        } catch (IOException e) {
	        throw new PascalRuntimeException.CantReadInputException(e);
        }
    }

    private void readOneToReference(Reference reference) {
	    try {
            switch (reference.getFrameSlot().getKind()) {
                case Byte:
                    char value = readChar();
                    this.setReferenceChar(reference, value);
                    break;
                case Double:
                    double doubleValue = readDouble();
                    this.setReferenceDouble(reference, doubleValue);
                    break;
                case Long:
                    long longValue = readLong();
                    this.setReferenceLong(reference, longValue);
                    break;
                case Object:
                    Object objectValue = readObject(reference);
                    this.setReferenceObject(reference, objectValue);
                    break;
                default:
                    throw new PascalRuntimeException("Wrong value passed to read.");
            }
        } catch (IOException e) {
	        throw new PascalRuntimeException.CantReadInputException(e);
        }
    }

    // TODO: TP can't read boolean but it would be a good feature
    private boolean readBoolean() {
        return false;
    }

    private char readChar() throws IOException {
        Pattern delimiterPattern = this.getContext().getInput().delimiter();
        this.getContext().getInput().useDelimiter("");
	    char value = this.getContext().getInput().next().charAt(0);
	    this.getContext().getInput().useDelimiter(delimiterPattern);

	    return value;
    }

    private double readDouble() throws IOException {
        return this.getContext().getInput().nextDouble();
    }

    private long readLong() throws IOException {
        return this.getContext().getInput().nextLong();
    }

    private Object readObject(Reference reference) throws IOException {
	    try {
            Object referenceValue = reference.getFromFrame().getObject(reference.getFrameSlot());
            if (referenceValue instanceof String) {
                return readString();
            } else {
                // TODO: arrays
                throw new PascalRuntimeException("Not supported yet.");
            }
        } catch (FrameSlotTypeException e) {
	        throw new PascalRuntimeException("Unknown read type");
        }
    }

    private String readString() throws IOException {
        return this.readUntilNewline();
    }

    private void setReferenceChar(Reference reference, char value) {
	    reference.getFromFrame().setByte(reference.getFrameSlot(), (byte) value);
    }

    private void setReferenceDouble(Reference reference, double value) {
        reference.getFromFrame().setDouble(reference.getFrameSlot(), value);
    }

    private void setReferenceLong(Reference reference, long value) {
        reference.getFromFrame().setLong(reference.getFrameSlot(), value);
    }

    private void setReferenceObject(Reference reference, Object value) {
        reference.getFromFrame().setObject(reference.getFrameSlot(), value);
    }

    private String readUntilNewline() {
	    return this.getContext().getInput().next("[^" + NEW_LINE + "]");
    }

}
