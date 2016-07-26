package cz.cuni.mff.d3s.trupple.language.nodes.control;

import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.exceptions.BreakException;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;

public final class BreakNode extends StatementNode {

	@Override
	public void executeVoid(VirtualFrame frame){
		throw BreakException.SINGLETON;
	}
}
