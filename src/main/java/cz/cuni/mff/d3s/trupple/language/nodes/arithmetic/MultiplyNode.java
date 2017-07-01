package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.BinaryExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.utils.BinaryArgumentPrimitiveTypes;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.SetDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.IntDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

@NodeInfo(shortName = "*")
public abstract class MultiplyNode extends BinaryExpressionNode {

    MultiplyNode() {
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(IntDescriptor.getInstance(), IntDescriptor.getInstance()), IntDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), LongDescriptor.getInstance()), LongDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), IntDescriptor.getInstance()), LongDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(IntDescriptor.getInstance(), LongDescriptor.getInstance()), LongDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), LongDescriptor.getInstance()), LongDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(RealDescriptor.getInstance(), LongDescriptor.getInstance()), RealDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), RealDescriptor.getInstance()), RealDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(RealDescriptor.getInstance(), RealDescriptor.getInstance()), RealDescriptor.getInstance());
    }

    @Specialization
    int mul(int left, int right) {
        return left * right;
    }

    @Specialization
    long mul(long left, long right) {
        return left * right;
    }

	@Specialization
	double mul(double left, double right) {
		return left * right;
	}

	@Specialization
	SetTypeValue mul(SetTypeValue left, SetTypeValue right) {
		return SetTypeValue.intersect(left, right);
	}

    @Override
    public boolean verifyNonPrimitiveArgumentTypes(TypeDescriptor leftType, TypeDescriptor rightType) {
        return this.verifyBothCompatibleSetTypes(leftType, rightType);
    }

    @Override
    protected TypeDescriptor getNonPrimitiveArgumentsResultingType(TypeDescriptor leftType, TypeDescriptor rightType) {
        if (leftType instanceof SetDescriptor) {
            return new SetDescriptor(((SetDescriptor) leftType).getBaseTypeDescriptor());
        }
        return null;
    }

}