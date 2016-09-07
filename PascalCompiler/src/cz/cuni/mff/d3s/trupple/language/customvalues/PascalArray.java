package cz.cuni.mff.d3s.trupple.language.customvalues;

import java.util.Arrays;

import cz.cuni.mff.d3s.trupple.language.customtypes.IOrdinalType;

public class PascalArray implements ICustomValue {
	private final Object[] array;
	private final IOrdinalType sourceOrdinal;
	
	public PascalArray(Object[] objects, IOrdinalType ordinal) {
		this.sourceOrdinal = ordinal;
		final int size = ordinal.getSize();
		array = new Object[size];
		initializeObject(objects);
	}
	
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
	
	private void initializeObject(Object[] values) {
		assert values.length == array.length;
		
		for(int i=0; i<array.length; i++) {
			array[i] = values[i];
		}
	}
	
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
	
	public Object getValueAt(Object[] indexes) {
		int realIndex = sourceOrdinal.getRealIndex(indexes[0]);
		if(indexes.length == 1) {
			return array[realIndex];
		}
		else {
			PascalArray innerArray = (PascalArray) array[realIndex];
			return innerArray.getValueAt(removeFirstElement(indexes));
		}
	}
	
	public void setValueAt(Object[] indexes, Object value) {
		if(indexes.length == 1) {
			array[sourceOrdinal.getRealIndex(indexes[0])] = value;
		} else {
			PascalArray innerArray = (PascalArray) array[sourceOrdinal.getRealIndex(indexes[0])];
			innerArray.setValueAt(removeFirstElement(indexes), value);
		}
	}
	
	private Object[] removeFirstElement(Object[] array) {
		return Arrays.copyOfRange(array, 1, array.length);
	}

	@Override
	public Object getValue() {
		return array;
	}
}
