package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute.AccessNode;

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

    /**
     * Truffle DSL won't generate {@link AssignmentNodeWithRouteNodeGen} unless this class contains at least one method annotated
     * with @Specialization annotation, even though there are some inherited from the parent class {@link AssignmentNode}.
     * It takes no right side parameter, which cannot be achieved in assignment statement, so this shall not break anything.
     * @param frame this parameter is needed to generate {@link AssignmentNodeWithRouteNodeGen}
     */
    @Specialization
    void totallyUnnecessarSpecializationFunctionWhichWillNeverBeUsedButTruffleDSLJustFuckingNeedsItSoItCanGenerateTheActualNodeFromThisClass_IJustWantedToCreateTheLongestIdentifierIHaveEverCreateInMyLife (VirtualFrame frame) {

    }

}
