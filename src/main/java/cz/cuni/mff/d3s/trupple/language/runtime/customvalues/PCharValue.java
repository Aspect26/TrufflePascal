package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import com.oracle.truffle.api.CompilerDirectives;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.IndexOutOfBoundsException;

/**
 * Representation of variables of PChar type. It is a slight wrapper to Java's {@link String}.
 */
@CompilerDirectives.ValueType
public class PCharValue implements PascalArray {

    private String data;

    public PCharValue() {
        data = "\0";
    }

    public PCharValue(long size) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < size - 1; ++i) {
            str.append(' ');
        }
        str.append('\0');
        this.data = str.toString();
    }

    private PCharValue(PCharValue source) {
        this.data = source.data;
    }

    private PCharValue(String data) {
        this.data = data;
    }

    public void assignString(String value) {
        this.data = value + "\0";
    }

    @Override
    public String toString() {
        return data;
    }

    @Override
    public Object getValueAt(int index) {
        return this.data.charAt(index);
    }

    @Override
    public void setValueAt(int index, Object value) {
        this.checkArrayIndex(index);
        this.data = this.data.substring(0, index) + value + this.data.substring(index + 1);
    }

    @Override
    public Object createDeepCopy() {
        return new PCharValue(this);
    }

    private void checkArrayIndex(int index) {
        if (index >= this.data.length()) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Creates and returns a new PChar string which is created by concatenation of two PChar strings.
     * @param left the left argument of the concatenation operation
     * @param right the right argument of the concatenation operation
     * @return the PChar string
     */
    public static PCharValue concat(PCharValue left, PCharValue right) {
        StringBuilder newData = new StringBuilder();
        newData.append(left.data);
        newData.deleteCharAt(newData.length() - 1);
        newData.append(right.data);

        return new PCharValue(newData.toString());
    }

}
