package cz.cuni.mff.d3s.trupple.language.types;

import cz.cuni.mff.d3s.trupple.language.parser.types.IOrdinalType;
import cz.cuni.mff.d3s.trupple.language.parser.types.IOrdinalType.Type;
import cz.cuni.mff.d3s.trupple.language.parser.types.EnumOrdinal;

public class PascalArray {
	private final Object[] array;
	private final int offset;
	private final IOrdinalType sourceOrdinal;
	
	public PascalArray(String typeIdentifier, IOrdinalType ordinal) {
		this.offset = ordinal.getFirstIndex();
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
		
		Type type = this.sourceOrdinal.getType();
		if(type == Type.NUMERIC) {
			//TODO: thisssssss casssssstsssssss :'(
			realIndex = (int)((Long)index - offset);
		}
		else if(type == Type.BOOLEAN) {
			realIndex = ((Boolean)index)? 1 : 0;
		}
		else if(type == Type.CHAR) {
			realIndex = (Character)index;
		}
		else if(type == Type.ENUM) {
			realIndex = sourceOrdinal.getRealIndex(index);
		}
		realIndex = sourceOrdinal.getRealIndex(index);
		return array[realIndex];
	}
}
