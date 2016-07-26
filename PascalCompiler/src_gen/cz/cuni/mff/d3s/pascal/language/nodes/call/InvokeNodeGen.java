// CheckStyle: start generated
package cz.cuni.mff.d3s.pascal.language.nodes.call;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.pascal.language.PascalTypesGen;
import cz.cuni.mff.d3s.pascal.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.pascal.language.runtime.PascalFunction;

@GeneratedBy(InvokeNode.class)
public final class InvokeNodeGen extends InvokeNode {

    @Child private ExpressionNode functionNode_;
    @CompilationFinal private boolean seenUnsupported0;

    private InvokeNodeGen(ExpressionNode[] argumentNodes, ExpressionNode functionNode) {
        super(argumentNodes);
        this.functionNode_ = functionNode;
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        PascalFunction functionNodeValue_;
        try {
            functionNodeValue_ = PascalTypesGen.expectPascalFunction(functionNode_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            throw unsupported(ex.getResult());
        }
        return this.executeGeneric(frameValue, functionNodeValue_);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeGeneric(frameValue);
        return;
    }

    private UnsupportedSpecializationException unsupported(Object functionNodeValue) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {functionNode_}, functionNodeValue);
    }

    public static InvokeNode create(ExpressionNode[] argumentNodes, ExpressionNode functionNode) {
        return new InvokeNodeGen(argumentNodes, functionNode);
    }

}
