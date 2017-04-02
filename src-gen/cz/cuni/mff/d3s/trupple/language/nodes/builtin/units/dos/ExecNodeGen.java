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
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(ExecNode.class)
public final class ExecNodeGen extends ExecNode {

    @Child private ExpressionNode child0_;
    @Child private ExpressionNode child1_;
    @CompilationFinal private boolean seenUnsupported0;

    private ExecNodeGen(ExpressionNode child0, ExpressionNode child1) {
        this.child0_ = child0;
        this.child1_ = child1;
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        String child0Value_;
        try {
            child0Value_ = PascalTypesGen.expectString(child0_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            Object child1Value = child1_.executeGeneric(frameValue);
            throw unsupported(ex.getResult(), child1Value);
        }
        String child1Value_;
        try {
            child1Value_ = PascalTypesGen.expectString(child1_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            throw unsupported(child0Value_, ex.getResult());
        }
        return this.exec(child0Value_, child1Value_);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeGeneric(frameValue);
        return;
    }

    private UnsupportedSpecializationException unsupported(Object child0Value, Object child1Value) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {child0_, child1_}, child0Value, child1Value);
    }

    public static ExecNode create(ExpressionNode child0, ExpressionNode child1) {
        return new ExecNodeGen(child0, child1);
    }

}
