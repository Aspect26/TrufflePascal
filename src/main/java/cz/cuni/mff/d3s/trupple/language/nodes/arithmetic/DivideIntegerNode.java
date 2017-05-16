package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.utils.BinaryArgumentPrimitiveTypes;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.IntDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

@NodeInfo(shortName = "div")
public abstract class DivideIntegerNode extends BinaryExpressionNode {

	DivideIntegerNode() {
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(IntDescriptor.getInstance(), IntDescriptor.getInstance()), IntDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(IntDescriptor.getInstance(), LongDescriptor.getInstance()), LongDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), IntDescriptor.getInstance()), LongDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), LongDescriptor.getInstance()), LongDescriptor.getInstance());
    }

    @Specialization
    protected int divInt(int left, int right) {
	    return left / right;
    }

	@Specialization
	protected long divLong(long left, long right) {
		return left / right;
	}
}
