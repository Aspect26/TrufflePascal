package cz.cuni.mff.d3s.trupple.language.parser.identifierstable;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.DuplicitIdentifierException;
import cz.cuni.mff.d3s.trupple.language.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.UnknownIdentifierException;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.UnknownTypeException;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdentifiersTable {

    /** Map of all identifiers: e.g.: variable names, function names, types names, ... */
    Map<String, TypeDescriptor> identifiersMap;

    /** Map of type identifiers: e.g.: integer, boolean, enums, records, ... */
    private Map<String, TypeDescriptor> typeDescriptors;

    private FrameDescriptor frameDescriptor;

    public IdentifiersTable() {
        this.initialize();
        addBuiltinTypes();
        addBuiltinFunctions();
    }

    private void initialize() {
        this.identifiersMap = new HashMap<>();
        this.typeDescriptors = new HashMap<>();
        this.frameDescriptor = new FrameDescriptor();
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

        for (Map.Entry<String, TypeDescriptor> typeEntry : typeDescriptors.entrySet()) {
            identifiersMap.put(typeEntry.getKey(), new TypeTypeDescriptor(typeEntry.getValue()));
        }
    }

    protected void addBuiltinFunctions() {
        identifiersMap.put("write", new BuiltinProcedureDescriptor.Write());
        identifiersMap.put("read", new BuiltinProcedureDescriptor.Read());
    }

    public FrameSlot getFrameSlot(String identifier) {
        return this.frameDescriptor.findFrameSlot(identifier);
    }

    public FrameSlotKind getFrameSlotKind(String identifier) {
        return this.identifiersMap.get(identifier).getSlotKind();
    }

    public FrameDescriptor getFrameDescriptor() {
        return this.frameDescriptor;
    }

    public TypeDescriptor getTypeDescriptor(String identifier)  {
        return this.typeDescriptors.get(identifier);
    }

    public TypeDescriptor getTypeTypeDescriptor(String typeIdentifier) {
        return this.typeDescriptors.get(typeIdentifier);
    }

    public Map<String, TypeDescriptor> getAll() {
        return this.identifiersMap;
    }

    public boolean containsIdentifier(String identifier) {
        return this.identifiersMap.containsKey(identifier);
    }

    public boolean isConstant(String identifier) {
        return this.identifiersMap.get(identifier) instanceof ConstantDescriptor;
    }

    public boolean isSubroutine(String identifier) {
        return this.identifiersMap.containsKey(identifier) && (this.identifiersMap.get(identifier) instanceof SubroutineDescriptor);
    }

    public boolean isParameterlessSubroutine(String identifier) {
        if (!this.identifiersMap.containsKey(identifier)) {
            return false;
        }

        TypeDescriptor descriptor = this.identifiersMap.get(identifier);
        return descriptor instanceof SubroutineDescriptor && !((SubroutineDescriptor) descriptor).hasParameters();
    }

    public void addType(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        if (this.identifiersMap.containsKey(identifier)) {
            throw new DuplicitIdentifierException(identifier);
        } else {
            this.typeDescriptors.put(identifier, typeDescriptor);
            this.identifiersMap.put(identifier, new TypeTypeDescriptor(typeDescriptor));
        }
    }

    public FrameSlot addReference(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        return this.registerNewIdentifier(identifier, new ReferenceDescriptor(typeDescriptor));
    }

    public FrameSlot addVariable(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        return this.registerNewIdentifier(identifier, typeDescriptor);
    }

    public void addReturnVariable(String identifier, List<FormalParameter> formalParameters, String typeName) throws LexicalException {
        TypeDescriptor typeDescriptor = typeDescriptors.get(typeName);
        if (typeDescriptor == null) {
            throw new UnknownTypeException(typeName);
        }

        TypeDescriptor returnDescriptor = new FunctionReturnDescriptor(formalParameters, typeDescriptor);
        this.registerNewIdentifier(identifier, returnDescriptor);
    }

    public void addConstant(String identifier, ConstantDescriptor descriptor) throws LexicalException {
        this.registerNewIdentifier(identifier, descriptor);
    }

    public TypeDescriptor createEnum(List<String> identifiers) throws LexicalException {
        EnumTypeDescriptor enumTypeDescriptor = new EnumTypeDescriptor(identifiers);

        for (String identifier : identifiers) {
            if (this.typeDescriptors.containsKey(identifier)) {
                throw new DuplicitIdentifierException(identifier);
            }
            this.registerNewIdentifier(identifier, new EnumValueDescriptor(enumTypeDescriptor, identifier));
        }

        return enumTypeDescriptor;
    }

    public FileDescriptor createFileDescriptor(TypeDescriptor contentTypeDescriptor) {
        return new FileDescriptor(contentTypeDescriptor);
    }

    public TypeDescriptor createArray(List<OrdinalDescriptor> ordinalDimensions, String returnType) throws LexicalException {
        TypeDescriptor returnTypeDescriptor = typeDescriptors.get(returnType);
        TypeDescriptor typeDescriptor = new ArrayDescriptor(ordinalDimensions, returnTypeDescriptor);

        if (returnTypeDescriptor == UnknownDescriptor.SINGLETON) {
            throw new UnknownTypeException(returnType);
        }

        return typeDescriptor;
    }

    public TypeDescriptor createSetType(OrdinalDescriptor base) {
        return new SetDescriptor(base);
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
            throw new UnknownTypeException(returnType);
        }
    }

    public ConstantDescriptor getConstant(String identifier) throws LexicalException {
        TypeDescriptor descriptor = this.getTypeDescriptor(identifier);
        if (descriptor == null) {
            throw new UnknownIdentifierException(identifier);
        } else if (! (descriptor instanceof ConstantDescriptor)) {
            throw new LexicalException("Not a constant: " + identifier);
        } else {
            return (ConstantDescriptor)descriptor;
        }
    }

    private FrameSlot registerNewIdentifier(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        if (this.identifiersMap.containsKey(identifier)){
            throw new DuplicitIdentifierException(identifier);
        } else {
            this.identifiersMap.put(identifier, typeDescriptor);
            return this.frameDescriptor.addFrameSlot(identifier, typeDescriptor.getSlotKind());
        }
    }
}
