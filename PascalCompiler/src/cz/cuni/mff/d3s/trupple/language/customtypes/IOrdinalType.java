package cz.cuni.mff.d3s.trupple.language.customtypes;

public interface IOrdinalType {
	
	public enum Type {
		NUMERIC,
		BOOLEAN,
		CHAR,
		ENUM
	}
	
	int getSize();
	Type getType();
	int getRealIndex(Object index);
}
