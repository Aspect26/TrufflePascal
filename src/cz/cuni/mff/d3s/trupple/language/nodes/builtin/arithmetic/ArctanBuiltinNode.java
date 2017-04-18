package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "arctan")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class ArctanBuiltinNode extends BuiltinNode {

    @Specialization
    double integerArctanValue(long value) {
        return Math.atan(value);
    }

    @Specialization
    double doubleArctanValue(double value) {
        return Math.atan(value);
    }

}
