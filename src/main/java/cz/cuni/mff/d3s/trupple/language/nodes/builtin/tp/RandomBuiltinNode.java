package cz.cuni.mff.d3s.trupple.language.nodes.builtin.tp;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.PascalLanguage;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

@NodeInfo(shortName = "random")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class RandomBuiltinNode extends ExpressionNode {

    @Specialization
	long getRandom(long upperBound) {
		return PascalLanguage.INSTANCE.findContext().getRandom(upperBound);
	}

    @Override
    public TypeDescriptor getType() {
        return LongDescriptor.getInstance();
    }

}
