package cz.cuni.mff.d3s.trupple.language.parser.identifierstable;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import cz.cuni.mff.d3s.trupple.language.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.language.parser.LexicalException;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdentifiersTable {

    /* TODO: HIGH PRIORITY oddelit types a variables
        type color(red,green,blue);
        identifikatory color,red,green aj blue maju rovnaky type descriptor
     */

    /** Map of all identifiers: e.g.: variable names, function names, types names, ... */
    private Map<String, TypeDescriptor> identifiersMap;

    /** Map of type identifiers: e.g.: integer, boolean, enums, records, ... */
    private Map<String, TypeDescriptor> typeDescriptors;

    private FrameDescriptor frameDescriptor;

    public IdentifiersTable() {
        this.identifiersMap = new HashMap<>();
        this.typeDescriptors = new HashMap<>();
        this.frameDescriptor = new FrameDescriptor();

        addBuiltinTypes();
    }

    private void addBuiltinTypes() {
        typeDescriptors.put("integer", PrimitiveDescriptor.longDescriptor());
        typeDescriptors.put("shortint", PrimitiveDescriptor.longDescriptor());
        typeDescriptors.put("longint", PrimitiveDescriptor.longDescriptor());
        typeDescriptors.put("byte", PrimitiveDescriptor.longDescriptor());
        typeDescriptors.put("word", PrimitiveDescriptor.longDescriptor());
        typeDescriptors.put("single", PrimitiveDescriptor.floatDescriptor());
        typeDescriptors.put("real", PrimitiveDescriptor.floatDescriptor());
        typeDescriptors.put("double", PrimitiveDescriptor.floatDescriptor());
        typeDescriptors.put("boolean", PrimitiveDescriptor.booleanDescriptor());
        typeDescriptors.put("char", PrimitiveDescriptor.charDescriptor());
    }

    public FrameSlot getFrameSlot(String identifier) {
        return this.frameDescriptor.findFrameSlot(identifier);
    }

    public FrameDescriptor getFrameDescriptor() {
        return this.frameDescriptor;
    }

    public TypeDescriptor getTypeDescriptor(String identifier) {
        return this.identifiersMap.get(identifier);
    }

    public boolean containsIdentifier(String identifier) {
        return this.identifiersMap.containsKey(identifier);
    }

    public boolean isVariable(String identifier) {
        return this.identifiersMap.containsKey(identifier) && this.identifiersMap.get(identifier).isVariable();
    }

    public boolean isSubroutine(String identifier) {
        return this.identifiersMap.containsKey(identifier) && (this.identifiersMap.get(identifier) instanceof SubroutineDescriptor);
    }

    public boolean isParameterlessSubroutine(String identifier) {
        if (!this.identifiersMap.containsKey(identifier)) {
            return false;
        }

        TypeDescriptor descriptor = this.identifiersMap.get(identifier);
        if (!(descriptor instanceof SubroutineDescriptor)){
            return false;
        } else {
            return ((SubroutineDescriptor) descriptor).hasParameters();
        }
    }

    public void addVariable(String typeName, String identifier) throws LexicalException {
        TypeDescriptor typeDescriptor = typeDescriptors.get(typeName);
        this.registerNewIdentifier(identifier, typeDescriptor);

        if (typeDescriptor == UnknownDescriptor.SINGLETON) {
            throw new LexicalException("Unknown type: " + typeName);
        }
    }

    public void addArrayVariable(String identifier, List<OrdinalDescriptor> ordinalDimensions, String returnType) throws LexicalException {
        TypeDescriptor returnTypeDescriptor = typeDescriptors.get(returnType);
        TypeDescriptor typeDescriptor = new ArrayDescriptor(ordinalDimensions, returnTypeDescriptor);

        this.registerNewIdentifier(identifier, typeDescriptor);

        if (returnTypeDescriptor == UnknownDescriptor.SINGLETON) {
            throw new LexicalException("Unknown type: " + returnType);
        }
    }

    public void addEnumType(String name, List<String> identifiers) throws LexicalException {
        if(this.typeDescriptors.containsKey(name))
            throw new LexicalException("The type is already defined: " + name + ".");

        TypeDescriptor typeDescriptor = new EnumTypeDescriptor(identifiers);
        this.registerNewIdentifier(name, typeDescriptor);
    }

    public void addProcedureInterface(String identifier, List<FormalParameter> formalParameters) throws LexicalException {
        TypeDescriptor typeDescriptor = new ProcedureDescriptor(formalParameters);
        this.registerNewIdentifier(identifier, typeDescriptor);
    }

    public void addFunctionInterface(String identifier, List<FormalParameter> formalParameters, String returnType) throws LexicalException {
        TypeDescriptor returnTypeDescriptor = typeDescriptors.get(returnType);
        TypeDescriptor typeDescriptor = new FunctionDescriptor(formalParameters, returnTypeDescriptor);
        this.registerNewIdentifier(identifier, typeDescriptor);

        if (returnTypeDescriptor == UnknownDescriptor.SINGLETON) {
            throw new LexicalException("Unknown return type: " + returnType);
        }
    }

    private void registerNewIdentifier(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        if (this.identifiersMap.containsKey(identifier)){
            throw new LexicalException("Duplicate identifier: " + identifier + ".");
        } else {
            this.identifiersMap.put(identifier, typeDescriptor);
            this.frameDescriptor.addFrameSlot(identifier, typeDescriptor.getSlotKind());
        }
    }
}
