package cz.cuni.mff.d3s.trupple.parser;

import java.util.*;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.sun.istack.internal.NotNull;
import cz.cuni.mff.d3s.trupple.language.builtinunits.*;
import cz.cuni.mff.d3s.trupple.language.builtinunits.graph.GraphBuiltinUnit;
import cz.cuni.mff.d3s.trupple.language.nodes.BlockNode;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.NopNode;
import cz.cuni.mff.d3s.trupple.language.nodes.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.*;
import cz.cuni.mff.d3s.trupple.language.nodes.call.InvokeNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReferenceInitializationNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.BreakNodeTP;
import cz.cuni.mff.d3s.trupple.language.nodes.control.CaseNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.ForNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.IfNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.RepeatNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.WhileNode;
import cz.cuni.mff.d3s.trupple.language.nodes.function.*;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.*;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.*;
import cz.cuni.mff.d3s.trupple.language.nodes.set.SymmetricDifferenceNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.*;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute.AccessNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute.SimpleAccessNode;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.UnknownIdentifierException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.UnknownTypeException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.UnknownDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.RecordDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.*;

public class NodeFactory {

    private interface GlobalObjectLookup<T> {

        T onFound(LexicalScope foundInScope, String foundIdentifier) throws LexicalException;

    }

    /**
     * The parser to be used for parsing. There can be two: parser for wirths' standard or turbo pascal standard.
     */
	private IParser parser;

    /**
     * Lexical scope of the main program excluding units. Holds information about registered identifiers and what type
     * are they assigned to.
     */
	private LexicalScope mainLexicalScope;

    /**
     * Current lexical scope.
     */
	private LexicalScope currentLexicalScope;

    /**
     * List of parsed units
     */
	private List<LexicalScope> units = new ArrayList<>();

    /**
     * List of all supported builtin units
     */
    private final Map<String, BuiltinUnit> builtinUnits = new HashMap<String, BuiltinUnit>(){{
        put("crt", new CrtBuiltinUnit());
        put("dos", new DosBuiltinUnit());
        put("string", new StringBuiltinUnit());
        put("graph", new GraphBuiltinUnit());
    }};

    /**
     * Flag that specifies whether the factory shall use TurboPascal specific nodes (when using TP standard) or not.
     */
	private final boolean usingTPExtension;

    /**
     * Specifies identifiers of arguments of the main program
     */
	private List<String> mainProgramArgumentsIdentifiers;

	public NodeFactory(IParser parser, boolean usingTPExtension) {
		this.parser = parser;
		this.usingTPExtension = usingTPExtension;

		this.mainLexicalScope = new LexicalScope(null, "_main", parser.isUsingTPExtension());
		this.currentLexicalScope = this.mainLexicalScope;
	}

	public void startPascal(Token identifierToken) {
		this.currentLexicalScope.setName(this.getIdentifierFromToken(identifierToken));
	}

	public void setMainProgramArguments(List<String> argumentIdentifiers) {
	    assert currentLexicalScope.getOuterScope() == null;
	    this.mainProgramArgumentsIdentifiers = argumentIdentifiers;
    }

    public void registerUnit(Token unitIdentifierToken) {
        String unitIdentifier = this.getIdentifierFromToken(unitIdentifierToken);
        if (this.builtinUnits.containsKey(unitIdentifier)) {
            BuiltinUnit builtinUnit = this.builtinUnits.get(unitIdentifier);
            builtinUnit.importTo(this.currentLexicalScope);
        } else {
            this.importUnitScope(unitIdentifier);
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

    private boolean containsUnit(String identifier) {
	    for (LexicalScope unitScope : this.units) {
	        if (unitScope.getName().equals(identifier)) {
	            return true;
            }
        }

        return false;
    }

    private void importUnitScope(String identifier) {
	    LexicalScope scope = this.getUnitScope(identifier);
	    if (scope == null) {
            parser.SemErr("Unknown unit: " + identifier + ". Did you forget to include it?");
            return;
        }

        this.currentLexicalScope.importUnitScope(scope);
    }

    private LexicalScope getUnitScope(String identifier) {
	    for (LexicalScope unitScope : this.units) {
	        if (unitScope.getName().equals(identifier)) {
	            return unitScope;
            }
        }

        return null;
    }

    private <T> T doLookup(String identifier, GlobalObjectLookup<T> lookupFunction, @NotNull LexicalException notFoundException, T notFoundReturnValue) {
	    assert notFoundException != null;

	    try {
            T result = lookupToParentScope(this.currentLexicalScope, identifier, lookupFunction);
            if (result == null) {
                result = lookupInUnits(identifier, lookupFunction);
            }
            if (result == null) {
                throw notFoundException;
            }
            return result;
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
            return notFoundReturnValue;
        }
    }

    private <T> T doLookup(String identifier, GlobalObjectLookup<T> lookupFunction, @NotNull LexicalException notFoundException) {
	    return this.doLookup(identifier, lookupFunction, notFoundException, null);
    }

    private <T> T lookupToParentScope(LexicalScope scope, String identifier, GlobalObjectLookup<T> lookupFunction) throws LexicalException {
        while (scope != null) {
            if (scope.containsLocalIdentifier(identifier)){
                return lookupFunction.onFound(scope, identifier);
            } else {
                scope = scope.getOuterScope();
            }
        }

        return null;
    }

    private <T> T lookupInUnits(String identifier, GlobalObjectLookup<T> lookupFunction) throws LexicalException {
        T result = null;

        for (LexicalScope unitScope : this.units) {
            result = lookupToParentScope(unitScope, identifier, lookupFunction);
            if (result != null) {
                break;
            }
        }

        return result;
    }

    public TypeDescriptor getTypeDescriptor(Token identifierToken) {
        String identifier = this.getTypeNameFromToken(identifierToken);
        return this.doLookup(identifier, LexicalScope::getTypeDescriptor, new UnknownTypeException(identifier), UnknownDescriptor.SINGLETON);
    }

    public void registerVariables(List<String> identifiers, TypeDescriptor typeDescriptor) {
        for (String identifier : identifiers) {
            try {
                currentLexicalScope.registerLocalVariable(identifier, typeDescriptor);
            } catch (LexicalException e) {
                parser.SemErr(e.getMessage());
            }
        }
    }

    public TypeDescriptor createArray(List<OrdinalDescriptor> ordinalDimensions, Token returnTypeToken) {
	    String typeIdentifier = this.getTypeNameFromToken(returnTypeToken);
	    TypeDescriptor returnTypeDescriptor = this.doLookup(typeIdentifier, LexicalScope::getTypeDescriptor, new UnknownTypeException(typeIdentifier));

	    return currentLexicalScope.createArrayType(ordinalDimensions, returnTypeDescriptor);
    }

    public TypeDescriptor createSetType(OrdinalDescriptor baseType) {
        return currentLexicalScope.createSetType(baseType);
    }

    public TypeDescriptor createFileType(TypeDescriptor contentTypeDescriptor) {
        return this.currentLexicalScope.createFileDescriptor(contentTypeDescriptor);
    }

    public void startRecord() {
	    this.currentLexicalScope = new LexicalScope(this.currentLexicalScope, "_record", this.usingTPExtension);
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

    public ConstantDescriptor createLongConstant(String sign, long value) {
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
            ConstantDescriptor constant = this.currentLexicalScope.getConstant(identifier);
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
	        // TODO: return some default constant
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

    public void forwardProcedure(Token identifierToken, List<FormalParameter> formalParameters) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        try {
            currentLexicalScope.registerProcedureInterface(identifier, formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void forwardFunction(Token identifierToken, List<FormalParameter> formalParameters, Token returnTypeToken) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        String returnType = this.getIdentifierFromToken(returnTypeToken);
        try {
            currentLexicalScope.registerFunctionInterface(identifier, formalParameters, returnType);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void startProcedureImplementation(Token identifierToken, List<FormalParameter> formalParameters) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        try {
            currentLexicalScope.tryRegisterProcedureInterface(identifier, formalParameters);
            currentLexicalScope = new LexicalScope(currentLexicalScope, identifier, parser.isUsingTPExtension());
            this.addParameterIdentifiersToLexicalScope(formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
	}

    public void startFunctionImplementation(Token identifierToken, List<FormalParameter> formalParameters, Token returnTypeToken) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        String returnType = this.getIdentifierFromToken(returnTypeToken);
        try {
            currentLexicalScope.tryRegisterFunctionInterface(identifier, formalParameters, returnType);
            currentLexicalScope = new LexicalScope(currentLexicalScope, identifier, parser.isUsingTPExtension());
            currentLexicalScope.registerReturnType(formalParameters, returnType);
            this.addParameterIdentifiersToLexicalScope(formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void appendFormalParameter(List<FormalParameter> parameter, List<FormalParameter> params) {
        params.addAll(parameter);
    }

    public List<FormalParameter> createFormalParametersList(List<String> identifiers, String typeName, boolean isOutput) {
        List<FormalParameter> paramList = new ArrayList<>();
        for (String identifier : identifiers) {
            paramList.add(new FormalParameter(identifier, typeName, isOutput));
        }

        return paramList;
    }

    public void finishProcedureImplementation(StatementNode bodyNode) {
        StatementNode subroutineNode = createSubroutineNode(bodyNode);
        final ProcedureBodyNode procedureBodyNode = new ProcedureBodyNode(subroutineNode);
        finishSubroutine(procedureBodyNode);
    }

    public void finishFunctionImplementation(StatementNode bodyNode) {
        StatementNode subroutineNode = createSubroutineNode(bodyNode);
        final FunctionBodyNode functionBodyNode = FunctionBodyNodeGen.create(subroutineNode, currentLexicalScope.getReturnSlot());
        finishSubroutine(functionBodyNode);
    }

    public void startLoop() {
        currentLexicalScope.increaseLoopDepth();
    }

    public StatementNode createForLoop(boolean ascending, Token variableToken, ExpressionNode startValue, ExpressionNode finalValue, StatementNode loopBody) {
        String iteratingIdentifier = this.getIdentifierFromToken(variableToken);
        FrameSlot iteratingSlot = currentLexicalScope.getLocalSlot(iteratingIdentifier);
        if (iteratingSlot == null) {
            parser.SemErr("Unknown identifier: " + iteratingIdentifier);
        }
        return new ForNode(ascending, iteratingSlot, startValue, finalValue, loopBody);
    }

    public StatementNode createRepeatLoop(ExpressionNode condition, StatementNode loopBody) {
        return new RepeatNode(condition, loopBody);
    }

    public StatementNode createWhileLoop(ExpressionNode condition, StatementNode loopBody) {
        return new WhileNode(condition, loopBody);
    }

    public StatementNode createBreak() {
        if (!currentLexicalScope.isInLoop()) {
            parser.SemErr("Break outside a loop: ");
        }
        return new BreakNodeTP();
    }

    public void finishLoop() {
        try {
            currentLexicalScope.decreaseLoopDepth();
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public StatementNode createIfStatement(ExpressionNode condition, StatementNode thenNode, StatementNode elseNode) {
        return new IfNode(condition, thenNode, elseNode);
    }

    public List<FrameSlot> stepIntoRecordsScope(List<String> recordIdentifiers) {
	    LexicalScope currentScope = this.getScope();
        List<FrameSlot> recordsSlots = new ArrayList<>();

        // TODO CRITICAL: a lookup function should be used for the first identifier
	    for (String recordIdentifier : recordIdentifiers) {
	        TypeDescriptor recordDescriptor = this.currentLexicalScope.getIdentifierDescriptor(recordIdentifier);
            if (recordDescriptor == null || !(recordDescriptor instanceof RecordDescriptor)) {
                parser.SemErr("Not a record: " + recordIdentifier);
                this.currentLexicalScope = currentScope;
                return Collections.emptyList();
            } else {
                recordsSlots.add(this.currentLexicalScope.getLocalSlot(recordIdentifier));
                this.currentLexicalScope = ((RecordDescriptor) recordDescriptor).getLexicalScope();
            }
        }

        return recordsSlots;
    }

    public WithNode createWithStatement(List<FrameSlot> frameSlots, StatementNode innerStatement) {
	    return new WithNode(frameSlots, innerStatement);
    }

    public CaseNode createCaseStatement(CaseStatementData data) {
        ExpressionNode[] indexes = data.indexNodes.toArray(new ExpressionNode[data.indexNodes.size()]);
        StatementNode[] statements = data.statementNodes.toArray(new StatementNode[data.statementNodes.size()]);

        return new CaseNode(data.caseExpression, indexes, statements, data.elseNode);
    }

    public StatementNode createNopStatement() {
        return new NopNode();
    }

    public ExpressionNode createBinaryExpression(Token operator, ExpressionNode leftNode, ExpressionNode rightNode) {
        switch (operator.val.toLowerCase()) {

            // arithmetic
            case "+":
                return (usingTPExtension)? AddNodeTPNodeGen.create(leftNode, rightNode) : AddNodeGen.create(leftNode, rightNode);
            case "-":
                return SubstractNodeGen.create(leftNode, rightNode);
            case "*":
                return MultiplyNodeGen.create(leftNode, rightNode);
            case "/":
                return DivideNodeGen.create(leftNode, rightNode);
            case "div":
                return DivideIntegerNodeGen.create(leftNode, rightNode);
            case "mod":
                return ModuloNodeGen.create(leftNode, rightNode);

            // logic
            case "and":
                return AndNodeGen.create(leftNode, rightNode);
            case "or":
                return OrNodeGen.create(leftNode, rightNode);

            case "<":
                return LessThanNodeGen.create(leftNode, rightNode);
            case "<=":
                return LessThanOrEqualNodeGen.create(leftNode, rightNode);
            case ">":
                return NotNodeGen.create(LessThanOrEqualNodeGen.create(leftNode, rightNode));
            case ">=":
                return NotNodeGen.create(LessThanNodeGen.create(leftNode, rightNode));
            case "=":
                return EqualsNodeGen.create(leftNode, rightNode);
            case "<>":
                return NotNodeGen.create(EqualsNodeGen.create(leftNode, rightNode));
            case "in":
                return InNodeGen.create(leftNode, rightNode);
            case "><":
                return SymmetricDifferenceNodeGen.create(leftNode, rightNode);

            default:
                parser.SemErr("Unknown binary operator: " + operator.val);
                return null;
        }
    }

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

    public AccessNode createSimpleAccessNode(Token identifierToken) {
	    String identifier = this.getIdentifierFromToken(identifierToken);
	    FrameSlot frameSlot = this.doLookup(identifier, LexicalScope::getLocalSlot, new UnknownIdentifierException(identifier));

	    return new SimpleAccessNode(frameSlot);
    }

    public ExpressionNode createAssignmentWithRoute(Token identifierToken, AccessNode accessNode, ExpressionNode valueNode) {
        String variableIdentifier = this.getIdentifierFromToken(identifierToken);
        FrameSlot frameSlot = this.doLookup(variableIdentifier, LexicalScope::getLocalSlot, new UnknownIdentifierException(variableIdentifier));

        // TODO: check if it is assignable
        return AssignmentNodeWithRouteNodeGen.create(accessNode, valueNode, frameSlot);
    }

    public ExpressionNode createExpressionFromSingleIdentifier(Token identifierToken) {
        String identifier = this.getIdentifierFromToken(identifierToken);

        return this.doLookup(identifier, (LexicalScope foundInLexicalScope, String foundIdentifier) -> {
            if (foundInLexicalScope.isParameterlessSubroutine(foundIdentifier)) {
                return this.createInvokeNode(foundInLexicalScope, foundIdentifier, Collections.emptyList());
            }
            else {
                // TODO: check if it is a constant or a variable
                return ReadVariableNodeGen.create(foundInLexicalScope.getLocalSlot(foundIdentifier));
            }
        }, new UnknownIdentifierException(identifier));
    }

    public ExpressionNode createExpressionFromIdentifierWithRoute(AccessNode accessNode) {
        return new ReadVariableWithRouteNode(accessNode);
    }

    public boolean shouldBeReference(Token subroutineToken, int parameterIndex) {
	    String subroutineIdentifier = this.getIdentifierFromToken(subroutineToken);
	    try {
	        LexicalScope ls = this.currentLexicalScope;
	        while(ls != null) {
	            if (ls.containsLocalIdentifier(subroutineIdentifier)) {
                    return ls.isReferenceParameter(subroutineIdentifier, parameterIndex);
                }
                ls = ls.getOuterScope();
            }
            return false;
        } catch(LexicalException e) {
	        parser.SemErr(e.getMessage());
	        return false;
        }
    }

    public ExpressionNode createSubroutineCall(Token identifierToken, List<ExpressionNode> params) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        return this.doLookup(identifier, (LexicalScope foundInScope, String foundIdentifier) -> {
            if (foundInScope.isSubroutine(foundIdentifier)) {
                foundInScope.verifyPassedArgumentsToSubroutine(foundIdentifier, params);
                return this.createInvokeNode(foundInScope, foundIdentifier, params);
            } else {
                throw new LexicalException(foundIdentifier + " is not a subroutine.");
            }
        }, new UnknownIdentifierException(identifier));
    }

    private ExpressionNode createInvokeNode(LexicalScope inScope, String identifier, List<ExpressionNode> params) {
        PascalContext context = inScope.getContext();
        ExpressionNode literal = new FunctionLiteralNode(context, identifier);
        return InvokeNodeGen.create(params.toArray(new ExpressionNode[params.size()]), literal);
    }

    public ExpressionNode createReferenceNode(Token variableToken) {
	    String variableIdentifier = this.getIdentifierFromToken(variableToken);
	    FrameSlot slot = this.currentLexicalScope.getLocalSlot(variableIdentifier);
        return new ReadReferencePassNode(slot);
    }

    public ExpressionNode createLogicLiteralNode(boolean value) {
        return new LogicLiteralNode(value);
    }

    public ExpressionNode createSetConstructorNode(List<ExpressionNode> valueNodes) {
	    return new SetConstructorNode(valueNodes);
    }

    public ExpressionNode createNumericLiteralNode(Token literalToken) {
        return new LongLiteralNode(getLongFromToken(literalToken));
    }

    public ExpressionNode createFloatLiteralNode(Token token) {
        return new DoubleLiteralNode(getDoubleFromToken(token));
    }

    public ExpressionNode createCharOrStringLiteralNode(String literal) {
        return (literal.length() == 1) ? new CharLiteralNode(literal.charAt(0)) : new StringLiteralNode(literal);
    }

    public BlockNode createBlockNode(List<StatementNode> bodyNodes) {
        return new BlockNode(bodyNodes.toArray(new StatementNode[bodyNodes.size()]));
    }

    // TODO: this main node can be in lexical scope instead of a parser
    public PascalRootNode finishMainFunction(BlockNode blockNode) {
	    this.addProgramArgumentsAssignmentNodes();
        StatementNode bodyNode = this.createSubroutineNode(blockNode);
        return new PascalRootNode(currentLexicalScope.getFrameDescriptor(), new ProcedureBodyNode(bodyNode));
    }

    private List<StatementNode> addProgramArgumentsAssignmentNodes() {
	    List<StatementNode> nodes = new ArrayList<>();
	    int currentArgument = 0;
	    for (String argumentIdentifier : this.mainProgramArgumentsIdentifiers) {
	        FrameSlot argumentSlot = this.currentLexicalScope.getLocalSlot(argumentIdentifier);
	        TypeDescriptor typeDescriptor = this.currentLexicalScope.getIdentifierDescriptor(argumentIdentifier);

	        if (typeDescriptor != null) {
                if (ProgramArgumentAssignmentNode.supportsType(typeDescriptor)) {
                    this.currentLexicalScope.addScopeArgument(new ProgramArgumentAssignmentNode(argumentSlot, typeDescriptor, currentArgument++));
                }
                // TODO: else -> show warning
            }
        }

        return nodes;
    }

    public String createStringFromToken(Token t) {
        String literal = t.val;
        literal = literal.substring(1, literal.length() - 1);
        literal = literal.replaceAll("''", "'");
        return literal;
    }

    public String getTypeNameFromToken(Token typeNameToken) {
        return typeNameToken.val.toLowerCase();
    }

    private StatementNode createSubroutineNode(StatementNode bodyNode) {
        List<StatementNode> subroutineNodes = new ArrayList<>();

        subroutineNodes.add(currentLexicalScope.createInitializationNode());
        subroutineNodes.add(bodyNode);

        return new BlockNode(subroutineNodes.toArray(new StatementNode[subroutineNodes.size()]));
    }

    private void addParameterIdentifiersToLexicalScope(List<FormalParameter> parameters) {
        try {
            int count = 0;
            for (FormalParameter parameter : parameters) {
                TypeDescriptor typeDescriptor = this.doLookup(parameter.type, LexicalScope::getTypeTypeDescriptor,
                        new UnknownTypeException(parameter.identifier));

                if (typeDescriptor == null)
                    return;

                if (parameter.isReference) {
                    FrameSlot frameSlot = this.currentLexicalScope.registerReferenceVariable(parameter.identifier, typeDescriptor);
                    final ReferenceInitializationNode initializationNode = new ReferenceInitializationNode(frameSlot, count++);

                    this.currentLexicalScope.addScopeArgument(initializationNode);
                } else {
                    FrameSlot frameSlot = this.currentLexicalScope.registerLocalVariable(parameter.identifier, typeDescriptor);
                    FrameSlotKind slotKind = this.currentLexicalScope.getSlotKind(parameter.identifier);
                    final ExpressionNode readNode = ReadSubroutineArgumentNodeGen.create(count++, slotKind);
                    final AssignmentNode assignment = AssignmentNodeGen.create(readNode, frameSlot);

                    this.currentLexicalScope.addScopeArgument(assignment);
                }
            }
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    private void finishSubroutine(ExpressionNode subroutineBodyNode) {
        final PascalRootNode rootNode = new PascalRootNode(currentLexicalScope.getFrameDescriptor(), subroutineBodyNode);

        String subroutineIdentifier = currentLexicalScope.getName();
        currentLexicalScope = currentLexicalScope.getOuterScope();
        currentLexicalScope.getContext().setSubroutineRootNode(subroutineIdentifier, rootNode);
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

    public LexicalScope getScope() {
	    return this.currentLexicalScope;
    }

    public void setScope(LexicalScope scope) {
	    this.currentLexicalScope = scope;
    }


    // ***************************************************
    // UNIT PART
    // ***************************************************
    public void startUnit(Token identifierToken) {
        String identifier = this.getIdentifierFromToken(identifierToken);

        if (this.containsUnit(identifier)) {
            parser.SemErr("Unit with name " + identifier + " is already defined.");
            return;
        }

        this.currentLexicalScope = new LexicalScope(null, identifier, this.usingTPExtension);
        this.units.add(this.currentLexicalScope);
    }

    public void addUnitProcedureInterface(Token identifierToken, List<FormalParameter> formalParameters) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        try {
            currentLexicalScope.registerProcedureInterface(identifier, formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void addUnitFunctionInterface(Token identifierToken, List<FormalParameter> formalParameters, String returnTypeName) {
	    String identifier = this.getIdentifierFromToken(identifierToken);
	    try {
            currentLexicalScope.registerFunctionInterface(identifier, formalParameters, returnTypeName);
        } catch (LexicalException e) {
	        parser.SemErr(e.getMessage());
        }
    }

    public void finishUnitInterfaceSection() {
	    assert !this.currentLexicalScope.equals(this.mainLexicalScope);
	    this.currentLexicalScope.markAllIdentifiersPublic();
    }

    public void startUnitProcedureImplementation(Token identifierToken, List<FormalParameter> formalParameters) {
	    String identifier = this.getIdentifierFromToken(identifierToken);
        try {
            if (currentLexicalScope.containsLocalIdentifier(identifier) && !currentLexicalScope.isSubroutine(identifier)) {
                parser.SemErr("Cannot implement. Not a procedure: " + identifier);
            } else if (!currentLexicalScope.containsLocalIdentifier(identifier)) {
                currentLexicalScope.registerProcedureInterface(identifier, formalParameters);
            }
            currentLexicalScope = new LexicalScope(currentLexicalScope, identifier, parser.isUsingTPExtension());
            this.addParameterIdentifiersToLexicalScope(formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void startUnitFunctionImplementation(Token identifierToken, List<FormalParameter> formalParameters, Token returnTypeToken) {
	    String identifier = this.getIdentifierFromToken(identifierToken);
        String returnTypeName = this.getTypeNameFromToken(returnTypeToken);

        try {
            if (currentLexicalScope.containsLocalIdentifier(identifier) && !currentLexicalScope.isSubroutine(identifier)) {
                parser.SemErr("Cannot implement. Not a function: " + identifier);
            } else if (!currentLexicalScope.containsLocalIdentifier(identifier)) {
                currentLexicalScope.registerFunctionInterface(identifier, formalParameters, returnTypeName);
            }
            currentLexicalScope = new LexicalScope(currentLexicalScope, identifier, parser.isUsingTPExtension());
            currentLexicalScope.registerReturnType(formalParameters, returnTypeName);
            this.addParameterIdentifiersToLexicalScope(formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void endUnit() {
	    this.currentLexicalScope = this.mainLexicalScope;
    }

}
