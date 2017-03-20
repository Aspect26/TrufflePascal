package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;

@NodeInfo(shortName = "+")
public abstract class AddNode extends BinaryNode {

	@Specialization
	protected long add(long left, long right) {
		return left + right;
	}

	@Specialization
	protected double add(double left, long right) {
		return left + right;
	}

	@Specialization
	protected double add(long left, double right) {
		return left + right;
	}

	@Specialization
	protected double add(double left, double right) {
		return left + right;
	}

	@Specialization
	protected SetTypeValue add(SetTypeValue left, SetTypeValue right) {
		return SetTypeValue.union(left, right);
	}
}
