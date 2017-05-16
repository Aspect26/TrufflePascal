package cz.cuni.mff.d3s.trupple.language.nodes.variables.read;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.NodeFields;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.UnexpectedRuntimeException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

@NodeFields({
    @NodeField(name = "slot", type = FrameSlot.class),
    @NodeField(name = "typeDescriptor", type = TypeDescriptor.class),
})
public abstract class ReadReferenceVariableNode extends ExpressionNode {

	protected abstract FrameSlot getSlot();

	protected abstract TypeDescriptor getTypeDescriptor();

    @Specialization(guards = "isInt()")
    int readIntReference(VirtualFrame frame) {
        try {
            Reference reference = (Reference) frame.getValue(getSlot());
            return reference.getFromFrame().getInt(reference.getFrameSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isLong()")
    long readLongReference(VirtualFrame frame) {
        try {
            Reference reference = (Reference) frame.getValue(getSlot());
            return reference.getFromFrame().getLong(reference.getFrameSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isDouble()")
    double readDoubleReference(VirtualFrame frame) {
        try {
            Reference reference = (Reference) frame.getValue(getSlot());
            return reference.getFromFrame().getDouble(reference.getFrameSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isChar()")
    char readCharReference(VirtualFrame frame) {
        try {
            Reference reference = (Reference) frame.getValue(getSlot());
            return (char) reference.getFromFrame().getByte(reference.getFrameSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isBoolean()")
    boolean readBooleanReference(VirtualFrame frame) {
        try {
            Reference reference = (Reference) frame.getValue(getSlot());
            return reference.getFromFrame().getBoolean(reference.getFrameSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization
    Object readReference(VirtualFrame frame) {
        Reference reference = (Reference) frame.getValue(getSlot());
        return reference.getFromFrame().getValue(reference.getFrameSlot());
    }

	@Override
    public TypeDescriptor getType() {
	    return this.getTypeDescriptor();
    }

}
