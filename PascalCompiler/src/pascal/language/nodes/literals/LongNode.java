package pascal.language.nodes.literals;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import pascal.language.nodes.PascalNode;

@NodeInfo(shortName = "const")
public class LongNode extends PascalNode {

	public final long value;
	
	public LongNode(long value){
		this.value = value;
	}
	
	@Override
	public long executeLong(VirtualFrame virtualFrame) {
		return this.value;
	}

	@Override 
	public Object execute(VirtualFrame virtualFrame){
		return this.value;
	}
}
