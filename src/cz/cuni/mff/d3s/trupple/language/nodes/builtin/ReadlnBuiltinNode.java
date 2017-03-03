package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import java.io.IOException;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;

@NodeInfo(shortName = "readln")
public abstract class ReadlnBuiltinNode extends BuiltinNode {

    private static final String NEW_LINE = System.getProperty("line.separator");

	@Specialization
    public Object read(Object[] arguments) {
	    Object result = new Object();

        if (arguments.length == 0) {
            this.readNewLine();
            return result;
        }

        for (Object argument : arguments) {
            Reference reference = (Reference)argument;
            result = this.readOne(reference);
        }

        return result;
    }

    private Object readOne(Reference reference) {
	    try {
            switch (reference.getFrameSlot().getKind()) {
                // TODO: TP can't read boolean but it would be a good feature
                /*
                case Boolean:
                return readBoolean(reference);*/
                case Byte:
                    return readChar(reference);
                case Double:
                    return readDouble(reference);
                case Long:
                    return readLong(reference);
                case Object:
                    return readObject(reference);
                default:
                    throw new PascalRuntimeException("Wrong value passed to read.");
            }
        } catch (IOException e) {
	        throw new PascalRuntimeException.CantReadInputException(e);
        }
    }


    private boolean readBoolean(Reference reference) {
        return false;
    }

    private char readChar(Reference reference) throws IOException {
	    int readCode = this.readNextChar();
	    FrameSlot slot = reference.getFrameSlot();
	    reference.getFromFrame().setByte(slot, (byte) readCode);

	    return (char) readCode;
    }

    private double readDouble(Reference reference) throws IOException {
	    String readString = this.readToWhiteSpace(true);

	    double readValue = Double.parseDouble(readString);
        FrameSlot slot = reference.getFrameSlot();
        reference.getFromFrame().setDouble(slot, readValue);

        return readValue;
    }

    private long readLong(Reference reference) throws IOException {
        String readString = this.readToWhiteSpace(true);

        long readValue = Long.parseLong(readString);
        FrameSlot slot = reference.getFrameSlot();
        reference.getFromFrame().setLong(slot, readValue);

        return readValue;
    }

    private Object readObject(Reference reference) throws IOException {
	    try {
            Object referenceValue = reference.getFromFrame().getObject(reference.getFrameSlot());
            if (referenceValue instanceof String) {
                return readString(reference);
            } else {
                throw new PascalRuntimeException("Not supported yet.");
            }
        } catch (FrameSlotTypeException e) {
	        throw new PascalRuntimeException("Unknown read type");
        }
	    // TODO: arrays
    }

    private String readString(Reference reference) throws IOException {
        String readString = this.readToWhiteSpace(false);
        FrameSlot slot = reference.getFrameSlot();
        reference.getFromFrame().setObject(slot, readString);

        return readString;
    }

    private String readToWhiteSpace(boolean removeLeadingWhitespaces) throws IOException {
        StringBuilder readString = new StringBuilder();
        int readCode = this.readNextChar();
        if (removeLeadingWhitespaces) {
            while(Character.isWhitespace(readCode)) {
                readCode = this.readNextChar();
            }
        }

        readString.append((char)readCode);
        readCode = this.readNextChar();
        while (!Character.isWhitespace(readCode)) {
            readString.append((char)readCode);
            readCode = this.readNextChar();
        }

        return readString.toString();
    }

    private void readNewLine() {
	    try {
            int readCode = this.readNextChar();
            while (!NEW_LINE.equals(Character.toString((char) readCode))) {
                readCode = this.readNextChar();
            }
        } catch (IOException e) {
	        throw new PascalRuntimeException.CantReadInputException(e);
        }
    }

    private int readNextChar() throws IOException {
	    return this.getContext().getInput().read();
    }
}
