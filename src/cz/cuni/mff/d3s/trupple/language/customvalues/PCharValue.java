package cz.cuni.mff.d3s.trupple.language.customvalues;

import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.IndexOutOfBoundsException;

public class PCharValue implements PascalArray {

    private String data;

    public PCharValue() {
        data = "\0";
    }

    private PCharValue(PCharValue source) {
        this.data = source.data;
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

}
