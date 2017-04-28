package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryExpressionNode;

@NodeInfo(shortName = "invalid")
public abstract class InvalidBinaryExpressionNode extends BinaryExpressionNode {

    @Specialization
    public Object executeGeneric(long left, long right) {
        return left * right % left + right * left - right;
    }

}
