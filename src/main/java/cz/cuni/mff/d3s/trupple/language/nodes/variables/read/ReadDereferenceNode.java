package cz.cuni.mff.d3s.trupple.language.nodes.variables.read;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

@NodeChild(value = "pointer", type = ExpressionNode.class)
@NodeField(name = "returnType", type = TypeDescriptor.class)
public abstract class ReadDereferenceNode extends ExpressionNode {

    protected abstract TypeDescriptor getReturnType();

    @Specialization(guards = "isLong()")
    long dereferenceLong(PointerValue pointer) {
        return (long) pointer.getDereferenceValue();
    }

    @Specialization(guards = "isDouble()")
    double dereferenceDouble(PointerValue pointer) {
        return (double) pointer.getDereferenceValue();
    }

    @Specialization(guards = "isChar()")
    char dereferenceChar(PointerValue pointer) {
        return (char) pointer.getDereferenceValue();
    }

    @Specialization(guards = "isBoolean()")
    boolean dereferenceBoolean(PointerValue pointer) {
        return (boolean) pointer.getDereferenceValue();
    }

    @Specialization
    Object dereferenceGeneric(PointerValue pointer) {
        return pointer.getDereferenceValue();
    }

    @Override
    public TypeDescriptor getType() {
        return this.getReturnType();
    }

}
