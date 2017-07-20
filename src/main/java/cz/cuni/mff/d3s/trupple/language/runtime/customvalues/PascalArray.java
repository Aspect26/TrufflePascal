package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import com.oracle.truffle.api.CompilerDirectives;

/**
 * Interface for all array-type variables. Currently it is used only for {@link PCharValue} and {@link PascalString}.
 * Other arrays are stored as Object[] or arrays of primitive types for better performance.
 */
@CompilerDirectives.ValueType
public interface PascalArray {

    /**
     * Gets value at specified index.
     * @param index the index
     */
    Object getValueAt(int index);

    /**
     * Sets value at specified index.
     * @param index the index
     * @param value the value
     */
    void setValueAt(int index, Object value);

    /**
     * Creates a deep copy of the array.
     */
    Object createDeepCopy();

}