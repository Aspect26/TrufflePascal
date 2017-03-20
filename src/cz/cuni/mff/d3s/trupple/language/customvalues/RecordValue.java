package cz.cuni.mff.d3s.trupple.language.customvalues;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import java.util.Map;

public class RecordValue {

    private final VirtualFrame frame;

    public RecordValue(FrameDescriptor frameDescriptor, Map<String, TypeDescriptor> types) {
        this.frame = Truffle.getRuntime().createVirtualFrame(new Object[frameDescriptor.getSize()], frameDescriptor);
        this.initValues(frameDescriptor, types);
    }

    private void initValues(FrameDescriptor frameDescriptor, Map<String, TypeDescriptor> types) {
        for (FrameSlot slot : frameDescriptor.getSlots()) {
            TypeDescriptor slotsType = types.get(slot.getIdentifier().toString());
            this.initValue(slot, slotsType);
        }
    }

    private void initValue(FrameSlot slot, TypeDescriptor type) {
        // TODO: whole initialization process is weird
        // this switch is surely a duplicity and is also somewhere else in the code
        switch (slot.getKind()) {
            case Long: frame.setLong(slot, (long) type.getDefaultValue()); break;
            case Double: frame.setDouble(slot, (double) type.getDefaultValue()); break;
            case Byte: frame.setByte(slot, (byte) (char) type.getDefaultValue()); break;
            case Boolean: frame.setBoolean(slot, (boolean) type.getDefaultValue()); break;
            case Object: frame.setObject(slot, type.getDefaultValue()); break;
        }
    }

    public VirtualFrame getFrame() {
        return this.frame;
    }

}
