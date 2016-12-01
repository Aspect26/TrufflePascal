package cz.cuni.mff.d3s.trupple.language.parser;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customtypes.EnumType;
import cz.cuni.mff.d3s.trupple.language.customtypes.ICustomType;
import cz.cuni.mff.d3s.trupple.language.customtypes.IOrdinalType;
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.InitializationNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.nodes.function.ReadSubroutineArgumentNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNodeGen;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Notes:
 * constants must be separated from the localIdentifiers because local identifiers only store descriptors of their types while
 * constants need to hold their value through the process of compiling the source code (e.g.:so they can be used in case statements
 */

class LexicalScope {

    class LexicalException extends Exception {

        private String message;

        public LexicalException(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return this.message;
        }
    }

    private final String name;
    private final LexicalScope outer;
    private final PascalContext context;
    private final Map<String, FrameSlot> localIdentifiers;
    private final Map<String, Object> localConstants;
    private final Map<String, ICustomType> customTypes;
    private final List<StatementNode> initializationNodes;

    private FrameDescriptor frameDescriptor;
    private FrameSlot returnSlot;

    List<StatementNode> scopeNodes = new ArrayList<>();

    LexicalScope(LexicalScope outer, String name) {
        this.name = name;
        this.outer = outer;
        this.returnSlot = null;
        this.localIdentifiers = new HashMap<>();
        this.initializationNodes = new ArrayList<>();
        this.localConstants = new HashMap<>();
        this.customTypes = new HashMap<>();

        this.frameDescriptor = (outer != null)? new FrameDescriptor(this.outer.frameDescriptor.getDefaultValue()) : new FrameDescriptor();
        this.context = (outer != null)? new PascalContext(outer.context) : new PascalContext(null);
    }

    String getName() {
        return this.name;
    }

    LexicalScope getOuterScope() {
        return this.outer;
    }

    Map<String, ICustomType> getAllCustomTypes() {
        return this.customTypes;
    }

    PascalContext getContext() {
        return this.context;
    }

    FrameSlot getLocalSlot(String identifier) {
        return this.localIdentifiers.get(identifier);
    }

    Object getLocalConstant(String identifier) {
        return this.localConstants.get(identifier);
    }

    List<StatementNode> getAllInitializationNoes() {
        return this.initializationNodes;
    }

    FrameDescriptor getFrameDescriptor() {
        return this.frameDescriptor;
    }

    FrameSlot getReturnSlot() {
        return this.returnSlot;
    }

    FrameSlot getVisibleSlot(String identifier) {
        FrameSlot slot = null;
        LexicalScope scope = this;
        while(scope != null && slot == null){
            slot = scope.getLocalSlot(identifier);
            scope = scope.getOuterScope();
        }

        return slot;
    }

    EnumType getVisibleEnumType(String identifier) {
        LexicalScope ls = this;
        while(ls != null) {
            ICustomType customType = customTypes.get(identifier);
            if(customType != null && customType instanceof EnumType)
                return (EnumType) customType;

            ls = ls.outer;
        }

        return null;
    }

    void registerCustomType(String name, ICustomType customType) {
        this.customTypes.put(name, customType);
    }

    FrameSlot registerLocalIdentifier(String identifier, String typeName) throws LexicalException {
        try {
            FrameSlotKind slotKind = this.getSlotByTypeName(typeName);
            FrameSlot newSlot = this.frameDescriptor.addFrameSlot(identifier, slotKind);
            this.localIdentifiers.put(identifier, newSlot);

            if (slotKind == FrameSlotKind.Illegal) {
                throw new LexicalException("Unkown variable type: " + typeName);
            }

            ICustomType customType = tryGetCustomType(typeName);
            if (customType != null) {
                if (customType instanceof EnumType) {
                    this.addInitializationNode(InitializationNodeFactory.create(newSlot, new EnumValue((EnumType)customType)));
                }
            }

            return newSlot;
        } catch (IllegalArgumentException e) {
            throw new LexicalException("Duplicate variable: " + identifier + ".");
        }
    }

    FrameSlot registerLocalConstant(String identifier, Object value) throws IllegalArgumentException {
        try {
            FrameSlot newSlot = frameDescriptor.addFrameSlot(identifier);
            this.localIdentifiers.put(identifier, newSlot);
            this.localConstants.put(identifier, value);
            return newSlot;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Duplicate variable: " + identifier + ".");
        }
    }

    void registerArrayVariable(String identifier, List<IOrdinalType> ordinalDimensions, String returnTypeName) throws LexicalException {
        try {
            FrameSlot newSlot = this.frameDescriptor.addFrameSlot(identifier, FrameSlotKind.Object);
            this.localIdentifiers.put(identifier, newSlot);
            PascalArray array = createMultidimensionalArray(ordinalDimensions, returnTypeName);
            this.addInitializationNode(InitializationNodeFactory.create(newSlot, array));
        } catch (IllegalArgumentException e) {
            throw new LexicalException("Duplicate variable: " + identifier + ".");
        }
    }

    void registerEnumType(String identifier, List<String> identifiers, boolean global) throws LexicalException {
        if(customTypes.containsKey(identifier))
            throw new LexicalException("Duplicate custom type: " + identifier + ".");

        EnumType enumType = new EnumType(identifier, identifiers, global);
        customTypes.put(identifier, enumType);
        localIdentifiers.put(identifier, frameDescriptor.addFrameSlot(identifier));

        for(String elementIdentifier : identifiers){
            if(localIdentifiers.containsKey(elementIdentifier)) {
                throw new LexicalException("Duplicate identifier: " + elementIdentifier + ".");
            }
            FrameSlot slot = this.frameDescriptor.addFrameSlot(elementIdentifier, FrameSlotKind.Object);
            this.initializationNodes.add(InitializationNodeFactory.create(slot,
                    new EnumValue(enumType, enumType.getFirstIndex())));

            localIdentifiers.put(elementIdentifier, slot);
        }
    }

    void registerFormalParameter(FormalParameter parameter) {
        FrameSlotKind slotKind = this.getSlotByTypeName(parameter.type);
        final ExpressionNode readNode = ReadSubroutineArgumentNodeGen.create(this.scopeNodes.size(), slotKind);

        FrameSlot newSlot = this.frameDescriptor.addFrameSlot(parameter.identifier, slotKind);
        this.localIdentifiers.put(parameter.identifier, newSlot);

        final AssignmentNode assignment = AssignmentNodeGen.create(readNode, newSlot);
        this.scopeNodes.add(assignment);
    }

    void addInitializationNode(StatementNode initializationNode) {
        this.initializationNodes.add(initializationNode);
    }

    boolean containsLocalIdentifier(String identifier) {
        return this.localIdentifiers.containsKey(identifier);
    }

    boolean containsLocalConstant(String identifier) {
        return this.localConstants.containsKey(identifier);
    }

    boolean containsCustomType(String typeName){
        return customTypes.containsKey(typeName);
    }

    boolean containsCustomValue(String identifier){
        for(ICustomType custom : customTypes.values()){
            if(custom.containsCustomValue(identifier))
                return true;
        }

        return false;
    }

    void startSubroutineImplementation(String identifier) throws LexicalException {
        if(this.context.containsIdentifier(identifier) &&
                this.context.getGlobalFunctionRegistry().lookup(identifier) == null &&
                this.context.getPrivateFunctionRegistry().lookup(identifier) == null) {
            throw new LexicalException("Duplicate identifier: " + identifier);
        }

        else if (this.context.getGlobalFunctionRegistry().lookup(identifier) != null &&
                this.context.getGlobalFunctionRegistry().lookup(identifier).isImplemented()) {
            throw new LexicalException("Subroutine is already implemented as public: " + identifier);
        }

        else if (this.context.getPrivateFunctionRegistry().lookup(identifier) != null &&
                this.context.getPrivateFunctionRegistry().lookup(identifier).isImplemented()){
            throw new LexicalException("Subroutine is already implemented as private: " + identifier);
        }
    }

    void setReturnSlot(String typeName) {
        this.returnSlot = this.frameDescriptor.addFrameSlot(this.name, getSlotByTypeName(typeName));
        this.localIdentifiers.put(this.name, this.returnSlot);
    }

    private PascalArray createMultidimensionalArray(List<IOrdinalType> ordinalDimensions, String componentType) {
        assert ordinalDimensions.size() > 0;

        if(ordinalDimensions.size() == 1) {
            return new PascalArray(componentType, ordinalDimensions.get(0));
        }

        else {
            int count = ordinalDimensions.get(0).getSize();
            List<IOrdinalType> innerDimensions = ordinalDimensions.subList(1, ordinalDimensions.size());
            PascalArray[] innerArrays = new PascalArray[count];
            for(int i = 0; i < count; i++) {
                innerArrays[i] = createMultidimensionalArray(innerDimensions, componentType);
            }
            return new PascalArray(innerArrays, ordinalDimensions.get(0));
        }
    }

    private ICustomType tryGetCustomType(String typeName) {
        LexicalScope scope = this;
        while(scope != null) {
            ICustomType customType = customTypes.get(typeName);
            if(customType != null)
                return customType;

            scope = scope.getOuterScope();
        }

        return null;
    }

    private FrameSlotKind getSlotByTypeName(String type) {
        if(this.containsCustomType(type))
            return FrameSlotKind.Object;

        switch (type) {

            // ordinals
            case "integer":
            case "shortint":
            case "longint":
            case "byte":
            case "word":
                return FrameSlotKind.Long;

            // floating points
            case "single":
            case "real":
            case "double":
                return FrameSlotKind.Double;

            // logical
            case "boolean":
                return FrameSlotKind.Boolean;

            // char
            case "char":
                return FrameSlotKind.Byte;

            default:
                return FrameSlotKind.Illegal;
        }
    }
}