package cz.cuni.mff.d3s.trupple.language.customvalues;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;

public class RecordValue implements ICustomValue {

    private final VirtualFrame frame;

    public RecordValue(FrameDescriptor frameDescriptor) {
        this.frame = Truffle.getRuntime().createVirtualFrame(new Object[frameDescriptor.getSize()], frameDescriptor);
    }

    @Override
    public Object getValue() {
        return this;
    }

    public VirtualFrame getFrame() {
        return this.frame;
    }
}
