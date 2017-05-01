package cz.cuni.mff.d3s.trupple.language.runtime.heap;

import cz.cuni.mff.d3s.trupple.parser.exceptions.OutOfMemoryException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.SegmentationFaultException;

import java.util.*;

/**
 * Represents a memory concept called Heap which stores structures that shall have global access, not local like in
 * stack. The internal representation of memory is a java map. The map takes care of allocating enough space for the
 * data stored in it as well as easy removing single objects from itself.
 */
public class PascalHeap {

    /**
     * The singleton instance of the heap.
     */
    private static PascalHeap INSTANCE = new PascalHeap();

    /**
     * Represents the nil value.
     */
    public static HeapSlot NIL = new HeapSlot(Integer.MIN_VALUE);

    /**
     * Represents the minimal address that can be allocated
     */
    private static final int MIN_ADDRESS = Integer.MIN_VALUE + 1;

    /**
     * The representation of heap's memory. Key represents an address (as integer) and value is the actual data structure
     * stored on the specified address. NOTE: faster implementation would be with an array.
     */
    private Map<Integer, Object> memory;

    /**
     * A hash set of all addresses that are being in use by some pointer in the program. They must be wrapped because the
     * garbage collecting process can change the address of any object, so the actual pointers in the application would
     * point to a wrong position. It is implemented as a hash set so the removal of a slot is in O(1).
     */
    private HashSet<HeapSlot> heapSlots;

    /**
     * Stores address to the first empty memory slot where new data can be stored.
     */
    private int emptyMemorySlot;

    private PascalHeap() {
        this.memory = new HashMap<>();
        this.heapSlots = new HashSet<>();
        this.emptyMemorySlot = this.MIN_ADDRESS;
    }

    public static PascalHeap getInstance() {
        return INSTANCE;
    }

    public HeapSlot allocateNewObject(Object object) {
        HeapSlot newHeapSlot = new HeapSlot(this.emptyMemorySlot++);
        this.allocateNewObject(newHeapSlot, object);

        return newHeapSlot;
    }

    private void allocateNewObject(HeapSlot slot, Object object) {
        this.memory.put(slot.getMemoryIndex(), object);
        this.heapSlots.add(slot);

        if (this.emptyMemorySlot == Integer.MAX_VALUE) {
            this.doGarbageCollecting();
        }
    }

    public void disposeObject(HeapSlot slot) {
        if (!this.heapSlots.contains(slot)) {
            throw new SegmentationFaultException();
        }

        this.heapSlots.remove(slot);
        this.memory.remove(slot.getMemoryIndex());
    }

    public void setValueAt(HeapSlot heapSlot, Object value) {
        if (!this.heapSlots.contains(heapSlot)) {
            throw new SegmentationFaultException();
        }

        this.memory.put(heapSlot.getMemoryIndex(), value);
    }

    public Object getValueAt(HeapSlot heapSlot) {
        if (!this.heapSlots.contains(heapSlot)) {
            throw new SegmentationFaultException();
        }

        return this.memory.get(heapSlot.getMemoryIndex());
    }

    // TODO: this needs to be tested
    private void doGarbageCollecting() {
        this.emptyMemorySlot = this.findFirstEmptyMemorySlot(this.MIN_ADDRESS);

        for (HeapSlot slot : this.heapSlots) {

            if (slot.getMemoryIndex() > this.emptyMemorySlot) {
                Object value = this.memory.get(slot.getMemoryIndex());
                this.memory.put(this.emptyMemorySlot, value);
                slot.setMemoryIndex(this.emptyMemorySlot++);
                this.emptyMemorySlot = this.findFirstEmptyMemorySlot(this.emptyMemorySlot);
            }
        }
    }

    private int findFirstEmptyMemorySlot(int fromIndex) {
        for (int i = fromIndex; i < Integer.MAX_VALUE; ++i) {
            if (!this.memory.containsKey(i)) {
                return i;
            }
        }

        throw new OutOfMemoryException();
    }
}
