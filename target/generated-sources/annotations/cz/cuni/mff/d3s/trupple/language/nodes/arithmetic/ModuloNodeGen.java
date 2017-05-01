// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(ModuloNode.class)
public final class ModuloNodeGen extends ModuloNode {

    @Child private ExpressionNode leftNode_;
    @Child private ExpressionNode rightNode_;
    @CompilationFinal private boolean seenUnsupported0;

    private ModuloNodeGen(ExpressionNode leftNode, ExpressionNode rightNode) {
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
        return executeLong(frameValue);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeLong(frameValue);
        return;
    }

    @Override
    public long executeLong(VirtualFrame frameValue) {
        long leftNodeValue_;
        try {
            leftNodeValue_ = leftNode_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            Object rightNodeValue = rightNode_.executeGeneric(frameValue);
            throw unsupported(ex.getResult(), rightNodeValue);
        }
        long rightNodeValue_;
        try {
            rightNodeValue_ = rightNode_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            throw unsupported(leftNodeValue_, ex.getResult());
        }
        return this.mod(leftNodeValue_, rightNodeValue_);
    }

    private UnsupportedSpecializationException unsupported(Object leftNodeValue, Object rightNodeValue) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {leftNode_, rightNode_}, leftNodeValue, rightNodeValue);
    }

    public static ModuloNode create(ExpressionNode leftNode, ExpressionNode rightNode) {
        return new ModuloNodeGen(leftNode, rightNode);
    }

}
