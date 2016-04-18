package pascal.language.nodes.literals;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import pascal.language.nodes.PascalNode;

@NodeInfo(shortName = "const")
public class StringNode extends PascalNode{
	
	public final String value;
	
	public StringNode(String value){
		this.value = value;
	}
	
	@Override
	public Object execute(VirtualFrame virtualFrame){
		return this.value;
	}
}
