package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.EnumTypeDescriptor;

import java.io.Serializable;

public class EnumValue implements Serializable {

	private final EnumTypeDescriptor enumType;
	private final String value;
	
	public EnumValue(EnumTypeDescriptor type, String value) {
		this.enumType = type;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

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

	int getIntValue() {
		return this.enumType.getIdentifiers().indexOf(value);
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

    public boolean lesserThan(EnumValue compareTo) {
		return this.enumType.getIdentifiers().indexOf(this.value) < this.enumType.getIdentifiers().indexOf(compareTo.value);
	}
}
