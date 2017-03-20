package cz.cuni.mff.d3s.trupple.language.nodes.logic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.BinaryNode;

@NodeInfo(shortName = "<")
public abstract class LessThanNode extends BinaryNode {

	@Override
	public abstract boolean executeBoolean(VirtualFrame frame);

	@Specialization
	protected boolean lessThan(long left, long right) {
		return left < right;
	}

	@Specialization
	protected boolean lessThan(double left, long right) {
		return left < right;
	}

	@Specialization
	protected boolean lessThan(long left, double right) {
		return left < right;
	}

	@Specialization
	protected boolean lessThan(double left, double right) {
		return left < right;
	}

	@Specialization
	protected boolean lessThan(char left, char right) {
		return left < right;
	}

	@Specialization
	protected boolean lessThan(boolean left, boolean right) {
		return !left && right;
	}

	@Specialization
	protected boolean lessThan(SetTypeValue left, SetTypeValue right) {
		return left.getSize() < right.getSize();
	}

	@Specialization
	protected boolean lessThan(EnumValue left, EnumValue right) {
		return left.lesserThan(right);
	}
}
