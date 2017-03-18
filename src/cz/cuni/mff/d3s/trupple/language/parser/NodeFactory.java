package cz.cuni.mff.d3s.trupple.language.parser;

import java.util.*;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.sun.istack.internal.NotNull;
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
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.UnknownIdentifierException;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.UnknownTypeException;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.*;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.constant.*;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

public class NodeFactory {

    private interface GlobalObjectLookup<T> {

        T onFound(LexicalScope foundInScope, String foundIdentifier) throws LexicalException;

    }

    /**
     * The parser to be used for parsing. There can be two: parser for wirths' standard or turbo pascal standard.
     */
	private IParser parser;

    /**
     * Current lexical scope. Holds information about registered identifiers and what type are they assigned to.
     */
	private LexicalScope lexicalScope;

    /**
     * Map if included units. This contains every unit found in a directory specified by -I parameter of the compiler.
     * This doesn't mean that all of these units must be used in the program. On the other hand, each of the units that
     * are used in the program by the uses statement must be contained in this map.
     */
	private List<String> includedUnits = new ArrayList<>();

    /**
     * A list of actually used units by the uses statement.
     */
	private List<String> usedUnits = new ArrayList<>();

    /**
     * Specifies a prefix to be added to all identifiers.
     * Identifiers imported from a unit shall have prefix containing name of that unit and a character that cannot occur
     * in any identifier's name.
     */
	private String identifiersPrefix = "";

    /**
     * Flag that specifies whether the factory shall use TurboPascal specific nodes (when using TP standard) or not.
     */
	private final boolean usingTPExtension;

	public NodeFactory(IParser parser, boolean usingTPExtension) {
		this.parser = parser;
		this.usingTPExtension = usingTPExtension;

		this.lexicalScope = new LexicalScope(null, "_main", parser.isUsingTPExtension());
	}

	public void startPascal(Token identifierToken) {
		this.lexicalScope.setName(this.getIdentifierFromToken(identifierToken));
	}

    public void registerUnit(Token unitIdentifierToken) {
        String unitIdentifier = this.getIdentifierFromToken(unitIdentifierToken);
        if (!this.includedUnits.contains(unitIdentifier)) {
            parser.SemErr("Unknown unit: " + unitIdentifier + ". Did you forget to include it?");
            return;
        }

        this.usedUnits.add(unitIdentifier);
    }

    public void registerNewType(Token identifierToken, TypeDescriptor typeDescriptor) {
        String identifier = this.getIdentifierFromToken(identifierToken);

        try {
            this.lexicalScope.registerNewType(identifier, typeDescriptor);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    private <T> T doLookup(String identifier, GlobalObjectLookup<T> lookupFunction, @NotNull LexicalException notFoundException, T notFoundReturnValue) {
	    assert notFoundException != null;

	    try {
            T result = lookupToParentScope(identifier, lookupFunction);
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

    private <T> T lookupToParentScope(String identifier, GlobalObjectLookup<T> lookupFunction) throws LexicalException {
        LexicalScope currentLexicalScope = this.lexicalScope;
        while (currentLexicalScope != null) {
            if (currentLexicalScope.containsLocalIdentifier(identifier)){
                return lookupFunction.onFound(currentLexicalScope, identifier);
            } else {
                currentLexicalScope = currentLexicalScope.getOuterScope();
            }
        }

        return null;
    }

    private <T> T lookupInUnits(String identifier, GlobalObjectLookup<T> lookupFunction) throws LexicalException {
        LexicalScope mainProgramLexicalScope = this.getRootLexicalScope(this.lexicalScope);

        for (int i = this.usedUnits.size() - 1; i > -1; --i) {
            String currentUnitName = this.usedUnits.get(i);
            String lookupIdentifier = currentUnitName + "." + identifier;

            if (mainProgramLexicalScope.containsLocalIdentifier(lookupIdentifier) && mainProgramLexicalScope.isIdentifierPublic(lookupIdentifier)) {
                return lookupFunction.onFound(mainProgramLexicalScope, lookupIdentifier);
            }
        }

        return null;
    }

    public TypeDescriptor getTypeDescriptor(Token identifierToken) {
        String identifier = this.getTypeNameFromToken(identifierToken);
        return this.doLookup(identifier, LexicalScope::getTypeDescriptor, new UnknownTypeException(identifier), UnknownDescriptor.SINGLETON);
    }

    public void registerVariables(List<String> identifiers, TypeDescriptor typeDescriptor) {
        for (String identifier : identifiers) {
            try {
                lexicalScope.registerLocalVariable(identifier, typeDescriptor);
            } catch (LexicalException e) {
                parser.SemErr(e.getMessage());
            }
        }
    }

    public TypeDescriptor createArray(List<OrdinalDescriptor> ordinalDimensions, Token returnTypeToken) {
	    String typeIdentifier = this.getTypeNameFromToken(returnTypeToken);
	    TypeDescriptor returnTypeDescriptor = this.doLookup(typeIdentifier, LexicalScope::getTypeDescriptor, new UnknownTypeException(typeIdentifier));

	    return lexicalScope.createArrayType(ordinalDimensions, returnTypeDescriptor);
    }

    public TypeDescriptor createSetType(OrdinalDescriptor baseType) {
        return lexicalScope.createSetType(baseType);
    }

    public TypeDescriptor createFileType(TypeDescriptor contentTypeDescriptor) {
        return this.lexicalScope.createFileDescriptor(contentTypeDescriptor);
    }

    public void startRecord() {
	    this.lexicalScope = new LexicalScope(this.lexicalScope, "_record", this.usingTPExtension);
    }

    public TypeDescriptor createRecordType() {
	    return this.lexicalScope.createRecordDescriptor();
    }

    public TypeDescriptor createPointerType(TypeDescriptor innerType) {
        return this.lexicalScope.createPointerDescriptor(innerType);
    }

    public void finishRecord() {
        assert this.lexicalScope.getOuterScope() != null;
        this.lexicalScope = this.lexicalScope.getOuterScope();
    }

    public OrdinalDescriptor createSimpleOrdinalDescriptor(final ConstantDescriptor lowerBound, final ConstantDescriptor upperBound) {
        try {
            return lexicalScope.createRangeDescriptor((OrdinalConstantDescriptor)lowerBound, (OrdinalConstantDescriptor)upperBound);
        } catch (LexicalException e){
            parser.SemErr(e.getMessage());
            return lexicalScope.createImplicitRangeDescriptor();
        } catch (ClassCastException e) {
            parser.SemErr("Not an ordinal constant");
            return lexicalScope.createImplicitRangeDescriptor();
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
            return lexicalScope.createEnumType(enumIdentifiers);
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
            ConstantDescriptor constant = this.lexicalScope.getConstant(identifier);
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
	        this.lexicalScope.registerConstant(identifier, descriptor);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void forwardProcedure(Token identifierToken, List<FormalParameter> formalParameters) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        try {
            lexicalScope.registerProcedureInterface(identifier, formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void forwardFunction(Token identifierToken, List<FormalParameter> formalParameters, Token returnTypeToken) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        String returnType = this.getIdentifierFromToken(returnTypeToken);
        try {
            lexicalScope.registerFunctionInterface(identifier, formalParameters, returnType);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void startProcedureImplementation(Token identifierToken, List<FormalParameter> formalParameters) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        try {
            lexicalScope.tryRegisterProcedureInterface(identifier, formalParameters);
            lexicalScope = new LexicalScope(lexicalScope, identifier, parser.isUsingTPExtension());
            this.addParameterIdentifiersToLexicalScope(formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
	}

    public void startFunctionImplementation(Token identifierToken, List<FormalParameter> formalParameters, Token returnTypeToken) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        String returnType = this.getIdentifierFromToken(returnTypeToken);
        try {
            lexicalScope.tryRegisterFunctionInterface(identifier, formalParameters, returnType);
            lexicalScope = new LexicalScope(lexicalScope, identifier, parser.isUsingTPExtension());
            lexicalScope.registerReturnType(formalParameters, returnType);
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
        final FunctionBodyNode functionBodyNode = FunctionBodyNodeGen.create(subroutineNode, lexicalScope.getReturnSlot());
        finishSubroutine(functionBodyNode);
    }

    public void startLoop() {
        lexicalScope.increaseLoopDepth();
    }

    public StatementNode createForLoop(boolean ascending, Token variableToken, ExpressionNode startValue, ExpressionNode finalValue, StatementNode loopBody) {
        String iteratingIdentifier = this.getIdentifierFromToken(variableToken);
        FrameSlot iteratingSlot = lexicalScope.getLocalSlot(iteratingIdentifier);
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
        if (!lexicalScope.isInLoop()) {
            parser.SemErr("Break outside a loop: ");
        }
        return new BreakNodeTP();
    }

    public void finishLoop() {
        try {
            lexicalScope.decreaseLoopDepth();
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public StatementNode createIfStatement(ExpressionNode condition, StatementNode thenNode, StatementNode elseNode) {
        return new IfNode(condition, thenNode, elseNode);
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

    public ExpressionNode createAssignmentWithRoute(Token identifierToken, List<AccessRouteNode> accessRoute, ExpressionNode valueNode) {
        String variableIdentifier = this.getIdentifierFromToken(identifierToken);
        FrameSlot frameSlot = this.doLookup(variableIdentifier, LexicalScope::getLocalSlot, new UnknownIdentifierException(variableIdentifier));

        // TODO: check if it is assignable
        return AssignmentNodeWithRouteNodeGen.create(accessRoute.toArray(new AccessRouteNode[accessRoute.size()]), valueNode, frameSlot);
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

    public ExpressionNode createExpressionFromIdentifierWithRoute(Token identifierToken, List<AccessRouteNode> accessRoute) {
        String identifier = this.getIdentifierFromToken(identifierToken);

        return this.doLookup(
                identifier,
                (LexicalScope foundInLexicalScope, String foundIdentifier) -> new ReadVariableWithRouteNode(accessRoute.toArray(new AccessRouteNode[accessRoute.size()]), foundInLexicalScope.getLocalSlot(foundIdentifier)),
                new UnknownIdentifierException(identifier)
        );
    }

    public boolean shouldBeReference(Token subroutineToken, int parameterIndex) {
	    String subroutineIdentifier = this.getIdentifierFromToken(subroutineToken);
	    try {
	        LexicalScope ls = this.lexicalScope;
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
	    FrameSlot slot = this.lexicalScope.getLocalSlot(variableIdentifier);
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

    public StatementNode createBlockNode(List<StatementNode> bodyNodes) {
        return new BlockNode(bodyNodes.toArray(new StatementNode[bodyNodes.size()]));
    }

    // TODO: this main node can be in lexical scope instead of a parser
    public PascalRootNode finishMainFunction(StatementNode blockNode) {
        StatementNode bodyNode = this.createSubroutineNode(blockNode);
        return new PascalRootNode(lexicalScope.getFrameDescriptor(), new ProcedureBodyNode(bodyNode));
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

    private LexicalScope getRootLexicalScope(LexicalScope lexicalScope) {
	    LexicalScope rootLexicalScope = lexicalScope;
        while (rootLexicalScope.getOuterScope() != null) {
            rootLexicalScope = rootLexicalScope.getOuterScope();
        }
        return rootLexicalScope;
    }

    private StatementNode createSubroutineNode(StatementNode bodyNode) {
        List<StatementNode> subroutineNodes = new ArrayList<>();

        subroutineNodes.add(lexicalScope.createInitializationNode());
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
                    FrameSlot frameSlot = this.lexicalScope.registerReferenceVariable(parameter.identifier, typeDescriptor);
                    final ReferenceInitializationNode initializationNode = new ReferenceInitializationNode(frameSlot, count++);

                    this.lexicalScope.addScopeArgument(initializationNode);
                } else {
                    FrameSlot frameSlot = this.lexicalScope.registerLocalVariable(parameter.identifier, typeDescriptor);
                    FrameSlotKind slotKind = this.lexicalScope.getSlotKind(parameter.identifier);
                    final ExpressionNode readNode = ReadSubroutineArgumentNodeGen.create(count++, slotKind);
                    final AssignmentNode assignment = AssignmentNodeGen.create(readNode, frameSlot);

                    this.lexicalScope.addScopeArgument(assignment);
                }
            }
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    private void finishSubroutine(ExpressionNode subroutineBodyNode) {
        final PascalRootNode rootNode = new PascalRootNode(lexicalScope.getFrameDescriptor(), subroutineBodyNode);

        String subroutineIdentifier = lexicalScope.getName();
        lexicalScope = lexicalScope.getOuterScope();
        lexicalScope.getContext().setSubroutineRootNode(subroutineIdentifier, rootNode);
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
        String identifier = identifierToken.val.toLowerCase();
        return this.identifiersPrefix + identifier;
    }


    // ***************************************************
    // UNIT PART
    // ***************************************************
    public void startUnit(Token identifierToken) {
        String identifier = this.getIdentifierFromToken(identifierToken);

        if (includedUnits.contains(identifier)) {
            parser.SemErr("Unit with name " + identifier + " is already defined.");
            return;
        }

        this.identifiersPrefix = identifier + ".";
        this.includedUnits.add(identifier);
    }

    public void addUnitProcedureInterface(Token identifierToken, List<FormalParameter> formalParameters) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        try {
            lexicalScope.registerProcedureInterface(identifier, formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void addUnitFunctionInterface(Token identifierToken, List<FormalParameter> formalParameters, String returnTypeName) {
	    String identifier = this.getIdentifierFromToken(identifierToken);
	    try {
            lexicalScope.registerFunctionInterface(identifier, formalParameters, returnTypeName);
        } catch (LexicalException e) {
	        parser.SemErr(e.getMessage());
        }
    }

    public void finishUnitInterfaceSection() {
	    assert this.identifiersPrefix.endsWith(".");

	    // TODO: oh god... there should be a variable containing current parsing unit instead of this...
	    String unitName = this.identifiersPrefix.substring(0, this.identifiersPrefix.length() - 1);
	    this.lexicalScope.markAllIdentifiersFromUnitPublic(unitName);
    }

    public void startUnitProcedureImplementation(Token identifierToken, List<FormalParameter> formalParameters) {
	    String identifier = this.getIdentifierFromToken(identifierToken);
        try {
            if (lexicalScope.containsLocalIdentifier(identifier) && !lexicalScope.isSubroutine(identifier)) {
                parser.SemErr("Cannot implement. Not a procedure: " + identifier);
            } else if (!lexicalScope.containsLocalIdentifier(identifier)) {
                lexicalScope.registerProcedureInterface(identifier, formalParameters);
            }
            lexicalScope = new LexicalScope(lexicalScope, identifier, parser.isUsingTPExtension());
            this.addParameterIdentifiersToLexicalScope(formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void startUnitFunctionImplementation(Token identifierToken, List<FormalParameter> formalParameters, Token returnTypeToken) {
	    String identifier = this.getIdentifierFromToken(identifierToken);
        String returnTypeName = this.getTypeNameFromToken(returnTypeToken);

        try {
            if (lexicalScope.containsLocalIdentifier(identifier) && !lexicalScope.isSubroutine(identifier)) {
                parser.SemErr("Cannot implement. Not a function: " + identifier);
            } else if (!lexicalScope.containsLocalIdentifier(identifier)) {
                lexicalScope.registerFunctionInterface(identifier, formalParameters, returnTypeName);
            }
            lexicalScope = new LexicalScope(lexicalScope, identifier, parser.isUsingTPExtension());
            lexicalScope.registerReturnType(formalParameters, returnTypeName);
            this.addParameterIdentifiersToLexicalScope(formalParameters);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    public void endUnit() {
	    this.identifiersPrefix = "";
    }

}
