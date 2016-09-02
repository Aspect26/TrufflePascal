package cz.cuni.mff.d3s.trupple.language.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;

import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.nodes.BlockNode;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.InitializationNode;
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
import cz.cuni.mff.d3s.trupple.language.nodes.variables.ArrayIndexAssignmentNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.ReadArrayIndexNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.ReadVariableNodeGen;
import cz.cuni.mff.d3s.trupple.language.parser.types.EnumOrdinal;
import cz.cuni.mff.d3s.trupple.language.parser.types.EnumType;
import cz.cuni.mff.d3s.trupple.language.parser.types.ICustomType;
import cz.cuni.mff.d3s.trupple.language.parser.types.IOrdinalType;
import cz.cuni.mff.d3s.trupple.language.parser.types.SimpleOrdinal;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalFunctionRegistry;

public class NodeFactory {

	// todo: pls move this to its own class
	static class LexicalScope {
		protected final LexicalScope outer;
		protected final Map<String, FrameSlot> localIdentifiers;
		protected final Map<String, Constant> constants;
		protected final String name;
		protected final PascalContext context;
		protected final Map<String, ICustomType> customTypes = new HashMap<>();
		
		/* List of initialization nodes (variables like array and enums are represented as Objects
		 * (duh) and they need to be initialized otherwise their value would be null)
		 */
		public final List<StatementNode> initializationNodes;
		
		public FrameDescriptor frameDescriptor;
		public List<StatementNode> scopeNodes = new ArrayList<>();
		public FrameSlot returnSlot = null;

		LexicalScope(LexicalScope outer, String name) {
			this.name = name;
			this.outer = outer;
			this.localIdentifiers = new HashMap<>();
			this.constants = new HashMap<>();
			this.frameDescriptor = new FrameDescriptor();
			this.initializationNodes = new ArrayList<>();
			
			if (outer != null) {
				localIdentifiers.putAll(outer.localIdentifiers);
				this.context = new PascalContext(outer.context);
			}
			else{
				this.context = new PascalContext(null);
			}
		}
		
		public boolean containsCustomType(String typeName){
			return customTypes.containsKey(typeName);
		}
		
		public boolean containsCustomValue(String identifier){
			for(ICustomType custom : customTypes.values()){
				if(custom.containsCustomValue(identifier))
					return true;
			}
			
			return false;
		}
		
		/**
		 * Registers a new identifier. 
		 * @param identifier
		 * @param identifiers
		 * @param global
		 * @return null if successful or name of already existing identifier.
 		 */
		public String registerEnumType(String identifier, List<String> identifiers, boolean global){
			if(customTypes.containsKey(identifier))
				return identifier;

			EnumType enumType = new EnumType(identifier, identifiers, global);
			customTypes.put(identifier, enumType);
			localIdentifiers.put(identifier, frameDescriptor.addFrameSlot(identifier));
			
			for(String elementIdentifier : identifiers){
				if(localIdentifiers.containsKey(elementIdentifier)) {
					return elementIdentifier;
				}
				FrameSlot slot = this.frameDescriptor.addFrameSlot(elementIdentifier, FrameSlotKind.Object);
				this.initializationNodes.add(new InitializationNode(slot, 
						new EnumValue(enumType, enumType.getFirstIndex())));
				
				localIdentifiers.put(elementIdentifier, null);
			}
			
			return null;
		}
		
		public EnumType getEnumType(String identifier) {
			LexicalScope ls = this;
			while(ls != null) {
				ICustomType customType = customTypes.get(identifier);
				if(customType != null && customType instanceof EnumType)
					return (EnumType) customType;
				
				ls = ls.outer;
			}
			
			return null;
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
	 * @param original descriptor to be copied
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

	// private final Source source;

	/* State while parsing a block. */
	private LexicalScope lexicalScope;

	/* State while parsing case statement */
	// --> TODO: this causes to be unable to create nested cases....
	private List<ExpressionNode> caseExpressions;
	private List<StatementNode> caseStatements;
	private StatementNode caseElse;

	/* List of units found in sources given (name -> function registry) */
	private Map<String, Unit> units = new HashMap<>();
	private Unit currentUnit = null;

	public NodeFactory(Parser parser) {
		this.parser = parser;

		this.lexicalScope = new LexicalScope(null, null);
		this.lexicalScope.frameDescriptor = new FrameDescriptor();
	}

	private FrameSlotKind getSlotByTypeName(String type) {
		// firstly check if it is not a custom type
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		if(ls.containsCustomType(type))
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
	
	public boolean containsIdentifier(String identifier) {
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		return ls.containsCustomValue(identifier);
	}

	public void finishVariableLineDefinition(List<String> identifiers, Token variableType) {
		String typeName = variableType.val.toLowerCase();
		FrameSlotKind slotKind = getSlotByTypeName(typeName);
		
		if (slotKind == FrameSlotKind.Illegal) {
			parser.SemErr("Unkown variable type: " + typeName);
		}
		
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		EnumType enumType = ls.getEnumType(typeName);
		
		for (String identifier : identifiers) {
			try {
				FrameSlot newSlot = ls.frameDescriptor.addFrameSlot(identifier, slotKind);
				ls.localIdentifiers.put(identifier, newSlot);
				if (enumType != null) {
					ls.initializationNodes.add(new InitializationNode(newSlot, new EnumValue(enumType)));
				}
			} catch (IllegalArgumentException e) {
				parser.SemErr("Duplicate variable: " + identifier + ".");
				continue;
			}
		}
	}
	
	public void finishArrayDefinition(List<String> identifiers, IOrdinalType ordinalRange, Token returnTypeToken) {
		//TODO: this! -> arrays in units
		if(currentUnit != null)
			return;
		
		for(String identifier : identifiers) {
			try {
				FrameSlot newSlot = lexicalScope.frameDescriptor.addFrameSlot(identifier, FrameSlotKind.Object);
				lexicalScope.localIdentifiers.put(identifier, newSlot);
				this.lexicalScope.initializationNodes.add(new InitializationNode(newSlot, new PascalArray(
						returnTypeToken.val.toLowerCase(), ordinalRange
						)));
			} catch (IllegalArgumentException e) {
				parser.SemErr("Duplicate variable: " + identifier + ".");
				continue;
			}
		}
	}
	
	public void registerEnumType(String identifier, List<String> identifiers){
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		boolean global = (currentUnit==null)? true : currentUnit.isInInterfaceSection();
		
		String duplicity = ls.registerEnumType(identifier, identifiers, global);
		if(duplicity != null)
			parser.SemErr("Duplicate variable: " + duplicity + ".");
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
			lexicalScope = lexicalScope.outer;
			lexicalScope.context.getGlobalFunctionRegistry().setFunctionRootNode(ls.name, rootNode);
		} else {
			currentUnit.registerProcedure(rootNode);
		}
	}

	public void startFunction(Token name) {
		startSubroutine(name);
	}

	public void setFunctionReturnValue(Token type) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		ls.returnSlot = ls.frameDescriptor.addFrameSlot(ls.name, getSlotByTypeName(type.val));
		ls.localIdentifiers.put(ls.name, ls.returnSlot);
	}

	public void finishFunction(StatementNode bodyNode) {
		StatementNode subroutineNode = finishSubroutine(bodyNode);
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		final FunctionBodyNode functionBodyNode = FunctionBodyNodeGen.create(subroutineNode, ls.returnSlot);
		final PascalRootNode rootNode = new PascalRootNode(ls.frameDescriptor, functionBodyNode);

		if (currentUnit == null) {
			lexicalScope = lexicalScope.outer;
			lexicalScope.context.getGlobalFunctionRegistry().setFunctionRootNode(ls.name, rootNode);
		} else {
			currentUnit.registerFunction(rootNode);
		}
	}

	private void startSubroutine(Token name) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		if (ls.outer != null) {
			ls.context.getOutput().println("Nested subroutines are not supported.");
			return;
		}
		
		String identifier = name.val.toLowerCase();
		if(ls.context.containsIdentifier(identifier)){
			ls.context.getOutput().println("Duplicate identifier.");
			return;
		}

		if (currentUnit == null) {
			ls.context.getGlobalFunctionRegistry().registerFunctionName(identifier);
			lexicalScope = new LexicalScope(lexicalScope, identifier);
			lexicalScope.frameDescriptor = copyFrameDescriptor(lexicalScope.outer.frameDescriptor);
		} else {
			currentUnit.startSubroutineImplementation(identifier);
		}
	}

	private StatementNode finishSubroutine(StatementNode bodyNode) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		if (ls.outer == null) {
			ls.context.getOutput().println("Can't leave subroutine.");
			return null;
		}

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
			ls.localIdentifiers.put(param.identifier, newSlot);
			ls.scopeNodes.add(assignment);
		}
	}
	
	public IOrdinalType createSimpleOrdinal(Token lowerBound, Token upperBound) {
		final int firstIndex = Integer.parseInt(lowerBound.val);
		final int lastIndex = Integer.parseInt(upperBound.val);
		final int size = lastIndex - firstIndex + 1;
		
		if(size <= 0) {
			parser.SemErr("Greater lower bound then upper bound.");
			return null;
		}
		
		return new SimpleOrdinal(firstIndex, size, IOrdinalType.Type.NUMERIC);
	}
	
	public IOrdinalType createSimpleOrdinalFromTypename(Token name){
		String identifier = name.val.toLowerCase();
		
		// TODO: is it good to be hardcoded?
		// TODO: name constants
		switch(identifier) {
			case "boolean": return new SimpleOrdinal(0, 2, IOrdinalType.Type.BOOLEAN);
			case "char": return new SimpleOrdinal(0, 256, IOrdinalType.Type.CHAR);
		}
		
		// search in custom defined types (only enum currently)
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		EnumType enumType = ls.getEnumType(identifier);
		if(enumType == null) {
			parser.SemErr("Unknown enumerable type " + identifier + ".");
			return null;
		}
		
		return new EnumOrdinal(enumType);
	}

	public void startMainFunction() {
	}

	public PascalRootNode finishMainFunction(StatementNode blockNode) {
		List<StatementNode> initializationNodes = this.lexicalScope.initializationNodes;
		initializationNodes.add(blockNode);
		StatementNode mainNode = new BlockNode(initializationNodes.toArray(new StatementNode[initializationNodes.size()]));
		return new PascalRootNode(lexicalScope.frameDescriptor, new ProcedureBodyNode(mainNode));
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
	
	public void setCaseElse(StatementNode statement){
		this.caseElse = statement;
	}

	public CaseNode finishCaseStatement(ExpressionNode caseIndex) {
		CaseNode node = new CaseNode(caseIndex, caseExpressions.toArray(new ExpressionNode[caseExpressions.size()]),
				caseStatements.toArray(new StatementNode[caseStatements.size()]), caseElse);

		caseExpressions = null;
		caseStatements = null;
		caseElse = null;

		return node;
	}

	public ExpressionNode createFunctionNode(Token tokenName) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		String functionName = tokenName.val.toLowerCase();

		PascalContext context = ls.context;
		while(context != null){
			if(!context.containsIdentifier(functionName)){
				context = context.getOuterContext();
			} else {
				return new FunctionLiteralNode(context, functionName);
			}
		}
		parser.SemErr("Undefined function: " + functionName);
		return null;
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
		return new ForNode(ascending, ls.localIdentifiers.get(variableToken.val.toLowerCase()), startValue, finalValue, loopBody);
	}

	public StatementNode createBreak() {
		return new BreakNode();
	}
	
	public ExpressionNode createReadArrayValue(Token identifier, ExpressionNode indexNode) {
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		return ReadArrayIndexNodeGen.create(indexNode, ls.localIdentifiers.get(identifier.val.toLowerCase()));
	}
	
	public ExpressionNode createIndexingNode(Token identifier) {
		return new StringLiteralNode(identifier.val.toLowerCase());
	}
	
	public ExpressionNode createArrayIndexAssignment(Token name, ExpressionNode indexNode, ExpressionNode valueNode) {
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		return new ArrayIndexAssignmentNode(ls.localIdentifiers.get(name.val.toLowerCase()), indexNode, valueNode);
	}

	public ExpressionNode readSingleIdentifier(Token nameToken) {
		String identifier = nameToken.val.toLowerCase();
		
		// firstly try to read a variable
		FrameSlot frameSlot = getVisibleSlot(identifier);
		if (frameSlot != null){
			return ReadVariableNodeGen.create(frameSlot);
		} else {
			// secondly, try to create a procedure or function literal (with no arguments)
			LexicalScope ls = (currentUnit==null)? lexicalScope : currentUnit.getLexicalScope();
			if(ls.context.containsParameterlessSubroutine(identifier)) {
				ExpressionNode literal = this.createFunctionNode(nameToken);
				return this.createCall(literal, new ArrayList<>());
			}
				
			return null;
		}
	}

	public ExpressionNode createCall(ExpressionNode functionLiteral, List<ExpressionNode> params) {
		return InvokeNodeGen.create(params.toArray(new ExpressionNode[params.size()]), functionLiteral);
	}

	public StatementNode createEmptyStatement() {
		return new NopNode();
	}

	public void createIntegerConstant(Token identifier, Token value) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		ls.localIdentifiers.put(identifier.val.toLowerCase(), null);
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
		FrameSlot slot = null;
		if (currentUnit != null)
			slot = currentUnit.getSlot(identifier);
		else {
			LexicalScope ls = lexicalScope;
			while(ls != null && slot == null){
				slot = lexicalScope.frameDescriptor.findFrameSlot(identifier);
				ls = ls.outer;
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
		
		Unit unit = units.get(importingUnit);
		// functions
		PascalFunctionRegistry fRegistry = unit.getContext().getGlobalFunctionRegistry();
		lexicalScope.context.getGlobalFunctionRegistry().addAll(fRegistry);
		
		// custom types
		for(String typeIdentifier : unit.getLexicalScope().customTypes.keySet()){
			ICustomType custom = unit.getLexicalScope().customTypes.get(typeIdentifier);
			if(custom.isGlobal()){
				lexicalScope.customTypes.put(typeIdentifier, custom);
			}
		}
	}

	/*****************************************************************************
	 * UNIT SECTION
	 *****************************************************************************/

	public void startUnit(Token t) {
		String unitName = t.val.toLowerCase();

		if (units.containsValue(t.val.toLowerCase())) {
			parser.SemErr("Unit with name " + unitName + " is already defined.");
			return;
		}

		currentUnit = new Unit(unitName);
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
	
	public void finishFormalParameterListProcedure(Token name, List<VariableDeclaration> parameters) {
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		String identifier = name.val.toLowerCase();
		
		// the subroutine is in outer context because now the praser is in the subroutine's own context
		ls.outer.context.setMySubroutineParametersCount(identifier, parameters.size());
		
		if(currentUnit == null)
			return;
		
		if (!currentUnit.checkProcedureMatchInterface(identifier, parameters)) {
			parser.SemErr("Procedure heading for " + identifier + " does not match any procedure from the interface.");
		}
	}

	public void finishFormalParameterListFunction(Token name, List<VariableDeclaration> parameters, String returnType) {
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		String identifier = name.val.toLowerCase();
		
		// the subroutine is in outer context because now the praser is in the subroutine's own context
		ls.outer.context.setMySubroutineParametersCount(identifier, parameters.size());
		
		if(currentUnit == null)
			return;
		
		if (!currentUnit.checkFunctionMatchInterface(identifier, parameters, returnType)) {
			parser.SemErr("Function heading for " + identifier + " does not match any function from the interface.");
		}
	}
	
	public void leaveUnitInterfaceSection(){
		assert currentUnit != null;
		
		currentUnit.leaveInterfaceSection();
	}
}
