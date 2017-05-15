package cz.cuni.mff.d3s.trupple.parser.identifierstable;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import cz.cuni.mff.d3s.trupple.language.nodes.root.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.parser.LexicalScope;
import cz.cuni.mff.d3s.trupple.parser.exceptions.DuplicitIdentifierException;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.UnknownIdentifierException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeTypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.*;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.*;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.ConstantDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.label.LabelDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.*;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdentifiersTable {

    /** Map of all identifiers: e.g.: variable names, function names, types names, ... */
    private Map<String, TypeDescriptor> identifiersMap;

    /** Map of type identifiers: e.g.: integer, boolean, enums, records, ... */
    Map<String, TypeDescriptor> typeDescriptors;

    private FrameDescriptor frameDescriptor;

    public IdentifiersTable() {
        this.initialize();
    }

    public void addBuiltins() {
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
        typeDescriptors.put("integer", LongDescriptor.getInstance());
        typeDescriptors.put("shortint", LongDescriptor.getInstance());
        typeDescriptors.put("longint", LongDescriptor.getInstance());
        typeDescriptors.put("int64", LongDescriptor.getInstance());
        typeDescriptors.put("byte", LongDescriptor.getInstance());
        typeDescriptors.put("word", LongDescriptor.getInstance());
        typeDescriptors.put("single", RealDescriptor.getInstance());
        typeDescriptors.put("real", RealDescriptor.getInstance());
        typeDescriptors.put("double", RealDescriptor.getInstance());
        typeDescriptors.put("boolean", BooleanDescriptor.getInstance());
        typeDescriptors.put("char", CharDescriptor.getInstance());

        for (Map.Entry<String, TypeDescriptor> typeEntry : typeDescriptors.entrySet()) {
            identifiersMap.put(typeEntry.getKey(), new TypeTypeDescriptor(typeEntry.getValue()));
        }
    }

    private void addBuiltinConstants() {
        try {
            this.registerNewIdentifier("nil", new NilPointerDescriptor());
        } catch (LexicalException e) {
            throw new PascalRuntimeException("Could not initialize builtin constants");
        }
    }

    protected void addBuiltinFunctions() {
        try {
            this.registerNewIdentifier("write", new WriteSubroutineDescriptor());
            this.registerNewIdentifier("writeln", new WritelnSubroutineDescriptor());
            this.registerNewIdentifier("read", new ReadSubroutineDescriptor());
            this.registerNewIdentifier("readln", new ReadlnSubroutineDescriptor());
            this.registerNewIdentifier("abs", new AbsSubroutineDescriptor());
            this.registerNewIdentifier("sqr", new SqrSubroutineDescriptor());
            this.registerNewIdentifier("sin", new SinSubroutineDescriptor());
            this.registerNewIdentifier("cos", new CosSubroutineDescriptor());
            this.registerNewIdentifier("exp", new ExpSubroutineDescriptor());
            this.registerNewIdentifier("sqrt", new SqrtSubroutineDescriptor());
            this.registerNewIdentifier("ln", new LnSubroutineDescriptor());
            this.registerNewIdentifier("arctan", new ArctanSubroutineDescriptor());
            this.registerNewIdentifier("trunc", new TruncSubroutineDescriptor());
            this.registerNewIdentifier("round", new RoundSubroutineDescriptor());
            this.registerNewIdentifier("new", new NewSubroutineDescriptor());
            this.registerNewIdentifier("dispose", new DisposeSubroutineDescriptor());
            this.registerNewIdentifier("succ", new SuccSubroutineDescriptor());
            this.registerNewIdentifier("pred", new PredSubroutineDescriptor());
            this.registerNewIdentifier("rewrite", new RewriteSubroutineDescriptor());
            this.registerNewIdentifier("reset", new ResetSubroutineDescriptor());
            this.registerNewIdentifier("chr", new ChrSubroutineDescriptor());
            this.registerNewIdentifier("ord", new OrdSubroutineDescriptor());
            this.registerNewIdentifier("eof", new EofSubroutineDescriptor());
            this.registerNewIdentifier("eol", new EolSubroutineDescriptor());
            this.registerNewIdentifier("odd", new OddSubroutineDescriptor());
            this.registerNewIdentifier("put", new BuiltinProcedureDescriptor.NotSupportedSubroutine());
        } catch (LexicalException e) {
            // TODO: inform
        }
    }

    public FrameSlot getFrameSlot(String identifier) {
        return this.frameDescriptor.findFrameSlot(identifier);
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

    public Map<String, TypeDescriptor> getAllIdentifiers() {
        return this.identifiersMap;
    }

    public PascalSubroutine getSubroutine(String identifier) throws LexicalException {
        TypeDescriptor subroutineDescriptor = this.identifiersMap.get(identifier);
        if (subroutineDescriptor == null) {
            throw new UnknownIdentifierException(identifier);
        } else if (!(subroutineDescriptor instanceof SubroutineDescriptor)) {
            throw new LexicalException("Not a subroutine: " + identifier);
        } else {
            return ((SubroutineDescriptor) subroutineDescriptor).getSubroutine();
        }
    }

    public void setSubroutineRootNode(String identifier, PascalRootNode rootNode) throws LexicalException {
        TypeDescriptor descriptor = this.identifiersMap.get(identifier);
        if (descriptor == null) {
            throw new UnknownIdentifierException(identifier);
        }
        ((SubroutineDescriptor)descriptor).setRootNode(rootNode);
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

    public boolean isLabel(String identifier) {
        return this.identifiersMap.get(identifier) instanceof LabelDescriptor;
    }

    public void addLabel(String identifier) throws LexicalException {
        this.registerNewIdentifier(identifier, new LabelDescriptor(identifier));
    }

    public void addType(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        this.registerNewIdentifier(identifier, new TypeTypeDescriptor(typeDescriptor));
        this.typeDescriptors.put(identifier, typeDescriptor);
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

    public void addVariable(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        this.registerNewIdentifier(identifier, typeDescriptor);
    }

    public void addReturnVariable(String identifier, TypeDescriptor returnTypeDescriptor) throws LexicalException {
        this.registerNewIdentifier(identifier, new ReturnTypeDescriptor(returnTypeDescriptor));
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

    public void addProcedureInterfaceIfNotForwarded(String identifier, List<FormalParameter> formalParameters) throws LexicalException {
        TypeDescriptor descriptor = this.identifiersMap.get(identifier);
        if (descriptor != null) {
            if (!(descriptor instanceof ProcedureDescriptor)) {
                throw new LexicalException("Not a subroutine");
            } else {
                if (!SubroutineDescriptor.compareFormalParametersExact(((SubroutineDescriptor) descriptor).getFormalParameters(), formalParameters)) {
                    throw new LexicalException("Argument types mismatch");
                }
            }
            return;
        }

        this.forwardProcedure(identifier, formalParameters);
    }

    public void addFunctionInterfaceIfNotForwarded(String identifier, List<FormalParameter> formalParameters, TypeDescriptor returnType) throws LexicalException {
        TypeDescriptor descriptor = this.identifiersMap.get(identifier);
        if (descriptor != null) {
            if (!(descriptor instanceof FunctionDescriptor)) {
                throw new LexicalException("Not a subroutine");
            } else {
                if (!SubroutineDescriptor.compareFormalParametersExact(((SubroutineDescriptor) descriptor).getFormalParameters(), formalParameters)) {
                    throw new LexicalException("Argument types mismatch");
                }
            }
            return;
        }
        this.forwardFunction(identifier, formalParameters, returnType);
    }

    public void forwardProcedure(String identifier, List<FormalParameter> formalParameters) throws LexicalException {
        TypeDescriptor typeDescriptor = new ProcedureDescriptor(formalParameters);
        this.registerNewIdentifier(identifier, typeDescriptor);
    }

    public void forwardFunction(String identifier, List<FormalParameter> formalParameters, TypeDescriptor returnTypeDescriptor) throws LexicalException {
        TypeDescriptor typeDescriptor = new FunctionDescriptor(formalParameters, returnTypeDescriptor);
        this.registerNewIdentifier(identifier, typeDescriptor);
    }

    public void addSubroutine(String identifier, SubroutineDescriptor descriptor) throws LexicalException {
        this.registerNewIdentifier(identifier, descriptor);
    }

    public ConstantDescriptor getConstant(String identifier) throws LexicalException {
        TypeDescriptor descriptor = this.identifiersMap.get(identifier);
        if (descriptor == null) {
            throw new UnknownIdentifierException(identifier);
        } else if (! (descriptor instanceof ConstantDescriptor)) {
            throw new LexicalException("Not a constant: " + identifier);
        } else {
            return (ConstantDescriptor)descriptor;
        }
    }

    FrameSlot registerNewIdentifier(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        if (this.identifiersMap.containsKey(identifier)){
            throw new DuplicitIdentifierException(identifier);
        } else {
            this.identifiersMap.put(identifier, typeDescriptor);
            return this.frameDescriptor.addFrameSlot(identifier, typeDescriptor.getSlotKind());
        }
    }

}
