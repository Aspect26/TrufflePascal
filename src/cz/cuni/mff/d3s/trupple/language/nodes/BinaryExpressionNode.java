package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.PointerDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.SetDescriptor;

import java.util.HashMap;
import java.util.Map;

@NodeChildren({
        @NodeChild(value = "leftNode", type = ExpressionNode.class),
        @NodeChild(value = "rightNode", type = ExpressionNode.class)
})
public abstract class BinaryExpressionNode extends ExpressionNode {

    protected abstract ExpressionNode getLeftNode();

    protected abstract ExpressionNode getRightNode();

    protected Map<BinaryArgumentPrimitiveTypes, TypeDescriptor> typeTable = new HashMap<>();

    @Override
    public TypeDescriptor getType() {
        TypeDescriptor primitiveType = this.typeTable.get(new BinaryArgumentPrimitiveTypes(getLeftNode().getType(), getRightNode().getType()));
        return (primitiveType != null) ? primitiveType : this.getNonPrimitiveArgumentsResultingType(getLeftNode().getType(), getRightNode().getType());
    }

    @Override
    public boolean verifyChildrenNodeTypes() {
        TypeDescriptor leftType = getLeftNode().getType();
        TypeDescriptor rightType = getRightNode().getType();
        if (this.typeTable.containsKey(new BinaryArgumentPrimitiveTypes(leftType, rightType))) {
            return true;
        }

        for (BinaryArgumentPrimitiveTypes allowedType : this.typeTable.keySet()) {
            TypeDescriptor allowedLeftType = allowedType.getLeftType();
            TypeDescriptor allowedRightType = allowedType.getRightType();
            if (leftType.convertibleTo(allowedLeftType) && rightType.convertibleTo(allowedRightType)) {
                return true;
            }
        }

        return this.verifyNonPrimitiveArgumentTypes(getLeftNode().getType(), getRightNode().getType());
    }

    protected boolean verifyNonPrimitiveArgumentTypes(TypeDescriptor leftType, TypeDescriptor rightType) {
        return false;
    }

    protected TypeDescriptor getNonPrimitiveArgumentsResultingType(TypeDescriptor leftType, TypeDescriptor rightType) {
        return null;
    }

    protected boolean verifyBothCompatibleSetTypes(TypeDescriptor leftType, TypeDescriptor rightType) {
        return leftType instanceof SetDescriptor && rightType instanceof SetDescriptor &&
                ((SetDescriptor) leftType).getInnerType() == ((SetDescriptor) rightType).getInnerType();
    }

    protected boolean verifyBothCompatiblePointerTypes(TypeDescriptor leftType, TypeDescriptor rightType) {
        if (!(leftType instanceof PointerDescriptor && rightType instanceof PointerDescriptor)) {
            return false;
        }
        TypeDescriptor leftInnerType = ((PointerDescriptor) leftType).getInnerTypeDescriptor();
        TypeDescriptor rightInnerType = ((PointerDescriptor) rightType).getInnerTypeDescriptor();
        return (leftInnerType == null) || (rightInnerType == null) || (rightInnerType.equals(leftInnerType));
    }

}
