package cz.cuni.mff.d3s.trupple.language.nodes.builtin.io;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.CantReadInputException;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

// TODO: refactor this class pls, this is a horrid mess
@NodeInfo(shortName = "read")
@NodeChild(value = "arguments", type = ExpressionNode[].class)
@GenerateNodeFactory
public abstract class ReadBuiltinNode extends StatementNode {

    private static final String NEW_LINE = System.getProperty("line.separator");

	@Specialization
    public void read(Object[] arguments) {
        if (arguments.length == 0) {
            readOne();
        }

        FileValue file = tryGetFileValue((Reference) arguments[0]);
        if (file != null) {
            read(file, Arrays.copyOfRange(arguments, 1, arguments.length));
        } else {
            read(null, arguments);
        }
    }

    private FileValue tryGetFileValue(Reference reference) {
        try {
            Object referenceValue = reference.getFromFrame().getObject(reference.getFrameSlot());
            if (referenceValue instanceof FileValue) {
                return (FileValue) referenceValue;
            } else {
                return null;
            }
        } catch (FrameSlotTypeException e) {
            return null;
        }
    }

    private String readOne() {
	    try {
	        return this.readString();
        } catch (IOException e) {
	        throw new CantReadInputException(e);
        }
    }

    private Object read(FileValue file, Object[] arguments) {
        for (Object argument : arguments) {
            Reference reference = (Reference)argument;
            this.readOneToReference(file, reference);
        }

        return new Object();
    }

    private void readOneToReference(FileValue file, Reference reference) {
	    try {
            switch (reference.getFrameSlot().getKind()) {
                case Byte:
                    char value = readChar(file);
                    this.setReferenceChar(reference, value);
                    break;
                case Double:
                    double doubleValue = readDouble(file);
                    this.setReferenceDouble(reference, doubleValue);
                    break;
                case Long:
                    long longValue = readLong(file);
                    this.setReferenceLong(reference, longValue);
                    break;
                case Object:
                    Object objectValue = readObject(file, reference);
                    this.setReferenceObject(reference, objectValue);
                    break;
                default:
                    throw new PascalRuntimeException("Wrong value passed to read.");
            }
        } catch (IOException e) {
	        throw new CantReadInputException(e);
        }
    }

    private char readChar(FileValue file) throws IOException {
        if (file == null) {
            Pattern delimiterPattern = PascalContext.getInstance().getInput().delimiter();
            PascalContext.getInstance().getInput().useDelimiter("");
            char value = PascalContext.getInstance().getInput().next().charAt(0);
            PascalContext.getInstance().getInput().useDelimiter(delimiterPattern);

            return value;
        } else {
            try {
                Object obj = file.read();
                return (char) obj;
            } catch (ClassCastException e) {
                // TODO: custom exception?
                throw new PascalRuntimeException("Object in a file is not a character");
            }
        }
    }

    private double readDouble(FileValue file) throws IOException {
        if (file == null) {
            return PascalContext.getInstance().getInput().nextDouble();
        } else {
            try {
                Object obj = file.read();
                return (double) obj;
            } catch (ClassCastException e) {
                // TODO: custom exception?
                throw new PascalRuntimeException("Object in a file is not a double");
            }
        }
    }

    private long readLong(FileValue file) throws IOException {
        if (file == null) {
            return PascalContext.getInstance().getInput().nextLong();
        } else {
            try {
                Object obj = file.read();
                return (long) obj;
            } catch (ClassCastException e) {
                // TODO: custom exception?
                throw new PascalRuntimeException("Object in a file is not an integral value");
            }
        }
    }

    private Object readObject(FileValue file, Reference reference) throws IOException {
        try {
            Object referenceValue = reference.getFromFrame().getObject(reference.getFrameSlot());
            if (file == null) {
                if (referenceValue instanceof String) {
                    return readString();
                } else {
                    // TODO: arrays
                    throw new PascalRuntimeException("Not supported yet.");
                }
            } else {
                return file.read();
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
	    return PascalContext.getInstance().getInput().next("[^" + NEW_LINE + "]");
    }

}
