package cz.cuni.mff.d3s.trupple.language.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;

import cz.cuni.mff.d3s.trupple.language.nodes.BlockNode;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.NopNode;
import cz.cuni.mff.d3s.trupple.language.nodes.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.AddNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.DivideIntegerNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.DivideNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.ModuloNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.MultiplyNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.NegationNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.SubstractNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.InvokeNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.control.BreakNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.CaseNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.ForNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.IfNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.RepeatNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.WhileNode;
import cz.cuni.mff.d3s.trupple.language.nodes.function.FunctionBodyNode;
import cz.cuni.mff.d3s.trupple.language.nodes.function.FunctionBodyNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.function.ProcedureBodyNode;
import cz.cuni.mff.d3s.trupple.language.nodes.function.ReadSubroutineArgumentNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.CharLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.DoubleLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.FunctionLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.LogicLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.LongLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.StringLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.AndNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.EqualsNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.LessThanNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.LessThanOrEqualNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.NotNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.OrNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.ReadVariableNodeGen;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalFunctionRegistry;

public class NodeFactory {

	static class LexicalScope {
		protected final LexicalScope outer;
		protected final Map<String, FrameSlot> locals;
		protected final Map<String, Constant> constants;
		protected final String name;

		public FrameDescriptor frameDescriptor;
		public List<StatementNode> scopeNodes = new ArrayList<>();
		public FrameSlot returnSlot = null;

		LexicalScope(LexicalScope outer, String name) {
			this.name = name;
			this.outer = outer;
			this.locals = new HashMap<>();
			this.constants = new HashMap<>();
			this.frameDescriptor = new FrameDescriptor();
			if (outer != null) {
				locals.putAll(outer.locals);
			}
		}
	}

	interface Constant {

	}

	class IntegerConstant implements Constant {
	}

	/**
	 * WTF -> FrameDescriptor.copy() function does not copy slot kinds
	 * 
	 * @see com.oracle.truffle.api.frame.FrameDescriptor#copy
	 * @param original
	 *            descriptor to be copied
	 * @return new descriptor with the same default value and slots
	 */
	public static FrameDescriptor copyFrameDescriptor(FrameDescriptor original) {
		FrameDescriptor clonedFrameDescriptor = new FrameDescriptor(original.getDefaultValue());
		for (FrameSlot slot : original.getSlots()) {
			clonedFrameDescriptor.addFrameSlot(slot.getIdentifier(), slot.getInfo(), slot.getKind());
		}
		return clonedFrameDescriptor;
	}

	// Reference to parser -> needed for throwing semantic errors
	private Parser parser;

	/* State while parsing a source unit. */
	private final PascalContext context;
	// private final Source source;

	/* State while parsing a block. */
	private LexicalScope lexicalScope;

	/* State while parsing case statement */
	private List<ExpressionNode> caseExpressions;
	private List<StatementNode> caseStatements;

	/* List of units found in sources given (name -> function registry) */
	private Map<String, UnitInterface> units = new HashMap<>();
	private UnitInterface currentUnit = null;

	public NodeFactory(Parser parser, PascalContext context) {
		this.context = context;
		this.parser = parser;

		this.lexicalScope = new LexicalScope(null, null);
		this.lexicalScope.frameDescriptor = new FrameDescriptor();
	}

	private FrameSlotKind getSlotByTypeName(String type) {
		switch (type) {

		// ordinals
		case "integer":
		case "cardinal":
		case "shortint":
		case "smallint":
		case "longint":
		case "int64":
		case "byte":
		case "word":
		case "longword":
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

	public void finishVariableLineDefinition(List<String> identifiers, Token variableType) {
		FrameSlotKind slotKind = getSlotByTypeName(variableType.val);

		if (slotKind == FrameSlotKind.Illegal) {
			parser.SemErr("Unkown variable type: " + variableType.val);
		}

		for (String identifier : identifiers) {
			try {
				if (currentUnit == null) {
					FrameSlot newSlot = lexicalScope.frameDescriptor.addFrameSlot(identifier, slotKind);
					lexicalScope.locals.put(identifier, newSlot);
				} else {
					currentUnit.addGlobalVariable(identifier, slotKind);
				}
			} catch (IllegalArgumentException e) {
				parser.SemErr("Duplicate variable: " + identifier + ".");
				continue;
			}
		}
	}

	public void startProcedure(Token name) {
		startSubroutine(name);
	}

	public void finishProcedure(StatementNode bodyNode) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		StatementNode subroutineNode = finishSubroutine(bodyNode);
		final ProcedureBodyNode functionBodyNode = new ProcedureBodyNode(subroutineNode);
		final PascalRootNode rootNode = new PascalRootNode(ls.frameDescriptor, functionBodyNode);

		if (currentUnit == null) {
			context.getFunctionRegistry().register(ls.name, rootNode);
			lexicalScope = lexicalScope.outer;
		} else {
			currentUnit.getFunctionRegistry().register(ls.name, rootNode);
			currentUnit.leaveLexicalScope();
		}
	}

	public void startFunction(Token name) {
		startSubroutine(name);
	}

	public void setFunctionReturnValue(Token type) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		ls.returnSlot = ls.frameDescriptor.addFrameSlot(ls.name, getSlotByTypeName(type.val));
		ls.locals.put(ls.name, ls.returnSlot);
	}

	public void finishFunction(StatementNode bodyNode) {
		StatementNode subroutineNode = finishSubroutine(bodyNode);
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		final FunctionBodyNode functionBodyNode = FunctionBodyNodeGen.create(subroutineNode, ls.returnSlot);
		final PascalRootNode rootNode = new PascalRootNode(ls.frameDescriptor, functionBodyNode);

		if (currentUnit == null) {
			context.getFunctionRegistry().register(ls.name, rootNode);
			lexicalScope = lexicalScope.outer;
		} else {
			currentUnit.getFunctionRegistry().register(ls.name, rootNode);
			currentUnit.leaveLexicalScope();
		}
	}

	private void startSubroutine(Token name) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		if (ls.outer != null) {
			context.getOutput().println("Nested subroutines are not supported.");
			return;
		}

		String identifier = name.val.toLowerCase();
		if (currentUnit == null) {
			context.getFunctionRegistry().registerFunctionName(identifier);
			lexicalScope = new LexicalScope(lexicalScope, identifier);
			lexicalScope.frameDescriptor = copyFrameDescriptor(lexicalScope.outer.frameDescriptor);
		} else {
			if (!currentUnit.startSubroutineImplementation(identifier)) {
				parser.SemErr("Subroutine already defined: " + identifier + ",");
			}
		}
	}

	private StatementNode finishSubroutine(StatementNode bodyNode) {
		if (currentUnit == null && lexicalScope.outer == null) {
			context.getOutput().println("Can't leave subroutine.");
			return null;
		}

		if (currentUnit != null && currentUnit.getLexicalScope().outer == null) {
			context.getOutput().println("Can't leave subroutine.");
			return null;
		}

		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		ls.scopeNodes.add(bodyNode);
		final StatementNode subroutineNode = new BlockNode(
				ls.scopeNodes.toArray(new StatementNode[ls.scopeNodes.size()]));
		return subroutineNode;
	}

	public void appendFormalParameter(List<VariableDeclaration> parameter, List<VariableDeclaration> params) {
		for (VariableDeclaration param : parameter) {
			params.add(param);
		}
	}

	public List<VariableDeclaration> createFormalParametersList(List<String> identifiers, String typeName) {
		List<VariableDeclaration> paramList = new ArrayList<>();
		for (String identifier : identifiers) {
			paramList.add(new VariableDeclaration(identifier, typeName));
		}

		return paramList;
	}

	public void addFormalParameters(List<VariableDeclaration> params) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		for (VariableDeclaration param : params) {
			FrameSlotKind slotKind = getSlotByTypeName(param.type);
			final ExpressionNode readNode = ReadSubroutineArgumentNodeGen.create(ls.scopeNodes.size(), slotKind);
			FrameSlot newSlot = ls.frameDescriptor.addFrameSlot(param.identifier, slotKind);
			final AssignmentNode assignment = AssignmentNodeGen.create(readNode, newSlot);
			ls.locals.put(param.identifier, newSlot);
			ls.scopeNodes.add(assignment);
		}
	}

	public void startMainFunction() {
	}

	public PascalRootNode finishMainFunction(StatementNode blockNode) {
		return new PascalRootNode(lexicalScope.frameDescriptor, new ProcedureBodyNode(blockNode));
	}

	public void startMainBlock() {
	}

	public StatementNode finishMainBlock(List<StatementNode> bodyNodes) {
		lexicalScope = lexicalScope.outer;
		return new BlockNode(bodyNodes.toArray(new StatementNode[bodyNodes.size()]));
	}

	public StatementNode finishBlock(List<StatementNode> bodyNodes) {
		return new BlockNode(bodyNodes.toArray(new StatementNode[bodyNodes.size()]));
	}

	public void startCaseList() {
		this.caseExpressions = new ArrayList<>();
		this.caseStatements = new ArrayList<>();
	}

	public void addCaseOption(ExpressionNode expression, StatementNode statement) {
		this.caseExpressions.add(expression);
		this.caseStatements.add(statement);
	}

	public CaseNode finishCaseStatement(ExpressionNode caseIndex) {
		CaseNode node = new CaseNode(caseIndex, caseExpressions.toArray(new ExpressionNode[caseExpressions.size()]),
				caseStatements.toArray(new StatementNode[caseStatements.size()]));

		caseExpressions = null;
		caseStatements = null;

		return node;
	}

	public ExpressionNode createFunctionNode(Token tokenName) {
		String functionName = tokenName.val.toLowerCase();

		if (currentUnit == null) {
			if (context.getFunctionRegistry().lookup(functionName) == null) {
				parser.SemErr("Undefined function: " + functionName);
				return null;
			} else {
				return new FunctionLiteralNode(context, tokenName.val.toLowerCase());
			}
		} else {
			if (currentUnit.getFunctionRegistry().lookup(functionName) == null) {
				parser.SemErr("Undefined function: " + functionName);
				return null;
			} else {
				return new FunctionLiteralNode(currentUnit.getCotnext(), tokenName.val.toLowerCase());
			}
		}
	}

	public StatementNode createIfStatement(ExpressionNode condition, StatementNode thenNode, StatementNode elseNode) {
		return new IfNode(condition, thenNode, elseNode);
	}

	public StatementNode createWhileLoop(ExpressionNode condition, StatementNode loopBody) {
		return new WhileNode(condition, loopBody);
	}

	public StatementNode createRepeatLoop(ExpressionNode condition, StatementNode loopBody) {
		return new RepeatNode(condition, loopBody);
	}

	public StatementNode createForLoop(boolean ascending, Token variableToken, ExpressionNode startValue,
			ExpressionNode finalValue, StatementNode loopBody) {

		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		return new ForNode(ascending, ls.locals.get(variableToken.val.toLowerCase()), startValue, finalValue, loopBody);
	}

	public StatementNode createBreak() {
		return new BreakNode();
	}

	public ExpressionNode readVariable(Token nameToken) {
		String identifier = nameToken.val.toLowerCase();
		FrameSlot frameSlot = getVisibleSlot(identifier);

		if (frameSlot == null)
			return null;

		return ReadVariableNodeGen.create(frameSlot);
	}

	public ExpressionNode createCall(ExpressionNode functionLiteral, List<ExpressionNode> params) {
		return InvokeNodeGen.create(params.toArray(new ExpressionNode[params.size()]), functionLiteral);
	}

	public StatementNode createEmptyStatement() {
		return new NopNode();
	}

	public void createIntegerConstant(Token identifier, Token value) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		ls.locals.put(identifier.val.toLowerCase(), null);
		// ls.constants.put(identifier.val.toLowerCase(), new
		// IntegerConstant(value.val.toLowerCase()));
	}

	public void createFloatConstant(Token identifier, Token value) {

	}

	public void createStringOrCharConstant(Token identifier, Token value) {

	}

	public void createBooleanConstant(Token identifier, boolean value) {

	}

	public ExpressionNode createCharOrStringLiteral(Token literalToken) {
		String literal = literalToken.val;
		assert literal.length() >= 2 && literal.startsWith("'") && literal.endsWith("'");
		literal = literal.substring(1, literal.length() - 1);

		return (literal.length() == 1) ? new CharLiteralNode(literal.charAt(0)) : new StringLiteralNode(literal);
	}

	public ExpressionNode createNumericLiteral(Token literalToken) {
		try {
			return new LongLiteralNode(Long.parseLong(literalToken.val));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public ExpressionNode createFloatLiteral(Token token) {
		double value = Float.parseFloat(token.val.toString());
		return new DoubleLiteralNode(value);
	}

	public ExpressionNode createRealLiteral(Token integerPart, Token fractionalPart, Token exponentOp, Token exponent) {
		int integer = Integer.parseInt(integerPart.val);
		double fractional = (fractionalPart == null) ? 0 : Double.parseDouble(fractionalPart.val);
		int exponentMultiplier = (exponentOp != null && exponentOp.val == "-") ? -1 : 1;
		int exponentValue = (exponent == null) ? 0 : Integer.parseInt(exponent.val);

		double value = integer;

		while (fractional > 1)
			fractional /= 10;
		value += fractional;

		value = value * Math.pow(10, exponentValue * exponentMultiplier);

		return new DoubleLiteralNode(value);
	}

	public ExpressionNode createLogicLiteral(boolean value) {
		return new LogicLiteralNode(value);
	}

	public ExpressionNode createAssignment(Token nameToken, ExpressionNode valueNode) {
		FrameSlot slot = getVisibleSlot(nameToken.val.toLowerCase());
		if (slot == null)
			return null;

		return AssignmentNodeGen.create(valueNode, slot);
	}

	private FrameSlot getVisibleSlot(String identifier) {
		FrameSlot slot;
		if (currentUnit != null)
			slot = currentUnit.getSlot(identifier);
		else {
			slot = lexicalScope.frameDescriptor.findFrameSlot(identifier);
			if (slot == null) {
				Iterator<Entry<String, UnitInterface>> it = units.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, UnitInterface> pair = it.next();
					slot = pair.getValue().getSlot(identifier);
				}
			}
		}

		return slot;
	}

	public ExpressionNode createBinary(Token operator, ExpressionNode leftNode, ExpressionNode rightNode) {
		switch (operator.val.toLowerCase()) {

		// arithmetic
		case "+":
			return AddNodeGen.create(leftNode, rightNode);
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

		default:
			parser.SemErr("Unexpected binary operator: " + operator.val);
			return null;
		}
	}

	public ExpressionNode createUnary(Token operator, ExpressionNode son) {
		switch (operator.val) {
		case "+":
			return son; // unary + in Pascal markss identity
		case "-":
			return NegationNodeGen.create(son);
		case "not":
			return NotNodeGen.create(son);
		default:
			parser.SemErr("Unexpected unary operator: " + operator.val);
			return null;
		}
	}

	public void importUnit(Token unitToken) {
		String importingUnit = unitToken.val.toLowerCase();

		if (!units.containsKey(importingUnit)) {
			parser.SemErr("Unknown unit. Did you imported it to compiler? - " + importingUnit);
			return;
		}
		PascalFunctionRegistry fRegistry = units.get(importingUnit).getFunctionRegistry();

		this.context.getFunctionRegistry().addAll(fRegistry);
	}

	/*****************************************************************************
	 * UNIT SECTION
	 */

	public void startUnit(Token t) {
		String unitName = t.val.toLowerCase();

		if (units.containsValue(t.val.toLowerCase())) {
			parser.SemErr("Unit with name " + unitName + " is already defined.");
			return;
		}

		currentUnit = new UnitInterface(unitName);
		this.units.put(unitName, currentUnit);
	}

	public void endUnit() {
		currentUnit = null;
	}

	public void addProcedureInterface(Token name, List<VariableDeclaration> formalParameters) {
		if (!currentUnit.addProcedureInterface(name.val.toLowerCase(), formalParameters)) {
			parser.SemErr("Subroutine with this name is already defined: " + name);
		}
	}

	public void addFunctionInterface(Token name, List<VariableDeclaration> formalParameters, String returnType) {
		if (!currentUnit.addFunctionInterface(name.val.toLowerCase(), formalParameters, returnType)) {
			parser.SemErr("Subroutine with this name is already defined: " + name);
		}
	}

	public void checkUnitInterfaceMatchProcedure(Token name, List<VariableDeclaration> parameters) {
		if (currentUnit == null)
			return;

		String identifier = name.val.toLowerCase();
		if (!currentUnit.checkProcedureMatch(identifier, parameters)) {
			parser.SemErr("Procedure heading for " + identifier + " does not match any procedure from the interface.");
		}
	}

	public void checkUnitInterfaceMatchFunction(Token name, List<VariableDeclaration> parameters, String returnType) {
		if (currentUnit == null)
			return;

		String identifier = name.val.toLowerCase();
		if (!currentUnit.checkFunctionMatch(identifier, parameters, returnType)) {
			parser.SemErr("Function heading for " + identifier + " does not match any function from the interface.");
		}
	}
}
