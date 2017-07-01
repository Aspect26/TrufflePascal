package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.utils.BinaryArgumentPrimitiveTypes;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.IntDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

@NodeInfo(shortName = "/")
public abstract class DivideNode extends BinaryExpressionNode {

    DivideNode() {
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(IntDescriptor.getInstance(), IntDescriptor.getInstance()), RealDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), LongDescriptor.getInstance()), RealDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), IntDescriptor.getInstance()), RealDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(IntDescriptor.getInstance(), LongDescriptor.getInstance()), RealDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), LongDescriptor.getInstance()), RealDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(RealDescriptor.getInstance(), LongDescriptor.getInstance()), RealDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), RealDescriptor.getInstance()), RealDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(RealDescriptor.getInstance(), RealDescriptor.getInstance()), RealDescriptor.getInstance());
    }

    @Specialization
    protected double divideInts(int left, int right) {
        return left / (double) right;
    }

    @Specialization
    protected double divideLong(long left, long right) {
        return left / right;
    }

	@Specialization
	protected double divideDouble(double left, double right) {
		return left / right;
	}

}