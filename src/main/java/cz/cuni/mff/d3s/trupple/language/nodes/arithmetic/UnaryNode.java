package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

/**
 * Base class for each unary node. It has one child node which is the operation's argument.
 */
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class UnaryNode extends ExpressionNode {

    protected abstract ExpressionNode getArgument();

    /**
     * Results of the most unary types have the same type as its argument (e.g. -1, not, ...)
     * @return resulting type of the operation
     */
    @Override
    public TypeDescriptor getType() {
        return this.getArgument().getType();
    }

}
