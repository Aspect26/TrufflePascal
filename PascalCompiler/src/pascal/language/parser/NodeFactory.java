package pascal.language.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;

import pascal.language.nodes.BlockNode;
import pascal.language.nodes.ExpressionNode;
import pascal.language.nodes.NopNode;
import pascal.language.nodes.PascalRootNode;
import pascal.language.nodes.StatementNode;
import pascal.language.nodes.arithmetic.AddNodeGen;
import pascal.language.nodes.arithmetic.DivideIntegerNodeGen;
import pascal.language.nodes.arithmetic.ModuloNodeGen;
import pascal.language.nodes.arithmetic.MultiplyNodeGen;
import pascal.language.nodes.arithmetic.NegationNodeGen;
import pascal.language.nodes.arithmetic.SubstractNodeGen;
import pascal.language.nodes.call.InvokeNodeGen;
import pascal.language.nodes.control.BreakNode;
import pascal.language.nodes.control.CaseNode;
import pascal.language.nodes.control.ForNode;
import pascal.language.nodes.control.IfNode;
import pascal.language.nodes.control.RepeatNode;
import pascal.language.nodes.control.WhileNode;
import pascal.language.nodes.function.FunctionBodyNode;
import pascal.language.nodes.function.FunctionBodyNodeGen;
import pascal.language.nodes.function.ProcedureBodyNode;
import pascal.language.nodes.function.ReadSubroutineArgumentNodeGen;
import pascal.language.nodes.literals.CharLiteralNode;
import pascal.language.nodes.literals.DoubleLiteralNode;
import pascal.language.nodes.literals.FunctionLiteralNode;
import pascal.language.nodes.literals.LogicLiteralNode;
import pascal.language.nodes.literals.LongLiteralNode;
import pascal.language.nodes.literals.StringLiteralNode;
import pascal.language.nodes.logic.AndNodeGen;
import pascal.language.nodes.logic.EqualsNodeGen;
import pascal.language.nodes.logic.LessThanNodeGen;
import pascal.language.nodes.logic.LessThanOrEqualNodeGen;
import pascal.language.nodes.logic.NotNodeGen;
import pascal.language.nodes.logic.OrNodeGen;
import pascal.language.nodes.variables.AssignmentNode;
import pascal.language.nodes.variables.AssignmentNodeGen;
import pascal.language.nodes.variables.ReadVariableNodeGen;
import pascal.language.runtime.PascalContext;
import pascal.language.runtime.PascalFunctionRegistry;

public class NodeFactory {
	
    static class LexicalScope {
        protected final LexicalScope outer;
        protected final Map<String, FrameSlot> locals;
        protected final String name;
        
        public FrameDescriptor frameDescriptor;
        public List<StatementNode> scopeNodes = new ArrayList<>();
        public FrameSlot returnSlot = null;

        LexicalScope(LexicalScope outer, String name) {
        	this.name = name;
            this.outer = outer;
            this.locals = new HashMap<>();
            this.frameDescriptor = new FrameDescriptor();
            if (outer != null) {
                locals.putAll(outer.locals);
            }
        }
    }
    
    /**
     * WTF -> FrameDescriptor.copy() function does not copy slot kinds
     * @see com.oracle.truffle.api.frame.FrameDescriptor#copy
     * @param original descriptor to be copied
     * @return new descriptor with the same default value and slots
     */
    public FrameDescriptor copyFrameDescriptor(FrameDescriptor original) {
        FrameDescriptor clonedFrameDescriptor = new FrameDescriptor(original.getDefaultValue());
        for(FrameSlot slot : original.getSlots()){
        	clonedFrameDescriptor.addFrameSlot(slot.getIdentifier(), slot.getInfo(), slot.getKind());
        }
        return clonedFrameDescriptor;
    }
    
    // Reference to parser -> needed for throwing semantic errors
    private Parser parser;
    
	 /* State while parsing a source unit. */
    private final PascalContext context;
    //private final Source source;
    
    /* State while parsing a block. */
    private LexicalScope lexicalScope;
    
    /* State while parsing variable line */
    private List<String> newVariableNames;
    
    /* State while parsing case statement */
    private List<ExpressionNode> caseExpressions;
    private List<StatementNode> caseStatements;
    
    /* List of units found in sources given (name -> function registry) */
    private Map<String, PascalFunctionRegistry> units = new HashMap<>();
    
    /* Interface state while parsing unit interface */
    private Map<String, List<FormalParameter>> unitProcedures = new HashMap<>();
    private Map<String, FunctionFormalParameters> unitFunctions = new HashMap<>();
    
    class FunctionFormalParameters{
    	public FunctionFormalParameters(List<FormalParameter> formalParameters, String typeName){
    		this.formalParameters = formalParameters;
    		this.typeName = typeName;
    	}
    	
    	public List<FormalParameter> formalParameters;
    	public String typeName;
    }
    
    private String unitName;
    
    public NodeFactory(Parser parser, PascalContext context){
    	this.context = context;
    	this.parser = parser;
    	
    	this.lexicalScope = new LexicalScope(null, null);
		this.lexicalScope.frameDescriptor = new FrameDescriptor();
    }
    
    private FrameSlotKind getSlotByTypeName(String type){
    	switch(type){
    	
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
    
    public void startVariableLineDefinition(){
    	assert newVariableNames == null;
    	newVariableNames = new ArrayList<>();
    }
    
    public void addNewUnknownVariable(Token tokenName){
    	newVariableNames.add(tokenName.val);
    }
    
    public void finishVariableLineDefinition(Token variableType){
    	FrameSlotKind slotKind = getSlotByTypeName(variableType.val);
    	
    	if(slotKind == FrameSlotKind.Illegal){
    		parser.SemErr("Unkown variable type: " + variableType.val);
    	}
    	
    	for(String variableName : newVariableNames){
    		FrameSlot newSlot = lexicalScope.frameDescriptor.addFrameSlot(variableName, slotKind);
    		lexicalScope.locals.put(variableName, newSlot);
    	}
    	
    	newVariableNames = null;
    }
    
    public void startProcedure(Token name){
    	startSubroutine(name);
    }
    
    public void finishProcedure(StatementNode bodyNode){
    	StatementNode subroutineNode = finishSubroutine(bodyNode);
    	final ProcedureBodyNode functionBodyNode = new ProcedureBodyNode(subroutineNode);
    	final PascalRootNode rootNode = new PascalRootNode(lexicalScope.frameDescriptor, functionBodyNode);
    	
    	if(unitName == null){
    		context.getFunctionRegistry().register(lexicalScope.name, rootNode);
    	} else{
    		units.get(unitName).register(lexicalScope.name, rootNode);
    	}
    		
    	lexicalScope = lexicalScope.outer;
    }
    
    public void startFunction(Token name){
    	startSubroutine(name);
    }
    
    public void setFunctionReturnValue(Token type){
    	lexicalScope.returnSlot = 
    			lexicalScope.frameDescriptor.addFrameSlot(lexicalScope.name, getSlotByTypeName(type.val));
    	lexicalScope.locals.put(lexicalScope.name, lexicalScope.returnSlot);
    }
    
    public void finishFunction(StatementNode bodyNode){
    	StatementNode subroutineNode = finishSubroutine(bodyNode);
    	
    	final FunctionBodyNode functionBodyNode = 
    			FunctionBodyNodeGen.create(subroutineNode, lexicalScope.returnSlot);
    	final PascalRootNode rootNode = new PascalRootNode(lexicalScope.frameDescriptor, functionBodyNode);
    	
    	if(unitName == null){
    		context.getFunctionRegistry().register(lexicalScope.name, rootNode);
    	} else{
    		units.get(unitName).register(lexicalScope.name, rootNode);
    	}

    	lexicalScope = lexicalScope.outer;
    }
    
    private void startSubroutine(Token name){
     	if(lexicalScope.outer != null){
    		context.getOutput().println("Nested subroutines are not supported.");
    		return;
    	}
    	
    	if(unitName == null)
    		context.getFunctionRegistry().registerFunctionName(name.val.toLowerCase());
    	else{
    		// TODO: check if function has interface in current unit
    	}
    	
    	lexicalScope = new LexicalScope(lexicalScope, name.val.toLowerCase());
    	lexicalScope.frameDescriptor = copyFrameDescriptor(lexicalScope.outer.frameDescriptor);
    }
    
    private StatementNode finishSubroutine(StatementNode bodyNode){
    	if(lexicalScope.outer == null){
    		context.getOutput().println("Can't leave subroutine.");
    		return null;
    	}
    	
    	lexicalScope.scopeNodes.add(bodyNode);
    	final StatementNode subroutineNode = new BlockNode(lexicalScope.scopeNodes.toArray(new StatementNode[lexicalScope.scopeNodes.size()]));
    	return subroutineNode;
    }
    
    public void addFormalParameters(List<String> names, String type){
    	FrameSlotKind slotKind = getSlotByTypeName(type);
    	
    	for(String name : names){
    		final ExpressionNode readNode = ReadSubroutineArgumentNodeGen.create(lexicalScope.scopeNodes.size(), slotKind);
    		FrameSlot newSlot = lexicalScope.frameDescriptor.addFrameSlot(name, slotKind);
    		final AssignmentNode assignment = AssignmentNodeGen.create(readNode, newSlot);
    		lexicalScope.locals.put(name, newSlot);
    		lexicalScope.scopeNodes.add(assignment);
    	}
    }
    
	public void startMainFunction(){
	}
	
	public PascalRootNode finishMainFunction(StatementNode blockNode){
		return new PascalRootNode(lexicalScope.frameDescriptor, new ProcedureBodyNode(blockNode));
	}
	
	public void startMainBlock(){
	}
	
	public StatementNode finishMainBlock(List<StatementNode> bodyNodes){
		lexicalScope = lexicalScope.outer;
		return new BlockNode(bodyNodes.toArray(new StatementNode[bodyNodes.size()]));
	}
	
	public StatementNode finishBlock(List<StatementNode> bodyNodes){
		return new BlockNode(bodyNodes.toArray(new StatementNode[bodyNodes.size()]));
	}
	
	public void startCaseList(){
		this.caseExpressions = new ArrayList<>();
		this.caseStatements = new ArrayList<>();
	}
	
	public void addCaseOption(ExpressionNode expression, StatementNode statement){
		this.caseExpressions.add(expression);
		this.caseStatements.add(statement);
	}
	
	public CaseNode finishCaseStatement(ExpressionNode caseIndex){
		CaseNode node = new CaseNode(caseIndex, 
				caseExpressions.toArray(new ExpressionNode[caseExpressions.size()]),
				caseStatements.toArray(new StatementNode[caseStatements.size()]));
		
		caseExpressions = null;
		caseStatements = null;
		
		return node;
	}
	
	public ExpressionNode createFunctionNode(Token tokenName){
		String functionName = tokenName.val.toLowerCase();
		if(context.getFunctionRegistry().lookup(functionName) == null){
			if(unitName != null){
				if(units.get(unitName).lookup(functionName) != null)
					return new FunctionLiteralNode(context, tokenName.val.toLowerCase());
			}
			parser.SemErr("Undefined function: " + functionName);
			return null;
		}
		return new FunctionLiteralNode(context, tokenName.val.toLowerCase());
	}
	
	public StatementNode createIfStatement(ExpressionNode condition, StatementNode thenNode, StatementNode elseNode){
		return new IfNode(condition, thenNode, elseNode);
	}
	
	public StatementNode createWhileLoop(ExpressionNode condition, StatementNode loopBody){
		return new WhileNode(condition, loopBody);
	}
	
	public StatementNode createRepeatLoop(ExpressionNode condition, StatementNode loopBody){
		return new RepeatNode(condition, loopBody);
	}
	
	public StatementNode createForLoop(boolean ascending, Token variableToken, ExpressionNode startValue,
			ExpressionNode finalValue, StatementNode loopBody){
		
		return new ForNode(ascending, lexicalScope.locals.get(variableToken.val.toLowerCase()), 
				startValue, finalValue, loopBody);
	}
	
	public StatementNode createBreak(){
		return new BreakNode();
	}
	
	public ExpressionNode readVariable(Token nameToken){
		final FrameSlot frameSlot = lexicalScope.locals.get(nameToken.val.toLowerCase());
		if(frameSlot == null)
			return null;
		
		return ReadVariableNodeGen.create(frameSlot);
	}
	
	public ExpressionNode createCall(ExpressionNode functionLiteral, List<ExpressionNode> params){
		return InvokeNodeGen.create(params.toArray(new ExpressionNode[params.size()]), functionLiteral);
	}
	
	public StatementNode createEmptyStatement(){
		return new NopNode();
	}
	
	public ExpressionNode createCharLiteral(Token literalToken){
		String literal = literalToken.val;
		assert literal.length() >= 2 && literal.startsWith("'") && literal.endsWith("'");
		literal = literal.substring(1, literal.length() - 1);
		
		return (literal.length() == 1)? 
				new CharLiteralNode(literal.charAt(0)) : new StringLiteralNode(literal);
	}
	
	public ExpressionNode createNumericLiteral(Token literalToken){
		try{
			return new LongLiteralNode(Long.parseLong(literalToken.val));
		} catch (NumberFormatException e){
			return null;
		}
	}
	
	public ExpressionNode createRealLiteral(Token integerPart, Token fractionalPart, Token exponentOp, Token exponent){
		int integer = Integer.parseInt(integerPart.val);
		double fractional = (fractionalPart == null)? 0 : Double.parseDouble(fractionalPart.val);
		int exponentMultiplier = (exponentOp != null && exponentOp.val == "-")? -1 : 1;
		int exponentValue = (exponent == null)? 0 : Integer.parseInt(exponent.val);
		
		double value = integer;
		
		while(fractional > 1)
			fractional /= 10;
		value += fractional;
		
		value = value * Math.pow(10, exponentValue*exponentMultiplier);
		
		return new DoubleLiteralNode(value);
	}
	
	public ExpressionNode createLogicLiteral(boolean value){
		return new LogicLiteralNode(value);
	}
	
	public ExpressionNode createAssignment(Token nameToken, ExpressionNode valueNode){
		FrameSlot slot = lexicalScope.frameDescriptor.findFrameSlot(nameToken.val.toLowerCase());
		if(slot == null)
			return null;
		
		return AssignmentNodeGen.create(valueNode, slot);
	}
	
	public ExpressionNode createBinary(Token operator, ExpressionNode leftNode, ExpressionNode rightNode){
		switch(operator.val.toLowerCase()){
		
		// arithmetic
		case "+":
			return AddNodeGen.create(leftNode, rightNode);
		case "-":
			return SubstractNodeGen.create(leftNode, rightNode);
		case "*":
			return MultiplyNodeGen.create(leftNode, rightNode);
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
	
	public ExpressionNode createUnary(Token operator, ExpressionNode son){
		switch(operator.val){
		case "+":
			return son;   // unary + in Pascal markss identity
		case "-":
			return NegationNodeGen.create(son);
		case "not":
			return NotNodeGen.create(son);
		default:
			parser.SemErr("Unexpected unary operator: " + operator.val);
			return null;
		}
	}
	
	public void importUnit(Token unitToken){
		String importingUnit = unitToken.val.toLowerCase();
		
		PascalFunctionRegistry fRegistry = units.get(importingUnit);
		if(fRegistry == null){
			parser.SemErr("Unknown unit. Did you imported it to compiler? - " + importingUnit);
			return;
		}
		
		this.context.getFunctionRegistry().addAll(fRegistry);
	}
	
	/*****************************************************************************
	 * UNIT SECTION
	 */
	
	public void startUnit(Token t){
		this.unitName = t.val.toLowerCase();
		this.lexicalScope = new LexicalScope(null, unitName);
		
		if(units.containsValue(t.val.toLowerCase())){
			parser.SemErr("Unit with name " + unitName + " is already defined.");
			return;
		}
		this.units.put(unitName, new PascalFunctionRegistry(context));
	}
	
	public void endUnit(){
		this.unitName = null;
	}
	
	public void appendIFormalParameter(List<FormalParameter> parameter, List<FormalParameter> params){
		for(FormalParameter param : parameter){
			params.add(param);
		}
	}
	
	public List<FormalParameter> createInterfaceParameter(List<String> identifiers, String typeName){
		List<FormalParameter> paramList = new ArrayList<>();
		for(String identifier : identifiers){
			paramList.add(new FormalParameter(identifier, typeName));
		}
		
		return paramList;
	}
	
	public void addProcedureInterface(String name, List<FormalParameter> formalParameters){
		if(subroutineExists(name))
				return;

		units.get(unitName).registerFunctionName(name);
		unitProcedures.put(name, formalParameters);
	}
	
	public void addFunctionInterface(String name, List<FormalParameter> formalParameters, String returnType){
		if(subroutineExists(name))
			return;

		units.get(unitName).registerFunctionName(name);
		unitFunctions.put(name, new FunctionFormalParameters(formalParameters, returnType));
	}
	
	private boolean subroutineExists(String name){
		if(unitProcedures.containsKey(name)){
			parser.SemErr("Procedure with name " + name + " is already defined in unit file " + unitName);
			return true;
		}
		else if(unitFunctions.containsKey(name)){
			parser.SemErr("Function with name " + name + " is already defined in unit file " + unitName);
			return true;
		}
		
		return false;
	}
}
