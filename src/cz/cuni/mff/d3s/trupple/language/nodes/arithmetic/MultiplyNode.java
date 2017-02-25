package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryNode;

@NodeInfo(shortName = "*")
public abstract class MultiplyNode extends BinaryNode {

	@Specialization
	protected long mul(long left, long right) {
		return left * right;
	}

	@Specialization
	protected double mul(double left, long right) {
		return left * right;
	}

	@Specialization
	protected double mul(long left, double right) {
		return left * right;
	}

	@Specialization
	protected double mul(double left, double right) {
		return left * right;
	}

	@Specialization
	protected SetTypeValue mul(SetTypeValue left, SetTypeValue right) {
		return SetTypeValue.intersect(left, right);
	}
}
