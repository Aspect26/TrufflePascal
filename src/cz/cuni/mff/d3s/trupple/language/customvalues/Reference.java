package cz.cuni.mff.d3s.trupple.language.customvalues;

import com.oracle.truffle.api.frame.VirtualFrame;

public class Reference implements ICustomValue {

    private final VirtualFrame fromFrame;
    private final String identifier;

    public Reference(VirtualFrame frame, String identifier) {
        this.fromFrame = frame;
        this.identifier = identifier;
    }

    @Override
    public Object getValue() {
        return this;
    }

    public VirtualFrame getFromFrame() {
        return fromFrame;
    }

    public String getIdentifier() {
        return identifier;
    }
}
