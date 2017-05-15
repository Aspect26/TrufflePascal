package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

@NodeChild(type = ExpressionNode.class)
@NodeField(name = "offset", type = int.class)
public abstract class ReadIndexNode extends ExpressionNode {

    protected abstract int getOffset();

    /**
     * We need to return an int because arrays may be indexed only by ints in Java
     */
    @Specialization
    int getIntIndex(long index) {
        return (int) index - getOffset();
    }

    @Specialization
    int getCharIndex(char index) {
        return (int) index - getOffset();
    }

    @Specialization
    int getBooleanIndex(boolean index) {
        return ((index)? 1:0) - getOffset();
    }

    @Specialization
    int getEnumIndex(EnumValue index) {
        return (int) index.getOrdinalValue() - getOffset();
    }

    @Override
    public TypeDescriptor getType() {
        return LongDescriptor.getInstance();
    }
}
