package cz.cuni.mff.d3s.trupple.language.customvalues;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

public class Reference implements ICustomValue {

    private final VirtualFrame fromFrame;
    private final FrameSlot frameSlot;

    public Reference(VirtualFrame frame, FrameSlot frameSlot) {
        this.fromFrame = frame;
        this.frameSlot = frameSlot;
    }

    @Override
    public Object getValue() {
        return this;
    }

    public VirtualFrame getFromFrame() {
        return fromFrame;
    }

    public FrameSlot getFrameSlot() {
        return frameSlot;
    }
}