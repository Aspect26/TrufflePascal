package pascal.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

import pascal.language.runtime.Null;

public class ReadArgumentNode extends ExpressionNode {

	private final int index;
	
	public ReadArgumentNode(int index){
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
