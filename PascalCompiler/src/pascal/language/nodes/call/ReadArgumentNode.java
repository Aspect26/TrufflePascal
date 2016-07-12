package pascal.language.nodes.call;

import com.oracle.truffle.api.frame.VirtualFrame;

import pascal.language.nodes.ExpressionNode;
import pascal.language.runtime.Null;

public class ReadArgumentNode extends ExpressionNode {

	private final int index;
	//private final FrameSlotKind kind;
	
	public ReadArgumentNode(/*FrameSlotKind type,*/ int index){
		//this.kind = type;
		this.index = index;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame){
		Object[] args = frame.getArguments();
		if (index < args.length) {
            return args[index];
		} else{
			return Null.SINGLETON;
		}
	}
}
