package cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;

@NodeInfo(shortName = "chr")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class ChrBuiltinNode extends ExpressionNode {

    @Specialization
    char chr(long value) {
        return (char) (value % 256);
    }

    @Override
    public TypeDescriptor getType() {
        return CharDescriptor.getInstance();
    }

}
