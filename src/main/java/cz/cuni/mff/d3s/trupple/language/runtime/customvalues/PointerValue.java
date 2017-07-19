package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import com.oracle.truffle.api.CompilerDirectives;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.language.runtime.heap.HeapSlot;
import cz.cuni.mff.d3s.trupple.language.runtime.heap.PascalHeap;

/**
 * Representation of pointer type variables. It holds a slot to the heap and descriptor of the type the pointers points
 * to.
 */
@CompilerDirectives.ValueType
public class PointerValue {

    private HeapSlot heapSlot;

    private final TypeDescriptor innerType;

    public PointerValue(TypeDescriptor innerType) {
        this.innerType = innerType;
        this.heapSlot = PascalHeap.NIL;
    }

    /**
     * Two pointers are equal if they point to the same heap slot.
     */
    @Override
    public boolean equals(Object compareTo) {
        if (compareTo instanceof PointerValue) {
            PointerValue pointerValue = (PointerValue) compareTo;
            return pointerValue.getHeapSlot().equals(this.getHeapSlot());
        } else {
            return false;
        }
    }

    /**
     * Returns the value in the heap to which the pointer points.
     */
    public Object getDereferenceValue() {
        return PascalHeap.getInstance().getValueAt(this.heapSlot);
    }

    /**
     * Gets the slot to the heap (where the pointers points to).
     */
    public HeapSlot getHeapSlot() {
        return heapSlot;
    }

    /**
     * Gets the type of the value the pointer points to.
     */
    public TypeDescriptor getType() {
        return innerType;
    }

    /**
     * Sets a new heap slot.
     * @param heapSlot the new slot
     */
    public void setHeapSlot(HeapSlot heapSlot) {
        this.heapSlot = heapSlot;
    }

    /**
     * Sets the value at the address the pointer points to.
     * @param value the new value
     */
    public void setDereferenceValue(Object value) {
        PascalHeap.getInstance().setValueAt(this.heapSlot, value);
    }

}
