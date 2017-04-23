package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryArgumentPrimitiveTypes;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.SetDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

@NodeInfo(shortName = "+")
public abstract class AddNode extends BinaryNode {

    AddNode() {
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), LongDescriptor.getInstance()), LongDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(RealDescriptor.getInstance(), RealDescriptor.getInstance()), RealDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(RealDescriptor.getInstance(), LongDescriptor.getInstance()), RealDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(RealDescriptor.getInstance(), RealDescriptor.getInstance()), RealDescriptor.getInstance());
    }

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
