package cz.cuni.mff.d3s.trupple.language.nodes.call;

import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public class ReadArgumentNode extends ExpressionNode {

	private final int index;
	private final TypeDescriptor argumentType;

	public ReadArgumentNode(int index, TypeDescriptor argumentType) {
		this.index = index + 1;
        this.argumentType = argumentType;
    }

	@Override
	public Object executeGeneric(VirtualFrame frame) {
        return frame.getArguments()[index];
	}

    @Override
    public TypeDescriptor getType() {
        return this.argumentType;
    }
}
