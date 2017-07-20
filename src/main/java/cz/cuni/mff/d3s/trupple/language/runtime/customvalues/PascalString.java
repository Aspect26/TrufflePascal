package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

/**
 * Representation of a string-type variable.
 */
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
    public Object getValueAt(int index) {
        return this.value.charAt(index);
    }

    @Override
    public void setValueAt(int index, Object value) {
        char newChar = (Character) value;
        this.value = this.value.substring(0, index) + newChar + this.value.substring(++index);
    }

    @Override
    public Object createDeepCopy() {
        return new PascalString(this);
    }

    @Override
    public String toString() {
        return this.value;
    }

    /**
     * Returns a new string whose value is the current value concatenated with specified character.
     * @return the new string
     */
    public PascalString concatenate(char value) {
        return new PascalString(this.value.concat(String.valueOf(value)));
    }

    /**
     * Returns a new string whose value is the current value concatenated with another string.
     * @param value the string to be appended to current value
     * @return the new string
     */
    public PascalString concatenate(PascalString value) {
        return new PascalString(this.value.concat(value.value));
    }

}
