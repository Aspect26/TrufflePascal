package pascal.language.nodes.logic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import pascal.language.nodes.BinaryNode;

@NodeInfo(shortName = "<")
public abstract class LessThanOrEqualNode extends BinaryNode {
	
	@Override
    public abstract boolean executeBoolean(VirtualFrame frame);
	
	@Specialization
    protected boolean lessThanOrEqual(long left, long right) {
        return left <= right;
    }
}
