package cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

@NodeInfo(shortName = "ord")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class OrdBuiltinNode extends ExpressionNode {

    @Specialization
    int ord(int value) {
        return value;
    }

    @Specialization
    int ord(long value) {
        return (int) value;
    }

    @Specialization
    int ord(boolean value) {
        return value? 1:0;
    }

    @Specialization
    int ord(char value) {
        return (int) value;
    }

    @Specialization
    int ord(EnumValue value) {
        return (int) value.getOrdinalValue();
    }

    @Override
    public TypeDescriptor getType() {
        return LongDescriptor.getInstance();
    }

}
