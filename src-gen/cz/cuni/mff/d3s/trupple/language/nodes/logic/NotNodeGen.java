// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.logic;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(NotNode.class)
public final class NotNodeGen extends NotNode {

    @Child private ExpressionNode argument_;
    @CompilationFinal private boolean seenUnsupported0;

    private NotNodeGen(ExpressionNode argument) {
        this.argument_ = argument;
    }

    @Override
    protected ExpressionNode getArgument() {
        return this.argument_;
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
    public boolean executeBoolean(VirtualFrame frameValue) {
        boolean argumentValue_;
        try {
            argumentValue_ = argument_.executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            throw unsupported(ex.getResult());
        }
        return this.logicalNot(argumentValue_);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeBoolean(frameValue);
        return;
    }

    private UnsupportedSpecializationException unsupported(Object argumentValue) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {argument_}, argumentValue);
    }

    public static NotNode create(ExpressionNode argument) {
        return new NotNodeGen(argument);
    }

}
