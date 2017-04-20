// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.call;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.FunctionLiteralNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalSubroutine;

@GeneratedBy(InvokeNode.class)
public final class InvokeNodeGen extends InvokeNode {

    @Child private FunctionLiteralNode functionNode_;

    private InvokeNodeGen(ExpressionNode[] argumentNodes, FunctionLiteralNode functionNode) {
        super(argumentNodes);
        this.functionNode_ = functionNode;
    }

    @Override
    protected FunctionLiteralNode getFunctionNode() {
        return this.functionNode_;
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        PascalSubroutine functionNodeValue_ = functionNode_.executeGeneric(frameValue);
        return this.executeGeneric(frameValue, functionNodeValue_);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeGeneric(frameValue);
        return;
    }

    public static InvokeNode create(ExpressionNode[] argumentNodes, FunctionLiteralNode functionNode) {
        return new InvokeNodeGen(argumentNodes, functionNode);
    }

}
