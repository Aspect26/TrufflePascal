package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "exp")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class ExpBuiltinNode extends BuiltinNode {

    @Specialization
    double integerExponentialValue(long value) {
        return Math.exp(value);
    }

    @Specialization
    double doubleExponentialValue(double value) {
        return Math.exp(value);
    }

}
