package cz.cuni.mff.d3s.trupple.language.parser;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customtypes.ICustomType;
import cz.cuni.mff.d3s.trupple.language.nodes.InitializationNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.LexicalException;
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
        return new OrdinalDescriptor.RangeDescriptor(lowerBound, upperBound);
    }

    // TODO: make enums, integer, long, arrays,... ordinal
    OrdinalDescriptor createRangeDescriptorFromTypename(String typeName) throws LexicalException {
        TypeDescriptor typeDescriptor = this.localIdentifiers.getTypeDescriptor(typeName);
        if (typeDescriptor == null) {
            throw new LexicalException("Unknown type: " + typeName + ".");
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

    List<StatementNode> createInitializationNodes() {
        List<StatementNode> initializationNodes = new ArrayList<>();

        for (Map.Entry<String, TypeDescriptor> entry : this.localIdentifiers.getAll().entrySet()) {
            String identifier = entry.getKey();
            TypeDescriptor typeDescriptor = entry.getValue();

            StatementNode initializationNode = createInitializationNode(identifier, typeDescriptor);
            if (initializationNode != null)
                initializationNodes.add(initializationNode);
            // TODO: inform variable not initialized
        }

        return initializationNodes;
    }

    private StatementNode createInitializationNode(String identifier, TypeDescriptor typeDescriptor) {
        FrameSlot frameSlot = this.localIdentifiers.getFrameSlot(identifier);
        FrameSlotKind slotKind = typeDescriptor.getSlotKind();

        // TODO: separate this into multiple methods
        if (typeDescriptor instanceof PrimitiveDescriptor) {
            switch (slotKind) {
                case Boolean: return InitializationNodeFactory.create(frameSlot, false);
                case Long: return InitializationNodeFactory.create(frameSlot, 0);
                case Double: return InitializationNodeFactory.create(frameSlot, 0.0d);
                case Byte: return InitializationNodeFactory.create(frameSlot, '0');
            }
        } else if (typeDescriptor instanceof ConstantDescriptor) {
            // TODO: add boolean constant
            if (typeDescriptor instanceof LongConstantDescriptor)
                return InitializationNodeFactory.create(frameSlot, ((LongConstantDescriptor)typeDescriptor).getValue());
            else if (typeDescriptor instanceof RealConstantDescriptor)
                return InitializationNodeFactory.create(frameSlot, ((RealConstantDescriptor)typeDescriptor).getValue());
            else if (typeDescriptor instanceof CharConstantDescriptor)
                return InitializationNodeFactory.create(frameSlot, ((CharConstantDescriptor)typeDescriptor).getValue());
            else if (typeDescriptor instanceof StringConstantDescriptor)
                return InitializationNodeFactory.create(frameSlot, ((StringConstantDescriptor)typeDescriptor).getValue());
            else if (typeDescriptor instanceof BooleanConstantDescriptor)
                return InitializationNodeFactory.create(frameSlot, ((BooleanConstantDescriptor)typeDescriptor).getValue());
        }

        return null;
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