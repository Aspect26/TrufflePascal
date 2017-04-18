package cz.cuni.mff.d3s.trupple.language.nodes.builtin.tp;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "randomize")
public class RandomizeBuiltinNode extends BuiltinNode {
	
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        PascalContext.getInstance().randomize();
        return null;
    }
}
