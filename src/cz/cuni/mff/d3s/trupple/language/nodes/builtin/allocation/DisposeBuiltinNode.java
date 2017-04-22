package cz.cuni.mff.d3s.trupple.language.nodes.builtin.allocation;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.heap.PascalHeap;

@NodeInfo(shortName = "dispose")
@NodeChild(type = ExpressionNode.class)
public abstract class DisposeBuiltinNode extends StatementNode {

    @Specialization
    void dispose(PointerValue pointerValue) {
        PascalHeap.getInstance().disposeObject(pointerValue.getHeapSlot());
        pointerValue.setHeapSlot(PascalHeap.NIL);
    }

}
