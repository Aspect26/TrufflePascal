package cz.cuni.mff.d3s.trupple.language.nodes.control;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.LessThanNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.LessThanOrEqualNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.NotNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.write.SimpleAssignmentNode;

/**
 * Node representing for cycle.
 */
@NodeInfo(shortName = "for")
public class ForNode extends StatementNode {

    private interface ControlInterface {

        void increaseControlVariable() throws FrameSlotTypeException;

        void decreaseControlVariable() throws FrameSlotTypeException;
    }

    private final boolean ascending;
    private final FrameSlot controlSlot;
    @Child
    private SimpleAssignmentNode assignment;
    @Child
    private StatementNode body;
    @Child
    private ExpressionNode hasEndedNode;

    public ForNode(boolean ascending, SimpleAssignmentNode assignment, FrameSlot controlSlot, ExpressionNode finalValue,
                   ExpressionNode readControlVariableNode, StatementNode body) {
        this.ascending = ascending;
        this.assignment = assignment;
        this.controlSlot = controlSlot;
        this.body = body;
        this.hasEndedNode = (ascending)?
                LessThanOrEqualNodeGen.create(readControlVariableNode, finalValue)
                :
                NotNodeGen.create(LessThanNodeGen.create(readControlVariableNode, finalValue));
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        try {
            ControlInterface controlInterface = null;

            switch (controlSlot.getKind()) {
                case Int:
                    controlInterface = this.createIntControlInterface(frame);
                    break;
                case Long:
                    controlInterface = this.createLongControlInterface(frame);
                    break;
                case Byte:
                    controlInterface = this.createCharControlInterface(frame);
                    break;
                case Object:
                    Object controlValue = frame.getObject(controlSlot);
                    if (controlValue instanceof EnumValue) {
                        controlInterface = this.createEnumControlInterface(frame);
                        break;
                    } else {
                        throw new PascalRuntimeException("Unsupported control variable type");
                    }
            }

            this.execute(frame, controlInterface, ascending);
        } catch (FrameSlotTypeException | UnexpectedResultException e) {
            throw new PascalRuntimeException("Something went wrong.");
        }
    }

    private void execute(VirtualFrame frame, ControlInterface control, boolean ascending) throws FrameSlotTypeException, UnexpectedResultException {
        this.assignment.executeVoid(frame);
        // TODO: check if it should even start
        while (this.hasEndedNode.executeBoolean(frame)) {
            body.executeVoid(frame);
            if (ascending) {
                control.increaseControlVariable();
            } else {
                control.decreaseControlVariable();
            }
        }
    }

    private ControlInterface createIntControlInterface(VirtualFrame frame) {

        return new ControlInterface() {

            @Override
            public void increaseControlVariable() throws FrameSlotTypeException {
                int controlValue = frame.getInt(controlSlot);
                frame.setInt(controlSlot, ++controlValue);
            }

            @Override
            public void decreaseControlVariable() throws FrameSlotTypeException {
                int controlValue = frame.getInt(controlSlot);
                frame.setInt(controlSlot, --controlValue);
            }
        };

    }

    private ControlInterface createLongControlInterface(VirtualFrame frame) {

        return new ControlInterface() {

            @Override
            public void increaseControlVariable() throws FrameSlotTypeException {
                long controlValue = frame.getLong(controlSlot);
                frame.setLong(controlSlot, ++controlValue);
            }

            @Override
            public void decreaseControlVariable() throws FrameSlotTypeException {
                long controlValue = frame.getLong(controlSlot);
                frame.setLong(controlSlot, --controlValue);
            }
        };
    }

    private ControlInterface createCharControlInterface(VirtualFrame frame) {

        return new ControlInterface() {

            @Override
            public void increaseControlVariable() throws FrameSlotTypeException {
                char controlValue = (char)frame.getByte(controlSlot);
                frame.setByte(controlSlot, (byte)++controlValue);
            }

            @Override
            public void decreaseControlVariable() throws FrameSlotTypeException {
                char controlValue = (char)frame.getByte(controlSlot);
                frame.setByte(controlSlot, (byte)--controlValue);
            }
        };
    }

    private ControlInterface createEnumControlInterface(VirtualFrame frame) {

        return new ControlInterface() {

            @Override
            public void increaseControlVariable() throws FrameSlotTypeException {
                EnumValue controlValue = (EnumValue) frame.getObject(controlSlot);
                frame.setObject(controlSlot, controlValue.getNext());
            }

            @Override
            public void decreaseControlVariable() throws FrameSlotTypeException {
                EnumValue controlValue = (EnumValue) frame.getObject(controlSlot);
                frame.setObject(controlSlot, controlValue.getNext());
            }
        };
    }
}