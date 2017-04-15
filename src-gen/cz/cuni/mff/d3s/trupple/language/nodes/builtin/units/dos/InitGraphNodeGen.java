// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.dos;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.PascalTypesGen;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(InitGraphNode.class)
public final class InitGraphNodeGen extends InitGraphNode {

    @Child private ExpressionNode child0_;
    @Child private ExpressionNode child1_;
    @Child private ExpressionNode child2_;
    @Child private ExpressionNode child3_;
    @CompilationFinal private boolean seenUnsupported0;

    private InitGraphNodeGen(ExpressionNode child0, ExpressionNode child1, ExpressionNode child2, ExpressionNode child3) {
        this.child0_ = child0;
        this.child1_ = child1;
        this.child2_ = child2;
        this.child3_ = child3;
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        Reference child0Value_;
        try {
            child0Value_ = PascalTypesGen.expectReference(child0_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            Object child1Value = child1_.executeGeneric(frameValue);
            Object child2Value = child2_.executeGeneric(frameValue);
            Object child3Value = child3_.executeGeneric(frameValue);
            throw unsupported(ex.getResult(), child1Value, child2Value, child3Value);
        }
        Reference child1Value_;
        try {
            child1Value_ = PascalTypesGen.expectReference(child1_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            Object child2Value = child2_.executeGeneric(frameValue);
            Object child3Value = child3_.executeGeneric(frameValue);
            throw unsupported(child0Value_, ex.getResult(), child2Value, child3Value);
        }
        Reference child2Value_;
        try {
            child2Value_ = PascalTypesGen.expectReference(child2_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            Object child3Value = child3_.executeGeneric(frameValue);
            throw unsupported(child0Value_, child1Value_, ex.getResult(), child3Value);
        }
        Reference child3Value_;
        try {
            child3Value_ = PascalTypesGen.expectReference(child3_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            throw unsupported(child0Value_, child1Value_, child2Value_, ex.getResult());
        }
        return this.getTime(child0Value_, child1Value_, child2Value_, child3Value_);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeGeneric(frameValue);
        return;
    }

    private UnsupportedSpecializationException unsupported(Object child0Value, Object child1Value, Object child2Value, Object child3Value) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {child0_, child1_, child2_, child3_}, child0Value, child1Value, child2Value, child3Value);
    }

    public static InitGraphNode create(ExpressionNode child0, ExpressionNode child1, ExpressionNode child2, ExpressionNode child3) {
        return new InitGraphNodeGen(child0, child1, child2, child3);
    }

}
