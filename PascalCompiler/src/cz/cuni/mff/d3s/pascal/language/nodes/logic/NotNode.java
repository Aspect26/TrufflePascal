package cz.cuni.mff.d3s.pascal.language.nodes.logic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.pascal.language.nodes.UnaryNode;

@NodeInfo(shortName = "!")
public abstract class NotNode extends UnaryNode {
	
	@Override
    public abstract boolean executeBoolean(VirtualFrame frame);
	
	@Specialization
    protected boolean logicalNot(boolean child) {
        return !child;
    }
}
