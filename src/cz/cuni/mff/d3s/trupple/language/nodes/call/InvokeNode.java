package cz.cuni.mff.d3s.trupple.language.nodes.call;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.FunctionLiteralNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

// TODO: do not combine @NodeChild and @Child annotations -> it is messy
@NodeInfo(shortName = "invoke")
@NodeChild(value = "functionNode", type = FunctionLiteralNode.class)
public abstract class InvokeNode extends ExpressionNode {

    @Children
	private final ExpressionNode[] argumentNodes;
	@Child
	private DispatchNode dispatchNode;

	protected abstract FunctionLiteralNode getFunctionNode();

	InvokeNode(ExpressionNode[] argumentNodes) {
		this.argumentNodes = argumentNodes;
		this.dispatchNode = DispatchNodeGen.create();
	}

	@Specialization
	@ExplodeLoop
	public Object executeGeneric(VirtualFrame frame, PascalSubroutine function) {
		CompilerAsserts.compilationConstant(argumentNodes.length);

		Object[] argumentValues = new Object[argumentNodes.length + 1];
		argumentValues[0] = frame;
		for (int i = 0; i < argumentNodes.length; i++) {
			argumentValues[i+1] = argumentNodes[i].executeGeneric(frame);
		}
		return dispatchNode.executeDispatch(frame, function, argumentValues);
	}

	@Override
    public TypeDescriptor getType() {
	    return getFunctionNode().getType();
    }
}
