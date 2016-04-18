package pascal.language.runtime;

import com.oracle.truffle.api.object.ObjectType;

public class PascalObjectType extends ObjectType{
	public final String name;
	
	public PascalObjectType(String name){
		this.name = name;
	}
	
	@Override
	public String toString(){
		return "[" + this.name + "]";
	}
}
