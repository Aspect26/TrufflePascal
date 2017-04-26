// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(OddBuiltinNode.class)
public final class OddBuiltinNodeGen extends OddBuiltinNode {

    @Child private ExpressionNode argument_;
    @CompilationFinal private boolean seenUnsupported0;

    private OddBuiltinNodeGen(ExpressionNode argument) {
        this.argument_ = argument;
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
        long argumentValue_;
        try {
            argumentValue_ = argument_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            throw unsupported(ex.getResult());
        }
        return this.odd(argumentValue_);
    }

    private UnsupportedSpecializationException unsupported(Object argumentValue) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {argument_}, argumentValue);
    }

    public static OddBuiltinNode create(ExpressionNode argument) {
        return new OddBuiltinNodeGen(argument);
    }

}
