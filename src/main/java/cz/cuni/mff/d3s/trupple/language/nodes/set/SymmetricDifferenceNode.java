package cz.cuni.mff.d3s.trupple.language.nodes.set;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.SetDescriptor;

@NodeInfo(shortName = "><")
public abstract class SymmetricDifferenceNode extends BinaryExpressionNode {

	@Override
	public abstract Object executeGeneric(VirtualFrame frame);

	@Specialization
	SetTypeValue diff(SetTypeValue left, SetTypeValue right) {
		return SetTypeValue.symmetricDifference(left, right);
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
