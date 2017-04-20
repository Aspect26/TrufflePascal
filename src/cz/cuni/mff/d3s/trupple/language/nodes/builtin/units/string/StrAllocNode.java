package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.string;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import cz.cuni.mff.d3s.trupple.language.customvalues.PCharValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.heap.HeapSlot;
import cz.cuni.mff.d3s.trupple.language.runtime.heap.PascalHeap;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.PointerDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.extension.PCharDesriptor;

@NodeChild(type = ExpressionNode.class)
public abstract class StrAllocNode extends ExpressionNode {

    @Specialization
    public PointerValue strAlloc(long size) {
        PCharValue pchar = new PCharValue(size);
        HeapSlot heapSlot = PascalHeap.getInstance().allocateNewObject(pchar);
        PointerValue pointer = new PointerValue(new PCharDesriptor());
        pointer.setHeapSlot(heapSlot);

        return pointer;
    }

    @Override
    public TypeDescriptor getType() {
        return new PointerDescriptor(PCharDesriptor.getInstance());
    }

}
