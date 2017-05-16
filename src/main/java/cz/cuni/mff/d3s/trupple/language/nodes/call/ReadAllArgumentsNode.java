package cz.cuni.mff.d3s.trupple.language.nodes.call;

import java.util.Arrays;

import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public class ReadAllArgumentsNode extends ExpressionNode {

	@Override
	public Object[] executeGeneric(VirtualFrame frame) {
		Object[] args = frame.getArguments();
		return Arrays.copyOfRange(args, 1, args.length);
	}

    @Override
    public TypeDescriptor getType() {
        return null;
    }
}
