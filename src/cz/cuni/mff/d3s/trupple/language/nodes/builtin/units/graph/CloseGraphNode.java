package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.graph;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.graphics.PascalGraphMode;

public abstract class CloseGraphNode extends ExpressionNode {

    @Specialization
    public long closeGraph(VirtualFrame frame) {
        return PascalGraphMode.close();
    }

}
