package cz.cuni.mff.d3s.trupple.language.parser;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import cz.cuni.mff.d3s.trupple.language.customtypes.ICustomType;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.DuplicitIdentifierException;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.IdentifiersTable;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.*;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LexicalScope {


    private final String name;
    private final LexicalScope outer;
    private final IdentifiersTable localIdentifiers;
    private int loopDepth;
    private final PascalContext context;
    private final List<StatementNode> readArgumentNodes = new ArrayList<>();

    private final Map<String, ICustomType> customTypes;

    LexicalScope(LexicalScope outer, String name) {
        this.name = name;
        this.outer = outer;
        this.customTypes = new HashMap<>();
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

    FrameSlot getReturnSlot() {
        return this.localIdentifiers.getFrameSlot(this.name);
    }

    TypeDescriptor getTypeTypeDescriptor(String identifier) throws LexicalException {
        return this.localIdentifiers.getTypeTypeDescriptor(identifier);
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

    boolean isParameterlessSubroutine(String identifier) {
        return this.localIdentifiers.isParameterlessSubroutine(identifier);
    }

    boolean isSubroutine(String identifier) {
        return this.localIdentifiers.isSubroutine(identifier);
    }

    boolean containsLocalIdentifier(String identifier) {
        return this.localIdentifiers.containsIdentifier(identifier);
    }

    FrameSlot registerLocalVariable(String identifier, String typeName) throws LexicalException {
        return this.localIdentifiers.addVariable(identifier, typeName);
    }

    FrameSlot registerReferenceVariable(String identifier, String typeName) throws LexicalException {
        return this.localIdentifiers.addReference(identifier, typeName);
    }

    void registerReturnType(List<FormalParameter> formalParameters, String typeName) throws LexicalException {
        this.localIdentifiers.addReturnVariable(this.getName(), formalParameters, typeName);
    }

    FrameSlot registerLocalVariable(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        return this.localIdentifiers.addVariable(typeDescriptor, identifier);
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

    void forwardProcedureInterface(String identifier, List<FormalParameter> formalParameters) throws LexicalException {
        this.localIdentifiers.addProcedureInterface(identifier, formalParameters);
        this.context.registerSubroutineName(identifier, true);
    }

    void forwardFunctionInterface(String identifier, List<FormalParameter> formalParameters, String returnType) throws LexicalException {
        this.localIdentifiers.addFunctionInterface(identifier, formalParameters, returnType);
        this.context.registerSubroutineName(identifier, true);
    }

    void startProcedureInterface(String identifier, List<FormalParameter> formalParameters) throws LexicalException {
        try {
            this.localIdentifiers.addProcedureInterface(identifier, formalParameters);
        } catch (DuplicitIdentifierException e) {
            if (this.context.isImplemented(identifier)) {
                throw e;
            }
        }
        this.context.registerSubroutineName(identifier, true);
    }

    void startFunctionInterface(String identifier, List<FormalParameter> formalParameters, String returnType) throws LexicalException {
        try {
            this.localIdentifiers.addFunctionInterface(identifier, formalParameters, returnType);
        } catch (DuplicitIdentifierException e) {
            if (this.context.isImplemented(identifier)) {
                throw e;
            }
        }
        this.context.registerSubroutineName(identifier, true);
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

    //
    // THIS SHOULD BE REMOVED

    Map<String, ICustomType> getAllCustomTypes() {
        return this.customTypes;
    }

    void registerCustomType(String name, ICustomType customType) {
        this.customTypes.put(name, customType);
    }
}