package pascal.language.nodes;

import java.util.List;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.SourceSection;

@NodeInfo(shortName = "block", description = "The node implementation of a block of source code.")
public class BlockNode extends StatementNode {

	@Children private final StatementNode[] bodyNodes;
	
	public BlockNode(StatementNode[] bodyNodes){
		this.bodyNodes = bodyNodes;
	}
	
	@Override
	public void executeVoid(VirtualFrame virtualFrame) {
		
		CompilerAsserts.compilationConstant(bodyNodes.length);
		
		for(StatementNode statement : bodyNodes){
			statement.executeVoid(virtualFrame);
		}
	}

}
