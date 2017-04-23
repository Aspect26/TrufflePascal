package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.file.EofBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadAllArgumentsNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.FileDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;

import java.util.List;

public class EofSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public EofSubroutineDescriptor() {
        super(EofBuiltinNodeFactory.create(new ExpressionNode[]{new ReadAllArgumentsNode()}),
                new FormalParameter("p", new FileDescriptor(null), false));
    }

    @Override
    public void verifyArguments(List<ExpressionNode> passedArguments) throws LexicalException {
        if (passedArguments.size() == 1) {
            if (!(passedArguments.get(0).getType() instanceof FileDescriptor)) {
                throw new LexicalException("Eof take zero or on file type argument");
            }
        } else if (passedArguments.size() > 1) {
            throw new LexicalException("Eof take zero or on file type argument");
        }
    }

}