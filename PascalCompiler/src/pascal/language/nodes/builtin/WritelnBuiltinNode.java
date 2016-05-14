package pascal.language.nodes.builtin;

import java.io.PrintWriter;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import pascal.language.nodes.ExpressionNode;
import pascal.language.runtime.PascalContext;

@NodeInfo(shortName = "writeln")
public class WritelnBuiltinNode extends BuiltinNode{

	private final PascalContext context;
	private final ExpressionNode[] params;
	
	public WritelnBuiltinNode(PascalContext context, ExpressionNode[] params){
		this.context = context;
		this.params = params;
	}
	
	@Override
	public PascalContext getContext() {
		return context;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		for(ExpressionNode param : params){
			context.getOutput().println(param.executeGeneric(frame));
		}
		return null;
	}
}
