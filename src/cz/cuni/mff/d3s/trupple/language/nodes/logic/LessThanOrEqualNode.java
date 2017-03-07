package cz.cuni.mff.d3s.trupple.language.nodes.logic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryNode;

@NodeInfo(shortName = "<=")
public abstract class LessThanOrEqualNode extends BinaryNode {

	@Override
	public abstract boolean executeBoolean(VirtualFrame frame);

	@Specialization
	protected boolean lessThanOrEqual(long left, long right) {
		return left <= right;
	}

	@Specialization
	protected boolean lessThan(double left, long right) {
		return left <= right;
	}

	@Specialization
	protected boolean lessThan(long left, double right) {
		return left <= right;
	}

	@Specialization
	protected boolean lessThan(double left, double right) {
		return left <= right;
	}

	@Specialization
	protected boolean lessThan(char left, char right) {
		return left <= right;
	}

	@Specialization
	protected boolean lessThan(boolean left, boolean right) {
		return !left && right;
	}

	@Specialization
	protected boolean lessThanOrEqual(SetTypeValue left, SetTypeValue right) {
		return (left.getSize() < right.getSize()) || (left.getSize() == right.getSize());
	}
}
