package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

public class ArrayIndexAssignmentNode extends ExpressionNode {

	private final FrameSlot frameSlot;
	@Child private ExpressionNode indexNode;
	@Child private ExpressionNode valueNode;
	
	public ArrayIndexAssignmentNode(FrameSlot frameSlot, ExpressionNode indexNode, ExpressionNode valueNode) {
		this.frameSlot = frameSlot;
		this.indexNode = indexNode;
		this.valueNode = valueNode;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		try {
			PascalArray array = (PascalArray)frame.getObject(frameSlot);
			Object index = indexNode.executeGeneric(frame);
			Object value =  valueNode.executeGeneric(frame);
			array.setValueAt(index, value);
			return value;
		} catch (FrameSlotTypeException e) {
			System.err.println("Wrong access to array at runtime.");
			return null;
		}
	}
}
