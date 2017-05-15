package cz.cuni.mff.d3s.trupple.language.nodes.variables.write;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PointerValue;

@NodeChildren({
        @NodeChild(value = "pointerNode", type = ExpressionNode.class),
        @NodeChild(value = "valueNode", type = ExpressionNode.class)
})
public abstract class AssignToDereferenceNode extends StatementNode {

    @Specialization
    void assignGeneric(PointerValue pointer, Object value) {
        pointer.setDereferenceValue(value);
    }

}
