package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.array.PascalArray;

public class PascalString implements PascalArray {

    private String value;

    public PascalString() {
        this.value = "";
    }

    public PascalString(String value) {
        this.value = value;
    }

    private PascalString(PascalString pascalString) {
        this.value = pascalString.value;
    }

    @Override
    public Object getValueAt(Object index) {
        int intIndex = this.getIndex(index);
        return this.value.charAt(intIndex);
    }

    @Override
    public Object getValueAt(Object[] indexes) {
        return this.getValueAt(indexes[0]);
    }

    @Override
    public void setValueAt(Object index, Object value) {
        int intIndex = this.getIndex(index);
        char newChar = (Character) value;
        this.value = this.value.substring(0, intIndex) + newChar + this.value.substring(++intIndex);
    }

    @Override
    public void setValueAt(Object[] indexes, Object value) {
        this.setValueAt(indexes[0], value);
    }

    @Override
    public Object createDeepCopy() {
        return new PascalString(this);
    }

    @Override
    public String toString() {
        return this.value;
    }

    public PascalString concatenate(char value) {
        return new PascalString(this.value.concat(String.valueOf(value)));
    }

    public PascalString concatenate(PascalString value) {
        return new PascalString(this.value.concat(value.value));
    }

    private int getIndex(Object index) {
        int intIndex = (int) (long) index;
        if (intIndex >= this.value.length()) {
            throw new IndexOutOfBoundsException();
        }

        return intIndex;
    }
}
