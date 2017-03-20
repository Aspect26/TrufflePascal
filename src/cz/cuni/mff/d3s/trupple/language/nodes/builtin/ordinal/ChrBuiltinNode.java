package cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "chr")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class ChrBuiltinNode extends BuiltinNode {

    public ChrBuiltinNode(PascalContext context) {
        super(context);
    }

    @Specialization
    public char chr(long value) {
        return (char) (value % 256);
    }

}
