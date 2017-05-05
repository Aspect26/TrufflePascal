package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.ExactMath;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.BinaryExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.utils.BinaryArgumentPrimitiveTypes;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.SetDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

@NodeInfo(shortName = "-")
public abstract class SubtractNode extends BinaryExpressionNode {

    SubtractNode() {
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), LongDescriptor.getInstance()), LongDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(RealDescriptor.getInstance(), RealDescriptor.getInstance()), RealDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(RealDescriptor.getInstance(), LongDescriptor.getInstance()), RealDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(RealDescriptor.getInstance(), RealDescriptor.getInstance()), RealDescriptor.getInstance());
    }

	@Specialization
	long sub(long left, long right) {
		return ExactMath.subtractExact(left, right);
	}

	@Specialization
	double sub(double left, double right) {
		return left - right;
	}

	@Specialization
	SetTypeValue sub(SetTypeValue left, SetTypeValue right) {
		return SetTypeValue.difference(left, right);
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
