package cz.cuni.mff.d3s.trupple.language.nodes.call;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.PascalLanguage;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public abstract class ContextInvokeNode extends ExpressionNode {

    private final String subroutineIdentifier;
    private final String unitIdentifier;
    private final TypeDescriptor returnType;
    @CompilerDirectives.CompilationFinal private PascalSubroutine subroutine;
    @CompilerDirectives.CompilationFinal private VirtualFrame unitFrame;
    @Children private final ExpressionNode[] argumentNodes;

    protected ContextInvokeNode(String identifier, String unitIdentifier, ExpressionNode[] argumentNodes, TypeDescriptor returnType) {
        this.subroutineIdentifier = identifier;
        this.unitIdentifier = unitIdentifier;
        this.argumentNodes = argumentNodes;
        this.returnType = returnType;
    }

    @Override
    public TypeDescriptor getType() {
        return this.returnType;
    }

    @Specialization
    Object invoke(VirtualFrame frame) {
        if (subroutine == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            subroutine = this.getSubroutine();
            unitFrame = this.getUnitFrame();
        }
        Object[] argumentValues = this.evaluateArguments(frame);
        return this.subroutine.getCallTarget().call(argumentValues);
    }

    private PascalSubroutine getSubroutine() {
        return PascalLanguage.INSTANCE.findContext().getSubroutine(this.unitIdentifier, this.subroutineIdentifier);
    }

    private VirtualFrame getUnitFrame() {
        return PascalLanguage.INSTANCE.findContext().getUnitFrame(this.unitIdentifier);
    }

    private Object[] evaluateArguments(VirtualFrame frame) {
        Object[] argumentValues = new Object[argumentNodes.length + 1];
        argumentValues[0] = unitFrame;
        for (int i = 0; i < argumentNodes.length; i++) {
            argumentValues[i+1] = argumentNodes[i].executeGeneric(frame);
        }

        return argumentValues;
    }
}
