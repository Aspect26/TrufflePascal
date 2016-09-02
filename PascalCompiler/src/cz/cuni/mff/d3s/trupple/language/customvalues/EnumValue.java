package cz.cuni.mff.d3s.trupple.language.customvalues;

import cz.cuni.mff.d3s.trupple.language.parser.types.EnumType;

public class EnumValue implements ICustomValue {

	private final EnumType enumType;
	private final int index;
	
	public EnumValue(EnumType type, int index) {
		this.enumType = type;
		this.index = index;
	}
	
	public EnumValue(EnumType type) {
		this(type, type.getFirstIndex());
	}

	public EnumType getEnumType() {
		return enumType;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	@Override
	public Object getValue() {
		return this;
	}
}
