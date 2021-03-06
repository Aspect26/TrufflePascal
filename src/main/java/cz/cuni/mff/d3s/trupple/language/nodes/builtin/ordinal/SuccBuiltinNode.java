package cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

/**
 * Node representing Pascal's built in subroutine succ. It receives an ordinal type value as an argument and returns its
 * successor.
 *
 * This node uses specializations which means that it is not used directly but completed node is generated by Truffle.
 * {@link SuccBuiltinNodeGen}
 */
@NodeInfo(shortName = "succ")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class SuccBuiltinNode extends ExpressionNode {

    protected abstract ExpressionNode getArgument();

    @Specialization
    int succ(int value) {
        return  ++value;
    }

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
