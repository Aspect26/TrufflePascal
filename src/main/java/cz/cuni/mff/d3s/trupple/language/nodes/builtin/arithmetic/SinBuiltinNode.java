package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

/**
 * Node representing Pascal's built in subroutine sin.
 *
 * This node uses specializations which means that it is not used directly but completed node is generated by Truffle.
 * {@link SinBuiltinNodeGen}
 */
@NodeInfo(shortName = "sin")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class SinBuiltinNode extends ExpressionNode {

    @Specialization
    double sin(double value) {
        return Math.sin(value);
    }

    @Override
    public TypeDescriptor getType() {
        return RealDescriptor.getInstance();
    }

}
