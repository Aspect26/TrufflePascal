package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

@NodeChildren({
        @NodeChild(value = "leftNode", type = ExpressionNode.class),
        @NodeChild(value = "rightNode", type = ExpressionNode.class)
})
public abstract class BinaryNode extends ExpressionNode {

    protected abstract ExpressionNode getLeftNode();

    protected abstract ExpressionNode getRightNode();

    protected Map<Pair<TypeDescriptor, TypeDescriptor>, TypeDescriptor> typeTable = new HashMap<>();

    @Override
    public TypeDescriptor getType() {
        // TODO: will this work?
        return this.typeTable.get(new Pair<>(getLeftNode().getType(), getRightNode().getType()));
    }

}
