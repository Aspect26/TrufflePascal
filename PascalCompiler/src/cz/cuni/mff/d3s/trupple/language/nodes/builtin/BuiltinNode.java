package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeChild(value = "arguments", type = ExpressionNode[].class)
@NodeField(name = "context", type = PascalContext.class)
@GenerateNodeFactory
public abstract class BuiltinNode extends ExpressionNode{

	public abstract PascalContext getContext();
}
