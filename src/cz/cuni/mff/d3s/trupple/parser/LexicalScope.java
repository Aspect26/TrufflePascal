package cz.cuni.mff.d3s.trupple.parser;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.nodes.BlockNode;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.parser.exceptions.DuplicitIdentifierException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.IdentifiersTable;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.IdentifiersTableTP;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.FileDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.PointerDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.RecordDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.ConstantDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.LongConstantDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.OrdinalConstantDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import java.util.*;

public class LexicalScope {

    private String name;
    private final LexicalScope outer;
    private final IdentifiersTable localIdentifiers;
    private int loopDepth;
    private final PascalContext context;
    private final Set<String> publicIdentifiers;

    // TODO: this should not be here
    private final List<StatementNode> readArgumentNodes = new ArrayList<>();

    LexicalScope(LexicalScope outer, String name, boolean usingTPExtension) {
        this.name = name;
        this.outer = outer;
        this.publicIdentifiers = new HashSet<>();
        this.localIdentifiers = (usingTPExtension)? new IdentifiersTableTP() : new IdentifiersTable();
        this.context = (outer != null)? new PascalContext(outer.context, usingTPExtension) : new PascalContext(null, usingTPExtension);
    }

    String getName() {
        return this.name;
    }

    LexicalScope getOuterScope() {
        return this.outer;
    }

    public PascalContext getContext() {
        return this.context;
    }

    public IdentifiersTable getIdentifiersTable() {
        return this.localIdentifiers;
    }

    public FrameDescriptor getFrameDescriptor() {
        return this.localIdentifiers.getFrameDescriptor();
    }

    FrameSlot getLocalSlot(String identifier) {
        return this.localIdentifiers.getFrameSlot(identifier);
    }

    FrameSlotKind getSlotKind(String identifier) {
        return this.localIdentifiers.getFrameSlotKind(identifier);
    }

    FrameSlot getReturnSlot() {
        return this.localIdentifiers.getFrameSlot(this.name);
    }

    TypeDescriptor getIdentifierDescriptor(String identifier) {
        return this.localIdentifiers.getIdentifierDescriptor(identifier);
    }

    TypeDescriptor getTypeDescriptor(String identifier) {
        return this.localIdentifiers.getTypeDescriptor(identifier);
    }

    TypeDescriptor getTypeTypeDescriptor(String typeIdentifier) {
        return this.localIdentifiers.getTypeTypeDescriptor(typeIdentifier);
    }

    ConstantDescriptor getConstant(String identifier) throws LexicalException {
        return this.localIdentifiers.getConstant(identifier);
    }

    public void setName(String identifier) {
        this.name = identifier;
    }

    void registerNewType(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        this.localIdentifiers.addType(identifier, typeDescriptor);
    }

    boolean isReferenceParameter(String identifier, int parameterIndex) throws LexicalException {
        TypeDescriptor subroutineDescriptor = this.localIdentifiers.getAll().get(identifier);
        if (!(subroutineDescriptor instanceof SubroutineDescriptor)) {
            throw new LexicalException("Not a subroutine: " + identifier);
        }

        SubroutineDescriptor descriptor = (SubroutineDescriptor)subroutineDescriptor;
        return descriptor.isReferenceParameter(parameterIndex);
    }

    boolean isParameterlessSubroutine(String identifier) {
        return this.localIdentifiers.isParameterlessSubroutine(identifier);
    }

    boolean isSubroutine(String identifier) {
        return this.localIdentifiers.isSubroutine(identifier);
    }

    void importUnitScope(LexicalScope unitScope) {

    }

    void verifyPassedArgumentsToSubroutine(String identifier, List<ExpressionNode> params) throws LexicalException {
        this.localIdentifiers.verifyPassedArgumentsToSubroutine(identifier, params);
    }

    boolean containsLocalIdentifier(String identifier) {
        return this.localIdentifiers.containsIdentifier(identifier);
    }

    FrameSlot registerReferenceVariable(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        return this.localIdentifiers.addReference(identifier, typeDescriptor);
    }

    void registerReturnType(List<FormalParameter> formalParameters, String typeName) throws LexicalException {
        this.localIdentifiers.addReturnVariable(this.getName(), formalParameters, typeName);
    }

    FrameSlot registerLocalVariable(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        return this.localIdentifiers.addVariable(identifier, typeDescriptor);
    }

    void addScopeArgument(StatementNode initializationNode) {
        this.readArgumentNodes.add(initializationNode);
    }

    TypeDescriptor createArrayType(List<OrdinalDescriptor> ordinalDimensions, TypeDescriptor typeDescriptor) {
        return this.localIdentifiers.createArray(ordinalDimensions, typeDescriptor);
    }

    TypeDescriptor createEnumType(List<String> identifiers) throws LexicalException {
        return this.localIdentifiers.createEnum(identifiers);
    }

    FileDescriptor createFileDescriptor(TypeDescriptor contentTypeDescriptor) {
        return this.localIdentifiers.createFileDescriptor(contentTypeDescriptor);
    }

    RecordDescriptor createRecordDescriptor() {
        return this.localIdentifiers.createRecordDescriptor(this);
    }

    PointerDescriptor createPointerDescriptor(String innerTypeIdentifier) {
        return this.localIdentifiers.createPointerDescriptor(innerTypeIdentifier);
    }

    TypeDescriptor createSetType(OrdinalDescriptor baseType) {
        return this.localIdentifiers.createSetType(baseType);
    }

    void initializeAllUninitializedPointerDescriptors() throws LexicalException {
        this.localIdentifiers.initializeAllUninitializedPointerDescriptors();
    }

    // NOTE: the procedure could have been forwarded
    void tryRegisterProcedureInterface(String identifier, List<FormalParameter> formalParameters) throws LexicalException {
        try {
            this.localIdentifiers.addProcedureInterface(identifier, formalParameters);
        } catch (DuplicitIdentifierException e) {
            if (this.context.isImplemented(identifier)) {
                throw e;
            }
        }
        this.context.registerSubroutineName(identifier);
    }

    // NOTE: the function could have been forwarded
    void tryRegisterFunctionInterface(String identifier, List<FormalParameter> formalParameters, String returnType) throws LexicalException {
        try {
            this.localIdentifiers.addFunctionInterface(identifier, formalParameters, returnType);
        } catch (DuplicitIdentifierException e) {
            if (this.context.isImplemented(identifier)) {
                throw e;
            }
        }
        this.context.registerSubroutineName(identifier);
    }

    void registerProcedureInterface(String identifier, List<FormalParameter> formalParameters) throws LexicalException {
        this.localIdentifiers.addProcedureInterface(identifier, formalParameters);
        this.context.registerSubroutineName(identifier);
    }

    void registerFunctionInterface(String identifier, List<FormalParameter> formalParameters, String returnTypeName) throws LexicalException {
        this.localIdentifiers.addFunctionInterface(identifier, formalParameters, returnTypeName);
        this.context.registerSubroutineName(identifier);
    }

    public void registerSubroutine(String identifier, ExpressionNode bodyNode, SubroutineDescriptor descriptor) {
        try {
            this.localIdentifiers.addSubroutine(identifier, descriptor);
            final PascalRootNode rootNode = new PascalRootNode(this.getFrameDescriptor(), bodyNode);
            this.context.registerSubroutineName(identifier);
            this.context.setSubroutineRootNode(identifier, rootNode);
        } catch (LexicalException e) {
            // TODO: this is called from BuiltinUnitAbstr only
        }
    }

    void registerConstant(String identifier, ConstantDescriptor constant) throws LexicalException {
        this.localIdentifiers.addConstant(identifier, constant);
    }

    OrdinalDescriptor createRangeDescriptor(OrdinalConstantDescriptor lowerBound, OrdinalConstantDescriptor upperBound)  throws LexicalException {
        if (!lowerBound.getClass().equals(upperBound.getClass())) {
            throw new LexicalException("Range type mismatch");
        }

        long lower = lowerBound.getOrdinalValue();
        long upper = upperBound.getOrdinalValue();

        if (lower > upper) {
            throw new LexicalException("Lower upper bound than lower bound");
        }

        return new OrdinalDescriptor.RangeDescriptor(lowerBound, upperBound);
    }

    OrdinalDescriptor createImplicitRangeDescriptor() {
        return new OrdinalDescriptor.RangeDescriptor(new LongConstantDescriptor(0), new LongConstantDescriptor(1));
    }

    BlockNode createInitializationNode() {
        InitializationNodeGenerator initNodeGenerator = new InitializationNodeGenerator(this.localIdentifiers);

        List<StatementNode> initializationNodes = initNodeGenerator.generate();
        initializationNodes.addAll(this.readArgumentNodes);

        return new BlockNode(initializationNodes.toArray(new StatementNode[initializationNodes.size()]));
    }

    void markAllIdentifiersPublic() {
        Map<String, TypeDescriptor> allIdentifiers = this.localIdentifiers.getAll();
        for (Map.Entry<String, TypeDescriptor> entry : allIdentifiers.entrySet()) {
            String currentIdentifier = entry.getKey();
            this.publicIdentifiers.add(currentIdentifier);
        }
    }

    void increaseLoopDepth() {
        ++loopDepth;
    }

    void decreaseLoopDepth() throws LexicalException {
        if (loopDepth == 0) {
            throw new LexicalException("Cannot leave cycle.");
        } else {
            --loopDepth;
        }
    }

    boolean isInLoop() {
        return loopDepth > 0;
    }
}