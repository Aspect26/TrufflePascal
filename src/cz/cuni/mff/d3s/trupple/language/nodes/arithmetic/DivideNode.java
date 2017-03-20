package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "/")
public abstract class DivideNode extends BinaryNode {

	@Specialization
	protected double div(long left, long right) {
		return (double) left / right;
	}

	@Specialization
	protected double div(double left, long right) {
		return left / right;
	}

	@Specialization
	protected double div(long left, double right) {
		return left / right;
	}

	@Specialization
	protected double div(double left, double right) {
		return left / right;
	}
}
