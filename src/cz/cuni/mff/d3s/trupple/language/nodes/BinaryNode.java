package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;

@NodeChildren({ @NodeChild("leftNode"), @NodeChild("rightNode") })
public abstract class BinaryNode extends ExpressionNode {

}
