package cz.cuni.mff.d3s.trupple.parser;

import java.util.*;

import com.oracle.truffle.api.frame.FrameSlot;
import cz.cuni.mff.d3s.trupple.language.PascalLanguage;
import cz.cuni.mff.d3s.trupple.language.builtinunits.*;
import cz.cuni.mff.d3s.trupple.language.builtinunits.dos.DosBuiltinUnit;
import cz.cuni.mff.d3s.trupple.language.builtinunits.crt.CrtBuiltinUnit;
import cz.cuni.mff.d3s.trupple.language.builtinunits.strings.StringBuiltinUnit;
import cz.cuni.mff.d3s.trupple.language.nodes.*;
import cz.cuni.mff.d3s.trupple.language.builtinunits.graph.GraphBuiltinUnit;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.*;
import cz.cuni.mff.d3s.trupple.language.nodes.call.*;
import cz.cuni.mff.d3s.trupple.language.nodes.control.*;
import cz.cuni.mff.d3s.trupple.language.nodes.function.*;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.*;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.*;
import cz.cuni.mff.d3s.trupple.language.nodes.root.FunctionPascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.root.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.root.ProcedurePascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.set.SymmetricDifferenceNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.*;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.*;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.ReadDereferenceNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute.*;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;
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
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;

public class NodeFactory {

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

	public void setMainProgramArguments(List<String> argumentIdentifiers) {
	    assert currentLexicalScope.getOuterScope() == null;
	    this.mainProgramArgumentsIdentifiers = argumentIdentifiers;
    }

    public void registerUnit(Token unitIdentifierToken) {
        String unitIdentifier = this.getIdentifierFromToken(unitIdentifierToken);  // TODO: can't use getIdentifierFromToken() here
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
	    return PascalLanguage.INSTANCE.findContext().isUnitRegistered(identifier);
    }

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

    private <T> T doLookup(String identifier, GlobalObjectLookup<T> lookupFunction) {
	    return this.doLookup(identifier, lookupFunction, false);
    }

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

    public void forwardProcedure(ProcedureHeading heading) {
        String identifier = this.getIdentifierFromToken(heading.identifierToken);
        try {
            currentLexicalScope.forwardProcedure(identifier, heading.formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void forwardFunction(FunctionHeading heading) {
        String identifier = this.getIdentifierFromToken(heading.identifierToken);

        try {
            currentLexicalScope.forwardFunction(identifier, heading.formalParameters, heading.returnTypeDescriptor);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

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

    public void startFunctionImplementation(FunctionHeading heading) {
        String identifier = this.getIdentifierFromToken(heading.identifierToken);

        try {
            currentLexicalScope.registerFunctionInterfaceIfNotForwarded(identifier, heading.formalParameters, heading.returnTypeDescriptor);
            currentLexicalScope = new LexicalScope(currentLexicalScope, identifier, parser.isUsingTPExtension());
            currentLexicalScope.registerReturnVariable(currentLexicalScope.getName(), heading.returnTypeDescriptor);
            this.addParameterIdentifiersToLexicalScope(heading.formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
            currentLexicalScope = new LexicalScope(currentLexicalScope, identifier, parser.isUsingTPExtension());
        }
    }

    public List<FormalParameter> createFormalParametersList(List<String> identifiers, Token typeDescriptor, boolean isOutput) {
        List<FormalParameter> paramList = new ArrayList<>();
        for (String identifier : identifiers) {
            paramList.add(new FormalParameter(identifier, this.getTypeDescriptor(typeDescriptor), isOutput));
        }

        return paramList;
    }

    public FormalParameter createProcedureFormalParameter(ProcedureHeading heading) {
        String identifier = this.getIdentifierFromToken(heading.identifierToken);
        ProcedureDescriptor descriptor = new ProcedureDescriptor(heading.formalParameters);

        return new FormalParameter(identifier, descriptor, false, heading.descriptor);
    }

    public FormalParameter createFunctionFormalParameter(FunctionHeading heading) {
        String identifier = this.getIdentifierFromToken(heading.identifierToken);
        FunctionDescriptor descriptor = new FunctionDescriptor(heading.formalParameters, heading.returnTypeDescriptor);

        return new FormalParameter(identifier, descriptor, false, heading.descriptor);
    }

    public void finishProcedureImplementation(StatementNode bodyNode) {
        StatementNode subroutineNode = createSubroutineNode(bodyNode);
        final ProcedureBodyNode procedureBodyNode = new ProcedureBodyNode(subroutineNode);
        final PascalRootNode rootNode = new ProcedurePascalRootNode(currentLexicalScope.getFrameDescriptor(), procedureBodyNode);
        finishSubroutine(rootNode);
    }

    public void finishFunctionImplementation(StatementNode bodyNode) {
        StatementNode subroutineNode = createSubroutineNode(bodyNode);
        final FunctionBodyNode functionBodyNode = FunctionBodyNodeGen.create(
                subroutineNode,
                currentLexicalScope.getReturnSlot(),
                currentLexicalScope.getIdentifierDescriptor(currentLexicalScope.getName()));
        final PascalRootNode rootNode = new FunctionPascalRootNode(currentLexicalScope.getFrameDescriptor(), functionBodyNode);
        finishSubroutine(rootNode);
    }

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

    public StatementNode createForLoop(boolean ascending, Token variableToken, ExpressionNode startValue, ExpressionNode finalValue, StatementNode loopBody) {
        String iteratingIdentifier = this.getIdentifierFromToken(variableToken);
        FrameSlot controlSlot = this.doLookup(iteratingIdentifier, LexicalScope::getLocalSlot);
        TypeDescriptor controlSlotType = currentLexicalScope.getIdentifierDescriptor(iteratingIdentifier);
        if (controlSlot == null) {
            parser.SemErr("Unknown identifier: " + iteratingIdentifier);
        }
        if (startValue.getType() != finalValue.getType() && !startValue.getType().convertibleTo(finalValue.getType())) {
            parser.SemErr("Type mismatch in beginning and last value of for loop.");
        }
        AssignmentNode initialAssignment = this.createAssignmentNode(iteratingIdentifier, startValue);
        return new ForNode(ascending, initialAssignment, controlSlot, controlSlotType, finalValue, startValue, loopBody);
    }

    public StatementNode createRepeatLoop(ExpressionNode condition, StatementNode loopBody) {
        if (!(condition.getType() == BooleanDescriptor.getInstance())) {
            parser.SemErr("Unless condition must be a boolean value");
        }
        return new RepeatNode(condition, loopBody);
    }

    public StatementNode createWhileLoop(ExpressionNode condition, StatementNode loopBody) {
        if (!(condition.getType() == BooleanDescriptor.getInstance())) {
            parser.SemErr("While condition must be a boolean value");
        }
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
        if (!(condition.getType() == BooleanDescriptor.getInstance())) {
            parser.SemErr("If condition must be a boolean value");
        }
	    return new IfNode(condition, thenNode, elseNode);
    }

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

    public WithNode createWithStatement(List<FrameSlot> frameSlots, StatementNode innerStatement) {
	    return new WithNode(frameSlots, innerStatement);
    }

    public CaseNode createCaseStatement(CaseStatementData data) {
        ExpressionNode[] indexes = data.indexNodes.toArray(new ExpressionNode[data.indexNodes.size()]);
        StatementNode[] statements = data.statementNodes.toArray(new StatementNode[data.statementNodes.size()]);

        return new CaseNode(data.caseExpression, indexes, statements, data.elseNode);
    }

    public StatementNode createGotoStatement(Token labelToken) {
	    String labelIdentifier = this.getIdentifierFromToken(labelToken);
	    return new GotoNode(labelIdentifier);
    }

    public StatementNode createNopStatement() {
        return new NopNode();
    }

    public ExpressionNode createBinaryExpression(Token operatorToken, ExpressionNode leftNode, ExpressionNode rightNode) {
        String operator = operatorToken.val.toLowerCase();
        ExpressionNode resultExpression;
	    switch (operator) {

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
	    FrameSlot frameSlot = this.doLookup(identifier, LexicalScope::getLocalSlot, true);
        TypeDescriptor typeDescriptor = this.doLookup(identifier, LexicalScope::getIdentifierDescriptor);
        if (typeDescriptor instanceof ReferenceDescriptor) {
            typeDescriptor = ((ReferenceDescriptor) typeDescriptor).getReferencedType();
        }

	    return new SimpleAccessNode(frameSlot, typeDescriptor);
    }

    public StatementNode createAssignmentNode(Token identifierToken, AccessNode accessNode, ExpressionNode valueNode) {
        String variableIdentifier = this.getIdentifierFromToken(identifierToken);
        FrameSlot frameSlot = this.doLookup(variableIdentifier, LexicalScope::getLocalSlot, true);
        this.checkTypesAreCompatible(valueNode.getType(), accessNode.getType());
        TypeDescriptor targetType = this.doLookup(variableIdentifier, LexicalScope::getIdentifierDescriptor, true);

        if (accessNode instanceof SimpleAccessNode) {
            return (targetType instanceof ReferenceDescriptor)? AssignReferenceNodeGen.create(valueNode, frameSlot) : AssignmentNodeGen.create(valueNode, frameSlot);
        } else {
            return AssignmentNodeWithRouteNodeGen.create(accessNode, valueNode, frameSlot);
        }
    }

    public ExpressionNode createExpressionFromSingleIdentifier(Token identifierToken) {
        String identifier = this.getIdentifierFromToken(identifierToken);

        return this.doLookup(identifier, (LexicalScope foundInLexicalScope, String foundIdentifier) -> {
            if (foundInLexicalScope.isParameterlessSubroutine(foundIdentifier)) {
                SubroutineDescriptor descriptor = (SubroutineDescriptor) foundInLexicalScope.getIdentifierDescriptor(foundIdentifier);
                return this.createInvokeNode(foundIdentifier, descriptor, foundInLexicalScope, Collections.emptyList());
            }
            else {
                // TODO: check if it is a constant or a variable
                boolean isReference = foundInLexicalScope.getIdentifierDescriptor(foundIdentifier) instanceof ReferenceDescriptor;
                return ReadVariableNodeGen.create(foundInLexicalScope.getLocalSlot(foundIdentifier), foundInLexicalScope.getIdentifierDescriptor(foundIdentifier), isReference);
            }
        });
    }

    public PointerDereference createPointerDereferenceAccessNode(AccessNode previousAccessNode) {
	    TypeDescriptor dereferencedType = previousAccessNode.getType();
	    if (!(dereferencedType instanceof PointerDescriptor)) {
	        parser.SemErr("Can not dereference variable of this type");
	        return new PointerDereference(previousAccessNode, dereferencedType);
        } else {
            return new PointerDereference(previousAccessNode, ((PointerDescriptor) dereferencedType).getInnerTypeDescriptor());
        }
    }

    public RecordAccessNode createRecordAccessNode(AccessNode previousAccessNode, Token accessVariableToken) {
	    TypeDescriptor accessedVariableDescriptor = previousAccessNode.getType();
        String variableIdentifier = this.getIdentifierFromToken(accessVariableToken);
	    if (!(accessedVariableDescriptor instanceof RecordDescriptor)) {
            parser.SemErr("Cannot access non record type this way");
            return new RecordAccessNode(previousAccessNode, variableIdentifier, accessedVariableDescriptor);
        } else {
	        RecordDescriptor accessedRecordDescriptor = (RecordDescriptor) accessedVariableDescriptor;
	        if (!accessedRecordDescriptor.containsIdentifier(variableIdentifier)) {
                parser.SemErr("Cannot access non record type this way");
                return new RecordAccessNode(previousAccessNode, variableIdentifier, accessedVariableDescriptor);
            } else {
	            TypeDescriptor accessedVariableType = accessedRecordDescriptor.getLexicalScope().getIdentifierDescriptor(variableIdentifier);
                return new RecordAccessNode(previousAccessNode, variableIdentifier, accessedVariableType);
            }
        }
    }

    public ExpressionNode createReadFromArrayNode(ExpressionNode arrayExpression, List<ExpressionNode> indexes) {
	    TypeDescriptor returnDescriptor = arrayExpression.getType();
	    ExpressionNode result = arrayExpression;
	    for (ExpressionNode index : indexes) {
            if (!(returnDescriptor instanceof ArrayDescriptor)) {
                parser.SemErr("Not an array");
                break;
            }
            returnDescriptor = ((ArrayDescriptor) returnDescriptor).getOneStepInnerDescriptor();
            result = ReadFromArrayNodeGen.create(result, index, returnDescriptor);
        }
        return result;
    }

    public ReadDereferenceNode createReadDereferenceNode(ExpressionNode pointerExpression) {
        TypeDescriptor returnType = null;
	    if (!(pointerExpression.getType() instanceof PointerDescriptor)) {
            parser.SemErr("Can not dereference this type");
        } else {
	        returnType = ((PointerDescriptor) pointerExpression.getType()).getInnerTypeDescriptor();
        }

        return ReadDereferenceNodeGen.create(pointerExpression, returnType);
    }

    public ReadFromRecordNode createReadFromRecordNode(ExpressionNode recordExpression, Token identifierToken) {
        TypeDescriptor descriptor = recordExpression.getType();
        String identifier = this.getIdentifierFromToken(identifierToken);
        TypeDescriptor returnType = null;

        if (!(descriptor instanceof RecordDescriptor)) {
            parser.SemErr("Cannot access non record type this way");
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

    public ArrayAccessNode createArrayAccessNode(AccessNode previousAccessNode, List<ExpressionNode> indexNodes) {
        TypeDescriptor accessedVariableDescriptor = previousAccessNode.getType();

        for (int i = 0; i< indexNodes.size(); ++i) {
            if (!(accessedVariableDescriptor instanceof ArrayDescriptor)) {
                parser.SemErr("Cannot index this type");
                break;
            } else {
                ArrayDescriptor accessingArrayDescriptor = (ArrayDescriptor) accessedVariableDescriptor;
                accessedVariableDescriptor = accessingArrayDescriptor.getOneStepInnerDescriptor();
            }
        }

	    return new ArrayAccessNode(previousAccessNode, indexNodes, accessedVariableDescriptor);
    }

    public boolean shouldBeReference(Token subroutineToken, int parameterIndex) {
        String subroutineIdentifier = this.getIdentifierFromToken(subroutineToken);
        return this.doLookup(subroutineIdentifier,
                (LexicalScope foundInScope, String foundIdentifier) -> foundInScope.isReferenceParameter(foundIdentifier, parameterIndex));
    }

    public boolean shouldBeSubroutine(Token subroutineToken, int parameterIndex) {
        String subroutineIdentifier = this.getIdentifierFromToken(subroutineToken);
        return this.doLookup(subroutineIdentifier,
                (LexicalScope foundInScope, String foundIdentifier) -> foundInScope.isSubroutineParameter(foundIdentifier, parameterIndex));
    }

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
            return ContextInvokeNodeGen.create(identifier, unitIdentifier, arguments, returnType);
        } else {
	        FrameSlot subroutineSlot = subroutineScope.getLocalSlot(identifier);
	        return InvokeNodeGen.create(subroutineSlot, arguments, returnType);
        }
    }

    public ExpressionNode createReferencePassNode(Token variableToken) {
        String variableIdentifier = this.getIdentifierFromToken(variableToken);
        FrameSlot slot = this.doLookup(variableIdentifier, LexicalScope::getLocalSlot);
        return new StoreReferenceArgumentNode(slot, currentLexicalScope.getIdentifierDescriptor(variableIdentifier));
    }

    public ExpressionNode createSubroutineParameterPassNode(Token variableToken) {
        String variableIdentifier = this.getIdentifierFromToken(variableToken);
        PascalSubroutine function = this.doLookup(variableIdentifier, LexicalScope::getSubroutine);
        TypeDescriptor descriptor = this.doLookup(variableIdentifier, LexicalScope::getIdentifierDescriptor);

        return new StoreSubroutineArgumentNode(function, (SubroutineDescriptor) descriptor);
    }

    public ExpressionNode createLogicLiteralNode(boolean value) {
        return new LogicLiteralNode(value);
    }

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
        return new LongLiteralNode(getLongFromToken(literalToken));
    }

    public ExpressionNode createFloatLiteralNode(Token token) {
        return new DoubleLiteralNode(getDoubleFromToken(token));
    }

    public ExpressionNode createCharOrStringLiteralNode(String literal) {
        return (literal.length() == 1) ? new CharLiteralNode(literal.charAt(0)) : new StringLiteralNode(literal);
    }

    public StatementNode createBlockNode(List<StatementNode> bodyNodes, boolean useExtendedVersion) {
        return (useExtendedVersion)?
                new ExtendedBlockNode(bodyNodes.toArray(new StatementNode[bodyNodes.size()]))
                :
                new BlockNode(bodyNodes.toArray(new StatementNode[bodyNodes.size()]));
    }

    public PascalRootNode finishMainFunction(StatementNode blockNode) {
        this.addUnitInitializationNodes();
	    this.addProgramArgumentsAssignmentNodes();

        StatementNode bodyNode = this.createSubroutineNode(blockNode);
        return new ProcedurePascalRootNode(currentLexicalScope.getFrameDescriptor(), new ProcedureBodyNode(bodyNode));
    }

    public PascalRootNode createUnitRootNode() {
	    return new ProcedurePascalRootNode(currentLexicalScope.getFrameDescriptor(), this.currentLexicalScope.createInitializationBlock());
    }

    private void addProgramArgumentsAssignmentNodes() {
	    int currentArgument = 0;
	    for (String argumentIdentifier : this.mainProgramArgumentsIdentifiers) {
	        FrameSlot argumentSlot = this.currentLexicalScope.getLocalSlot(argumentIdentifier);
	        TypeDescriptor typeDescriptor = this.currentLexicalScope.getIdentifierDescriptor(argumentIdentifier);

	        if (typeDescriptor != null) {
                if (ProgramArgumentAssignmentNode.supportsType(typeDescriptor)) {
                    this.currentLexicalScope.addScopeInitializationNode(new ProgramArgumentAssignmentNode(argumentSlot, typeDescriptor, currentArgument++));
                }
                // TODO: else -> show warning
            }
        }
    }

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

    private StatementNode createSubroutineNode(StatementNode bodyNode) {
        List<StatementNode> subroutineNodes = new ArrayList<>();

        subroutineNodes.add(currentLexicalScope.createInitializationBlock());
        subroutineNodes.add(bodyNode);

        return new BlockNode(subroutineNodes.toArray(new StatementNode[subroutineNodes.size()]));
    }

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
                    final AssignmentNode assignment = this.createAssignmentNode(parameter.identifier, readNode);

                    this.currentLexicalScope.addScopeInitializationNode(assignment);
                }
            }
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    private AssignmentNode createAssignmentNode(String targetIdentifier, ExpressionNode valueNode) {
        TypeDescriptor targetType = this.doLookup(targetIdentifier, LexicalScope::getIdentifierDescriptor);
        this.checkTypesAreCompatible(valueNode.getType(), targetType);
	    FrameSlot targetSlot = this.doLookup(targetIdentifier, LexicalScope::getLocalSlot);
        return AssignmentNodeGen.create(valueNode, targetSlot);
    }

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

    private <T extends ExpressionNode> T verifyOperandArguments(T node, String operator) {
	    return this.verifyChildrenNodes(node, "Wrong operand types provided to " + operator + " operator");
    }

    private <T extends StatementNode> T verifyChildrenNodes(T node, String onInvalidText) {
        if (!node.verifyChildrenNodeTypes()) {
            parser.SemErr(onInvalidText);
        }

        return node;
    }

    public void assertLegalsCaseValues(OrdinalDescriptor ordinal, List<ConstantDescriptor> constants) {
	    if (!this.usingTPExtension) {
            if (ordinal.getSize() != constants.size()) {
                parser.SemErr("Constants list of variant part of record type must contain all values of varant's selector type");
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

    private String getPascalUnescapedString(String str) {
	    String result = str;
        result = result.replaceAll("''", "'");
        result = result.replaceAll("\\\\0", String.valueOf('\0'));
        result = result.replaceAll("\\\\n", String.valueOf('\n'));

        return result;
    }


    // ***************************************************
    // UNIT PART
    // ***************************************************
    public void startUnit(Token identifierToken) {
        String identifier = this.getIdentifierFromToken(identifierToken);

        if (PascalLanguage.INSTANCE.findContext().isUnitRegistered(identifier)) {
            parser.SemErr("Unit with name " + identifier + " is already defined.");
            return;
        }

        UnitLexicalScope outerUnitScope = (this.units.size() > 0)? this.units.get(this.units.size() - 1) : null;
        UnitLexicalScope unitScope = new UnitLexicalScope(outerUnitScope, identifier, this.usingTPExtension);
        this.currentLexicalScope = unitScope;
        this.units.add(unitScope);
    }

    public void addUnitProcedureInterface(Token identifierToken, List<FormalParameter> formalParameters) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        try {
            currentLexicalScope.forwardProcedure(identifier, formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void addUnitFunctionInterface(Token identifierToken, List<FormalParameter> formalParameters, Token returnTypeToken) {
	    String identifier = this.getIdentifierFromToken(identifierToken);
	    TypeDescriptor returnTypeDescriptor = this.getTypeDescriptor(returnTypeToken);

	    try {
            currentLexicalScope.forwardFunction(identifier, formalParameters, returnTypeDescriptor);
        } catch (LexicalException e) {
	        parser.SemErr(e.getMessage());
        }
    }

    public void finishUnitInterfaceSection() {
        ((UnitLexicalScope) this.currentLexicalScope).markAllIdentifiersPublic();
    }

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
            currentLexicalScope.registerReturnVariable(currentLexicalScope.getName(), returnTypeDescriptor);
            this.addParameterIdentifiersToLexicalScope(heading.formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void finishUnit() {
    }

}
