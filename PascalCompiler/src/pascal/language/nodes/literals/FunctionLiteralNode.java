package pascal.language.nodes.literals;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import pascal.language.nodes.ExpressionNode;
import pascal.language.runtime.PascalContext;
import pascal.language.runtime.PascalFunction;

@NodeInfo(shortName = "func")
public final class FunctionLiteralNode extends ExpressionNode{
	private final String value;
	private final PascalContext context;

    public FunctionLiteralNode(PascalContext context, String value){
    	this.value = value;
    	this.context = context;
    }
    
    @Override
    public PascalFunction executeGeneric(VirtualFrame frame){
    	return context.getFunctionRegistry().lookup(value);
    }
}
