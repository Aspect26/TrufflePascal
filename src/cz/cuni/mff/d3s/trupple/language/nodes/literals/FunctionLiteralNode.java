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
			function = context.getFunctionRegistry().lookup(value);

			if(function == null)
				context = context.getOuterContext();
			
			if(function != null)
				break;
		}
		
		if(function == null)
			throw new RuntimeException("Function " + this.value + " does not exist in this context.");
		
		return function;
	}

	@Override
	public String toString() {
		return this.value;
	}
}
