package cz.cuni.mff.d3s.trupple.language.nodes.variables.read;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.NodeFields;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.UnexpectedRuntimeException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.ConstantDescriptor;

@NodeFields({
    @NodeField(name = "typeDescriptor", type = ConstantDescriptor.class),
})
public abstract class ReadConstantNode extends ExpressionNode {

	protected abstract ConstantDescriptor getTypeDescriptor();

	@CompilerDirectives.CompilationFinal private int intValue;
    @CompilerDirectives.CompilationFinal private long longValue;
    @CompilerDirectives.CompilationFinal private double doubleValue;
    @CompilerDirectives.CompilationFinal private char charValue;
    @CompilerDirectives.CompilationFinal private boolean booleanValue;
    @CompilerDirectives.CompilationFinal private Object genericValue;

	public ReadConstantNode(Object value) {
	    if (value instanceof Integer) {
	        this.intValue = (int) value;
        } else if (value instanceof Long) {
            this.longValue = (long) value;
        } else if (value instanceof Double) {
            this.doubleValue = (double) value;
        } else if (value instanceof Character) {
            this.charValue= (char) value;
        } else if (value instanceof Boolean) {
            this.booleanValue = (boolean) value;
        }
        this.genericValue = value;
    }

    @Specialization(guards = "isInt()")
    int readInt(VirtualFrame frame) {
        return this.intValue;
    }

	@Specialization(guards = "isLong()")
    long readLong(VirtualFrame frame) {
        return this.longValue;
    }

    @Specialization(guards = "isDouble()")
    double readDouble(VirtualFrame frame) {
        return this.doubleValue;
    }

    @Specialization(guards = "isChar()")
    char readChar(VirtualFrame frame) {
        return this.charValue;
    }

    @Specialization(guards = "isBoolean()")
    boolean readBoolean(VirtualFrame frame) {
        return booleanValue;
    }

    @Specialization
    Object readGeneric(VirtualFrame frame) {
	    return this.genericValue;
    }

	@Override
    public TypeDescriptor getType() {
	    return this.getTypeDescriptor().getType();
    }

}
