package cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "ord")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class OrdBuiltinNode extends BuiltinNode {

    public OrdBuiltinNode(PascalContext context) {
        super(context);
    }

    @Specialization
    public long ord(long value) {
        return value;
    }

    @Specialization
    public long ord(boolean value) {
        return value? 1:0;
    }

    @Specialization
    public long ord(char value) {
        return (long) value;
    }

    @Specialization
    public long ord(EnumValue value) {
        return value.getOrdinalValue();
    }

}
