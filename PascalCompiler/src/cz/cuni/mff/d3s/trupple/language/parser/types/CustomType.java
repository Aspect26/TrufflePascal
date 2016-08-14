package cz.cuni.mff.d3s.trupple.language.parser.types;

public abstract class CustomType implements ICustomType{
	
	private String identifier;
	private boolean global;
	
	public CustomType(String identifier, boolean global){
		this.identifier = identifier;
		this.global = global;
	}
	
	@Override
	public String getIdentifier(){
		return identifier;
	}
	
	@Override
	public boolean isGlobal(){
		return global;
	}
}