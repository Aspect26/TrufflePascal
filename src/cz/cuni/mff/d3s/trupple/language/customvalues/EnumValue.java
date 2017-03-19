package cz.cuni.mff.d3s.trupple.language.customvalues;

import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.compound.EnumTypeDescriptor;

import java.io.Serializable;

public class EnumValue implements ICustomValue, Serializable {

	private final EnumTypeDescriptor enumType;
	private final String value;
	
	public EnumValue(EnumTypeDescriptor type, String value) {
		this.enumType = type;
		this.value = value;
	}
	
	public EnumTypeDescriptor getTypeDescriptor() {
		return this.enumType;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public boolean equals(Object enumValue) {
		if (enumValue instanceof EnumValue) {
			return this.getValue().equals(((EnumValue) enumValue).getValue());
		} else {
			return super.equals(enumValue);
		}
	}

	@Override
	public int hashCode() {
	    return this.getValue().hashCode();
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

    public boolean lesserThan(EnumValue compareTo) {
		return this.enumType.getIdentifiers().indexOf(this.value) < this.enumType.getIdentifiers().indexOf(compareTo.getValue());
	}
}
