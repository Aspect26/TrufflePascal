package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.BinaryNode;

@NodeInfo(shortName = "div")
public abstract class DivideIntegerNode extends BinaryNode {

	// in the standard, there is specified that DIV operator only takes
	// integer types as operands
	@Specialization
	protected long div(long left, long right) {
		return left / right;
	}
}
