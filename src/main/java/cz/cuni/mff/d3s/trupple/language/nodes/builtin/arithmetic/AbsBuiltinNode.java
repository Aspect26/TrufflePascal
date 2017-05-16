package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

@NodeInfo(shortName = "abs")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class AbsBuiltinNode extends ExpressionNode {

    protected abstract ExpressionNode getArgument();

    @Specialization
    int intAbsoluteValue(int value) {
        return Math.abs(value);
    }

    @Specialization
    long longAbsoluteValue(long value) {
        return Math.abs(value);
    }

    @Specialization
    double doubleAbsoluteValue(double value) {
        return Math.abs(value);
    }

    @Override
    public TypeDescriptor getType() {
        return getArgument().getType();
    }

}
