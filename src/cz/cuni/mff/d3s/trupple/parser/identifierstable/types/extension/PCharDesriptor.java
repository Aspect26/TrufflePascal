package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.extension;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customvalues.PCharValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.ArrayDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.PrimitiveDescriptor;

import java.util.ArrayList;
import java.util.List;

public class PCharDesriptor extends ArrayDescriptor {

    private static PCharDesriptor instance = new PCharDesriptor();

    public PCharDesriptor() {
        super(new ArrayList<OrdinalDescriptor>(){{add(LongDescriptor.getInstance());}}, CharDescriptor.getInstance());
    }

    public static PCharDesriptor getInstance() {
        return instance;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return new PCharValue();
    }

}