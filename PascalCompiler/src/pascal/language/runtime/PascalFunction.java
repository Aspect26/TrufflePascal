package pascal.language.runtime;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.utilities.CyclicAssumption;


public class PascalFunction implements TruffleObject{

    /** The current implementation of this function. */
    private RootCallTarget callTarget;
    
    public PascalFunction(){
    }
    
    protected void setCallTarget(RootCallTarget callTarget) {
        this.callTarget = callTarget;
    }
    
    public RootCallTarget getCallTarget(){
    	return callTarget;
    }
    
	@Override
	public ForeignAccess getForeignAccess() {
		// TODO Foreign access
		return null;
	}
}
