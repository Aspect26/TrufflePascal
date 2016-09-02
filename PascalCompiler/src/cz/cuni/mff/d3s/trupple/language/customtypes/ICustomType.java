package cz.cuni.mff.d3s.trupple.language.customtypes;

public interface ICustomType {
	
	String getIdentifier();
	boolean isGlobal();
	
	boolean containsCustomValue(String identifier);
	long getCustomValue(String identifier);
}