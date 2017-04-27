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
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PointerValue;

@GeneratedBy(NewBuiltinNode.class)
public final class NewBuiltinNodeGen extends NewBuiltinNode {

    @Child private ExpressionNode argument_;
    @CompilationFinal private boolean seenUnsupported0;

    private NewBuiltinNodeGen(ExpressionNode argument) {
        this.argument_ = argument;
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        PointerValue argumentValue_;
        try {
            argumentValue_ = expectPointerValue(argument_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            throw unsupported(ex.getResult());
        }
        this.allocate(argumentValue_);
        return;
    }

    private UnsupportedSpecializationException unsupported(Object argumentValue) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {argument_}, argumentValue);
    }

    private static PointerValue expectPointerValue(Object value) throws UnexpectedResultException {
        if (value instanceof PointerValue) {
            return (PointerValue) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static NewBuiltinNode create(ExpressionNode argument) {
        return new NewBuiltinNodeGen(argument);
    }

}
