package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.compound;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customvalues.RecordValue;
import cz.cuni.mff.d3s.trupple.language.parser.LexicalScope;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.TypeDescriptor;

public class RecordDescriptor implements TypeDescriptor {

    private final LexicalScope innerScope;

    public RecordDescriptor(LexicalScope innerScope) {
        this.innerScope = innerScope;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return new RecordValue(this.innerScope.getFrameDescriptor());
    }

    public LexicalScope getLexicalScope() {
        return innerScope;
    }

}
