package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "abs")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class AbsBuiltinNode extends BuiltinNode {

    public AbsBuiltinNode(PascalContext context) {
        super(context);
    }

    @Specialization
    long integerAbsoluteValue(long value) {
        return Math.abs(value);
    }

    @Specialization
    double doubleAbsoluteValue(double value) {
        return Math.abs(value);
    }

}
