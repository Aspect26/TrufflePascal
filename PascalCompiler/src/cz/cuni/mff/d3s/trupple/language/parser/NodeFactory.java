package cz.cuni.mff.d3s.trupple.language.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;

import cz.cuni.mff.d3s.trupple.language.customtypes.EnumOrdinal;
import cz.cuni.mff.d3s.trupple.language.customtypes.EnumType;
import cz.cuni.mff.d3s.trupple.language.customtypes.ICustomType;
import cz.cuni.mff.d3s.trupple.language.customtypes.IOrdinalType;
import cz.cuni.mff.d3s.trupple.language.customtypes.SimpleOrdinal;
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.nodes.BlockNode;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.InitializationNodeFactory;
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
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.RandomBuiltinNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.RandomizeBuiltinNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.ReadlnBuiltinNode;
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
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalFunctionRegistry;

public class NodeFactory {

	private Parser parser;
	private LexicalScope lexicalScope;

	/* State while parsing case statement */
	// --> TODO: this causes to be unable to create nested cases....
	private List<ExpressionNode> caseExpressions;
	private List<StatementNode> caseStatements;
	private StatementNode caseElse;

	private Map<String, Unit> units = new HashMap<>();
	private Unit currentUnit = null;

	public NodeFactory(Parser parser) {
		this.parser = parser;
		this.lexicalScope = new LexicalScope(null, null);
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
			ls.registerLocalIdentifier(identifier, typeName);
		}
	}
	
	public void finishArrayDefinition(List<String> identifiers, List<IOrdinalType> ordinalDimensions, Token returnTypeToken) {
		//TODO: this! -> arrays in units
		if(currentUnit != null)
			return;
		
		for(String identifier : identifiers) {
            try {
                lexicalScope.registerArrayVariable(identifier, ordinalDimensions, returnTypeToken.val.toLowerCase());
            } catch (IllegalArgumentException e) {
                parser.SemErr(e.getMessage());
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

	// TODO: remove this duplicit with finishFunction
	public void finishProcedure(StatementNode bodyNode) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		StatementNode subroutineNode = finishSubroutine(bodyNode);
		final ProcedureBodyNode functionBodyNode = new ProcedureBodyNode(subroutineNode);
		final PascalRootNode rootNode = new PascalRootNode(ls.getFrameDescriptor(), functionBodyNode);

		if (currentUnit == null) {
			lexicalScope = lexicalScope.getOuterScope();
			lexicalScope.getContext().getGlobalFunctionRegistry().setFunctionRootNode(ls.getName(), rootNode);
			lexicalScope.getContext().getGlobalFunctionRegistry().lookup(ls.getName()).setImplemented(true);
		} else {
			currentUnit.registerProcedure(rootNode);
		}
	}

	public void startFunction(Token name) {
		startSubroutine(name);
	}

	public void setFunctionReturnValue(Token type) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
        ls.setReturnSlot(type.val);
	}

	public void finishFunction(StatementNode bodyNode) {
		StatementNode subroutineNode = finishSubroutine(bodyNode);
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		final FunctionBodyNode functionBodyNode = FunctionBodyNodeGen.create(subroutineNode, ls.getReturnSlot());
		final PascalRootNode rootNode = new PascalRootNode(ls.getFrameDescriptor(), functionBodyNode);

		if (currentUnit == null) {
			lexicalScope = lexicalScope.getOuterScope();
			lexicalScope.getContext().getGlobalFunctionRegistry().setFunctionRootNode(ls.getName(), rootNode);
			lexicalScope.getContext().getGlobalFunctionRegistry().lookup(ls.getName()).setImplemented(true);
		} else {
			currentUnit.registerFunction(rootNode);
		}
	}

	private void startSubroutine(Token name) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		String identifier = name.val.toLowerCase();
		if(!checkSubroutineCanBeImplemented(identifier)) {
			return;
		}

		if (currentUnit == null) {
			ls.getContext().getGlobalFunctionRegistry().registerFunctionName(identifier);
			lexicalScope = new LexicalScope(lexicalScope, identifier);
		} else {
			currentUnit.startSubroutineImplementation(identifier);
		}
	}
	
	private boolean checkSubroutineCanBeImplemented(String identifier) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		
		if(ls.getContext().containsIdentifier(identifier) &&
				ls.getContext().getGlobalFunctionRegistry().lookup(identifier) == null &&
				ls.getContext().getPrivateFunctionRegistry().lookup(identifier) == null) {
			ls.getContext().getOutput().println("Duplicate identifier.");
			return false;
		}
		
		if (ls.getContext().getGlobalFunctionRegistry().lookup(identifier) != null && ls.getContext().getGlobalFunctionRegistry().lookup(identifier).isImplemented()){
			ls.getContext().getOutput().println("Subroutine is already implemented.");
			return false;
		}
		
		if (ls.getContext().getPrivateFunctionRegistry().lookup(identifier) != null && ls.getContext().getPrivateFunctionRegistry().lookup(identifier).isImplemented()){
			ls.getContext().getOutput().println("Subroutine is already implemented.");
			return false;
		}
		
		return true;
	}

	private StatementNode finishSubroutine(StatementNode bodyNode) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		ls.scopeNodes.add(bodyNode);
		final StatementNode subroutineNode = new BlockNode(
				ls.scopeNodes.toArray(new StatementNode[ls.scopeNodes.size()]));
		
		return subroutineNode;
	}

	public void appendFormalParameter(List<FormalParameter> parameter, List<FormalParameter> params) {
		for (FormalParameter param : parameter) {
			params.add(param);
		}
	}

	public List<FormalParameter> createFormalParametersList(List<String> identifiers, String typeName, boolean isOutput) {
		List<FormalParameter> paramList = new ArrayList<>();
		for (String identifier : identifiers) {
			paramList.add(new FormalParameter(identifier, typeName, isOutput));
		}

		return paramList;
	}

	public void addFormalParameters(List<FormalParameter> params) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		for (FormalParameter param : params) {
			ls.registerFormalParameter(param);
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
		List<StatementNode> initializationNodes = this.lexicalScope.getAllInitializationNoes();
		initializationNodes.add(blockNode);
		StatementNode mainNode = new BlockNode(initializationNodes.toArray(new StatementNode[initializationNodes.size()]));
		return new PascalRootNode(lexicalScope.getFrameDescriptor(), new ProcedureBodyNode(mainNode));
	}

	public void startMainBlock() {
	}

	public StatementNode finishMainBlock(List<StatementNode> bodyNodes) {
		lexicalScope = lexicalScope.getOuterScope();
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

		PascalContext context = ls.getContext();
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
		return new ForNode(ascending, ls.getLocalIdentifier(variableToken.val.toLowerCase()), startValue, finalValue, loopBody);
	}

	public StatementNode createBreak() {
		return new BreakNode();
	}
	
	public ExpressionNode createReadArrayValue(Token identifier, List<ExpressionNode> indexingNodes) {
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		return ReadArrayIndexNodeGen.create(indexingNodes.toArray(new ExpressionNode[indexingNodes.size()]), 
				ls.getLocalIdentifier(identifier.val.toLowerCase()));
	}
	
	public ExpressionNode createIndexingNode(Token identifier) {
		return new StringLiteralNode(identifier.val.toLowerCase());
	}
	
	public ExpressionNode createArrayIndexAssignment(Token name, List<ExpressionNode> indexingNodes, ExpressionNode valueNode) {
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		return new ArrayIndexAssignmentNode(ls.getLocalIdentifier(name.val.toLowerCase()),
				indexingNodes.toArray(new ExpressionNode[indexingNodes.size()]), valueNode);
	}
	
	public StatementNode createRandomizeNode() {
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		return new RandomizeBuiltinNode(ls.getContext());
	}
	
	public ExpressionNode createRandomNode() {
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		return new RandomBuiltinNode(ls.getContext());
	}
	
	public ExpressionNode createRandomNode(Token numericLiteral) {
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		return new RandomBuiltinNode(ls.getContext(), Long.parseLong(numericLiteral.val));
	}

	public ExpressionNode readSingleIdentifier(Token nameToken) {
		String identifier = nameToken.val.toLowerCase();
		FrameSlot frameSlot = lexicalScope.getVisibleIdentifier(identifier);
		
		// firstly try to read a variable
		if (frameSlot != null){
			return ReadVariableNodeGen.create(frameSlot);
			
		// secondly, try to create a procedure or function literal (with no arguments)
		} else {
			LexicalScope ls = (currentUnit==null)? lexicalScope : currentUnit.getLexicalScope();
			while(ls != null) {
				if(ls.getContext().containsParameterlessSubroutine(identifier)) {
					ExpressionNode literal = this.createFunctionNode(nameToken);
					return this.createCall(literal, new ArrayList<>());
				} else {
					ls = ls.getOuterScope();
				}
			}
				
			return null;
		}
	}

	public ExpressionNode createCall(ExpressionNode functionLiteral, List<ExpressionNode> params) {
		return InvokeNodeGen.create(params.toArray(new ExpressionNode[params.size()]), functionLiteral);
	}
	
	public StatementNode createReadLine() {
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		return new ReadlnBuiltinNode(ls.getContext());
	}
	
	public StatementNode createReadLine(List<String> identifiers){
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		
		FrameSlot[] slots = new FrameSlot[identifiers.size()];
		for(int i = 0; i < slots.length; i++) {
			String currentIdentifier = identifiers.get(i);
			slots[i] = ls.getLocalIdentifier(currentIdentifier);
			if(slots[i] == null) {
				parser.SemErr("Unknown identifier: " + currentIdentifier + ".");
			}
		}
		
		ReadlnBuiltinNode readln = new ReadlnBuiltinNode(ls.getContext(), slots);
		return readln;
	}

	public StatementNode createEmptyStatement() {
		return new NopNode();
	}
	
	public String createStringFromLiteral(Token t) {
		return (String)t.val.subSequence(1, t.val.length() - 1);
	}
	
	public void createNumericConstant(Token nameToken, NumericConstant value) {
		if (value.isDoubleType) {
			this.createDoubleConstant(nameToken, value.getDouble());
		} else {
			this.createLongConstant(nameToken, value.getLong());
		}
	}

	// Tip of the day: Fuck Java
	
	public void createLongConstant(Token nameToken, long value) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		FrameSlot newSlot = registerConstant(ls, nameToken, value);
		
		if (newSlot == null) {
			return;
		}
		ls.addInitializationNode(InitializationNodeFactory.create(newSlot, value));
	}

	public void createDoubleConstant(Token nameToken, double value) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		FrameSlot newSlot = registerConstant(ls, nameToken, value);
		
		if (newSlot == null) {
			return;
		}
		ls.addInitializationNode(InitializationNodeFactory.create(newSlot, value));
	}

	public void createStringOrCharConstant(Token nameToken, String value) {
		if(value.length() == 1) {
			createCharConstant(nameToken, value.charAt(0));
		} else {
			createStringConstant(nameToken, value);
		}
	}
	
	public void createCharConstant(Token nameToken, char value) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		FrameSlot newSlot = registerConstant(ls, nameToken, value);
		
		if (newSlot == null) {
			return;
		}
		ls.addInitializationNode(InitializationNodeFactory.create(newSlot, value));
	}
	
	public void createStringConstant(Token nameToken, String value) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		FrameSlot newSlot = registerConstant(ls, nameToken, value);
		
		if (newSlot == null) {
			return;
		}
		ls.addInitializationNode(InitializationNodeFactory.create(newSlot, value));
	}

	public void createBooleanConstant(Token nameToken, boolean value) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		FrameSlot newSlot = registerConstant(ls, nameToken, value);
		
		if (newSlot == null) {
			return;
		}
		ls.addInitializationNode(InitializationNodeFactory.create(newSlot, value));
	}
	
	public void createObjectConstant(Token nameToken, Token objectNameToken) {
		/*
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		
		String identifier = nameToken.val.toLowerCase();
		FrameSlot objectValueSlot = getVisibleSlot(objectNameToken.val.toLowerCase());
		*/
	}
	
	private FrameSlot registerConstant(LexicalScope ls, Token nameToken, Object value) {
		String identifier = nameToken.val.toLowerCase();
        try {
            return ls.registerLocalConstant(identifier, value);
        } catch (IllegalArgumentException e) {
            parser.SemErr("Duplicate identifier: " + identifier + ".");
            return null;
        }
	}
	
	public NumericConstant createUnsignedConstant(NumericConstant value, Token signToken) {
		switch(signToken.val) {
		case "-":
			return (value.isDoubleType)?
					new NumericConstant(-value.getDouble(), true) :
					new NumericConstant(-value.getLong(), false);
		case "+":
			return value;
		default:
			parser.SemErr("Unkown operator " + signToken.val + ".");
			return null;
		}
	}

	public NumericConstant createNumericConstantFromBinary(NumericConstant value, NumericConstant rvalue, Token opToken) {
		boolean isDoubleType = value.isDoubleType || rvalue.isDoubleType;
		
		switch(opToken.val) {
		case "-":
			return (isDoubleType)?
				new NumericConstant(value.getDouble() - rvalue.getDouble()) :
				new NumericConstant(value.getLong() - rvalue.getLong(), false);
		case "+":
			return (isDoubleType)?
					new NumericConstant(value.getDouble() + rvalue.getDouble()) :
					new NumericConstant(value.getLong() + rvalue.getLong(), false);
		case "*":
			return (isDoubleType)?
					new NumericConstant(value.getDouble() * rvalue.getDouble()) :
					new NumericConstant(value.getLong() * rvalue.getLong(), false);
		case "/":
			return new NumericConstant(value.getDouble() / rvalue.getDouble());
		case "div":
			if (isDoubleType) {
				parser.SemErr("Operand types do not match operator.");
				return null;
			}
			return new NumericConstant(value.getLong() / rvalue.getLong(), false);
		case "mod":
			if (isDoubleType) {
				parser.SemErr("Operand types do not match operator.");
				return null;
			}
			return new NumericConstant(value.getLong() % rvalue.getLong(), false);
		default:
			parser.SemErr("Unkown operator " + opToken.val + ".");
			return null;
		}
	}
	
	public NumericConstant getNumericConstant(Token nameToken) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		String identifier = nameToken.val.toLowerCase();
		Object c = getConstant(ls, identifier);
		if (c == null) {
			return null;
		}
		
		if (c instanceof Long) {
			return new NumericConstant(c, false);
		} else if (c instanceof Double) {
			return new NumericConstant(c, true);
		} else {
			parser.SemErr("Wrong constant type in expression " + identifier +".");
			return null;
		}
	}
	
	public String getStringConstant(Token nameToken) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		String identifier = nameToken.val.toLowerCase();
		Object str = getConstant(ls, identifier);
		if (str == null) {
			return null;
		} else if ( str instanceof String) {
			return (String)str;
		} else {
			parser.SemErr("Wrong constant type in expression " + identifier +".");
			return null;
		}
	}
	
	public boolean getBooleanConstant(Token nameToken) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		String identifier = nameToken.val.toLowerCase();
		Object b = getConstant(ls, identifier);
		if (b == null) {
			return false;
		} else if (b instanceof Boolean){
			return (boolean)b;
		} else {
			parser.SemErr("Wrong constant type in expression " + identifier +".");
			return false;
		}
	}
	
	private Object getConstant(LexicalScope ls, String identifier) {
		if (!ls.containsLocalConstant(identifier)) {
			parser.SemErr("Unknown constant " + identifier +".");
			return null;
		}
		
		return ls.getLocalConstant(identifier);
	}
	
	public ExpressionNode createCharOrStringLiteral(String literal) {
		return (literal.length() == 1) ? new CharLiteralNode(literal.charAt(0)) : new StringLiteralNode(literal);
	}

	public String createStringFromToken(Token t) {
		String literal = t.val;
		return literal.substring(1, literal.length() - 1);
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
		FrameSlot slot = lexicalScope.getVisibleIdentifier(nameToken.val.toLowerCase());
		if (slot == null)
			return null;

		return AssignmentNodeGen.create(valueNode, slot);
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
		lexicalScope.getContext().getGlobalFunctionRegistry().addAll(fRegistry);
		
		// custom types
		for(String typeIdentifier : unit.getLexicalScope().getAllCustomTypes().keySet()){
			ICustomType custom = unit.getLexicalScope().getAllCustomTypes().get(typeIdentifier);
			if(custom.isGlobal()){
				lexicalScope.registerCustomType(typeIdentifier, custom);
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

	public void addProcedureInterface(Token name, List<FormalParameter> formalParameters) {
		if(currentUnit == null) {
			lexicalScope.getContext().getGlobalFunctionRegistry().registerFunctionName(name.val.toLowerCase());
		} else if (!currentUnit.addProcedureInterface(name.val.toLowerCase(), formalParameters)) {
			parser.SemErr("Subroutine with this name is already defined: " + name);
		}
	}

	public void addFunctionInterface(Token name, List<FormalParameter> formalParameters, String returnType) {
		if(currentUnit == null) {
			lexicalScope.getContext().getGlobalFunctionRegistry().registerFunctionName(name.val.toLowerCase());
		} else if (!currentUnit.addFunctionInterface(name.val.toLowerCase(), formalParameters, returnType)) {
			parser.SemErr("Subroutine with this name is already defined: " + name);
		}
	}
	
	public void finishFormalParameterListProcedure(Token name, List<FormalParameter> parameters) {
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		String identifier = name.val.toLowerCase();
		
		// the subroutine is in outer context because now the praser is in the subroutine's own context
		ls.getOuterScope().getContext().setMySubroutineParametersCount(identifier, parameters.size());
		
		if (currentUnit == null)
			return;
		
		if (!currentUnit.checkProcedureMatchInterface(identifier, parameters)) {
			parser.SemErr("Procedure heading for " + identifier + " does not match any procedure from the interface.");
		}
	}

	public void finishFormalParameterListFunction(Token name, List<FormalParameter> parameters, String returnType) {
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		String identifier = name.val.toLowerCase();
		
		// the subroutine is in outer context because now the praser is in the subroutine's own context
		ls.getOuterScope().getContext().setMySubroutineParametersCount(identifier, parameters.size());
		
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
