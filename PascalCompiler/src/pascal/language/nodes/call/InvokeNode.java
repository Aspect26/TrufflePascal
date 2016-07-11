package pascal.language.nodes.call;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.Message;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;

import pascal.language.nodes.ExpressionNode;
import pascal.language.runtime.Null;
import pascal.language.runtime.PascalContext;
import pascal.language.runtime.PascalFunction;

@NodeInfo(shortName = "invoke")
@NodeChildren({@NodeChild(value = "functionNode", type = ExpressionNode.class)})
public abstract class InvokeNode extends ExpressionNode {
	@Children private final ExpressionNode[] argumentNodes;
    @Child private DispatchNode dispatchNode;
    
    InvokeNode(ExpressionNode[] argumentNodes){
    	this.argumentNodes = argumentNodes;
    	this.dispatchNode = DispatchNodeGen.create();
    }
    
    @Specialization
    @ExplodeLoop
    public Object executeGeneric(VirtualFrame frame, PascalFunction function) {
        CompilerAsserts.compilationConstant(argumentNodes.length);

        Object[] argumentValues = new Object[argumentNodes.length];
        for (int i = 0; i < argumentNodes.length; i++) {
            argumentValues[i] = argumentNodes[i].executeGeneric(frame);
        }
        return dispatchNode.executeDispatch(frame, function, argumentValues);
    }
}
