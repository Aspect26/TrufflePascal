package cz.cuni.mff.d3s.trupple.language.customvalues;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.*;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import java.util.Map;

public class RecordValue {

    private final VirtualFrame frame;
    private final FrameDescriptor frameDescriptor;

    public RecordValue(FrameDescriptor frameDescriptor, Map<String, TypeDescriptor> types) {
        this(frameDescriptor);
        this.initValues(frameDescriptor, types);
    }

    private RecordValue(FrameDescriptor frameDescriptor) {
        this.frameDescriptor = frameDescriptor;
        this.frame = Truffle.getRuntime().createVirtualFrame(new Object[frameDescriptor.getSize()], frameDescriptor);
    }

    private void initValues(FrameDescriptor frameDescriptor, Map<String, TypeDescriptor> types) {
        for (FrameSlot slot : frameDescriptor.getSlots()) {
            TypeDescriptor slotsType = types.get(slot.getIdentifier().toString());
            this.initValue(slot, slotsType);
        }
    }

    private void initValue(FrameSlot slot, TypeDescriptor descriptor) {
        this.setValue(slot, descriptor.getDefaultValue());
    }

    private void setValue(FrameSlot slot, Object value) {
        // TODO: whole initialization process is weird
        // this switch is surely a duplicity and is also somewhere else in the code
        switch (slot.getKind()) {
            case Long: frame.setLong(slot, (long) value); break;
            case Double: frame.setDouble(slot, (double) value); break;
            case Byte: frame.setByte(slot, (byte) (char) value); break;
            case Boolean: frame.setBoolean(slot, (boolean) value); break;
            case Object: frame.setObject(slot, value); break;
        }
    }

    private void copySlotValue(VirtualFrame fromFrame, VirtualFrame toFrame, FrameSlot slot) throws FrameSlotTypeException {
        // TODO: this switch is surely a duplicity and is also somewhere else in the code
        switch (slot.getKind()) {
            case Long: toFrame.setLong(slot, fromFrame.getLong(slot)); break;
            case Double: toFrame.setDouble(slot, fromFrame.getDouble(slot)); break;
            case Byte: toFrame.setByte(slot, fromFrame.getByte(slot)); break;
            case Boolean: toFrame.setBoolean(slot, fromFrame.getBoolean(slot)); break;
            case Object: toFrame.setObject(slot, fromFrame.getObject(slot)); break;
        }
    }

    public VirtualFrame getFrame() {
        return this.frame;
    }

    public RecordValue getCopy() {
        RecordValue copy = new RecordValue(this.frameDescriptor);
        for (FrameSlot slot : this.frameDescriptor.getSlots()) {
            try {
                this.copySlotValue(this.frame, copy.frame, slot);
            } catch (FrameSlotTypeException e) {
                throw new PascalRuntimeException("Unexpected type in record copying");
            }
        }

        return copy;
    }

}
