package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

public class RouteTarget {

    public final VirtualFrame frame;

    public final FrameSlot slot;

    public final boolean isArray;

    public final Object[] arrayIndexes;

    public RouteTarget(VirtualFrame frame, FrameSlot slot, boolean isArray, Object[] arrayIndexes) {
        this.frame = frame;
        this.slot = slot;
        this.isArray = isArray;
        this.arrayIndexes = arrayIndexes;
    }
}
