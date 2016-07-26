package cz.cuni.mff.d3s.trupple.language.nodes.logic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.BinaryNode;

@NodeInfo(shortName = "<")
public abstract class LessThanOrEqualNode extends BinaryNode {

	@Override
	public abstract boolean executeBoolean(VirtualFrame frame);

	@Specialization
	protected boolean lessThanOrEqual(long left, long right) {
		return left <= right;
	}
}
