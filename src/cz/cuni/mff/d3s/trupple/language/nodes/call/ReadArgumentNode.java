package cz.cuni.mff.d3s.trupple.language.nodes.call;

import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.Null;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public class ReadArgumentNode extends ExpressionNode {

	private final int index;

	public ReadArgumentNode(int index) {
		this.index = index + 1;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		Object[] args = frame.getArguments();
		if (index < args.length) {
			return args[index];
		} else {
			return Null.SINGLETON;
		}
	}

    @Override
    public TypeDescriptor getType() {
        return null;
    }
}
