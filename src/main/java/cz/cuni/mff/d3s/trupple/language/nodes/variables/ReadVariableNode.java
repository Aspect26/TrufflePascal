package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.NodeFields;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

@NodeFields({
    @NodeField(name = "slot", type = FrameSlot.class),
    @NodeField(name = "typeDescriptor", type = TypeDescriptor.class)
})
public abstract class ReadVariableNode extends ExpressionNode {

	protected abstract FrameSlot getSlot();

	protected abstract TypeDescriptor getTypeDescriptor();

    @Specialization
	Object readObject(VirtualFrame frame) {
        VirtualFrame slotsFrame = this.getFrameContainingSlot(frame, getSlot());
        return this.getValueFromSlot(slotsFrame, getSlot());
	}

	@Override
    public TypeDescriptor getType() {
	    return this.getTypeDescriptor();
    }

}
