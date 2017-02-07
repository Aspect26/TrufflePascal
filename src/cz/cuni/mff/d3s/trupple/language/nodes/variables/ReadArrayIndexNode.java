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
		PascalArray array = getMostInnerArray(frame, indexes);
        Object lastIndex = indexes[indexes.length - 1];
		try {
			return (Long)array.getValueAt(lastIndex);
		} catch(ClassCastException e) {
			throw new FrameSlotTypeException();
		}
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected boolean readBool(VirtualFrame frame) throws FrameSlotTypeException {
        Object[] indexes = getIndexes(frame);
        PascalArray array = getMostInnerArray(frame, indexes);
        Object lastIndex = indexes[indexes.length - 1];
		try {
			return (Boolean)array.getValueAt(lastIndex);
		} catch(ClassCastException e) {
			throw new FrameSlotTypeException();
		}
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected char readChar(VirtualFrame frame) throws FrameSlotTypeException {
        Object[] indexes = getIndexes(frame);
        PascalArray array = getMostInnerArray(frame, indexes);
        Object lastIndex = indexes[indexes.length - 1];
		try {
			// TODO: possible cause of crash
			return (char)array.getValueAt(lastIndex);
		} catch(ClassCastException e) {
			throw new FrameSlotTypeException();
		}
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected double readDouble(VirtualFrame frame) throws FrameSlotTypeException {
        Object[] indexes = getIndexes(frame);
        PascalArray array = getMostInnerArray(frame, indexes);
        Object lastIndex = indexes[indexes.length - 1];
		try {
			return (Double)array.getValueAt(lastIndex);
		} catch(ClassCastException e) {
			throw new FrameSlotTypeException();
		}
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected Object readObject(VirtualFrame frame) throws FrameSlotTypeException {
        Object[] indexes = getIndexes(frame);
        PascalArray array = getMostInnerArray(frame, indexes);
        Object lastIndex = indexes[indexes.length - 1];
		return array.getValueAt(lastIndex);
	}
	
	private Object[] getIndexes(VirtualFrame frame) {
		Object[] indexes = new Object[indexingNodes.length];
		for(int i = 0; i < indexes.length; i++) {
			indexes[i] = this.indexingNodes[i].executeGeneric(frame);
		}
		return indexes;
	}

	private PascalArray getMostInnerArray(VirtualFrame frame, Object[] indexes) throws FrameSlotTypeException {
		PascalArray array = (PascalArray)frame.getObject(getSlot());

		for (int i = 0; i < indexes.length - 1; ++i) {
			array = (PascalArray)array.getValueAt(indexes[i]);
		}

		return array;
	}
}
