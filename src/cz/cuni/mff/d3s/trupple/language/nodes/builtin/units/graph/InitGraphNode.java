package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.graph;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalString;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.graphics.PascalGraphMode;

@NodeChildren({
        @NodeChild(type = ExpressionNode.class),
        @NodeChild(type = ExpressionNode.class),
        @NodeChild(type = ExpressionNode.class)
})
public abstract class InitGraphNode extends StatementNode {

    @Specialization
    public void initGraph(long graphDriver, long graphMode, PascalString pathToDriver) {
        PascalGraphMode.init();
    }

}
