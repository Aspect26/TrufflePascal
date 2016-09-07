package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

public class ArrayIndexAssignmentNode extends ExpressionNode {

	private final FrameSlot frameSlot;
	@Children final private ExpressionNode[] indexingNodes;
	@Child private ExpressionNode valueNode;
	
	public ArrayIndexAssignmentNode(FrameSlot frameSlot, ExpressionNode[] indexingNodes, ExpressionNode valueNode) {
		this.frameSlot = frameSlot;
		this.indexingNodes = indexingNodes;
		this.valueNode = valueNode;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		try {
			PascalArray array = (PascalArray)frame.getObject(frameSlot);
			Object[] indexes = getIndexes(frame);
			Object value =  valueNode.executeGeneric(frame);
			array.setValueAt(indexes, value);
			return value;
		} catch (FrameSlotTypeException e) {
			System.err.println("Wrong access to array at runtime.");
			return null;
		}
	}
	
	private Object[] getIndexes(VirtualFrame frame) {
		Object[] indexes = new Object[this.indexingNodes.length];
		for(int i = 0; i < indexes.length; i++) {
			indexes[i] = this.indexingNodes[i].executeGeneric(frame);
		}
		
		return indexes;
	}
}
