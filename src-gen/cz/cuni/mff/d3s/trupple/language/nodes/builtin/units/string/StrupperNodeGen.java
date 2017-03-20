// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.string;

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

@GeneratedBy(StrupperNode.class)
public final class StrupperNodeGen extends StrupperNode {

    @Child private ExpressionNode child0_;
    @CompilationFinal private boolean seenUnsupported0;

    private StrupperNodeGen(ExpressionNode child0) {
        this.child0_ = child0;
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
            throw unsupported(ex.getResult());
        }
        return this.toUpper(child0Value_);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeGeneric(frameValue);
        return;
    }

    private UnsupportedSpecializationException unsupported(Object child0Value) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {child0_}, child0Value);
    }

    public static StrupperNode create(ExpressionNode child0) {
        return new StrupperNodeGen(child0);
    }

}
