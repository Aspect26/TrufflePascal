package cz.cuni.mff.d3s.trupple.language.types;

public class PascalArray {
	private final Object[] array;
	private final int offset;
	
	public PascalArray(String typeIdentifier, int size, int offset) {
		this.offset = offset;
		
		// this needs to be here so the array can be final
		switch(typeIdentifier){
		// integer types
		case "integer":
		case "shortint":
		case "longint":
		case "byte":
		case "word":
			array = new Integer[size];
			break;

		// floating points
		case "single":
		case "real":
		case "double":
			array = new Double[size];
			break;

		// logical
		case "boolean":
			array = new Boolean[size];
			break;

		// char
		case "char":
			array = new Byte[size];
			break;

		default:
			array = null;
			break;
		}
	}
}
