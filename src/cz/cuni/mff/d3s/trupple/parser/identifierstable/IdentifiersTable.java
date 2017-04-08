package cz.cuni.mff.d3s.trupple.parser.identifierstable;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.LexicalScope;
import cz.cuni.mff.d3s.trupple.parser.exceptions.DuplicitIdentifierException;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.UnknownIdentifierException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.UnknownTypeException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeTypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.UnknownDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.*;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.*;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.ConstantDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.label.LabelDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdentifiersTable {

    /** Map of all identifiers: e.g.: variable names, function names, types names, ... */
    Map<String, TypeDescriptor> identifiersMap;

    /** Map of type identifiers: e.g.: integer, boolean, enums, records, ... */
    Map<String, TypeDescriptor> typeDescriptors;

    private FrameDescriptor frameDescriptor;

    public IdentifiersTable() {
        this.initialize();
        addBuiltinTypes();
        addBuiltinConstants();
        addBuiltinFunctions();
    }

    private void initialize() {
        this.identifiersMap = new HashMap<>();
        this.typeDescriptors = new HashMap<>();
        this.frameDescriptor = new FrameDescriptor();
    }

    protected void addBuiltinTypes() {
        typeDescriptors.put("integer", new LongDescriptor());
        typeDescriptors.put("shortint", new LongDescriptor());
        typeDescriptors.put("longint", new LongDescriptor());
        typeDescriptors.put("byte", new LongDescriptor());
        typeDescriptors.put("word", new LongDescriptor());
        typeDescriptors.put("single", new RealDescriptor());
        typeDescriptors.put("real", new RealDescriptor());
        typeDescriptors.put("double", new RealDescriptor());
        typeDescriptors.put("boolean", new BooleanDescriptor());
        typeDescriptors.put("char", new CharDescriptor());

        for (Map.Entry<String, TypeDescriptor> typeEntry : typeDescriptors.entrySet()) {
            identifiersMap.put(typeEntry.getKey(), new TypeTypeDescriptor(typeEntry.getValue()));
        }
    }

    private void addBuiltinConstants() {
        try {
            this.registerNewIdentifier("nil", new NilPointerDescriptor());
        } catch (LexicalException e) {
            // TODO: could not initialize exception
        }
    }

    protected void addBuiltinFunctions() {
        identifiersMap.put("write", new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin());
        identifiersMap.put("read", new BuiltinProcedureDescriptor.FullReferenceParameterBuiltin());
        identifiersMap.put("writeln", new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin());
        identifiersMap.put("readln", new BuiltinProcedureDescriptor.FullReferenceParameterBuiltin());
        identifiersMap.put("put", new BuiltinProcedureDescriptor.NotSupportedSubroutine());
        identifiersMap.put("succ", new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin());
        identifiersMap.put("pred", new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin());
        identifiersMap.put("abs", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("sqr", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("sin", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("cos", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("exp", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("ln", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("sqrt", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("arctan", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("trunc", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("round", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("new", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("dispose", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("rewrite", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("reset", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("eof", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("chr", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
        identifiersMap.put("ord", new BuiltinProcedureDescriptor.OneArgumentBuiltin());
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

    public TypeDescriptor getIdentifierDescriptor(String identifier) {
        return this.identifiersMap.get(identifier);
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

    public void verifyPassedArgumentsToSubroutine(String identifier, List<ExpressionNode> params) throws LexicalException {
        SubroutineDescriptor subroutineDescriptor = (SubroutineDescriptor) this.identifiersMap.get(identifier);
        subroutineDescriptor.verifyArguments(params);
    }

    public void addLabel(String identifier) throws LexicalException {
        this.registerNewIdentifier(identifier, new LabelDescriptor(identifier));
    }

    public void addType(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        // TODO: this is a duplicit -> call registerNewIdentifier()
        if (this.identifiersMap.containsKey(identifier)) {
            throw new DuplicitIdentifierException(identifier);
        } else {
            this.typeDescriptors.put(identifier, typeDescriptor);
            this.identifiersMap.put(identifier, new TypeTypeDescriptor(typeDescriptor));
        }
    }

    public void initializeAllUninitializedPointerDescriptors() throws LexicalException {
        for (Map.Entry<String, TypeDescriptor> typeEntry : this.typeDescriptors.entrySet()) {
            TypeDescriptor type = typeEntry.getValue();
            if (type instanceof PointerDescriptor) {
                PointerDescriptor pointerDescriptor = (PointerDescriptor) type;
                if (!pointerDescriptor.isInnerTypeInitialized()) {
                    TypeDescriptor pointerInnerType = this.getTypeDescriptor(pointerDescriptor.getInnerTypeIdentifier());
                    if (pointerInnerType == null) {
                        throw new LexicalException("Pointer type not declared in the same type statement.");
                    } else {
                        pointerDescriptor.setInnerType(pointerInnerType);
                    }
                }
            }
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
            this.registerNewIdentifier(identifier, new EnumLiteralDescriptor(enumTypeDescriptor, identifier));
        }

        return enumTypeDescriptor;
    }

    public FileDescriptor createFileDescriptor(TypeDescriptor contentTypeDescriptor) {
        return new FileDescriptor(contentTypeDescriptor);
    }

    public RecordDescriptor createRecordDescriptor(LexicalScope recordScope) {
        return new RecordDescriptor(recordScope);
    }

    public PointerDescriptor createPointerDescriptor(String innerTypeIdentifier) {
        TypeDescriptor innerTypeDescriptor = this.getTypeDescriptor(innerTypeIdentifier);
        return (innerTypeDescriptor == null)? new PointerDescriptor(innerTypeIdentifier) : new PointerDescriptor(innerTypeDescriptor);
    }

    public TypeDescriptor createArray(List<OrdinalDescriptor> ordinalDimensions, TypeDescriptor typeDescriptor) {
        return new ArrayDescriptor(ordinalDimensions, typeDescriptor);
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

    public void addSubroutine(String identifier, SubroutineDescriptor descriptor) throws LexicalException {
        this.registerNewIdentifier(identifier, descriptor);
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
