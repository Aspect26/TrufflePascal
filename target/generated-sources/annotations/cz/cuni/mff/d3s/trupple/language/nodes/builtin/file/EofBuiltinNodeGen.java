// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.file;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(EofBuiltinNode.class)
public final class EofBuiltinNodeGen extends EofBuiltinNode {

    @Child private ExpressionNode arguments0_;
    @CompilationFinal private boolean seenUnsupported0;

    private EofBuiltinNodeGen(ExpressionNode[] arguments) {
        this.arguments0_ = arguments != null && 0 < arguments.length ? arguments[0] : null;
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
        Object[] arguments0Value_;
        try {
            arguments0Value_ = expectObjectArray(arguments0_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            throw unsupported(ex.getResult());
        }
        return this.isEof(arguments0Value_);
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

    public static EofBuiltinNode create(ExpressionNode[] arguments) {
        return new EofBuiltinNodeGen(arguments);
    }

}
