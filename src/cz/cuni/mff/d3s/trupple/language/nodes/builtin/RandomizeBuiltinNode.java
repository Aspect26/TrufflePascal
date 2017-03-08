package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "randomize")
@NodeChild(value = "arguments", type = ExpressionNode[].class)
public class RandomizeBuiltinNode extends BuiltinNode {
	
	private final PascalContext context;
	
	public RandomizeBuiltinNode(PascalContext context) {
		this.context = context;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		this.context.randomize();
		return null;
	}
}
