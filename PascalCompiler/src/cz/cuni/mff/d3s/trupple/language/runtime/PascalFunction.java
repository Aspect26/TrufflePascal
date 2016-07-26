package cz.cuni.mff.d3s.trupple.language.runtime;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;


public class PascalFunction implements TruffleObject{

    /** The current implementation of this function. */
    private RootCallTarget callTarget;
    
    private final String name;
    
    public PascalFunction(String name){
    	this.name = name;
    }
    
    protected void setCallTarget(RootCallTarget callTarget) {
        this.callTarget = callTarget;
    }
    
    public RootCallTarget getCallTarget(){
    	return callTarget;
    }
    
    @Override
    public String toString(){
    	return name;
    }
    
	@Override
	public ForeignAccess getForeignAccess() {
		// TODO Foreign access
		return null;
	}
}
