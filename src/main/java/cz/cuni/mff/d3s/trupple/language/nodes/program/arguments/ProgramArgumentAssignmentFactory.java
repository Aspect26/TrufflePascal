package cz.cuni.mff.d3s.trupple.language.nodes.program.arguments;

import com.oracle.truffle.api.frame.FrameSlot;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.write.SimpleAssignmentNodeGen;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.FileDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.ArrayDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.*;

/**
 * Factory used to create program argument assignment nodes for specific types. It uses combination of
 * {@link cz.cuni.mff.d3s.trupple.language.nodes.variables.write.SimpleAssignmentNode} and {@link ReadArgumentNode}
 * where possible.
 */
public class ProgramArgumentAssignmentFactory {

    public static StatementNode create(FrameSlot frameSlot, int index, TypeDescriptor type) throws LexicalException {
        if (type == IntDescriptor.getInstance() || type == LongDescriptor.getInstance() || type == RealDescriptor.getInstance() ||
                type == BooleanDescriptor.getInstance()) {
            return SimpleAssignmentNodeGen.create(new ReadArgumentNode(index, type), frameSlot);
        } else if (type == StringDescriptor.getInstance()) {
            return new StringProgramArgumentAssignmentNode(frameSlot, index);
        } else if (type instanceof FileDescriptor) {
            return new FileProgramArgumentAssignmentNode(frameSlot, index);
        } else {
            throw new LexicalException("Unsupported program argument type for argument number " + index);
        }
    }

}
