package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "round")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class RoundBuiltinNode extends BuiltinNode {

    public RoundBuiltinNode(PascalContext context) {
        super(context);
    }

    @Specialization
    long round(double value) {
        return Math.round(value);
    }

}
