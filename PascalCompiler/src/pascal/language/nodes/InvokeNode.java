package pascal.language.nodes;

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

import pascal.language.runtime.Null;
import pascal.language.runtime.PascalContext;
import pascal.language.runtime.PascalFunction;

import com.oracle.truffle.api.nodes.Node.Child;
import com.oracle.truffle.api.nodes.Node.Children;

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
    
    @Child private Node crossLanguageCall;
    
    @Specialization
    @ExplodeLoop
    protected Object executeGeneric(VirtualFrame frame, TruffleObject function) {
        /*
         * The number of arguments is constant for one invoke node. During compilation, the loop is
         * unrolled and the execute methods of all arguments are inlined. This is triggered by the
         * ExplodeLoop annotation on the method. The compiler assertion below illustrates that the
         * array length is really constant.
         */
        CompilerAsserts.compilationConstant(argumentNodes.length);

        Object[] argumentValues = new Object[argumentNodes.length];
        for (int i = 0; i < argumentNodes.length; i++) {
            argumentValues[i] = argumentNodes[i].executeGeneric(frame);
        }
        if (crossLanguageCall == null) {
            crossLanguageCall = insert(Message.createExecute(argumentValues.length).createNode());
        }
        try {
            Object res = ForeignAccess.sendExecute(crossLanguageCall, frame, function, argumentValues);
            return PascalContext.fromForeignValue(res);
        } catch (ArityException | UnsupportedTypeException | UnsupportedMessageException e) {
            return Null.SINGLETON;
        }
    }
}
