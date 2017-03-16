package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.*;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class AssignmentNodeWithRoute extends ExpressionNode {

    @Children private final AccessRouteNode[] accessRouteNodes;

    private VirtualFrame slotsFrame;
    private FrameSlot finalSlot;
    private boolean isArray;
    private Object[] arrayIndexes;

    AssignmentNodeWithRoute(AccessRouteNode[] accessRouteNodes) {
        this.accessRouteNodes = accessRouteNodes;
    }

    protected abstract FrameSlot getSlot();

    private interface SlotAssignment {

        void assign(VirtualFrame frame, FrameSlot frameSlot, Object value) throws FrameSlotTypeException;

    }

    private void makeAssignment(VirtualFrame frame, FrameSlot slot, SlotAssignment slotAssignment, Object value) {
        try {
            frame = this.getFrameContainingSlot(frame, slot);
            this.getAssignmentTarget(frame, slot);
            if (this.isArray) {
                PascalArray array = (PascalArray) this.slotsFrame.getObject(this.finalSlot);
                array.setValueAt(this.arrayIndexes, value);
            } else {
                slotAssignment.assign(this.slotsFrame, this.finalSlot, value);
            }
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Wrong access");
        }
    }

	@Specialization
	long writeLong(VirtualFrame frame, long value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setLong(assignmentFrameSlot, (long) assignmentValue),
                value
        );

        return value;
	}

	@Specialization
	boolean writeBoolean(VirtualFrame frame, boolean value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setBoolean(assignmentFrameSlot, (boolean) assignmentValue),
                value
        );

        return value;
	}

	// NOTE: characters are stored as bytes, since there is no FrameSlotKind for char
	@Specialization
	char writeChar(VirtualFrame frame, char value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setByte(assignmentFrameSlot, (byte)((char) assignmentValue)),
                value
        );

        return value;
	}

	@Specialization
	double writeDouble(VirtualFrame frame, double value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setDouble(assignmentFrameSlot, (double) assignmentValue),
                value
        );

        return value;
	}
	
	@Specialization
	Object writeEnum(VirtualFrame frame, EnumValue value) {
        this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, value);
        return value;
	}
	
	@Specialization
	Object assignArray(VirtualFrame frame, PascalArray array) {
		PascalArray arrayCopy = array.createDeepCopy();
        this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, arrayCopy);
        return arrayCopy;
	}

    @Specialization(guards = "isSet(frame, getSlot())")
    protected Object assignSet(VirtualFrame frame, SetTypeValue set) {
        SetTypeValue setCopy = set.createDeepCopy();
        this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, setCopy);
        return setCopy;
    }

    /**
     * Find the VirtualFrame and its FrameSlot where the assignment target is located. The variables are found via
     * list of access route nodes. The result is stored in object's private properties slotsFrame and finalSlot. It also
     * sets the isArray field to true, if the assignment target shall be extracted from an array (the last access of the
     * variable is an array access, not record element access).
     * @param frame the frame, in which the array index expressions shall be evaluated
     * @return true if the target is an array, false otherwise
     * @throws FrameSlotTypeException if the access goes wrong (e.g.: it is indexing a variable which is not an array)
     */
    private void getAssignmentTarget(VirtualFrame frame, FrameSlot firstSlot) throws FrameSlotTypeException {
        this.isArray = false;
        this.finalSlot = firstSlot;
	    this.slotsFrame = frame;
	    this.evaluateIndexNodes(frame);
	    RecordValue recordFromArray = null;

	    for (AccessRouteNode accessRouteNode : this.accessRouteNodes) {
	        if (accessRouteNode instanceof AccessRouteNode.EnterRecord) {
	            RecordValue record = (recordFromArray == null)? (RecordValue) this.slotsFrame.getObject(this.finalSlot) : recordFromArray;
	            this.slotsFrame = record.getFrame();
	            this.finalSlot = ((AccessRouteNode.EnterRecord) accessRouteNode).getRecordFrameSlot();
	            this.isArray = false;
	            recordFromArray = null;
            } else if (accessRouteNode instanceof AccessRouteNode.ArrayIndex) {
	            PascalArray array = (PascalArray) this.slotsFrame.getObject(this.finalSlot);
                this.arrayIndexes = ((AccessRouteNode.ArrayIndex) accessRouteNode).getIndexes();
                Object value = array.getValueAt(this.arrayIndexes);
                if (value instanceof RecordValue) {
                    recordFromArray = (RecordValue) value;
                }
                this.isArray = true;
            }
        }

        Reference referenceVariable = this.tryGetReference(this.slotsFrame, this.finalSlot);
        if (referenceVariable != null) {
            this.slotsFrame = referenceVariable.getFromFrame();
            this.finalSlot = referenceVariable.getFrameSlot();
        }
    }

    private void evaluateIndexNodes(VirtualFrame frame) {
        for (AccessRouteNode accessRouteNode : this.accessRouteNodes) {
            accessRouteNode.executeVoid(frame);
        }
    }

}
