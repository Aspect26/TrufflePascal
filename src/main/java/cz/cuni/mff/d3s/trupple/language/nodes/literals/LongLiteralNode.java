package cz.cuni.mff.d3s.trupple.language.nodes.literals;

import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

public class LongLiteralNode extends ExpressionNode {

	private final long value;

	public LongLiteralNode(long value) {
		this.value = value;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return value;
	}

    @Override
    public TypeDescriptor getType() {
        return LongDescriptor.getInstance();
    }

}
