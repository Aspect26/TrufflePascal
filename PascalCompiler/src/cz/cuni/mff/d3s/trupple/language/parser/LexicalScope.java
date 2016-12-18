package cz.cuni.mff.d3s.trupple.language.parser;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customtypes.ICustomType;
import cz.cuni.mff.d3s.trupple.language.nodes.InitializationNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.UnknownTypeException;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.IdentifiersTable;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.*;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import org.junit.runners.model.Statement;

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

    private final Map<String, ICustomType> customTypes;

    List<StatementNode> scopeNodes = new ArrayList<>();

    LexicalScope(LexicalScope outer, String name, String returnType) {
        this.name = name;
        this.outer = outer;
        this.customTypes = new HashMap<>();

        this.localIdentifiers = new IdentifiersTable();
        this.context = (outer != null)? new PascalContext(outer.context) : new PascalContext(null);

        if (returnType != null) {
            try {
                this.registerLocalVariable(name, returnType);
            } catch (LexicalException e) {
                // TODO: what to do here?
            }
        }
    }

    LexicalScope(LexicalScope outer, String name) {
        this(outer, name, null);
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

    boolean isVariable(String identifier) {
        return this.localIdentifiers.isVariable(identifier);
    }

    boolean isConstant(String identifier) {
        return this.localIdentifiers.getTypeDescriptor(identifier) instanceof ConstantDescriptor;
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

    void registerLocalVariable(String identifier, String typeName) throws LexicalException {
        this.localIdentifiers.addVariable(typeName, identifier);
    }

    void registerLocalArrayVariable(String identifier, List<OrdinalDescriptor> ordinalDimensions, String returnTypeName) throws LexicalException {
        this.localIdentifiers.addArrayVariable(identifier, ordinalDimensions, returnTypeName);
    }

    void registerEnumType(String identifier, List<String> identifiers) throws LexicalException {
        this.localIdentifiers.addEnumType(identifier, identifiers);
    }

    void registerProcedureInterface(String identifier, List<FormalParameter> formalParameters) throws LexicalException {
        this.localIdentifiers.addProcedureInterface(identifier, formalParameters);
        this.context.registerSubroutineName(identifier, true);
    }

    void registerFunctionInterface(String identifier, List<FormalParameter> formalParameters, String returnType) throws LexicalException {
        this.localIdentifiers.addFunctionInterface(identifier, formalParameters, returnType);
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

    // TODO: make enums, integer, long, arrays,... ordinal
    OrdinalDescriptor createRangeDescriptorFromTypename(String typeName) throws LexicalException {
        TypeDescriptor typeDescriptor = this.localIdentifiers.getTypeDescriptor(typeName);
        if (typeDescriptor == null) {
            throw new UnknownTypeException(typeName);
        }
        else if (!(typeDescriptor instanceof OrdinalDescriptor)) {
            throw new LexicalException("The type is not ordinal: " + typeName + ".");
        }
        else {
            return (OrdinalDescriptor)typeDescriptor;
        }
    }

    OrdinalDescriptor createImplicitRangeDescriptor() {
        return new OrdinalDescriptor.RangeDescriptor(0, 1);
    }

    List<StatementNode> createInitializationNodes() throws LexicalException {
        InitializationNodeGenerator initNodeGenerator = new InitializationNodeGenerator(this.localIdentifiers);
        return initNodeGenerator.generate();
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


    // THE FRAME DESCRIPTOR WILL BE CREATED AFTER THE PARSING IS FINISHED
    // INITIALIZATION NODES:
    //  PRIMITIVES:
    //   new ...
    //  ARRAY:
    //   PascalArray array = createMultidimensionalArray(ordinalDimensions, returnTypeName);
    //   this.addInitializationNode(InitializationNodeFactory.create(newSlot, array));
    //  CONSTANTS:
    //   from their values in descriptors
    // ----------------------------

    Map<String, ICustomType> getAllCustomTypes() {
        return this.customTypes;
    }

    void registerCustomType(String name, ICustomType customType) {
        this.customTypes.put(name, customType);
    }
}