package cz.cuni.mff.d3s.trupple.language.parser.types;

public interface IOrdinalType {
	
	public enum Type {
		INTEGER,
		BOOLEAN,
		CHAR,
		ENUM
	}
	
	int getFirstIndex();
	int getSize();
	Type getType();
}
