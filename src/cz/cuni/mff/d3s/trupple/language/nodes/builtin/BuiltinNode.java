package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@GenerateNodeFactory
public abstract class BuiltinNode extends ExpressionNode {

    protected final PascalContext context;

    public BuiltinNode(PascalContext context) {
        this.context = context;
    }

}
