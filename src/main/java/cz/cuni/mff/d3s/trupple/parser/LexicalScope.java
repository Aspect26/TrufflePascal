package cz.cuni.mff.d3s.trupple.parser;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.PascalLanguage;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.BlockNode;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.InitializationNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.root.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.IdentifiersTable;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.IdentifiersTableTP;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.FileDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.PointerDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.ArrayDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.EnumTypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.RecordDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.ConstantDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.LongConstantDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.OrdinalConstantDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.ReturnTypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;

import java.util.*;

/**
 * This class represents currently parsed lexical scope. It is a slight wrapper of {@link IdentifiersTable} with some
 * extended functionality. Lexical scope of pascal are scopes of subroutines.
 */
public class LexicalScope {

    private String name;
    private final LexicalScope outer;
    private int loopDepth;
    IdentifiersTable localIdentifiers;
    final List<StatementNode> scopeInitializationNodes = new ArrayList<>();

    /**
     * Default constructor.
     * @param outer instance of outer lexical scope
     * @param name name of the current lexical scope
     * @param usingTPExtension a flag whether support for Turbo Pascal extensions is turned on
     */
    LexicalScope(LexicalScope outer, String name, boolean usingTPExtension) {
        this.name = name;
        this.outer = outer;
        this.localIdentifiers = (usingTPExtension)? new IdentifiersTableTP() : new IdentifiersTable();
        this.localIdentifiers.addBuiltins();
    }

    String getName() {
        return this.name;
    }

    LexicalScope getOuterScope() {
        return this.outer;
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

    PascalSubroutine getSubroutine(String identifier) throws LexicalException {
        return this.localIdentifiers.getSubroutine(identifier);
    }

    SubroutineDescriptor getSubroutineDescriptor(String identifier, List<ExpressionNode> actualArguments) throws LexicalException {
        SubroutineDescriptor subroutineDescriptor = (SubroutineDescriptor) this.getIdentifierDescriptor(identifier);
        return subroutineDescriptor.getOverload(actualArguments);
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

    ConstantDescriptor getConstant(String identifier) throws LexicalException {
        return this.localIdentifiers.getConstant(identifier);
    }

    public void setName(String identifier) {
        this.name = identifier;
    }

    void setSubroutineRootNode(String identifier, PascalRootNode rootNode) throws LexicalException {
        PascalLanguage.INSTANCE.updateSubroutine(this.name, identifier, rootNode);
        this.localIdentifiers.setSubroutineRootNode(identifier, rootNode);
    }

    void registerLabel(String identifier) throws LexicalException {
        this.localIdentifiers.addLabel(identifier);
    }

    void registerNewType(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        this.localIdentifiers.addType(identifier, typeDescriptor);
    }

    /**
     * Checks whether subroutine with the specified identifier in current scope receives reference passed variable as
     * argument at specified index.
     * @param identifier identifier of the subroutine
     * @param parameterIndex index of the subroutine's formal parameter
     * @throws LexicalException if the received identifier does not represent a subroutine or the specified index is
     * invalid for the subroutine
     */
    boolean isReferenceParameter(String identifier, int parameterIndex) throws LexicalException {
        TypeDescriptor subroutineDescriptor = this.localIdentifiers.getIdentifierDescriptor(identifier);
        if (!(subroutineDescriptor instanceof SubroutineDescriptor)) {
            throw new LexicalException("Not a subroutine: " + identifier);
        }

        SubroutineDescriptor descriptor = (SubroutineDescriptor)subroutineDescriptor;
        return descriptor.isReferenceParameter(parameterIndex);
    }

    /**
     * Checks whether subroutine with the specified identifier in current scope receives a subroutine as argument at
     * specified index.
     * @param identifier identifier of the subroutine
     * @param parameterIndex index of the subroutine's formal parameter
     * @throws LexicalException if the received identifier does not represent a subroutine or the specified index is
     * invalid for the subroutine
     */
    boolean isSubroutineParameter(String identifier, int parameterIndex) throws LexicalException {
        TypeDescriptor subroutineDescriptor = this.localIdentifiers.getIdentifierDescriptor(identifier);
        if (!(subroutineDescriptor instanceof SubroutineDescriptor)) {
            throw new LexicalException("Not a subroutine: " + identifier);
        }

        SubroutineDescriptor descriptor = (SubroutineDescriptor)subroutineDescriptor;
        return descriptor.isSubroutineParameter(parameterIndex);
    }

    boolean isParameterlessSubroutine(String identifier) {
        return this.localIdentifiers.isParameterlessSubroutine(identifier);
    }

    boolean isSubroutine(String identifier) {
        return this.localIdentifiers.isSubroutine(identifier);
    }

    boolean labelExists(String identifier) {
        return this.localIdentifiers.isLabel(identifier);
    }

    boolean containsLocalIdentifier(String identifier) {
        return this.localIdentifiers.containsIdentifier(identifier) && !(this.localIdentifiers.getIdentifierDescriptor(identifier) instanceof ReturnTypeDescriptor);
    }

    boolean containsPublicIdentifier(String identifier) {
        return this.containsLocalIdentifier(identifier);
    }

    /**
     * Checks whether current lexical scope contains return variable with the specified identifier.
     */
    boolean containsReturnType(String identifier, boolean onlyPublic) {
        return this.localIdentifiers.containsIdentifier(identifier) &&
                (this.localIdentifiers.getIdentifierDescriptor(identifier) instanceof ReturnTypeDescriptor) &&
                (!onlyPublic || this.containsPublicIdentifier(identifier));
    }

    FrameSlot registerReferenceVariable(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        return this.localIdentifiers.addReference(identifier, typeDescriptor);
    }

    void registerLocalVariable(String identifier, TypeDescriptor typeDescriptor) throws LexicalException {
        this.localIdentifiers.addVariable(identifier, typeDescriptor);
    }

    /**
     * Adds initialization scope for this lexical scope. These nodes are prepended to the main block's tree of the
     * subroutine this scope represents. They are required to initialize values of each local variable of the scope.
     * @param initializationNode the new initialization node
     */
    void addScopeInitializationNode(StatementNode initializationNode) {
        this.scopeInitializationNodes.add(initializationNode);
    }

    void registerReturnVariable(TypeDescriptor typeDescriptor) throws LexicalException {
        this.localIdentifiers.addReturnVariable(this.getName(), typeDescriptor);
    }

    ArrayDescriptor createArrayType(OrdinalDescriptor dimension, TypeDescriptor typeDescriptor) {
        return this.localIdentifiers.createArray(dimension, typeDescriptor);
    }

    EnumTypeDescriptor createEnumType(List<String> identifiers) throws LexicalException {
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

    /**
     * Pascal allows to declare a pointer to a type that is declared after the pointer's declaration. In these cases, we
     * create a pointer type with unspecified inner type but with the identifier of the type to be declared later. After
     * the whole types declaration statement is parsed, this function is called and sets the correct inner type
     * descriptors for each of these pointer types.
     * @throws LexicalException if the inner type was not declared
     */
    void initializeAllUninitializedPointerDescriptors() throws LexicalException {
        this.localIdentifiers.initializeAllUninitializedPointerDescriptors();
    }

    void registerProcedureInterfaceIfNotForwarded(String identifier, List<FormalParameter> formalParameters) throws LexicalException {
        this.localIdentifiers.addProcedureInterfaceIfNotForwarded(identifier, formalParameters);
    }

    void registerFunctionInterfaceIfNotForwarded(String identifier, List<FormalParameter> formalParameters, TypeDescriptor returnTypeDescriptor) throws LexicalException {
        this.localIdentifiers.addFunctionInterfaceIfNotForwarded(identifier, formalParameters, returnTypeDescriptor);
    }

    void forwardProcedure(String identifier, List<FormalParameter> formalParameters) throws LexicalException {
        this.localIdentifiers.forwardProcedure(identifier, formalParameters);
    }

    void forwardFunction(String identifier, List<FormalParameter> formalParameters, TypeDescriptor returnTypeDescriptor) throws LexicalException {
        this.localIdentifiers.forwardFunction(identifier, formalParameters, returnTypeDescriptor);
    }

    public void registerBuiltinSubroutine(String identifier, SubroutineDescriptor descriptor) {
        try {
            this.localIdentifiers.addSubroutine(identifier, descriptor);
            PascalLanguage.INSTANCE.updateSubroutine(this.name, identifier, descriptor.getRootNode());
        } catch (LexicalException e) {
            throw new PascalRuntimeException("Could not register builtin subroutine: " + identifier);
        }
    }

    public void registerType(String identifier, TypeDescriptor type) throws LexicalException{
        this.localIdentifiers.addType(identifier, type);
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

    /**
     * Creates a {@link BlockNode} containing each initialization node of the current scope and returns it.
     */
    BlockNode createInitializationBlock() {
        List<StatementNode> initializationNodes = this.generateInitializationNodes(null);
        initializationNodes.addAll(this.scopeInitializationNodes);

        return new BlockNode(initializationNodes.toArray(new StatementNode[initializationNodes.size()]));
    }

    /**
     * Generates initialization node for each declared identifier in the current scope and returns list of these nodes.
     * @param frame frame of the scope (used in scopes of units)
     */
    List<StatementNode> generateInitializationNodes(VirtualFrame frame)  {
        List<StatementNode> initializationNodes = new ArrayList<>();

        for (Map.Entry<String, TypeDescriptor> entry : this.localIdentifiers.getAllIdentifiers().entrySet()) {
            String identifier = entry.getKey();
            TypeDescriptor typeDescriptor = entry.getValue();

            StatementNode initializationNode = createInitializationNode(identifier, typeDescriptor, frame);
            if (initializationNode != null) {
                initializationNodes.add(initializationNode);
            }
        }

        return initializationNodes;
    }

    private StatementNode createInitializationNode(String identifier, TypeDescriptor typeDescriptor, VirtualFrame frame) {
        Object defaultValue = typeDescriptor.getDefaultValue();
        if (defaultValue == null) {
            return null;
        }

        FrameSlot frameSlot = this.localIdentifiers.getFrameSlot(identifier);
        return InitializationNodeFactory.create(frameSlot, typeDescriptor.getDefaultValue(), frame);
    }

    /**
     * Returns true if the parser is currently inside a loop in the currently parsed source.
     */
    boolean isInLoop() {
        return loopDepth > 0;
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

}