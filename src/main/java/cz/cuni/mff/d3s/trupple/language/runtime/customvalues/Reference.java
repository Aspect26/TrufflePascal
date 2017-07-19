package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Representation of variables passed as a reference to subroutines. It contains a frame of the variable and its slot.
 */
@CompilerDirectives.ValueType
public class Reference {

    private final VirtualFrame fromFrame;
    private final FrameSlot frameSlot;

    /**
     * Default c'tor.
     * @param frame the frame of the variable
     * @param frameSlot the variables slot to the frame
     */
    public Reference(VirtualFrame frame, FrameSlot frameSlot) {
        this.fromFrame = frame;
        this.frameSlot = frameSlot;
    }

    public VirtualFrame getFromFrame() {
        return fromFrame;
    }

    public FrameSlot getFrameSlot() {
        return frameSlot;
    }
}
