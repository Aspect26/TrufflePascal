package cz.cuni.mff.d3s.trupple.language.nodes.program.arguments;

import com.oracle.truffle.api.frame.FrameSlot;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.FileDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.*;

public class ProgramArgumentAssignmentFactory {

    public static StatementNode create(FrameSlot frameSlot, int index, TypeDescriptor type) throws LexicalException {
        index++; // NOTE: indexes are moved by one because the first argument is always parent frame
        if (type == IntDescriptor.getInstance()) {
            return new IntProgramArgumentAssignmentNode(frameSlot, index);
        } else if (type == LongDescriptor.getInstance()) {
            return new LongProgramArgumentAssignmentNode(frameSlot, index);
        } else if (type == RealDescriptor.getInstance()) {
            return new RealProgramArgumentAssignmentNode(frameSlot, index);
        } else if (type == BooleanDescriptor.getInstance()) {
            return new BooleanProgramArgumentAssignmentNode(frameSlot, index);
        } else if (type == StringDescriptor.getInstance()) {
            return new StringProgramArgumentAssignmentNode(frameSlot, index);
        } else if (type instanceof FileDescriptor) {
            return new FileProgramArgumentAssignmentNode(frameSlot, index);
        } else {
            throw new LexicalException("Unsupported program argument type for argument number " + index);
        }
    }

}
