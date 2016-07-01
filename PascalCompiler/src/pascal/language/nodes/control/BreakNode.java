package pascal.language.nodes.control;

import com.oracle.truffle.api.frame.VirtualFrame;

import pascal.exceptions.BreakException;
import pascal.language.nodes.StatementNode;

public final class BreakNode extends StatementNode {

	@Override
	public void executeVoid(VirtualFrame frame){
		throw BreakException.SINGLETON;
	}
}
