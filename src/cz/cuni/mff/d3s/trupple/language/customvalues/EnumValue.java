package cz.cuni.mff.d3s.trupple.language.customvalues;

import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.compound.EnumTypeDescriptor;

public class EnumValue implements ICustomValue {

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

	int getIntValue() {
		return this.enumType.getIdentifiers().indexOf(value);
	}

}
