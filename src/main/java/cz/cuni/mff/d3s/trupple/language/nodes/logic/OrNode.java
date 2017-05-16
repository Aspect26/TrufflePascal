package cz.cuni.mff.d3s.trupple.language.nodes.logic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.utils.BinaryArgumentPrimitiveTypes;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;

@NodeInfo(shortName = "||")
public abstract class OrNode extends BinaryExpressionNode {

    OrNode() {
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(BooleanDescriptor.getInstance(), BooleanDescriptor.getInstance()), BooleanDescriptor.getInstance());
    }

	@Specialization
	boolean logicalOr(boolean left, boolean right) {
		return left || right;
	}
}
