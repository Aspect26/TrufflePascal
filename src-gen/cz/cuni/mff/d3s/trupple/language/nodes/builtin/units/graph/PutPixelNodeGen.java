// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.graph;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(PutPixelNode.class)
public final class PutPixelNodeGen extends PutPixelNode {

    @Child private ExpressionNode child0_;
    @Child private ExpressionNode child1_;
    @Child private ExpressionNode child2_;
    @CompilationFinal private boolean seenUnsupported0;

    private PutPixelNodeGen(ExpressionNode child0, ExpressionNode child1, ExpressionNode child2) {
        this.child0_ = child0;
        this.child1_ = child1;
        this.child2_ = child2;
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
        long child0Value_;
        try {
            child0Value_ = child0_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            Object child1Value = child1_.executeGeneric(frameValue);
            Object child2Value = child2_.executeGeneric(frameValue);
            throw unsupported(ex.getResult(), child1Value, child2Value);
        }
        long child1Value_;
        try {
            child1Value_ = child1_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            Object child2Value = child2_.executeGeneric(frameValue);
            throw unsupported(child0Value_, ex.getResult(), child2Value);
        }
        long child2Value_;
        try {
            child2Value_ = child2_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            throw unsupported(child0Value_, child1Value_, ex.getResult());
        }
        return this.putPixel(child0Value_, child1Value_, child2Value_);
    }

    private UnsupportedSpecializationException unsupported(Object child0Value, Object child1Value, Object child2Value) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {child0_, child1_, child2_}, child0Value, child1Value, child2Value);
    }

    public static PutPixelNode create(ExpressionNode child0, ExpressionNode child1, ExpressionNode child2) {
        return new PutPixelNodeGen(child0, child1, child2);
    }

}
