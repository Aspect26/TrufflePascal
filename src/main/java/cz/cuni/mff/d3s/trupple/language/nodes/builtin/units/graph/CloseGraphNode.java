package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.graph;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.graphics.PascalGraphMode;

public abstract class CloseGraphNode extends StatementNode {

    @Specialization
    public void closeGraph(VirtualFrame frame) {
        PascalGraphMode.close();
    }

}
