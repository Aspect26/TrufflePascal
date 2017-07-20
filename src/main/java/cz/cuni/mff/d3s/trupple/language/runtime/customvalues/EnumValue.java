package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import com.oracle.truffle.api.CompilerDirectives;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.EnumTypeDescriptor;

import java.io.Serializable;

/**
 * Represents Pascal enum-type variable. It contains descriptor of its enum type and a value it represents (stored as a
 * String).
 */
@CompilerDirectives.ValueType
public class EnumValue implements Serializable {

	private final EnumTypeDescriptor enumType;
	// TODO: it would be better if the value were represented by an integer
	private final String value;
	
	public EnumValue(EnumTypeDescriptor type, String value) {
		this.enumType = type;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

    /**
     * It equals another value if it is of the same enum type and holds the same value.
     */
	@Override
	public boolean equals(Object enumValue) {
		if (enumValue instanceof EnumValue) {
			return this.value.equals(((EnumValue) enumValue).value);
		} else {
			return super.equals(enumValue);
		}
	}

	@Override
	public int hashCode() {
	    return this.value.hashCode();
    }

	public EnumValue getNext() {
	    return this.enumType.getNext(this.value);
    }

    public EnumValue getPrevious() {
	    return this.enumType.getPrevious(this.value);
    }

    public long getOrdinalValue() {
        return this.enumType.getOrdinalValue(this.value);
    }

    /**
     * Pascal allows lesser-than operation on enum values of the same enum type. An enum value is lesser if some other
     * enum value if it was declared sooner.
     */
    public boolean lesserThan(EnumValue compareTo) {
		return this.enumType.getIdentifiers().indexOf(this.value) < this.enumType.getIdentifiers().indexOf(compareTo.value);
	}
}
