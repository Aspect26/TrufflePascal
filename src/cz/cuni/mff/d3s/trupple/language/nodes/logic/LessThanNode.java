package cz.cuni.mff.d3s.trupple.language.nodes.logic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryArgumentPrimitiveTypes;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.BinaryNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

@NodeInfo(shortName = "<")
public abstract class LessThanNode extends BinaryNode {

    LessThanNode() {
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), LongDescriptor.getInstance()), BooleanDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(RealDescriptor.getInstance(), LongDescriptor.getInstance()), BooleanDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), RealDescriptor.getInstance()), BooleanDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(RealDescriptor.getInstance(), RealDescriptor.getInstance()), BooleanDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(CharDescriptor.getInstance(), CharDescriptor.getInstance()), BooleanDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(BooleanDescriptor.getInstance(), BooleanDescriptor.getInstance()), BooleanDescriptor.getInstance());
    }

	@Specialization
	boolean lessThan(long left, long right) {
		return left < right;
	}

	@Specialization
	boolean lessThan(double left, long right) {
		return left < right;
	}

	@Specialization
	boolean lessThan(long left, double right) {
		return left < right;
	}

	@Specialization
	boolean lessThan(double left, double right) {
		return left < right;
	}

	@Specialization
	boolean lessThan(char left, char right) {
		return left < right;
	}

	@Specialization
	boolean lessThan(boolean left, boolean right) {
		return !left && right;
	}

	@Specialization
	boolean lessThan(SetTypeValue left, SetTypeValue right) {
		return left.getSize() < right.getSize();
	}

	@Specialization
	boolean lessThan(EnumValue left, EnumValue right) {
		return left.lesserThan(right);
	}

	@Override
    public boolean verifyNonPrimitiveArgumentTypes(TypeDescriptor leftType, TypeDescriptor rightType) {
        return this.verifyBothCompatibleEnums(leftType, rightType) ||
                this.verifyBothCompatibleSetTypes(leftType, rightType);
    }

    @Override
    public TypeDescriptor getType() {
        return BooleanDescriptor.getInstance();
    }

}
