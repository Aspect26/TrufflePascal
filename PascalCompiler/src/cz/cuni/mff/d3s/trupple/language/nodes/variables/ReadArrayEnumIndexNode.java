package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.types.PascalArray;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class ReadArrayEnumIndexNode extends ExpressionNode {

	protected abstract FrameSlot getSlot();
	protected final String identifier;

	public ReadArrayEnumIndexNode(String identifier) {
		this.identifier = identifier;
	}
	
	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected long readLong(VirtualFrame frame) throws FrameSlotTypeException {
		PascalArray array = (PascalArray)frame.getObject(getSlot());
		try{
			return (Long)array.getValueAt(identifier);
		}
		catch(ClassCastException e) {
			throw new FrameSlotTypeException();
		}
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected boolean readBool(VirtualFrame frame) throws FrameSlotTypeException {
		PascalArray array = (PascalArray)frame.getObject(getSlot());
		try{
			return (Boolean)array.getValueAt(identifier);
		}
		catch(ClassCastException e) {
			throw new FrameSlotTypeException();
		}
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected char readChar(VirtualFrame frame) throws FrameSlotTypeException {
		PascalArray array = (PascalArray)frame.getObject(getSlot());
		try{
			//TODO: possible cause of crash
			return (char)array.getValueAt(identifier);
		}
		catch(ClassCastException e) {
			throw new FrameSlotTypeException();
		}
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected double readDouble(VirtualFrame frame) throws FrameSlotTypeException {
		PascalArray array = (PascalArray)frame.getObject(getSlot());
		try{
			return (Double)array.getValueAt(identifier);
		}
		catch(ClassCastException e) {
			throw new FrameSlotTypeException();
		}
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected Object readObject(VirtualFrame frame) throws FrameSlotTypeException {
		//TODO: can be array, record, enum ...
		return null;
	}
}
