package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class ReadArrayIndexNode extends ExpressionNode {

	protected abstract FrameSlot getSlot();
	@Children protected final ExpressionNode[] indexingNodes;

	public ReadArrayIndexNode(ExpressionNode[] indexingNodes) {
		this.indexingNodes = indexingNodes;
	}
	
	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected long readLong(VirtualFrame frame) throws FrameSlotTypeException {
		Object[] indexes = getIndexes(frame);
		PascalArray array = (PascalArray)frame.getObject(getSlot());
		try {
			return (Long)array.getValueAt(indexes);
		} catch(ClassCastException e) {
			throw new FrameSlotTypeException();
		}
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected boolean readBool(VirtualFrame frame) throws FrameSlotTypeException {
		Object[] indexes = getIndexes(frame);
		PascalArray array = (PascalArray)frame.getObject(getSlot());
		try {
			return (Boolean)array.getValueAt(indexes);
		} catch(ClassCastException e) {
			throw new FrameSlotTypeException();
		}
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected char readChar(VirtualFrame frame) throws FrameSlotTypeException {
		Object[] indexes = getIndexes(frame);
		PascalArray array = (PascalArray)frame.getObject(getSlot());
		try {
			// TODO: possible cause of crash
			return (char)array.getValueAt(indexes);
		} catch(ClassCastException e) {
			throw new FrameSlotTypeException();
		}
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected double readDouble(VirtualFrame frame) throws FrameSlotTypeException {
		Object[] indexes = getIndexes(frame);
		PascalArray array = (PascalArray)frame.getObject(getSlot());
		try {
			return (Double)array.getValueAt(indexes);
		} catch(ClassCastException e) {
			throw new FrameSlotTypeException();
		}
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected Object readObject(VirtualFrame frame) throws FrameSlotTypeException {
		//TODO: can be array, record, enum
		return null;
	}
	
	private Object[] getIndexes(VirtualFrame frame) {
		Object[] indexes = new Object[indexingNodes.length];
		for(int i = 0; i < indexes.length; i++) {
			indexes[i] = this.indexingNodes[i].executeGeneric(frame);
		}
		return indexes;
	}
}
