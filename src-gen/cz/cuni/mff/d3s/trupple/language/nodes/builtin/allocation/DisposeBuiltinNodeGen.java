// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.allocation;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(DisposeBuiltinNode.class)
public final class DisposeBuiltinNodeGen extends DisposeBuiltinNode {

    @Child private ExpressionNode child0_;
    @CompilationFinal private boolean seenUnsupported0;

    private DisposeBuiltinNodeGen(ExpressionNode child0) {
        this.child0_ = child0;
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        PointerValue child0Value_;
        try {
            child0Value_ = expectPointerValue(child0_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            throw unsupported(ex.getResult());
        }
        this.dispose(child0Value_);
        return;
    }

    private UnsupportedSpecializationException unsupported(Object child0Value) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {child0_}, child0Value);
    }

    private static PointerValue expectPointerValue(Object value) throws UnexpectedResultException {
        if (value instanceof PointerValue) {
            return (PointerValue) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static DisposeBuiltinNode create(ExpressionNode child0) {
        return new DisposeBuiltinNodeGen(child0);
    }

}
