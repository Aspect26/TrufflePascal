package cz.cuni.mff.d3s.trupple.parser;

import java.util.*;

import com.oracle.truffle.api.frame.FrameSlot;
import cz.cuni.mff.d3s.trupple.language.PascalLanguage;
import cz.cuni.mff.d3s.trupple.language.builtinunits.*;
import cz.cuni.mff.d3s.trupple.language.builtinunits.DosBuiltinUnit;
import cz.cuni.mff.d3s.trupple.language.builtinunits.CrtBuiltinUnit;
import cz.cuni.mff.d3s.trupple.language.builtinunits.StringBuiltinUnit;
import cz.cuni.mff.d3s.trupple.language.nodes.*;
import cz.cuni.mff.d3s.trupple.language.builtinunits.GraphBuiltinUnit;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.*;
import cz.cuni.mff.d3s.trupple.language.nodes.call.*;
import cz.cuni.mff.d3s.trupple.language.nodes.control.*;
import cz.cuni.mff.d3s.trupple.language.nodes.function.*;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.*;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.*;
import cz.cuni.mff.d3s.trupple.language.nodes.program.arguments.ProgramArgumentAssignmentFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.root.FunctionPascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.root.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.root.ProcedurePascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.set.SymmetricDifferenceNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.*;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.*;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.read.*;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.write.*;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.write.AssignReferenceNodeGen;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.UnexpectedRuntimeException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.UnknownIdentifierException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.UnknownDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.PointerDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.ReferenceDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.ArrayDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.RecordDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.*;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.FunctionDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.ProcedureDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.ReturnTypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;
import cz.cuni.mff.d3s.trupple.parser.utils.*;

/**
 * Factory for our nodes. The Parses use it to create nodes for currently parsed rules. It also contains current state
 * and identifiers table.
 */
public class NodeFactory {

    /**
     * Lambda interface used in {@link NodeFactory#doLookup(String, GlobalObjectLookup, boolean)} function. The function
     * looks up specified identifier and calls {@link GlobalObjectLookup#onFound(LexicalScope, String)} function  with
     * the found identifier and lexical sccope in which it was found.
     * @param <T>
     */
    private interface GlobalObjectLookup<T> {

        T onFound(LexicalScope foundInScope, String foundIdentifier) throws LexicalException;

    }

    /**
     * The parser to be used for parsing. There can be two: parser for wirths' standard or turbo pascal standard.
     */
	private IParser parser;

    /**
     * Current lexical scope.
     */
	private LexicalScope currentLexicalScope;

    /**
     * List of all parsed units
     */
	private List<UnitLexicalScope> units = new ArrayList<>();

    /**
     * List of all supported builtin units
     */
    private final Map<String, BuiltinUnit> builtinUnits = new HashMap<String, BuiltinUnit>(){{
        put("crt", new CrtBuiltinUnit());
        put("dos", new DosBuiltinUnit());
        put("stringscorejava", new StringBuiltinUnit());
        put("graphcorejava", new GraphBuiltinUnit());
    }};

    /**
     * Flag that specifies whether the factory shall use TurboPascal specific nodes (when using TP standard) or not.
     */
	private final boolean usingTPExtension;

    /**
     * Specifies identifiers of arguments of the main program
     */
	private List<String> mainProgramArgumentsIdentifiers = new ArrayList<>();

	public NodeFactory(IParser parser, boolean usingTPExtension) {
		this.parser = parser;
		this.usingTPExtension = usingTPExtension;
		this.currentLexicalScope = new LexicalScope(null, "_main", parser.isUsingTPExtension());
	}

	public void reset() {
        this.currentLexicalScope = new LexicalScope(null, "_main", parser.isUsingTPExtension());
    }

	public void startPascal(Token identifierToken) {
		this.currentLexicalScope.setName(this.getIdentifierFromToken(identifierToken));
	}

    /**
     * Sets the list of identifiers of program arguments.
     */
	public void setMainProgramArguments(List<String> argumentIdentifiers) {
	    assert currentLexicalScope.getOuterScope() == null;
	    this.mainProgramArgumentsIdentifiers = argumentIdentifiers;
    }

    /**
     * Called when the <i>uses statement</i> is parsed in a Pascal source file. It registers the specified unit to the
     * current scope.
     * @param unitIdentifierToken identifier of the unit
     */
    public void registerUnit(Token unitIdentifierToken) {
        String unitIdentifier = this.getIdentifierFromToken(unitIdentifierToken);
        if (this.builtinUnits.containsKey(unitIdentifier)) {
            BuiltinUnit builtinUnit = this.builtinUnits.get(unitIdentifier);
            UnitLexicalScope unitScope = new UnitLexicalScope(null, unitIdentifier, this.usingTPExtension);
            builtinUnit.importTo(unitScope);
            this.units.add(unitScope);
        } else if (!this.isUnitRegistered(unitIdentifier)) {
            parser.SemErr("Unknown unit: " + unitIdentifier + ". Did you forget to include it?");
        }
    }

    public void registerLabel(Token labelToken) {
	    String labelIdentifier = this.getIdentifierFromToken(labelToken);
	    try {
            this.currentLexicalScope.registerLabel(labelIdentifier);
        } catch (LexicalException e) {
	        parser.SemErr(e.getMessage());
        }
    }

    public void registerNewType(Token identifierToken, TypeDescriptor typeDescriptor) {
        String identifier = this.getIdentifierFromToken(identifierToken);

        try {
            this.currentLexicalScope.registerNewType(identifier, typeDescriptor);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    private boolean isUnitRegistered(String identifier) {
	    return PascalLanguage.INSTANCE.isUnitRegistered(identifier);
    }

    /**
     * Looks up the specified identifier (in current scope and its parent scope up to the topmost scope. If the
     * identifier is found it calls the {@link GlobalObjectLookup#onFound(LexicalScope, String)} function with the found
     * identifier and lexical scope in which it was found as arguments.
     * It firstly looks ups to the topmost lexical scope and if the identifier is not found then it looks up in the unit
     * scopes.
     * @param identifier identifier to be looked up
     * @param lookupFunction the function to be called when the identifier is found
     * @param withReturnType flag whether to also lookup for functions' return variable which are write-only
     * @param <T> type of the returned object
     * @return the value return from the {@link GlobalObjectLookup#onFound(LexicalScope, String)} function
     */
    private <T> T doLookup(String identifier, GlobalObjectLookup<T> lookupFunction, boolean withReturnType) {
	    try {
            T result = lookupToParentScope(this.currentLexicalScope, identifier, lookupFunction, withReturnType, false);
            if (result == null) {
                result = lookupInUnits(identifier, lookupFunction);
            }
            if (result == null) {
                throw new UnknownIdentifierException(identifier);
            }
            return result;
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
            return null;
        }
    }

    /**
     * Overload of {@link NodeFactory#doLookup(String, GlobalObjectLookup, boolean)}.
     */
    private <T> T doLookup(String identifier, GlobalObjectLookup<T> lookupFunction) {
	    return this.doLookup(identifier, lookupFunction, false);
    }

    /**
     * Helper function for {@link NodeFactory#doLookup(String, GlobalObjectLookup, boolean)}. Does the looking up to the
     * topmost lexical scope.
     * @param scope scope to begin the lookup in
     * @param identifier the identifier to lookup
     * @param lookupFunction function that is called when the identifier is found
     * @param <T> type of the returned object
     * @return the value return from the {@link GlobalObjectLookup#onFound(LexicalScope, String)} function
     */
    private <T> T lookupToParentScope(LexicalScope scope, String identifier, GlobalObjectLookup<T> lookupFunction,
                                      boolean withReturnType, boolean onlyPublic) throws LexicalException {
        while (scope != null) {
            if ((onlyPublic)? scope.containsPublicIdentifier(identifier) : scope.containsLocalIdentifier(identifier)) {
                return lookupFunction.onFound(scope, identifier);
            } else if (withReturnType && currentLexicalScope.containsReturnType(identifier, onlyPublic)) {
                return lookupFunction.onFound(currentLexicalScope, identifier);
            } else {
                scope = scope.getOuterScope();
            }
            withReturnType = false; // NOTE: return variable may be only in current scope
        }

        return null;
    }

    /**
     * Helper function for {@link NodeFactory#doLookup(String, GlobalObjectLookup, boolean)}. Does the looking up in the
     * units.
     * @param identifier the identifier to lookup
     * @param lookupFunction function that is called when the identifier is found
     * @param <T> type of the returned object
     * @return the value return from the {@link GlobalObjectLookup#onFound(LexicalScope, String)} function
     */
    private <T> T lookupInUnits(String identifier, GlobalObjectLookup<T> lookupFunction) throws LexicalException {
        T result = null;

        for (LexicalScope unitScope : this.units) {
            result = lookupToParentScope(unitScope, identifier, lookupFunction, false, unitScope != this.currentLexicalScope);
            if (result != null) {
                break;
            }
        }

        return result;
    }

    public TypeDescriptor getTypeDescriptor(Token identifierToken) {
        String identifier = this.getTypeNameFromToken(identifierToken);
        return this.doLookup(identifier, LexicalScope::getTypeDescriptor);
    }

    /**
     * Registers new variables to the current lexical scope with the specified identifiers and their type.
     */
    public void registerVariables(List<String> identifiers, TypeDescriptor typeDescriptor) {
        for (String identifier : identifiers) {
            try {
                currentLexicalScope.registerLocalVariable(identifier, typeDescriptor);
            } catch (LexicalException e) {
                parser.SemErr(e.getMessage());
            }
        }
    }

    public void registerRecordVariantTagVariable(Token identifierToken, Token typeToken) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        TypeDescriptor type = this.getTypeDescriptor(typeToken);

        if (!(type instanceof OrdinalDescriptor)) {
            parser.SemErr("Record variant selector must be of ordinal type");
        }

        try {
            currentLexicalScope.registerLocalVariable(identifier, type);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public TypeDescriptor createArray(List<OrdinalDescriptor> ordinalDimensions, Token returnTypeToken) {
	    String typeIdentifier = this.getTypeNameFromToken(returnTypeToken);
	    TypeDescriptor returnTypeDescriptor = this.doLookup(typeIdentifier, LexicalScope::getTypeDescriptor);
	    ArrayDescriptor array = currentLexicalScope.createArrayType(ordinalDimensions.get(ordinalDimensions.size() - 1), returnTypeDescriptor);

	    for (int i = ordinalDimensions.size() - 2; i > -1; --i) {
	        array = currentLexicalScope.createArrayType(ordinalDimensions.get(i), array);
        }

	    return array;
    }

    public TypeDescriptor createSetType(OrdinalDescriptor baseType) {
        return currentLexicalScope.createSetType(baseType);
    }

    public TypeDescriptor createFileType(TypeDescriptor contentTypeDescriptor) {
        return this.currentLexicalScope.createFileDescriptor(contentTypeDescriptor);
    }

    public void startRecord() {
        this.currentLexicalScope = new RecordLexicalScope(this.currentLexicalScope);
	}

    public TypeDescriptor createRecordType() {
	    return this.currentLexicalScope.createRecordDescriptor();
    }

    public TypeDescriptor createPointerType(Token typeToken) {
	    String typeIdentifier = this.getTypeNameFromToken(typeToken);
        return this.currentLexicalScope.createPointerDescriptor(typeIdentifier);
    }

    public void finishRecord() {
        assert this.currentLexicalScope.getOuterScope() != null;
        this.currentLexicalScope = this.currentLexicalScope.getOuterScope();
    }

    public void initializeAllUninitializedPointerDescriptors() {
	    try {
            this.currentLexicalScope.initializeAllUninitializedPointerDescriptors();
        } catch (LexicalException e) {
	        parser.SemErr(e.getMessage());
        }
    }

    public OrdinalDescriptor createSimpleOrdinalDescriptor(final ConstantDescriptor lowerBound, final ConstantDescriptor upperBound) {
        try {
            return currentLexicalScope.createRangeDescriptor((OrdinalConstantDescriptor)lowerBound, (OrdinalConstantDescriptor)upperBound);
        } catch (LexicalException e){
            parser.SemErr(e.getMessage());
            return currentLexicalScope.createImplicitRangeDescriptor();
        } catch (ClassCastException e) {
            parser.SemErr("Not an ordinal constant");
            return currentLexicalScope.createImplicitRangeDescriptor();
        }
    }

    public OrdinalDescriptor castTypeToOrdinalType(TypeDescriptor typeDescriptor) {
	    try {
	        return (OrdinalDescriptor) typeDescriptor;
        } catch (ClassCastException e) {
	        parser.SemErr("Not an ordinal");
	        return null;
        }
    }

    public TypeDescriptor registerEnum(List<String> enumIdentifiers) {
        try {
            return currentLexicalScope.createEnumType(enumIdentifiers);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
            return UnknownDescriptor.SINGLETON;
        }
    }

    public ConstantDescriptor createNumericConstant(String sign, long value) {
	    try {
	        return createIntConstant(sign, Math.toIntExact(value));
        } catch (ArithmeticException e) {
	        return createLongConstant(sign, value);
        }
    }

    private ConstantDescriptor createIntConstant(String sign, int value) {
        switch (sign) {
            case "":
            case "+": return new IntConstantDescriptor(value);
            case "-": return new IntConstantDescriptor(-value);
            default:
                parser.SemErr("Unknown unary operator: " + sign);
                return new IntConstantDescriptor(0);
        }
    }

    private ConstantDescriptor createLongConstant(String sign, long value) {
        switch (sign) {
            case "":
            case "+": return new LongConstantDescriptor(value);
            case "-": return new LongConstantDescriptor(-value);
            default:
                parser.SemErr("Unknown unary operator: " + sign);
                return new LongConstantDescriptor(0);
        }
    }

    public ConstantDescriptor createDoubleConstant(String sign, double value) {
        switch (sign) {
            case "":
            case "+": return new RealConstantDescriptor(value);
            case "-": return new RealConstantDescriptor(-value);
            default:
                parser.SemErr("Unknown unary operator: " + sign);
                return new RealConstantDescriptor(0);
        }
    }

    public ConstantDescriptor createCharOrStringConstant(String sign, String value) {
        if (!sign.isEmpty()) {
            parser.SemErr("String or char constants cannot have unary operator.");
            return new StringConstantDescriptor("");
        }
        if (value.length() == 1) {
            return new CharConstantDescriptor(value.charAt(0));
        } else {
            return new StringConstantDescriptor(value);
        }
    }

    public ConstantDescriptor createBooleanConstant(String sign, boolean value) {
        if (!sign.isEmpty()) {
            parser.SemErr("String or char constants cannot have unary operator.");
            return new StringConstantDescriptor("");
        }
        return new BooleanConstantDescriptor(value);
    }

    public ConstantDescriptor createConstantFromIdentifier(String sign, Token identifierToken) {
	    String identifier = this.getIdentifierFromToken(identifierToken);
	    try {
	        ConstantDescriptor constant = this.doLookup(identifier, LexicalScope::getConstant);
            if (sign.isEmpty()) {
                return constant;
            } else {
                if (constant.isSigned()) {
                    switch (sign) {
                        case "":case "+":
                            return constant;
                        case "-":
                            return constant.negatedCopy();
                        default:
                            throw new LexicalException("Wrong constant unary operator: " + sign);
                    }
                } else {
                    throw new LexicalException(identifier + " cannot be negated.");
                }
            }
        } catch (LexicalException e) {
	        parser.SemErr(e.getMessage());
	        return new LongConstantDescriptor(0);
        }
    }

    public void registerConstant(Token identifierToken, ConstantDescriptor descriptor) {
	    String identifier = this.getIdentifierFromToken(identifierToken);
	    try {
	        this.currentLexicalScope.registerConstant(identifier, descriptor);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void startMainFunction() {
    }

    /**
     * Forward declaration of a procedure with specified heading.
     */
    public void forwardProcedure(ProcedureHeading heading) {
        String identifier = this.getIdentifierFromToken(heading.identifierToken);
        try {
            currentLexicalScope.forwardProcedure(identifier, heading.formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    /**
     * Forward declaration of a function with specified heading.
     */
    public void forwardFunction(FunctionHeading heading) {
        String identifier = this.getIdentifierFromToken(heading.identifierToken);

        try {
            currentLexicalScope.forwardFunction(identifier, heading.formalParameters, heading.returnTypeDescriptor);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    /**
     * Checks whether the specified identifier is identifier of a return variable of any function visible from the
     * current lexical scope.
     */
    public boolean isReturnVariable(Token identifierToken) {
	    String identifier = this.getIdentifierFromToken(identifierToken);
	    TypeDescriptor typeDescriptor = this.doLookup(identifier, LexicalScope::getIdentifierDescriptor, true);

	    return typeDescriptor instanceof ReturnTypeDescriptor;
    }

    /**
     * Changes the current state to the beginning of an implementation of a new procedure. It creates new child scope
     * to the current scope and and sets it as the current scope.
     * @param heading heading of the new procedure
     */
    public void startProcedureImplementation(ProcedureHeading heading) {
        String identifier = this.getIdentifierFromToken(heading.identifierToken);
        try {
            currentLexicalScope.registerProcedureInterfaceIfNotForwarded(identifier, heading.formalParameters);
            currentLexicalScope = new LexicalScope(currentLexicalScope, identifier, parser.isUsingTPExtension());
            this.addParameterIdentifiersToLexicalScope(heading.formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
            currentLexicalScope = new LexicalScope(currentLexicalScope, identifier, parser.isUsingTPExtension());
        }
	}

    /**
     * Changes the current state to the beginning of an implementation of a new function. It creates new child scope
     * to the current scope and and sets it as the current scope.
     * @param heading heading of the new function
     */
	public void startFunctionImplementation(FunctionHeading heading) {
        String identifier = this.getIdentifierFromToken(heading.identifierToken);

        try {
            currentLexicalScope.registerFunctionInterfaceIfNotForwarded(identifier, heading.formalParameters, heading.returnTypeDescriptor);
            currentLexicalScope = new LexicalScope(currentLexicalScope, identifier, parser.isUsingTPExtension());
            currentLexicalScope.registerReturnVariable(heading.returnTypeDescriptor);
            this.addParameterIdentifiersToLexicalScope(heading.formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
            currentLexicalScope = new LexicalScope(currentLexicalScope, identifier, parser.isUsingTPExtension());
        }
    }

    /**
     * Creates list of {@link FormalParameter}s from specified identifiers and their type.
     * @param identifiers the identifiers
     * @param typeDescriptor type of all the identifiers
     * @param isReference flag whether the arguments are reference-passed
     * @return the newly created list
     */
    public List<FormalParameter> createFormalParametersList(List<String> identifiers, Token typeDescriptor, boolean isReference) {
        List<FormalParameter> paramList = new ArrayList<>();
        for (String identifier : identifiers) {
            paramList.add(new FormalParameter(identifier, this.getTypeDescriptor(typeDescriptor), isReference));
        }

        return paramList;
    }

    /**
     * Creates a {@link FormalParameter} whose type is a procedure with specified signature.
     * @param heading the procedure's signature
     */
    public FormalParameter createProcedureFormalParameter(ProcedureHeading heading) {
        String identifier = this.getIdentifierFromToken(heading.identifierToken);
        ProcedureDescriptor descriptor = new ProcedureDescriptor(heading.formalParameters);

        return new FormalParameter(identifier, descriptor, false, heading.descriptor);
    }

    /**
     * Creates a {@link FormalParameter} whose type is a function with specified signature.
     * @param heading the function's signature
     */
    public FormalParameter createFunctionFormalParameter(FunctionHeading heading) {
        String identifier = this.getIdentifierFromToken(heading.identifierToken);
        FunctionDescriptor descriptor = new FunctionDescriptor(heading.formalParameters, heading.returnTypeDescriptor);

        return new FormalParameter(identifier, descriptor, false, heading.descriptor);
    }

    /**
     * Changes current state after parsing of some procedure is finished. It finishes the procedure's AST and changes
     * current lexical scope to the parent of current lexical scope.
     * @param bodyNode body node of the parsed procedure
     */
    public void finishProcedureImplementation(StatementNode bodyNode) {
        StatementNode subroutineNode = createSubroutineNode(bodyNode);
        final ProcedureBodyNode procedureBodyNode = new ProcedureBodyNode(subroutineNode);
        final PascalRootNode rootNode = new ProcedurePascalRootNode(currentLexicalScope.getFrameDescriptor(), procedureBodyNode);
        finishSubroutine(rootNode);
    }

    /**
     * Changes current state after parsing of some function is finished. It finishes the function's AST and changes
     * current lexical scope to the parent of current lexical scope.
     * @param bodyNode body node of the parsed function
     */
    public void finishFunctionImplementation(StatementNode bodyNode) {
        StatementNode subroutineNode = createSubroutineNode(bodyNode);
        final FunctionBodyNode functionBodyNode = FunctionBodyNodeGen.create(
                subroutineNode,
                currentLexicalScope.getReturnSlot(),
                currentLexicalScope.getIdentifierDescriptor(currentLexicalScope.getName()));
        final PascalRootNode rootNode = new FunctionPascalRootNode(currentLexicalScope.getFrameDescriptor(), functionBodyNode);
        finishSubroutine(rootNode);
    }

    private StatementNode createSubroutineNode(StatementNode bodyNode) {
        List<StatementNode> subroutineNodes = new ArrayList<>();

        subroutineNodes.add(currentLexicalScope.createInitializationBlock());
        subroutineNodes.add(bodyNode);

        return new BlockNode(subroutineNodes.toArray(new StatementNode[subroutineNodes.size()]));
    }

    /**
     * Changes current state by increasing current loop depth.
     */
    public void startLoop() {
        currentLexicalScope.increaseLoopDepth();
    }

    public StatementNode createLabeledStatement(StatementNode statement, Token labelToken) {
	    String labelIdentifier = this.getIdentifierFromToken(labelToken);
	    if (!currentLexicalScope.labelExists(labelIdentifier)) {
	        parser.SemErr("Label " + labelIdentifier + " is not defined");
	        return statement;
        }
	    return new LabeledStatement(statement, labelIdentifier);
    }

    /**
     * Creates {@link ForNode} node with the specified parameters.
     * @param ascending flag whether the loop is ascending or descending (<i>to</i> or <i>downto</i> in Pascal source)
     * @param variableToken token of the control variable
     * @param startValue initial value of the control value
     * @param finalValue final value of the control value
     * @param loopBody loop's body node
     * @return the newly created node
     */
    public StatementNode createForLoop(boolean ascending, Token variableToken, ExpressionNode startValue, ExpressionNode finalValue, StatementNode loopBody) {
        String iteratingIdentifier = this.getIdentifierFromToken(variableToken);
        FrameSlot controlSlot = this.doLookup(iteratingIdentifier, LexicalScope::getLocalSlot);
        if (controlSlot == null) {
            parser.SemErr("Unknown identifier: " + iteratingIdentifier);
        }
        if (startValue.getType() != finalValue.getType() && !startValue.getType().convertibleTo(finalValue.getType())) {
            parser.SemErr("Type mismatch in beginning and last value of for loop.");
        }
        SimpleAssignmentNode initialAssignment = this.createAssignmentNode(iteratingIdentifier, startValue);
        ExpressionNode readControlVariableNode = this.createReadVariableNode(variableToken);
        return new ForNode(ascending, initialAssignment, controlSlot, finalValue, readControlVariableNode, loopBody);
    }

    /**
     * Creates {@link RepeatNode} node with the specified parameters.
     * @param condition the loop's condition node
     * @param loopBody loop's body node
     * @return the newly created node
     */
    public StatementNode createRepeatLoop(ExpressionNode condition, StatementNode loopBody) {
        if (!(condition.getType() == BooleanDescriptor.getInstance())) {
            parser.SemErr("Unless condition must be a boolean value");
        }
        return new RepeatNode(condition, loopBody);
    }

    /**
     * Creates {@link WhileNode} node with the specified parameters.
     * @param condition the loop's condition node
     * @param loopBody loop's body node
     * @return the newly created node
     */
    public StatementNode createWhileLoop(ExpressionNode condition, StatementNode loopBody) {
        if (!(condition.getType() == BooleanDescriptor.getInstance())) {
            parser.SemErr("While condition must be a boolean value");
        }
        return new WhileNode(condition, loopBody);
    }

    /**
     * Creates {@link BreakNodeTP}
     */
    public StatementNode createBreak() {
        if (!currentLexicalScope.isInLoop()) {
            parser.SemErr("Break outside a loop: ");
        }
        return new BreakNodeTP();
    }

    /**
     * Changes the current state by decreasing current loop depth.
     */
    public void finishLoop() {
        try {
            currentLexicalScope.decreaseLoopDepth();
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    /**
     * Creates {@link IfNode} with specified parameters.
     * @param condition the condition node
     * @param thenNode the node that is executed if the condition is true
     * @param elseNode the node that is executed if the condition is false
     * @return the newly created node
     */
    public StatementNode createIfStatement(ExpressionNode condition, StatementNode thenNode, StatementNode elseNode) {
        if (!(condition.getType() == BooleanDescriptor.getInstance())) {
            parser.SemErr("If condition must be a boolean value");
        }
	    return new IfNode(condition, thenNode, elseNode);
    }

    /**
     * Changes current state by stepping inside lexical scope of specified records (used for <i>with statement</i>).
      * @param recordIdentifiers identifiers of the records into which current lexical scope will submerge (in the order
     *                          from the first element of the list to the last)
     * @return list of slots (to frame descriptors) of records into which the current lexical scope submerged
     */
    public List<FrameSlot> stepIntoRecordsScope(List<String> recordIdentifiers) {
	    LexicalScope currentScope = this.currentLexicalScope;
        List<FrameSlot> recordsSlots = new ArrayList<>();

        try {
            String firstIdentifier = this.doLookup(recordIdentifiers.get(0), (LexicalScope scope, String identifier) -> {
                this.currentLexicalScope = scope;
                return identifier;
            });
            recordsSlots.add(this.stepIntoRecordElementScope(firstIdentifier));
            for (int i = 1; i < recordIdentifiers.size(); ++i) {
                recordsSlots.add(this.stepIntoRecordElementScope(recordIdentifiers.get(i)));
            }
            return recordsSlots;
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
            this.currentLexicalScope = currentScope;
            return Collections.emptyList();
        }
    }

    private FrameSlot stepIntoRecordElementScope(String recordIdentifier) throws LexicalException {
        TypeDescriptor recordDescriptor = this.currentLexicalScope.getIdentifierDescriptor(recordIdentifier);
        if (recordDescriptor == null || !(recordDescriptor instanceof RecordDescriptor)) {
            throw new LexicalException("Not a record: " + recordIdentifier);
        } else {
            FrameSlot recordSlot = this.currentLexicalScope.getLocalSlot(recordIdentifier);
            this.currentLexicalScope = ((RecordDescriptor) recordDescriptor).getLexicalScope();
            return recordSlot;
        }
    }

    /**
     * Creates a {@link WithNode} node.
     * @param frameSlots slots of records into which the node will submerge at runtime.
     * @param innerStatement statement which will be executed inside the with statement
     * @return the newly created node
     */
    public WithNode createWithStatement(List<FrameSlot> frameSlots, StatementNode innerStatement) {
	    return new WithNode(frameSlots, innerStatement);
    }

    /**
     * Creates a {@link CaseNode} from the specified data.
     * @param data data of the case statement
     * @return the newly created node
     */
    public CaseNode createCaseStatement(CaseStatementData data) {
        ExpressionNode[] indexes = data.indexNodes.toArray(new ExpressionNode[data.indexNodes.size()]);
        StatementNode[] statements = data.statementNodes.toArray(new StatementNode[data.statementNodes.size()]);

        return new CaseNode(data.caseExpression, indexes, statements, data.elseNode);
    }

    /**
     * Creates a {@link GotoNode} with jump to the specified label.
     * @param labelToken token of the label to which the goto will jump
     * @return the newly created node
     */
    public StatementNode createGotoStatement(Token labelToken) {
	    String labelIdentifier = this.getIdentifierFromToken(labelToken);
	    return new GotoNode(labelIdentifier);
    }

    /**
     * Creates {@link NopNode}.
     */
    public StatementNode createNopStatement() {
        return new NopNode();
    }

    /**
     * Creates {@link BinaryExpressionNode} from the specified operator and operands.
     * @param operator operator of the operation
     * @param leftNode left operand's node
     * @param rightNode right operand's node
     * @return the newly created node
     */
    public ExpressionNode createBinaryExpression(String operator, ExpressionNode leftNode, ExpressionNode rightNode) {
        ExpressionNode resultExpression;
	    switch (operator.toLowerCase()) {

            // arithmetic
            case "+":
                resultExpression = (usingTPExtension)? AddNodeTPNodeGen.create(leftNode, rightNode) : AddNodeGen.create(leftNode, rightNode); break;
            case "-":
                resultExpression = SubtractNodeGen.create(leftNode, rightNode); break;
            case "*":
                resultExpression = MultiplyNodeGen.create(leftNode, rightNode); break;
            case "/":
                resultExpression = DivideNodeGen.create(leftNode, rightNode); break;
            case "div":
                resultExpression = DivideIntegerNodeGen.create(leftNode, rightNode); break;
            case "mod":
                resultExpression = ModuloNodeGen.create(leftNode, rightNode); break;

            // logic
            case "and":
                resultExpression = AndNodeGen.create(leftNode, rightNode); break;
            case "or":
                resultExpression = OrNodeGen.create(leftNode, rightNode); break;
            case "<":
                resultExpression = LessThanNodeGen.create(leftNode, rightNode); break;
            case "<=":
                resultExpression = LessThanOrEqualNodeGen.create(leftNode, rightNode); break;
            case ">":
                BinaryExpressionNode lessThanOrEqual = this.verifyOperandArguments(LessThanOrEqualNodeGen.create(leftNode, rightNode), operator);
                resultExpression = NotNodeGen.create(lessThanOrEqual); break;
            case ">=":
                BinaryExpressionNode lessThan = this.verifyOperandArguments(LessThanNodeGen.create(leftNode, rightNode), operator);
                resultExpression = NotNodeGen.create(lessThan); break;
            case "=":
                resultExpression = EqualsNodeGen.create(leftNode, rightNode); break;
            case "<>":
                BinaryExpressionNode equals = this.verifyOperandArguments(EqualsNodeGen.create(leftNode, rightNode), operator);
                resultExpression = NotNodeGen.create(equals); break;
            case "in":
                resultExpression = InNodeGen.create(leftNode, rightNode); break;
            case "><":
                resultExpression = SymmetricDifferenceNodeGen.create(leftNode, rightNode); break;

            default:
                parser.SemErr("Unknown binary operator: " + operator);
                return InvalidBinaryExpressionNodeGen.create(leftNode, rightNode);
        }

        return this.verifyOperandArguments(resultExpression, operator);
    }

    /**
     * Creates {@link UnaryNode} from the specified operator and operand.
     * @param operator operator of the operation
     * @param son operand's node
     * @return the newly created node
     */
    public ExpressionNode createUnaryExpression(Token operator, ExpressionNode son) {
        switch (operator.val) {
            case "+":
                return son;
            case "-":
                return NegationNodeGen.create(son);
            case "not":
                return NotNodeGen.create(son);
            default:
                parser.SemErr("Unexpected unary operator: " + operator.val);
                return null;
        }
    }

    /**
     * Creates an assignment node from the received data and the assigned value's node.
     * @param assignmentData data of the assignment
     * @param valueNode value's node
     * @return the newly created node
     */
    public StatementNode finishAssignmentNode(AssignmentData assignmentData, ExpressionNode valueNode) {
        switch (assignmentData.type) {
            case Simple:
                return createSimpleAssignment(assignmentData.targetIdentifier, valueNode);
            case Array:
                return createAssignmentToArray(assignmentData.targetNode, assignmentData.arrayIndexNode, valueNode);
            case Dereference:
                return createAssignmentToDereference(assignmentData.targetNode, valueNode);
            case Record:
                return createAssignmentToRecordField(assignmentData.targetNode, assignmentData.targetIdentifier, valueNode);
        }

        throw new UnexpectedRuntimeException();
    }

    /**
     * Creates assignment node for assignment to a variable ({@link AssignReferenceNode} or {@link SimpleAssignmentNode}).
     * @param identifierToken identifier of the target variable
     * @param valueNode assigning value's node
     * @return the newly created node
     */
    public StatementNode createSimpleAssignment(Token identifierToken, ExpressionNode valueNode) {
        String variableIdentifier = this.getIdentifierFromToken(identifierToken);
        FrameSlot frameSlot = this.doLookup(variableIdentifier, LexicalScope::getLocalSlot, true);
        TypeDescriptor targetType = this.doLookup(variableIdentifier, LexicalScope::getIdentifierDescriptor, true);
        this.checkTypesAreCompatible(valueNode.getType(), targetType);

        return (targetType instanceof ReferenceDescriptor)? AssignReferenceNodeGen.create(valueNode, frameSlot) : SimpleAssignmentNodeGen.create(valueNode, frameSlot);
    }

    /**
     * Creates {@link AssignToArrayNode} for assignment to an array at specified index
     * @param arrayExpression expression node which returns the target array
     * @param indexExpressionNode expression node returning the index
     * @param valueNode assigning value's node
     * @return the newly created node
     */
    private StatementNode createAssignmentToArray(ExpressionNode arrayExpression, ExpressionNode indexExpressionNode, ExpressionNode valueNode) {
	    TypeDescriptor expressionType = getActualType(arrayExpression.getType());
	    int arrayOffset = 0;
	    if (!(expressionType instanceof ArrayDescriptor)) {
            parser.SemErr("Not an array");
        } else {
	        this.doTypeCheck(valueNode.getType(), ((ArrayDescriptor) expressionType).getValuesDescriptor());
	        arrayOffset = ((ArrayDescriptor) expressionType).getOffset();
        }
        ReadIndexNode indexNode = ReadIndexNodeGen.create(indexExpressionNode, arrayOffset);
        return AssignToArrayNodeGen.create(arrayExpression, indexNode, valueNode);
    }

    /**
     * Creates {@link AssignToDereferenceNode} for assignment to dereferenced array
     * @param pointerExpression expression node which returns the pointer
     * @param valueNode assigning value's node
     * @return the newly created node
     */
    private StatementNode createAssignmentToDereference(ExpressionNode pointerExpression, ExpressionNode valueNode) {
        TypeDescriptor expressionType = getActualType(pointerExpression.getType());
	    if (!(expressionType instanceof PointerDescriptor)) {
            parser.SemErr("Not a pointer");
        } else {
            this.doTypeCheck(valueNode.getType(), ((PointerDescriptor) pointerExpression.getType()).getInnerTypeDescriptor());
        }
        return AssignToDereferenceNodeGen.create(pointerExpression, valueNode);
    }

    /**
     * Creates {@link AssignToRecordField} node for assignment to variable inside a record
     * @param recordExpression expression node which returns the record
     * @param identifierToken token of the target record's variable
     * @param valueNode assigning value's node
     * @return the newly created node
     */
    private StatementNode createAssignmentToRecordField(ExpressionNode recordExpression, Token identifierToken, ExpressionNode valueNode) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        TypeDescriptor expressionType = getActualType(recordExpression.getType());
        if (!(expressionType instanceof RecordDescriptor)) {
            parser.SemErr("Not a record");
        }
        return AssignToRecordFieldNodeGen.create(identifier, recordExpression, valueNode);
    }

    /**
     * Creates an {@link ExpressionNode} from an identifier (can be variable read or parameterless function call)
     * @param identifierToken the identifier
     * @return the newly created node
     */
    public ExpressionNode createExpressionFromSingleIdentifier(Token identifierToken) {
        String identifier = this.getIdentifierFromToken(identifierToken);

        return this.doLookup(identifier, (LexicalScope foundInLexicalScope, String foundIdentifier) -> {
            if (foundInLexicalScope.isParameterlessSubroutine(foundIdentifier)) {
                SubroutineDescriptor descriptor = (SubroutineDescriptor) foundInLexicalScope.getIdentifierDescriptor(foundIdentifier);
                return this.createInvokeNode(foundIdentifier, descriptor, foundInLexicalScope, Collections.emptyList());
            }
            else {
                return createReadVariableFromScope(foundIdentifier, foundInLexicalScope);
            }
        });
    }

    /**
     * Creates node that read value from specified variable.
     * @param identifierToken identifier token of the variable
     * @return the newly created node
     */
    public ExpressionNode createReadVariableNode(Token identifierToken) {
	    String identifier = this.getIdentifierFromToken(identifierToken);
	    return this.doLookup(identifier, (LexicalScope foundInScope, String foundIdentifier) ->
	        createReadVariableFromScope(foundIdentifier, foundInScope)
        );
    }

    private ExpressionNode createReadVariableFromScope(String identifier, LexicalScope scope) {
        FrameSlot variableSlot = scope.getLocalSlot(identifier);
        TypeDescriptor type = scope.getIdentifierDescriptor(identifier);
        boolean isLocal = scope == currentLexicalScope;
        boolean isReference = type instanceof ReferenceDescriptor;
        boolean isConstant = type instanceof ConstantDescriptor;

        if (isConstant) {
            ConstantDescriptor constantType = (ConstantDescriptor) type;
            return ReadConstantNodeGen.create(constantType.getValue(), constantType);
        } else if (isLocal) {
            // TODO: check if it is a variable
            if (isReference) {
                return ReadReferenceVariableNodeGen.create(variableSlot, type);
            } else {
                return ReadLocalVariableNodeGen.create(variableSlot, type);
            }
        } else {
            return ReadGlobalVariableNodeGen.create(variableSlot, type);
        }
    }

    /**
     * Creates {@link ReadFromArrayNode} that reads value from specified array at specified index.
     * @param arrayExpression node that returns the array
     * @param indexes nodes of the indexes at which the array will be read
     * @return the newly created node
     */
    public ExpressionNode createReadFromArrayNode(ExpressionNode arrayExpression, List<ExpressionNode> indexes) {
	    ExpressionNode readArrayNode = arrayExpression;
	    for (ExpressionNode index : indexes) {
	        TypeDescriptor actualType = this.getActualType(readArrayNode.getType());
	        if (!(actualType instanceof  ArrayDescriptor)) {
                parser.SemErr("Not an array");
                break;
            }
            ReadIndexNode readIndexNode = ReadIndexNodeGen.create(index, ((ArrayDescriptor) actualType).getOffset());
            TypeDescriptor returnType = ((ArrayDescriptor) actualType).getValuesDescriptor();
	        readArrayNode = ReadFromArrayNodeGen.create(readArrayNode, readIndexNode, returnType);
        }
        return readArrayNode;
    }

    /**
     * Creates {@link ReadDereferenceNode} that reads value to which specified pointer points to.
     * @param pointerExpression node that returns the pointer
     * @return the newly created node
     */
    public ReadDereferenceNode createReadDereferenceNode(ExpressionNode pointerExpression) {
        PointerDescriptor pointerDescriptor = null;
        TypeDescriptor actualType = this.getActualType(pointerExpression.getType());
        if (actualType instanceof PointerDescriptor) {
            pointerDescriptor = (PointerDescriptor) actualType;
        }
	    else {
            parser.SemErr("Can not dereference this type");
        }
        TypeDescriptor returnType = (pointerDescriptor != null)? pointerDescriptor.getInnerTypeDescriptor() : null;

        return ReadDereferenceNodeGen.create(pointerExpression, returnType);
    }

    /**
     * Creates {@link ReadFromRecordNode} that reads value of specified variable from specified record.
     * @param recordExpression node that returns the record
     * @param identifierToken identifier of the variable to be read
     * @return tje newly created node
     */
    public ReadFromRecordNode createReadFromRecordNode(ExpressionNode recordExpression, Token identifierToken) {
        TypeDescriptor descriptor = this.getActualType(recordExpression.getType());
        String identifier = this.getIdentifierFromToken(identifierToken);
        TypeDescriptor returnType = null;

        if (!(descriptor instanceof RecordDescriptor)) {
            parser.SemErr("Can not access non record type this way");
        } else {
            RecordDescriptor accessedRecordDescriptor = (RecordDescriptor) descriptor;
            if (!accessedRecordDescriptor.containsIdentifier(identifier)) {
                parser.SemErr("The record does not contain this identifier");
            } else {
                returnType = accessedRecordDescriptor.getLexicalScope().getIdentifierDescriptor(identifier);
            }
        }

        return ReadFromRecordNodeGen.create(recordExpression, returnType, identifier);
    }

    /**
     * Check whether formal parameter of specified subroutine at specified index is reference-passed.
     * @param subroutineToken identifier of the subroutine
     * @param parameterIndex index of the parameter
     */
    public boolean shouldBeReference(Token subroutineToken, int parameterIndex) {
        String subroutineIdentifier = this.getIdentifierFromToken(subroutineToken);
        return this.doLookup(subroutineIdentifier,
                (LexicalScope foundInScope, String foundIdentifier) -> foundInScope.isReferenceParameter(foundIdentifier, parameterIndex));
    }

    /**
     * Check whether formal parameter of specified subroutine at specified index is subroutine-type.
     * @param subroutineToken identifier of the subroutine
     * @param parameterIndex index of the parameter
     */
    public boolean shouldBeSubroutine(Token subroutineToken, int parameterIndex) {
        String subroutineIdentifier = this.getIdentifierFromToken(subroutineToken);
        return this.doLookup(subroutineIdentifier,
                (LexicalScope foundInScope, String foundIdentifier) -> foundInScope.isSubroutineParameter(foundIdentifier, parameterIndex));
    }

    /**
     * Creates {@link InvokeNode} for call statement with specified parameters.
     * @param identifierToken identifier of the called subroutine
     * @param params argument expression nodes
     * @return the newly created node
     */
    public ExpressionNode createSubroutineCall(Token identifierToken, List<ExpressionNode> params) {
        String identifier = this.getIdentifierFromToken(identifierToken);

        return this.doLookup(identifier, (LexicalScope foundInScope, String foundIdentifier) -> {
            if (foundInScope.isSubroutine(foundIdentifier)) {
                SubroutineDescriptor subroutineDescriptor = foundInScope.getSubroutineDescriptor(foundIdentifier, params);
                subroutineDescriptor.verifyArguments(params);
                return this.createInvokeNode(foundIdentifier, subroutineDescriptor, foundInScope, params);
            } else {
                throw new LexicalException(foundIdentifier + " is not a subroutine.");
            }
        });
    }

    private ExpressionNode createInvokeNode(String identifier, SubroutineDescriptor descriptor, LexicalScope subroutineScope, List<ExpressionNode> argumentNodes) {
	    ExpressionNode[] arguments = argumentNodes.toArray(new ExpressionNode[argumentNodes.size()]);
	    TypeDescriptor returnType = (descriptor instanceof FunctionDescriptor)? ((FunctionDescriptor) descriptor).getReturnDescriptor() : null;

	    if (subroutineScope instanceof UnitLexicalScope) {
	        String unitIdentifier = subroutineScope.getName();
            return UnitInvokeNodeGen.create(identifier, unitIdentifier, arguments, returnType);
        } else {
	        FrameSlot subroutineSlot = subroutineScope.getLocalSlot(identifier);
	        return InvokeNodeGen.create(subroutineSlot, arguments, returnType);
        }
    }

    /**
     * Creates {@link StoreReferenceArgumentNode} for the specified variable.
     * @param variableToken identifier of the variable
     * @return the newly created node
     */
    public ExpressionNode createReferencePassNode(Token variableToken) {
        String variableIdentifier = this.getIdentifierFromToken(variableToken);
        FrameSlot slot = this.doLookup(variableIdentifier, LexicalScope::getLocalSlot);
        return new StoreReferenceArgumentNode(slot, currentLexicalScope.getIdentifierDescriptor(variableIdentifier));
    }

    /**
     * Creates {@link SubroutineLiteralNode} for the specified subroutine.
     * @param subroutineToken identifier of the subroutine
     * @return the newly created node
     */
    public ExpressionNode createSubroutineParameterPassNode(Token subroutineToken) {
        String subroutineIdentifier = this.getIdentifierFromToken(subroutineToken);
        PascalSubroutine subroutine = this.doLookup(subroutineIdentifier, LexicalScope::getSubroutine);
        TypeDescriptor descriptor = this.doLookup(subroutineIdentifier, LexicalScope::getIdentifierDescriptor);

        return SubroutineLiteralNodeGen.create(subroutine, (SubroutineDescriptor) descriptor);
    }

    public ExpressionNode createLogicLiteralNode(boolean value) {
        return LogicLiteralNodeGen.create(value);
    }

    /**
     * Creates {@link SetConstructorNode} for the specified values.
     * @param valueNodes nodes of the values
     * @return the newly created node
     */
    public ExpressionNode createSetConstructorNode(List<ExpressionNode> valueNodes) {
	    if (valueNodes.size() > 0) {
	        TypeDescriptor valuesType = valueNodes.get(0).getType();
	        for (ExpressionNode expressionNode : valueNodes) {
	            if (!expressionNode.getType().equals(valuesType) && !expressionNode.getType().convertibleTo(valuesType)) {
	                parser.SemErr("Type mismatch in set constructor");
	                break;
                }
            }
        }
	    return new SetConstructorNode(valueNodes);
    }

    public ExpressionNode createNumericLiteralNode(Token literalToken) {
	    try {
	        return IntLiteralNodeGen.create(this.getIntFromToken(literalToken));
        } catch(NumberFormatException e) {
	        return LongLiteralNodeGen.create(this.getLongFromToken(literalToken));
        }
    }

    public ExpressionNode createFloatLiteralNode(Token token) {
        return DoubleLiteralNodeGen.create(getDoubleFromToken(token));
    }

    public ExpressionNode createCharOrStringLiteralNode(String literal) {
        return (literal.length() == 1) ? CharLiteralNodeGen.create(literal.charAt(0)) : StringLiteralNodeGen.create(literal);
    }

    /**
     * Creates {@link BlockNode} from the specified list of {@link StatementNode}s.
     */
    public StatementNode createBlockNode(List<StatementNode> bodyNodes, boolean useExtendedVersion) {
        return (useExtendedVersion)?
                new ExtendedBlockNode(bodyNodes.toArray(new StatementNode[bodyNodes.size()]))
                :
                new BlockNode(bodyNodes.toArray(new StatementNode[bodyNodes.size()]));
    }

    /**
     * Changes the current state after finishing parsing of the source's main block.
     * @param blockNode block node of the body
     * @return the newly created root node
     */
    public PascalRootNode finishMainFunction(StatementNode blockNode) {
        this.addUnitInitializationNodes();
	    this.addProgramArgumentsAssignmentNodes();

        StatementNode bodyNode = this.createSubroutineNode(blockNode);
        MainFunctionBodyNode functionNode = MainFunctionBodyNodeGen.create(bodyNode, currentLexicalScope.getFrameDescriptor(), currentLexicalScope.getReturnSlot());
        return new FunctionPascalRootNode(currentLexicalScope.getFrameDescriptor(), functionNode);
    }

    public PascalRootNode createUnitRootNode() {
	    return new ProcedurePascalRootNode(currentLexicalScope.getFrameDescriptor(), this.currentLexicalScope.createInitializationBlock());
    }

    /**
     * Creates assignment nodes for assigning program argument values to program argument variable.
     */
    private void addProgramArgumentsAssignmentNodes() {
	    int currentArgument = 0;
	    for (String argumentIdentifier : this.mainProgramArgumentsIdentifiers) {
	        FrameSlot argumentSlot = this.currentLexicalScope.getLocalSlot(argumentIdentifier);
	        TypeDescriptor typeDescriptor = this.currentLexicalScope.getIdentifierDescriptor(argumentIdentifier);

	        if (typeDescriptor != null) {
                try {
                    this.currentLexicalScope.addScopeInitializationNode(ProgramArgumentAssignmentFactory.create(argumentSlot, currentArgument, typeDescriptor));
                } catch (LexicalException e) {
                    parser.SemErr(e.getMessage());
                }
            }
            ++currentArgument;
        }
    }

    /**
     * Add initialization nodes of each registered unit to list of initialization nodes of the current scope.
     */
    private void addUnitInitializationNodes() {
        for (LexicalScope unitScope : this.units) {
            this.currentLexicalScope.addScopeInitializationNode(unitScope.createInitializationBlock());
        }
    }

    public String createStringFromToken(Token t) {
        String literal = t.val;
        literal = literal.substring(1, literal.length() - 1);
        literal = this.getPascalUnescapedString(literal);
        return literal;
    }

    private String getTypeNameFromToken(Token typeNameToken) {
        return typeNameToken.val.toLowerCase();
    }

    /**
     * Add nodes that assign passed value to current subroutine to their corresponding argument variables to the current
     * scope's list of initialization nodes. The values are read from the frame's arguments at the runtime.
     * @param parameters the formal parameters
     */
    private void addParameterIdentifiersToLexicalScope(List<FormalParameter> parameters) {
        try {
            int count = 0;
            for (FormalParameter parameter : parameters) {
                TypeDescriptor typeDescriptor = parameter.type;

                if (typeDescriptor == null)
                    return;

                if (parameter.isReference) {
                    FrameSlot frameSlot = this.currentLexicalScope.registerReferenceVariable(parameter.identifier, typeDescriptor);
                    final ReferenceInitializationNode initializationNode = new ReferenceInitializationNode(frameSlot, count++);

                    this.currentLexicalScope.addScopeInitializationNode(initializationNode);
                } else {
                    this.currentLexicalScope.registerLocalVariable(parameter.identifier, typeDescriptor);
                    final ExpressionNode readNode = new ReadArgumentNode(count, parameters.get(count++).type);
                    final SimpleAssignmentNode assignment = this.createAssignmentNode(parameter.identifier, readNode);

                    this.currentLexicalScope.addScopeInitializationNode(assignment);
                }
            }
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    /**
     * Creates {@link SimpleAssignmentNode} for the specified variable and value.
     * @param targetIdentifier the variable's identifier
     * @param valueNode assigning value's node
     * @return the newly created node
     */
    private SimpleAssignmentNode createAssignmentNode(String targetIdentifier, ExpressionNode valueNode) {
        TypeDescriptor targetType = this.doLookup(targetIdentifier, LexicalScope::getIdentifierDescriptor);
        this.checkTypesAreCompatible(valueNode.getType(), targetType);
	    FrameSlot targetSlot = this.doLookup(targetIdentifier, LexicalScope::getLocalSlot);

	    return SimpleAssignmentNodeGen.create(valueNode, targetSlot);
    }

    /**
     * Checks whether the two types are compatible. The operation is not symmetric.
     */
    private boolean checkTypesAreCompatible(TypeDescriptor leftType, TypeDescriptor rightType) {
        if ((leftType != rightType) && !leftType.convertibleTo(rightType)) {
            if ((rightType instanceof FunctionDescriptor) && !this.checkTypesAreCompatible(leftType, ((FunctionDescriptor) rightType).getReturnDescriptor())) {
                parser.SemErr("Type mismatch");
                return false;
            }
        }

        return true;
    }

    private void finishSubroutine(PascalRootNode rootNode) {
        String subroutineIdentifier = currentLexicalScope.getName();
        currentLexicalScope = currentLexicalScope.getOuterScope();
        try {
            currentLexicalScope.setSubroutineRootNode(subroutineIdentifier, rootNode);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    private TypeDescriptor getActualType(TypeDescriptor typeDescriptor) {
	    if (typeDescriptor instanceof ReferenceDescriptor) {
	        return  ((ReferenceDescriptor) typeDescriptor).getReferencedType();
        } else if (typeDescriptor instanceof FunctionDescriptor) {
	        return ((FunctionDescriptor) typeDescriptor).getReturnDescriptor();
        }

        return typeDescriptor;
    }

    private <T extends ExpressionNode> T verifyOperandArguments(T node, String operator) {
	    return this.verifyChildrenNodes(node, "Wrong operand types provided to " + operator + " operator");
    }

    private <T extends StatementNode> T verifyChildrenNodes(T node, String onInvalidText) {
        if (!node.verifyChildrenNodeTypes()) {
            parser.SemErr(onInvalidText);
        }

        return node;
    }

    private void doTypeCheck(TypeDescriptor left, TypeDescriptor right) {
        if (!(left == right) && !left.convertibleTo(right)) {
            parser.SemErr("Type mismatch");
        }
    }

    /**
     * Checks whether the specified (record's variant's) case selector constants are valid for the given ordinal. The
     * must contain each value specified by the ordinal and no other.
     * @param ordinal the ordinal
     * @param constants the selector constants
     */
    public void assertLegalsCaseValues(OrdinalDescriptor ordinal, List<ConstantDescriptor> constants) {
	    if (!this.usingTPExtension) {
            if (ordinal.getSize() != constants.size()) {
                parser.SemErr("Constants list of variant part of record type must contain all values of variant's selector type");
                return;
            }
        }

	    List<Object> values = new ArrayList<>();
	    for (ConstantDescriptor constant : constants) {
	        Object value = constant.getValue();
	        if (values.contains(value)) {
                parser.SemErr("Duplicit case constant value");
                return;
            }
            if (!ordinal.containsValue(value)) {
                parser.SemErr("Case constant " + value + " has wrong type");
                return;
            }
	        values.add(constant.getValue());
        }
    }

    private int getIntFromToken(Token token) {
        return Integer.parseInt(token.val);
    }

    public long getLongFromToken(Token token) {
        try {
            return Long.parseLong(token.val);
        } catch (NumberFormatException e) {
            parser.SemErr("Integer literal out of range");
            return 0;
        }
    }

    public double getDoubleFromToken(Token token) {
        return Float.parseFloat(token.val);
    }

    public String getIdentifierFromToken(Token identifierToken) {
        return identifierToken.val.toLowerCase();
    }

    /**
     * Gets the current lexical scope.
     */
    public LexicalScope getScope() {
	    return this.currentLexicalScope;
    }

    /**
     * Sets new current lexical scope.
     */
    public void setScope(LexicalScope scope) {
	    this.currentLexicalScope = scope;
    }

    private String getPascalUnescapedString(String str) {
	    String result = str;
        result = result.replaceAll("''", "'");
        result = result.replaceAll("\\\\0", String.valueOf('\0'));
        result = result.replaceAll("\\\\n", String.valueOf('\n'));

        return result;
    }


    /**
     * Changes the current state to represent start of parsing a unit with specified identifier.
     * @param identifierToken the unit's identifier
     */
    public void startUnit(Token identifierToken) {
        String identifier = this.getIdentifierFromToken(identifierToken);

        if (PascalLanguage.INSTANCE.isUnitRegistered(identifier)) {
            parser.SemErr("Unit with name " + identifier + " is already defined.");
            return;
        }

        UnitLexicalScope outerUnitScope = (this.units.size() > 0)? this.units.get(this.units.size() - 1) : null;
        UnitLexicalScope unitScope = new UnitLexicalScope(outerUnitScope, identifier, this.usingTPExtension);
        this.currentLexicalScope = unitScope;
        this.units.add(unitScope);
    }

    /**
     * Add new procedure to the currently parsed unit's lexical scope.
     * @param identifierToken identifier ot the procedure
     * @param formalParameters formal parameters of the procedure
     */
    public void addUnitProcedureInterface(Token identifierToken, List<FormalParameter> formalParameters) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        try {
            currentLexicalScope.forwardProcedure(identifier, formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    /**
     * Add new function to the currently parsed unit's lexical scope.
     * @param identifierToken identifier ot the function
     * @param formalParameters formal parameters of the function
     */
    public void addUnitFunctionInterface(Token identifierToken, List<FormalParameter> formalParameters, Token returnTypeToken) {
	    String identifier = this.getIdentifierFromToken(identifierToken);
	    TypeDescriptor returnTypeDescriptor = this.getTypeDescriptor(returnTypeToken);

	    try {
            currentLexicalScope.forwardFunction(identifier, formalParameters, returnTypeDescriptor);
        } catch (LexicalException e) {
	        parser.SemErr(e.getMessage());
        }
    }

    /**
     * Changes the current state to represent that the parser finished parsing of current unit's interface part.
     */
    public void finishUnitInterfaceSection() {
        ((UnitLexicalScope) this.currentLexicalScope).markAllIdentifiersPublic();
    }

    /**
     * Changes the current state to represent that tha parser started parsing implementation of a procedure of currently
     * parsed unit.
     * @param heading signature of the procedure
     */
    public void startUnitProcedureImplementation(ProcedureHeading heading) {
	    String identifier = this.getIdentifierFromToken(heading.identifierToken);
        try {
            if (currentLexicalScope.containsLocalIdentifier(identifier) && !currentLexicalScope.isSubroutine(identifier)) {
                parser.SemErr("Cannot implement. Not a procedure: " + identifier);
            } else if (!currentLexicalScope.containsLocalIdentifier(identifier)) {
                currentLexicalScope.forwardProcedure(identifier, heading.formalParameters);
            }
            currentLexicalScope = new LexicalScope(currentLexicalScope, identifier, parser.isUsingTPExtension());
            this.addParameterIdentifiersToLexicalScope(heading.formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    /**
     * Changes the current state to represent that tha parser started parsing implementation of a function of currently
     * parsed unit.
     * @param heading signature of the function
     */
    public void startUnitFunctionImplementation(FunctionHeading heading) {
	    String identifier = this.getIdentifierFromToken(heading.identifierToken);
        TypeDescriptor returnTypeDescriptor = heading.returnTypeDescriptor;

        try {
            if (currentLexicalScope.containsLocalIdentifier(identifier) && !currentLexicalScope.isSubroutine(identifier)) {
                parser.SemErr("Cannot implement. Not a function: " + identifier);
            } else if (!currentLexicalScope.containsLocalIdentifier(identifier)) {
                currentLexicalScope.forwardFunction(identifier, heading.formalParameters, returnTypeDescriptor);
            }
            currentLexicalScope = new LexicalScope(currentLexicalScope, identifier, parser.isUsingTPExtension());
            currentLexicalScope.registerReturnVariable(returnTypeDescriptor);
            this.addParameterIdentifiersToLexicalScope(heading.formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void finishUnit() {
    }

}
