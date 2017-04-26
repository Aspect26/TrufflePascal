package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.io.WriteBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadAllArgumentsNode;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;

import java.util.ArrayList;
import java.util.List;

public class WriteSubroutineDescriptor extends BuiltinProcedureDescriptor.NoReferenceParameterBuiltin {

    public WriteSubroutineDescriptor() {
        super(WriteBuiltinNodeFactory.create(new ExpressionNode[]{new ReadAllArgumentsNode()}), new ArrayList<>());
    }

    @Override
    public void verifyArguments(List<ExpressionNode> passedArguments) throws LexicalException {

    }

}