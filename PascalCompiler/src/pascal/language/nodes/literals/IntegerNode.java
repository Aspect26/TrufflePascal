package pascal.language.nodes.literals;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import pascal.language.nodes.PascalNode;

@NodeInfo(shortName = "const")
public class IntegerNode extends PascalNode{

	public final int value;
	
	public IntegerNode(int value){
		this.value = value;
	}
	
	@Override
	public int executeInteger(VirtualFrame virtualFrame){
		return this.value;
	}
	
	@Override
	public Object execute(VirtualFrame virtualFrame) {
		return this.value;
	}

}
