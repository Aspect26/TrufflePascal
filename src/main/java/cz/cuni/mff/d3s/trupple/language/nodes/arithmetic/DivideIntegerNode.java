package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryArgumentPrimitiveTypes;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

@NodeInfo(shortName = "div")
public abstract class DivideIntegerNode extends BinaryExpressionNode {

	DivideIntegerNode() {
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), LongDescriptor.getInstance()), LongDescriptor.getInstance());
    }

	@Specialization
	protected long div(long left, long right) {
		return left / right;
	}
}
