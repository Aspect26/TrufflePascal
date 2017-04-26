package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

@NodeInfo(shortName = "trunc")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class  TruncBuiltinNode extends ExpressionNode {

    @Specialization
    long truncate(double value) {
        return (long) value;
    }

    @Override
    public TypeDescriptor getType() {
        return LongDescriptor.getInstance();
    }

}
