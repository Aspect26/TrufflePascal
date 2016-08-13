package cz.cuni.mff.d3s.trupple.language.nodes.literals;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalFunction;

@NodeInfo(shortName = "func")
public final class FunctionLiteralNode extends ExpressionNode {
	private final String value;
	private final PascalContext context;

	public FunctionLiteralNode(PascalContext context, String value) {
		this.value = value;
		this.context = context;
	}

	@Override
	public PascalFunction executeGeneric(VirtualFrame frame) {
		PascalContext context = this.context;
		PascalFunction function = null;
		while(context != null){
			function = context.getGlobalFunctionRegistry().lookup(value);
			if(function == null)
				function = context.getPrivateFunctionRegistry().lookup(value);
		
			if(function == null)
				context = context.getOuterContext();
			
			if(function != null)
				break;
		}
		
		if(function == null)
			throw new RuntimeException("Function " + function + " does not exist in this context.");
		
		return function;
	}
}
