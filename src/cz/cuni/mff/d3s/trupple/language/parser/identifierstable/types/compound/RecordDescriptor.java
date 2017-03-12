package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.compound;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customvalues.RecordValue;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.TypeDescriptor;

import java.util.Map;

public class RecordDescriptor implements TypeDescriptor {

    private final FrameDescriptor frameDescriptor;

    public RecordDescriptor(Map<String, TypeDescriptor> variables) {
        frameDescriptor = new FrameDescriptor();

        for (Map.Entry<String, TypeDescriptor> variable : variables.entrySet()) {
            frameDescriptor.addFrameSlot(variable.getKey(), variable.getValue().getSlotKind());
        }
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return new RecordValue(this.frameDescriptor);
    }
}
