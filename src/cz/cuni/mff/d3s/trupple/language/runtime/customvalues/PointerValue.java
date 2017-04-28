package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import com.oracle.truffle.api.CompilerDirectives;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.language.runtime.heap.HeapSlot;
import cz.cuni.mff.d3s.trupple.language.runtime.heap.PascalHeap;

@CompilerDirectives.ValueType
public class PointerValue {

    private HeapSlot heapSlot;

    private final TypeDescriptor innerType;

    public PointerValue(TypeDescriptor innerType) {
        this.innerType = innerType;
        this.heapSlot = PascalHeap.NIL;
    }

    @Override
    public boolean equals(Object compareTo) {
        if (compareTo instanceof PointerValue) {
            PointerValue pointerValue = (PointerValue) compareTo;
            return pointerValue.getHeapSlot().equals(this.getHeapSlot());
        } else {
            return false;
        }
    }

    public Object getDereferenceValue() {
        return PascalHeap.getInstance().getValueAt(this.heapSlot);
    }

    public HeapSlot getHeapSlot() {
        return heapSlot;
    }

    public TypeDescriptor getType() {
        return innerType;
    }

    public void setHeapSlot(HeapSlot heapSlot) {
        this.heapSlot = heapSlot;
    }

    public void setDereferenceValue(Object value) {
        PascalHeap.getInstance().setValueAt(this.heapSlot, value);
    }
}
