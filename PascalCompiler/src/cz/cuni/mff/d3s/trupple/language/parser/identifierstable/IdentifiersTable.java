package cz.cuni.mff.d3s.trupple.language.parser.identifierstable;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
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
        addBuiltinFunctions();
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

    private void addBuiltinFunctions() {
        identifiersMap.put("write", new ProcedureDescriptor(null));
        identifiersMap.put("writeln", new ProcedureDescriptor(null));
        identifiersMap.put("read", new ProcedureDescriptor(null));
        identifiersMap.put("readln", new ProcedureDescriptor(null));
    }

    public FrameSlot getFrameSlot(String identifier) {
        return this.frameDescriptor.findFrameSlot(identifier);
    }

    public FrameDescriptor getFrameDescriptor() {
        return this.frameDescriptor;
    }

    public TypeDescriptor getTypeTypeDescriptor(String identifier) {
        return this.typeDescriptors.get(identifier);
    }

    public Map<String, TypeDescriptor> getAll() {
        return this.identifiersMap;
    }

    public boolean containsIdentifier(String identifier) {
        return this.identifiersMap.containsKey(identifier);
    }

    public boolean isVariable(String identifier) {
        return this.identifiersMap.containsKey(identifier) && this.identifiersMap.get(identifier).isVariable();
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
        if (!(descriptor instanceof SubroutineDescriptor)){
            return false;
        } else {
            return !((SubroutineDescriptor) descriptor).hasParameters();
        }
    }

    public void addType(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        if (this.identifiersMap.containsKey(identifier)) {
            throw new DuplicitIdentifierException(identifier);
        } else {
            this.typeDescriptors.put(identifier, typeDescriptor);
        }
    }

    public FrameSlot addVariable(String typeName, String identifier) throws LexicalException {
        TypeDescriptor typeDescriptor = typeDescriptors.get(typeName);
        FrameSlot frameSlot = this.registerNewIdentifier(identifier, typeDescriptor);

        if (typeDescriptor == UnknownDescriptor.SINGLETON) {
            throw new UnknownTypeException(typeName);
        }

        return frameSlot;
    }

    public FrameSlot addVariable(TypeDescriptor typeDescriptor, String identifier) throws LexicalException {
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

    public void addLongConstant(String identifier, long value) throws LexicalException {
        this.registerNewIdentifier(identifier, new LongConstantDescriptor(value));
    }

    public void addRealConstant(String identifier, double value) throws LexicalException {
        this.registerNewIdentifier(identifier, new RealConstantDescriptor(value));
    }

    public void addBooleanConstant(String identifier, boolean value) throws LexicalException {
        this.registerNewIdentifier(identifier, new BooleanConstantDescriptor(value));
    }

    public void addCharConstant(String identifier, char value) throws LexicalException {
        this.registerNewIdentifier(identifier, new CharConstantDescriptor(value));
    }

    public void addStringConstant(String identifier, String value) throws LexicalException {
        this.registerNewIdentifier(identifier, new StringConstantDescriptor(value));
    }

    public void addConstantFromConstant(String identifier, String valueIdentifier) throws LexicalException {
        ConstantDescriptor valueDescriptor = getConstant(valueIdentifier);
        this.registerNewIdentifier(identifier, (valueDescriptor).shallowCopy());
    }

    public void addConstantFromNegatedConstant(String identifier, String valueIdentifier) throws LexicalException {
        ConstantDescriptor valueDescriptor = getConstant(valueIdentifier);
        this.registerNewIdentifier(identifier, (valueDescriptor).negatedCopy());
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

    public TypeDescriptor createArray(List<OrdinalDescriptor> ordinalDimensions, String returnType) throws LexicalException {
        TypeDescriptor returnTypeDescriptor = typeDescriptors.get(returnType);
        TypeDescriptor typeDescriptor = new ArrayDescriptor(ordinalDimensions, returnTypeDescriptor);

        if (returnTypeDescriptor == UnknownDescriptor.SINGLETON) {
            throw new UnknownTypeException(returnType);
        }

        return typeDescriptor;
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

    private ConstantDescriptor getConstant(String identifier) throws LexicalException {
        TypeDescriptor descriptor = this.getTypeTypeDescriptor(identifier);
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
