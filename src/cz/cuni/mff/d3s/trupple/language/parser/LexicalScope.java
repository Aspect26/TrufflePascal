package cz.cuni.mff.d3s.trupple.language.parser;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.DuplicitIdentifierException;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.IdentifiersTable;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.*;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

import java.util.*;

class LexicalScope {


    private String name;
    private final LexicalScope outer;
    private final IdentifiersTable localIdentifiers;
    private int loopDepth;
    private final PascalContext context;
    private final Set<String> publicIdentifiers;

    // TODO: wtf is this?
    private final List<StatementNode> readArgumentNodes = new ArrayList<>();

    LexicalScope(LexicalScope outer, String name) {
        this.name = name;
        this.outer = outer;
        this.publicIdentifiers = new HashSet<>();
        this.localIdentifiers = new IdentifiersTable();
        this.context = (outer != null)? new PascalContext(outer.context) : new PascalContext(null);
    }

    String getName() {
        return this.name;
    }

    LexicalScope getOuterScope() {
        return this.outer;
    }

    PascalContext getContext() {
        return this.context;
    }

    FrameDescriptor getFrameDescriptor() {
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

    TypeDescriptor getTypeDescriptor(String identifier) {
        return this.localIdentifiers.getTypeDescriptor(identifier);
    }

    TypeDescriptor getTypeTypeDescriptor(String typeIdentifier) {
        return this.localIdentifiers.getTypeTypeDescriptor(typeIdentifier);
    }

    public void setName(String identifier) {
        this.name = identifier;
    }

    void registerNewType(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        this.localIdentifiers.addType(identifier, typeDescriptor);
    }

    boolean isVariable(String identifier) {
        return this.localIdentifiers.isVariable(identifier);
    }

    boolean isConstant(String identifier) {
        return this.localIdentifiers.isConstant(identifier);
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

    boolean isIdentifierPublic(String identifier) {
        return this.publicIdentifiers.contains(identifier);
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

    TypeDescriptor createArrayType(List<OrdinalDescriptor> ordinalDimensions, String returnTypeName) throws LexicalException {
        return this.localIdentifiers.createArray(ordinalDimensions, returnTypeName);
    }

    TypeDescriptor createEnumType(List<String> identifiers) throws LexicalException {
        return this.localIdentifiers.createEnum(identifiers);
    }

    TypeDescriptor createSetType(OrdinalDescriptor baseType) {
        return this.localIdentifiers.createSetType(baseType);
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
        this.context.registerSubroutineName(identifier, true);
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
        this.context.registerSubroutineName(identifier, true);
    }

    void registerProcedureInterface(String identifier, List<FormalParameter> formalParameters) throws LexicalException {
        this.registerProcedureInterface(identifier, formalParameters, true);
    }

    void registerProcedureInterface(String identifier, List<FormalParameter> formalParameters, boolean isPublic) throws LexicalException {
        this.localIdentifiers.addProcedureInterface(identifier, formalParameters);
        this.context.registerSubroutineName(identifier, isPublic);
    }

    void registerFunctionInterface(String identifier, List<FormalParameter> formalParameters, String returnTypeName) throws LexicalException {
        this.registerFunctionInterface(identifier, formalParameters, returnTypeName, true);
    }

    void registerFunctionInterface(String identifier, List<FormalParameter> formalParameters, String returnTypeName, boolean isPublic) throws LexicalException {
        this.localIdentifiers.addFunctionInterface(identifier, formalParameters, returnTypeName);
        this.context.registerSubroutineName(identifier, isPublic);
    }

    void registerLongConstant(String identifier, long value) throws LexicalException {
        this.localIdentifiers.addLongConstant(identifier, value);
    }

    void registerRealConstant(String identifier, double value) throws LexicalException {
        this.localIdentifiers.addRealConstant(identifier, value);
    }

    void registerBooleanConstant(String identifier, boolean value) throws LexicalException {
        this.localIdentifiers.addBooleanConstant(identifier, value);
    }

    void registerCharConstant(String identifier, char value) throws LexicalException {
        this.localIdentifiers.addCharConstant(identifier, value);
    }

    void registerStringConstant(String identifier, String value) throws LexicalException {
        this.localIdentifiers.addStringConstant(identifier, value);
    }

    void registerConstantFromConstant(String identifier, String valueIdentifier) throws LexicalException {
        this.localIdentifiers.addConstantFromConstant(identifier, valueIdentifier);
    }

    void registerConstantFromNegatedConstant(String identifier, String valueIdentifier) throws LexicalException {
        this.localIdentifiers.addConstantFromNegatedConstant(identifier, valueIdentifier);
    }

    OrdinalDescriptor createRangeDescriptor(int lowerBound, int upperBound)  throws LexicalException {
        if (upperBound < lowerBound) {
            throw new LexicalException("Lower upper bound than lower bound.");
        }
        return new OrdinalDescriptor.RangeDescriptor(lowerBound, upperBound - lowerBound + 1);
    }

    OrdinalDescriptor createImplicitRangeDescriptor() {
        return new OrdinalDescriptor.RangeDescriptor(0, 1);
    }

    List<StatementNode> createInitializationNodes() throws LexicalException {
        InitializationNodeGenerator initNodeGenerator = new InitializationNodeGenerator(this.localIdentifiers);

        List<StatementNode> initializationNodes = initNodeGenerator.generate();
        initializationNodes.addAll(this.readArgumentNodes);

        return initializationNodes;
    }

    void markAllIdentifiersFromUnitPublic(String unitName) {
        Map<String, TypeDescriptor> allIdentifiers = this.localIdentifiers.getAll();
        for (Map.Entry<String, TypeDescriptor> entry : allIdentifiers.entrySet()) {
            String currentIdentifier = entry.getKey();
            if (currentIdentifier.startsWith(unitName + ".")) {
                this.publicIdentifiers.add(currentIdentifier);
            }
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