package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class UnaryNode extends ExpressionNode {

    protected abstract ExpressionNode getArgument();

    @Override
    public TypeDescriptor getType() {
        return this.getArgument().getType();
    }

}
