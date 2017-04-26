// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.io;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(ReadBuiltinNode.class)
public final class ReadBuiltinNodeGen extends ReadBuiltinNode {

    @Child private ExpressionNode arguments0_;
    @CompilationFinal private boolean seenUnsupported0;

    private ReadBuiltinNodeGen(ExpressionNode[] arguments) {
        this.arguments0_ = arguments != null && 0 < arguments.length ? arguments[0] : null;
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        Object[] arguments0Value_;
        try {
            arguments0Value_ = expectObjectArray(arguments0_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            throw unsupported(ex.getResult());
        }
        this.read(arguments0Value_);
        return;
    }

    private UnsupportedSpecializationException unsupported(Object arguments0Value) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {arguments0_}, arguments0Value);
    }

    private static Object[] expectObjectArray(Object value) throws UnexpectedResultException {
        if (value instanceof Object[]) {
            return (Object[]) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static ReadBuiltinNode create(ExpressionNode[] arguments) {
        return new ReadBuiltinNodeGen(arguments);
    }

}
