package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@NodeInfo(shortName = "pred")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class PredBuiltinNode extends BuiltinNode {

    @Specialization
    public long pred(long value) {
        return --value;
    }

    @Specialization
    public char pred(char value) {
        return --value;
    }

    @Specialization
    public boolean pred(boolean value) {
        if (!value) {
            // TODO: throw custom NoNextValue exception
            throw new IllegalArgumentException("No predcessor for TRUE value.");
        }

        return false;
    }

    @Specialization
    public EnumValue pred(EnumValue value) {
        return value.getPrevious();
    }
}
