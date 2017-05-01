package cz.cuni.mff.d3s.trupple.language.nodes.logic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryArgumentPrimitiveTypes;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.GenericEnumTypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

@NodeInfo(shortName = "=")
public abstract class EqualsNode extends BinaryExpressionNode {

    EqualsNode() {
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(BooleanDescriptor.getInstance(), BooleanDescriptor.getInstance()), BooleanDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), LongDescriptor.getInstance()), BooleanDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(RealDescriptor.getInstance(), RealDescriptor.getInstance()), BooleanDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(CharDescriptor.getInstance(), CharDescriptor.getInstance()), BooleanDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(GenericEnumTypeDescriptor.getInstance(), GenericEnumTypeDescriptor.getInstance()), BooleanDescriptor.getInstance());
    }

	@Specialization
	protected boolean equals(boolean left, boolean right) {
		return left == right;
	}

    @Specialization
    protected boolean equals(long left, long right) {
        return left == right;
    }

	@Specialization
	protected boolean equals(double left, double right) {
		return left == right;
	}

	@Specialization
    protected boolean equals(char left, char right) {
	    return left == right;
    }

	@Specialization
	protected boolean equals(EnumValue left, EnumValue right) { return left.equals(right); }

	@Specialization
	protected boolean equals(SetTypeValue left, SetTypeValue right) {
	    return left.equals(right);
    }

    @Specialization
    protected boolean equals(PointerValue left, PointerValue right) {
	    return left.equals(right);
    }

    // TODO: what about record type? file type?

    @Override
    public boolean verifyNonPrimitiveArgumentTypes(TypeDescriptor leftType, TypeDescriptor rightType) {
        return this.verifyBothCompatibleSetTypes(leftType, rightType) ||
                this.verifyBothCompatiblePointerTypes(leftType, rightType);
    }

    @Override
    public TypeDescriptor getType() {
        return BooleanDescriptor.getInstance();
    }

}
