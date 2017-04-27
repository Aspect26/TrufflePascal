package cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

// TODO: this node is created only once, not every time for each node -> this means that its argument type is some
// predefined type (which is long). Overloading of subroutines is not allowed in pascal so it is not implemented
// and static type check for this subroutine cannot work.
@NodeInfo(shortName = "succ")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class SuccBuiltinNode extends ExpressionNode {

    protected abstract ExpressionNode getArgument();

    @Specialization
    long succ(long value) {
        return ++value;
    }

    @Specialization
    char succ(char value) {
        return ++value;
    }

    @Specialization
    boolean succ(boolean value) {
        if (value) {
            // TODO: throw custom NoNextValue exception
            throw new IllegalArgumentException("No succesor for TRUE value.");
        }

        return true;
    }

    @Specialization
    EnumValue succ(EnumValue value) {
        return value.getNext();
    }

    @Override
    public TypeDescriptor getType() {
        return getArgument().getType();
    }

}
