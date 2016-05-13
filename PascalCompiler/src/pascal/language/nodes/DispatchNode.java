package pascal.language.nodes;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node;

import pascal.language.runtime.PascalFunction;

public abstract class DispatchNode extends Node {
	
	protected static final int INLINE_CACHE_SIZE = 2;
	
	public abstract Object executeDispatch(VirtualFrame frame, PascalFunction function, Object[] arguments);

	@Specialization(guards = "function.getCallTarget() == null")
	protected Object doundefinedFunction(PascalFunction function, Object[] arguments){
		//TODO: throw undefined function exception
		throw new RuntimeException();
	}
	
	@Specialization(limit = "INLINE_CACHE_SIZE", guards = "function == cachedFunction")
	protected static Object doDirect(VirtualFrame frame, PascalFunction function, Object[] arguments,  
            @Cached("function") PascalFunction cachedFunction,   
            @Cached("create(cachedFunction.getCallTarget())") DirectCallNode callNode) {
		return callNode.call(frame, arguments);
	}
	
	@Specialization(contains = "doDirect")
    protected static Object doIndirect(VirtualFrame frame, PascalFunction function, Object[] arguments,  
                    @Cached("create()") IndirectCallNode callNode) {
        return callNode.call(frame, function.getCallTarget(), arguments);
    }
}
