package cz.cuni.mff.d3s.trupple.language.nodes.builtin.allocation;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import cz.cuni.mff.d3s.trupple.language.runtime.heap.HeapSlot;
import cz.cuni.mff.d3s.trupple.language.runtime.heap.PascalHeap;

@NodeInfo(shortName = "new")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class NewBuiltinNode extends StatementNode {

    @Specialization
    void allocate(PointerValue pointerValue) {
        Object newObject = pointerValue.getType().getDefaultValue();
        HeapSlot heapSlot = PascalHeap.getInstance().allocateNewObject(newObject);
        pointerValue.setHeapSlot(heapSlot);
    }
}
