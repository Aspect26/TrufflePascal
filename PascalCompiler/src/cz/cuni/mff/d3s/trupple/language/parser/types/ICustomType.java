package cz.cuni.mff.d3s.trupple.language.parser.types;

public interface ICustomType {
	
	String getIdentifier();
	boolean containsCustomValue(String identifier);
	boolean isGlobal();
	long getCustomValue(String identifier);
}