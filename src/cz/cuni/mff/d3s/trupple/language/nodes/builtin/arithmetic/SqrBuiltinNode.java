package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "sqr")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class SqrBuiltinNode extends BuiltinNode {

    @Specialization
    long integerSquareValue(long value) {
        return (value * value);
    }

    @Specialization
    double doubleSquareRootValue(double value) {
        return value * value;
    }

}
