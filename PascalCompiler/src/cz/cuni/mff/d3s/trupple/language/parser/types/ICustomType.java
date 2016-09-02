package cz.cuni.mff.d3s.trupple.language.parser.types;

public interface ICustomType {
	
	String getIdentifier();
	boolean isGlobal();
	
	boolean containsCustomValue(String identifier);
	long getCustomValue(String identifier);
}