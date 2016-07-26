package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;

public abstract class StatementNode extends Node {

	public abstract void executeVoid(VirtualFrame frame);
}
