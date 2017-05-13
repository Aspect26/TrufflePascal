package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.array.PascalArray;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.IndexOutOfBoundsException;

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
    public Object getValueAt(Object index) {
        int indexValue = (int) (long) index;
        this.checkArrayIndex(indexValue);
        return this.data.charAt(indexValue);
    }

    @Override
    public Object getValueAt(Object[] indexes) {
        return this.getValueAt(indexes[0]);
    }

    @Override
    public void setValueAt(Object index, Object value) {
        int indexValue = (int) (long) index;
        this.checkArrayIndex(indexValue);
        this.data = this.data.substring(0, indexValue) + value + this.data.substring(indexValue + 1);
    }

    @Override
    public void setValueAt(Object[] indexes, Object value) {
        this.setValueAt(indexes[0], value);
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

    public static PCharValue concat(PCharValue left, PCharValue right) {
        StringBuilder newData = new StringBuilder();
        newData.append(left.data);
        newData.deleteCharAt(newData.length() - 1);
        newData.append(right.data);

        return new PCharValue(newData.toString());
    }

}
