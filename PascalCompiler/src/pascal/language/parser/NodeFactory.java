package pascal.language.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.source.Source;

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
import pascal.language.nodes.control.IfNode;
import pascal.language.nodes.control.RepeatNode;
import pascal.language.nodes.control.WhileNode;
import pascal.language.nodes.function.FunctionBodyNode;
import pascal.language.nodes.literals.CharLiteralNode;
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
import pascal.language.nodes.variables.AssignmentNodeGen;
import pascal.language.nodes.variables.ReadVariableNodeGen;
import pascal.language.runtime.PascalContext;

public class NodeFactory {
	
    static class LexicalScope {
        protected final LexicalScope outer;
        protected final Map<String, FrameSlot> locals;

        LexicalScope(LexicalScope outer) {
            this.outer = outer;
            this.locals = new HashMap<>();
            if (outer != null) {
                locals.putAll(outer.locals);
            }
        }
    }
    
    // Reference to parser -> needed for throwing semantic errors
    private Parser parser;
    
	 /* State while parsing a source unit. */
    private final PascalContext context;
    //private final Source source;
    
    /* State while parsing a function. */
    //private int parameterCount;
    private FrameDescriptor frameDescriptor;
    
    /* State while parsing a block. */
    private LexicalScope lexicalScope;
    
    /* State while parsing variable line */
    private List<String> newVariableNames;
    
    public NodeFactory(Parser parser, PascalContext context, Source source){
    	this.context = context;
    	this.parser = parser;
    	//this.source = source;
    	frameDescriptor = new FrameDescriptor();
    	lexicalScope = new LexicalScope(null);
    }
    
    public void startVariableLineDefinition(){
    	assert newVariableNames == null;
    	newVariableNames = new ArrayList<>();
    }
    
    public void addNewUnknownVariable(Token tokenName){
    	newVariableNames.add(tokenName.val);
    }
    
    public void finishVariableLineDefinition(Token variableType){
    	FrameSlotKind slotKind;
    	
    	switch(variableType.val){
    	
    	//ordinals
    	case "integer":
    		slotKind = FrameSlotKind.Long; break;
    	case "cardinal":
    		slotKind = FrameSlotKind.Long; break;
    	case "shortint":
    		slotKind = FrameSlotKind.Long; break;
    	case "smallint":
    		slotKind = FrameSlotKind.Long; break;
    	case "longint":
    		slotKind = FrameSlotKind.Long; break;
    	case "int64":
    		slotKind = FrameSlotKind.Long; break;
    	case "byte":
    		slotKind = FrameSlotKind.Long; break;
    	case "word":
    		slotKind = FrameSlotKind.Long; break;
    	case "longword":
    		slotKind = FrameSlotKind.Long; break;
    		
    	// logical
    	case "boolean":
    		slotKind = FrameSlotKind.Boolean; break;
    		
    	// char
    	case "char":
    		slotKind = FrameSlotKind.Byte; break;
    		
    	default:
    		slotKind = FrameSlotKind.Illegal; break;
    	}
    	
    	if(slotKind == FrameSlotKind.Illegal){
    		parser.SemErr("Unkown variable type: " + variableType.val);
    	}
    	
    	for(String variableName : newVariableNames){
    		FrameSlot newSlot = frameDescriptor.addFrameSlot(variableName, slotKind);
    		lexicalScope.locals.put(variableName, newSlot);
    	}
    	
    	newVariableNames = null;
    }
	
	public void startMainFunction(){
	}
	
	public PascalRootNode finishMainFunction(StatementNode blockNode){
		return new PascalRootNode(context, frameDescriptor, new FunctionBodyNode(blockNode));
	}
	
	public void startMainBlock(){
		lexicalScope = new LexicalScope(lexicalScope);
	}
	
	public StatementNode finishMainBlock(List<StatementNode> bodyNodes){
		lexicalScope = lexicalScope.outer;
		return new BlockNode(bodyNodes.toArray(new StatementNode[bodyNodes.size()]));
	}
	
	public StatementNode finishBlock(List<StatementNode> bodyNodes){
		return new BlockNode(bodyNodes.toArray(new StatementNode[bodyNodes.size()]));
	}
	
	public ExpressionNode createFunctionNode(Token tokenName){
		if(context.getFunctionRegistry().lookup(tokenName.val.toLowerCase()) == null)
			parser.SemErr("The function '" + tokenName.val + "' is undefined in the current context.");
			
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
	
	public ExpressionNode createLogicLiteral(boolean value){
		return new LogicLiteralNode(value);
	}
	
	public ExpressionNode createAssignment(Token nameToken, ExpressionNode valueNode){
		FrameSlot slot = frameDescriptor.findFrameSlot(nameToken.val.toLowerCase());
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
			return son;   // unary + in Pascal marks identity
		case "-":
			return NegationNodeGen.create(son);
		case "not":
			return NotNodeGen.create(son);
		default:
			parser.SemErr("Unexpected unary operator: " + operator.val);
			return null;
		}
	}
}
