package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "succ")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class SuccBuiltinNode extends BuiltinNode {

    public SuccBuiltinNode(PascalContext context) {
        super(context);
    }

    @Specialization
    public long succ(long value) {
        return ++value;
    }

    @Specialization
    public char succ(char value) {
        return ++value;
    }

    @Specialization
    public boolean succ(boolean value) {
        if (value) {
            // TODO: throw custom NoNextValue exception
            throw new IllegalArgumentException("No succesor for TRUE value.");
        }

        return true;
    }

    @Specialization
    public EnumValue succ(EnumValue value) {
        return value.getNext();
    }
}
