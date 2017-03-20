package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.ExactMath;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;

@NodeInfo(shortName = "-")
public abstract class SubstractNode extends BinaryNode {

	@Specialization
	protected long sub(long left, long right) {
		return ExactMath.subtractExact(left, right);
	}

	@Specialization
	protected double sub(double left, long right) {
		return left - right;
	}

	@Specialization
	protected double sub(long left, double right) {
		return left - right;
	}

	@Specialization
	protected double sub(double left, double right) {
		return left - right;
	}

	@Specialization
	protected SetTypeValue sub(SetTypeValue left, SetTypeValue right) {
		return SetTypeValue.difference(left, right);
	}
}
