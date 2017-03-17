package cz.cuni.mff.d3s.trupple.language.nodes.builtin.tp;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "random")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class RandomBuiltinNode extends BuiltinNode {



	public RandomBuiltinNode(PascalContext context) {
		super(context);
	}

	@Specialization
	public long getRandom(long upperBound) {
		return this.context.getRandom(upperBound);
	}

}
