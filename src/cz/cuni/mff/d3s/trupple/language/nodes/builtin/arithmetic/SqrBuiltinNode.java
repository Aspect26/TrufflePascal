package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

@NodeInfo(shortName = "sqr")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class SqrBuiltinNode extends ExpressionNode {

    protected abstract ExpressionNode getArgument();

    @Specialization
    long integerSquareValue(long value) {
        return (value * value);
    }

    @Specialization
    double doubleSquareRootValue(double value) {
        return value * value;
    }

    @Override
    public TypeDescriptor getType() {
        return getArgument().getType();
    }

}
