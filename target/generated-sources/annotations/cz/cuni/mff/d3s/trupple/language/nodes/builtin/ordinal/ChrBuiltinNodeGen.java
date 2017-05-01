// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(ChrBuiltinNode.class)
public final class ChrBuiltinNodeGen extends ChrBuiltinNode {

    @Child private ExpressionNode argument_;
    @CompilationFinal private boolean seenUnsupported0;

    private ChrBuiltinNodeGen(ExpressionNode argument) {
        this.argument_ = argument;
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        return executeChar(frameValue);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeChar(frameValue);
        return;
    }

    @Override
    public char executeChar(VirtualFrame frameValue) {
        long argumentValue_;
        try {
            argumentValue_ = argument_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            throw unsupported(ex.getResult());
        }
        return this.chr(argumentValue_);
    }

    private UnsupportedSpecializationException unsupported(Object argumentValue) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {argument_}, argumentValue);
    }

    public static ChrBuiltinNode create(ExpressionNode argument) {
        return new ChrBuiltinNodeGen(argument);
    }

}
