package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.ExactMath;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.BinaryNode;

@NodeInfo(shortName = "+")
public abstract class AddNode extends BinaryNode {

	@Specialization(rewriteOn = ArithmeticException.class)
	protected long add(long left, long right) {
		return ExactMath.addExact(left, right);
	}

	@Specialization(rewriteOn = ArithmeticException.class)
	protected double add(double left, long right) {
		return left + right;
	}

	@Specialization(rewriteOn = ArithmeticException.class)
	protected double add(long left, double right) {
		return left + right;
	}

	@Specialization(rewriteOn = ArithmeticException.class)
	protected double add(double left, double right) {
		return left + right;
	}
}
