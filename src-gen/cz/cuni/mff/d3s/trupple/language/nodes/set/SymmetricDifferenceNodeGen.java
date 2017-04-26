// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.set;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.PascalTypesGen;
import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(SymmetricDifferenceNode.class)
public final class SymmetricDifferenceNodeGen extends SymmetricDifferenceNode {

    @Child private ExpressionNode leftNode_;
    @Child private ExpressionNode rightNode_;
    @CompilationFinal private boolean seenUnsupported0;

    private SymmetricDifferenceNodeGen(ExpressionNode leftNode, ExpressionNode rightNode) {
        this.leftNode_ = leftNode;
        this.rightNode_ = rightNode;
    }

    @Override
    protected ExpressionNode getLeftNode() {
        return this.leftNode_;
    }

    @Override
    protected ExpressionNode getRightNode() {
        return this.rightNode_;
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        SetTypeValue leftNodeValue_;
        try {
            leftNodeValue_ = PascalTypesGen.expectSetTypeValue(leftNode_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            Object rightNodeValue = rightNode_.executeGeneric(frameValue);
            throw unsupported(ex.getResult(), rightNodeValue);
        }
        SetTypeValue rightNodeValue_;
        try {
            rightNodeValue_ = PascalTypesGen.expectSetTypeValue(rightNode_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            throw unsupported(leftNodeValue_, ex.getResult());
        }
        return this.diff(leftNodeValue_, rightNodeValue_);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeGeneric(frameValue);
        return;
    }

    private UnsupportedSpecializationException unsupported(Object leftNodeValue, Object rightNodeValue) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {leftNode_, rightNode_}, leftNodeValue, rightNodeValue);
    }

    public static SymmetricDifferenceNode create(ExpressionNode leftNode, ExpressionNode rightNode) {
        return new SymmetricDifferenceNodeGen(leftNode, rightNode);
    }

}
