package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.SqrtInvalidArgumentException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "sqrt")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class SqrtBuiltinNode extends BuiltinNode {

    public SqrtBuiltinNode(PascalContext context) {
        super(context);
    }

    @Specialization
    double integerSquareRootValue(long value) {
        return computeSquareRoot(value);
    }

    @Specialization
    double doubleSquareRootValue(double value) {
        return computeSquareRoot(value);
    }

    private double computeSquareRoot(double value) {
        if (value > 0) {
            return Math.sqrt(value);
        } else {
            throw new SqrtInvalidArgumentException(value);
        }
    }

}
