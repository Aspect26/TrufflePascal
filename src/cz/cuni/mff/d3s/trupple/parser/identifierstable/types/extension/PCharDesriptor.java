package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.extension;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customvalues.PCharValue;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.PrimitiveDescriptor;

public class PCharDesriptor extends PrimitiveDescriptor {

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return new PCharValue();
    }

}