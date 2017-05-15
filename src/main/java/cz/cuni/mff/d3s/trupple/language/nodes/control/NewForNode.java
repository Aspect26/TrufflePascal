package cz.cuni.mff.d3s.trupple.language.nodes.control;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal.PredBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal.SuccBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.LessThanNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.LessThanOrEqualNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.NotNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.read.ReadVariableNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.write.SimpleAssignmentNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.write.SimpleAssignmentNodeGen;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.BreakExceptionTP;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.UnexpectedRuntimeException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public class NewForNode extends StatementNode {

    @Child
    private SimpleAssignmentNode initialAssignment;
    @Child
    private StatementNode bodyNode;
    @Child
    private ExpressionNode endConditionNode;
    @Child
    private StatementNode controlVariableAssignment;

    public NewForNode(FrameSlot controlVariableSlot, SimpleAssignmentNode assignmentNode, ExpressionNode endValue,
                      TypeDescriptor controlVariableType,  boolean ascending, StatementNode bodyNode) {
        this.initialAssignment = assignmentNode;
        this.bodyNode = bodyNode;
        this.endConditionNode = this.createEndConditionNode(controlVariableSlot, controlVariableType, endValue, ascending);
        this.controlVariableAssignment = this.createControlVariableAssignment(controlVariableSlot, controlVariableType, ascending);
    }

    private ExpressionNode createEndConditionNode(FrameSlot frameSlot, TypeDescriptor controlVariableType, ExpressionNode endValue,
                                                  boolean ascending) {
        ExpressionNode controlVariableValue = ReadVariableNodeGen.create(frameSlot, controlVariableType, false);
        return (ascending)?
                NotNodeGen.create(LessThanOrEqualNodeGen.create(controlVariableValue, endValue))
                :
                LessThanNodeGen.create(controlVariableValue, endValue);
    }

    private StatementNode createControlVariableAssignment(FrameSlot frameSlot, TypeDescriptor controlVariableType, boolean ascending) {
        ExpressionNode actualValue = ReadVariableNodeGen.create(frameSlot, controlVariableType, false);
        ExpressionNode newValue = (ascending)?
                SuccBuiltinNodeGen.create(actualValue) : PredBuiltinNodeGen.create(actualValue);
        return SimpleAssignmentNodeGen.create(newValue, frameSlot);
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        this.initialAssignment.executeVoid(frame);
        try {
            while (!endConditionNode.executeBoolean(frame)) {
                this.bodyNode.executeVoid(frame);
                this.controlVariableAssignment.executeVoid(frame);
            }
        } catch (UnexpectedResultException e) {
            throw new UnexpectedRuntimeException();
        } catch (BreakExceptionTP e) {
            // NOTE: break
        }
    }

}
