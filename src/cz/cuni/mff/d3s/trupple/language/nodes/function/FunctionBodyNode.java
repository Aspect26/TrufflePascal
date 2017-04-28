package cz.cuni.mff.d3s.trupple.language.nodes.function;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.NodeFields;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;


@NodeFields({
    @NodeField(name = "slot", type = FrameSlot.class),
    @NodeField(name = "typeDescriptor", type = TypeDescriptor.class)
})
public abstract class FunctionBodyNode extends ExpressionNode {

	protected abstract FrameSlot getSlot();
    protected abstract TypeDescriptor getTypeDescriptor();

	@Child
	private StatementNode bodyNode;

	FunctionBodyNode(StatementNode bodyNode) {
		this.bodyNode = bodyNode;
	}

	@Specialization
    Object executeFunction(VirtualFrame frame) {
        bodyNode.executeVoid(frame);
        return frame.getValue(getSlot());
    }

    @Override
    public TypeDescriptor getType() {
        return this.getTypeDescriptor();
    }

}
