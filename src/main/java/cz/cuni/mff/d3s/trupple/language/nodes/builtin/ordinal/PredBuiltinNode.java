package cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

@NodeInfo(shortName = "pred")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class PredBuiltinNode extends ExpressionNode {

    protected abstract ExpressionNode getArgument();

    @Specialization
    int pred(int value) {
        return --value;
    }

    @Specialization
    long pred(long value) {
        return --value;
    }

    @Specialization
    char pred(char value) {
        return --value;
    }

    @Specialization
    boolean pred(boolean value) {
        if (!value) {
            // TODO: throw custom NoNextValue exception
            throw new IllegalArgumentException("No predecessor for TRUE value.");
        }

        return false;
    }

    @Specialization
    EnumValue pred(EnumValue value) {
        return value.getPrevious();
    }

    @Override
    public TypeDescriptor getType() {
        return getArgument().getType();
    }

}
