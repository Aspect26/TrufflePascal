package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "random")
@NodeChild(value = "arguments", type = ExpressionNode[].class)
public class RandomBuiltinNode extends BuiltinNode {

	private final PascalContext context;
	private final long bound;
	
	public RandomBuiltinNode(PascalContext context, long bound) {
		this.context = context;
		this.bound = bound;
	}
	
	public RandomBuiltinNode(PascalContext context) {
		this(context, Long.MAX_VALUE);
	}
	
	@Override
	public PascalContext getContext() {
		return context;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return this.getContext().getRandom(bound);
	}
}
