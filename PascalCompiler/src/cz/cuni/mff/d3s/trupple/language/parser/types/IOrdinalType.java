package cz.cuni.mff.d3s.trupple.language.parser.types;

public interface IOrdinalType {
	
	public enum Type {
		NUMERIC,
		BOOLEAN,
		CHAR,
		ENUM
	}
	
	int getFirstIndex();
	int getSize();
	Type getType();
	int getRealIndex(Object index);
}
