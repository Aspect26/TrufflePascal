package cz.cuni.mff.d3s.trupple.language.nodes.variables.read;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.NodeFields;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.RecordValue;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.UnexpectedRuntimeException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

@NodeChild(value = "record", type = ExpressionNode.class)
@NodeFields({
        @NodeField(name = "returnType", type = TypeDescriptor.class),
        @NodeField(name = "identifier", type = String.class)
})
public abstract class ReadFromRecordNode extends ExpressionNode {

    protected abstract TypeDescriptor getReturnType();

    protected abstract String getIdentifier();

    @Specialization(guards = "isLong()")
    long readLong(RecordValue record) {
        FrameSlot slot = record.getSlot(this.getIdentifier());
        try {
            return record.getFrame().getLong(slot);
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isDouble()")
    double readDouble(RecordValue record) {
        FrameSlot slot = record.getSlot(this.getIdentifier());
        try {
            return record.getFrame().getDouble(slot);
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isChar()")
    char readChar(RecordValue record) {
        FrameSlot slot = record.getSlot(this.getIdentifier());
        try {
            return (char) record.getFrame().getByte(slot);
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isBoolean()")
    boolean readBoolean(RecordValue record) {
        FrameSlot slot = record.getSlot(this.getIdentifier());
        try {
            return record.getFrame().getBoolean(slot);
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization
    Object readGeneric(RecordValue record) {
        FrameSlot slot = record.getSlot(this.getIdentifier());
        return record.getFrame().getValue(slot);
    }

    @Override
    public TypeDescriptor getType() {
        return this.getReturnType();
    }

}
