package cz.cuni.mff.d3s.trupple.language.customvalues;

import cz.cuni.mff.d3s.trupple.language.customtypes.IOrdinalType;

public class PascalArray implements ICustomValue {
	private final Object[] array;
	private final IOrdinalType sourceOrdinal;
	
	public PascalArray(String typeIdentifier, IOrdinalType ordinal) {
		this.sourceOrdinal = ordinal;
		final int size = ordinal.getSize();
		
		// this needs to be here so the array can be final
		switch(typeIdentifier){
		// integer types
		case "integer":
		case "shortint":
		case "longint":
		case "byte":
		case "word":
			array = new Long[size];
			initializeInteger();
			break;

		// floating points
		case "single":
		case "real":
		case "double":
			array = new Double[size];
			initializeDouble();
			break;

		// logical
		case "boolean":
			array = new Boolean[size];
			initializeBoolean();
			break;

		// char
		case "char":
			array = new Byte[size];
			initializeChar();
			break;

		default:
			array = null;
			break;
		}
	}
	
	// NOTE array initializations here
	private void initializeInteger() {
		for(int i=0; i<array.length; i++) {
			array[i] = new Long(0);
		}
	}
	
	private void initializeDouble() {
		for(int i=0; i<array.length; i++) {
			array[i] = new Double(0.0);
		}
	}
	
	private void initializeBoolean() {
		for(int i=0; i<array.length; i++) {
			array[i] = new Boolean(false);
		}
	}
	
	private void initializeChar() {
		for(int i=0; i<array.length; i++) {
			array[i] = new Byte((byte) 0);
		}
	}
	
	public Object getValueAt(Object index) {
		int realIndex = 0;
		realIndex = sourceOrdinal.getRealIndex(index);
		return array[realIndex];
	}
	
	public void setValueAt(Object index, Object value) {
		array[sourceOrdinal.getRealIndex(index)] = value;
	}

	@Override
	public Object getValue() {
		return array;
	}
}
