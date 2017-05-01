package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;

@NodeInfo(shortName = "odd")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class OddBuiltinNode extends ExpressionNode {

    @Specialization
    boolean odd(long value) {
        return (Math.abs(value) % 2) == 1;
    }

    @Override
    public TypeDescriptor getType() {
        return BooleanDescriptor.getInstance();
    }

}
