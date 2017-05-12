package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.*;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute.AccessNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.extension.PCharDesriptor;

public abstract class AssignmentNodeWithRoute extends AssignmentNode {

    @Child private AccessNode accessNode;

    AssignmentNodeWithRoute(AccessNode accessNode) {
        this.accessNode = accessNode;
    }

    @Override
    protected void makeAssignment(VirtualFrame frame, FrameSlot slot, SlotAssignment slotAssignment, Object value) {
        try {
            frame = this.getFrameContainingSlot(frame, slot);
            accessNode.executeVoid(frame);
            accessNode.assign(frame, slotAssignment, value);
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Wrong access");
        }
    }

    @Specialization
    void writeLong(VirtualFrame frame, long value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setLong(assignmentFrameSlot, (long) assignmentValue),
                value
        );
    }

    @Specialization
    void writeBoolean(VirtualFrame frame, boolean value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setBoolean(assignmentFrameSlot, (boolean) assignmentValue),
                value
        );
    }

    // NOTE: characters are stored as bytes, since there is no FrameSlotKind for char
    @Specialization
    void writeChar(VirtualFrame frame, char value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setByte(assignmentFrameSlot, (byte)((char) assignmentValue)),
                value
        );
    }

    @Specialization
    void writeDouble(VirtualFrame frame, double value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setDouble(assignmentFrameSlot, (double) assignmentValue),
                value
        );
    }

    @Specialization
    void assignString(VirtualFrame frame, PascalString value) {
        Object targetObject;
        try {
            targetObject = frame.getObject(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Unexpected error");
        }
        if (targetObject instanceof PascalString) {
            this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, value);
        } else if (targetObject instanceof PointerValue) {
            PointerValue pointerValue = (PointerValue) targetObject;
            if (pointerValue.getType() instanceof PCharDesriptor) {
                assignPChar(pointerValue, value);
            }
        }
    }

    @Specialization
    void assignPointers(VirtualFrame frame, PointerValue pointer) {
        this.makeAssignment(frame, getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot frameSlot, Object value) ->
                {
                    PointerValue assignmentTarget = (PointerValue) assignmentFrame.getObject(frameSlot);
                    assignmentTarget.setHeapSlot(((PointerValue) value).getHeapSlot());
                },
                pointer);
    }

    @Specialization
    void assignArray(VirtualFrame frame, PascalArray array) {
        PascalArray arrayCopy = (PascalArray) array.createDeepCopy();
        this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, arrayCopy);
    }

    @Specialization
    void assignSubroutine(VirtualFrame frame, PascalSubroutine subroutine) {
        this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, subroutine);
    }

    /**
     * Truffle DSL won't generate {@link AssignmentNodeWithRouteNodeGen} unless this class contains at least one method annotated
     * with @Specialization annotation, even though there are some inherited from the parent class {@link AssignmentNode}.
     * It takes no right side parameter, which cannot be achieved in assignment statement, so this shall not break anything.
     * @param frame this parameter is needed to generate {@link AssignmentNodeWithRouteNodeGen}
     */
    @Specialization
    void totallyUnnecessarySpecializationFunctionWhichWillNeverBeUsedButTruffleDSLJustFuckingNeedsItSoItCanGenerateTheActualNodeFromThisClass_IJustWantedToCreateTheLongestIdentifierIHaveEverCreateInMyLife (VirtualFrame frame) {

    }

}
