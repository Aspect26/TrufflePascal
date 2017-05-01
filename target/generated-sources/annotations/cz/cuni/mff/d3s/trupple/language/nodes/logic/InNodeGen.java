// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.logic;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.PascalTypesGen;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.SetTypeValue;

@GeneratedBy(InNode.class)
public final class InNodeGen extends InNode {

    @Child private ExpressionNode leftNode_;
    @Child private ExpressionNode rightNode_;
    @CompilationFinal private boolean seenUnsupported0;

    private InNodeGen(ExpressionNode leftNode, ExpressionNode rightNode) {
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
        return executeBoolean(frameValue);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeBoolean(frameValue);
        return;
    }

    @Override
    public boolean executeBoolean(VirtualFrame frameValue) {
        Object leftNodeValue_ = leftNode_.executeGeneric(frameValue);
        SetTypeValue rightNodeValue_;
        try {
            rightNodeValue_ = PascalTypesGen.expectSetTypeValue(rightNode_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            throw unsupported(leftNodeValue_, ex.getResult());
        }
        return this.inOperation(leftNodeValue_, rightNodeValue_);
    }

    private UnsupportedSpecializationException unsupported(Object leftNodeValue, Object rightNodeValue) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {leftNode_, rightNode_}, leftNodeValue, rightNodeValue);
    }

    public static InNode create(ExpressionNode leftNode, ExpressionNode rightNode) {
        return new InNodeGen(leftNode, rightNode);
    }

}
