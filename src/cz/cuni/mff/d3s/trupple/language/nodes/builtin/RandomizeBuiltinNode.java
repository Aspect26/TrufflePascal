package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "randomize")
public class RandomizeBuiltinNode extends BuiltinNode {
	
	public RandomizeBuiltinNode(PascalContext context) {
		super(context);
	}

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        this.context.randomize();
        return null;
    }
}
