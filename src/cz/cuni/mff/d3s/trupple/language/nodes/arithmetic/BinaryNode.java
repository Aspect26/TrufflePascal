package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@NodeChildren({
        @NodeChild(value = "leftNode", type = ExpressionNode.class),
        @NodeChild(value = "rightNode", type = ExpressionNode.class)
})
public abstract class BinaryNode extends ExpressionNode {

}
