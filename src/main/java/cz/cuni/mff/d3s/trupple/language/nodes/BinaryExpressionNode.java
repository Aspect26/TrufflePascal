package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import cz.cuni.mff.d3s.trupple.language.nodes.utils.BinaryArgumentPrimitiveTypes;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.PointerDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.SetDescriptor;

import java.util.HashMap;
import java.util.Map;

/**
 * Base node for each binary expression node. It also contains functions for static type checking.
 */
@NodeChildren({
        @NodeChild(value = "leftNode", type = ExpressionNode.class),
        @NodeChild(value = "rightNode", type = ExpressionNode.class)
})
public abstract class BinaryExpressionNode extends ExpressionNode {

    /**
     * Gets the left argument node.
     */
    protected abstract ExpressionNode getLeftNode();

    /**
     * Gets the right argument node.
     */
    protected abstract ExpressionNode getRightNode();

    /**
     * Each key is one allowed combination of arguments and value is resulting type of that one combination. Used for
     * static type checking. Classes that derive from this need only to fill this table.
     */
    protected Map<BinaryArgumentPrimitiveTypes, TypeDescriptor> typeTable = new HashMap<>();

    @Override
    public TypeDescriptor getType() {
        TypeDescriptor primitiveType = this.typeTable.get(new BinaryArgumentPrimitiveTypes(getLeftNode().getType(), getRightNode().getType()));
        return (primitiveType != null) ? primitiveType : this.getNonPrimitiveArgumentsResultingType(getLeftNode().getType(), getRightNode().getType());
    }

    /**
     * Checks whether provided argument types are allowed for the operation. In that case it returns true.
     */
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

    /**
     * Non primitive types cannot be compared as easily as the primitive ones so each node has to implement the verification
     * of these types individually.
     */
    protected boolean verifyNonPrimitiveArgumentTypes(TypeDescriptor leftType, TypeDescriptor rightType) {
        return false;
    }

    /**
     * Returns resulting type of the operation for specified types of arguments. This is used for non primitive types
     * which cannot be stored in the types table.
     */
    protected TypeDescriptor getNonPrimitiveArgumentsResultingType(TypeDescriptor leftType, TypeDescriptor rightType) {
        return null;
    }

    /**
     * Helper function that returns true if the given types are both set types and compatible.
     */
    protected boolean verifyBothCompatibleSetTypes(TypeDescriptor leftType, TypeDescriptor rightType) {
        return leftType instanceof SetDescriptor && rightType instanceof SetDescriptor &&
                ((SetDescriptor) leftType).getInnerType() == ((SetDescriptor) rightType).getInnerType();
    }

    /**
     * Helper function that returns true if the given types are both pointer types and compatible.
     */
    protected boolean verifyBothCompatiblePointerTypes(TypeDescriptor leftType, TypeDescriptor rightType) {
        if (!(leftType instanceof PointerDescriptor && rightType instanceof PointerDescriptor)) {
            return false;
        }
        TypeDescriptor leftInnerType = ((PointerDescriptor) leftType).getInnerTypeDescriptor();
        TypeDescriptor rightInnerType = ((PointerDescriptor) rightType).getInnerTypeDescriptor();
        return (leftInnerType == null) || (rightInnerType == null) || (rightInnerType.equals(leftInnerType));
    }

}
