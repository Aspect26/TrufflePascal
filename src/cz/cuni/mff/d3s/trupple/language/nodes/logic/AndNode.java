package cz.cuni.mff.d3s.trupple.language.nodes.logic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.BinaryNode;

@NodeInfo(shortName = "&&")
public abstract class AndNode extends BinaryNode {

	@Override
	public abstract boolean executeBoolean(VirtualFrame frame);

	@Specialization
	protected boolean logicalAnd(boolean left, boolean right) {
		return left && right;
	}
}
