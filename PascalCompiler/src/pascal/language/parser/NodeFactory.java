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
import pascal.language.nodes.PascalRootNode;
import pascal.language.nodes.StatementNode;
import pascal.language.nodes.arithmetic.AddNodeGen;
import pascal.language.nodes.arithmetic.DivideIntegerNodeGen;
import pascal.language.nodes.arithmetic.ModuloNodeGen;
import pascal.language.nodes.arithmetic.MultiplyNodeGen;
import pascal.language.nodes.arithmetic.NegationNodeGen;
import pascal.language.nodes.arithmetic.SubstractNodeGen;
import pascal.language.nodes.call.InvokeNodeGen;
import pascal.language.nodes.function.FunctionBodyNode;
import pascal.language.nodes.literals.FunctionLiteralNode;
import pascal.language.nodes.literals.LongLiteralNode;
import pascal.language.nodes.literals.StringLiteralNode;
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
    
    public NodeFactory(PascalContext context, Source source){
    	this.context = context;
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
    	case "long":
    		slotKind  = FrameSlotKind.Long;
    	default:
    		slotKind = FrameSlotKind.Illegal;
    	}
    	
    	assert slotKind != FrameSlotKind.Illegal;
    	
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
	
	public ExpressionNode createFunctionNode(Token tokenName){
		return new FunctionLiteralNode(context, tokenName.val);
	}
	
	public ExpressionNode readVariable(Token nameToken){
		final FrameSlot frameSlot = lexicalScope.locals.get(nameToken.val);
		if(frameSlot == null)
			return null;
		
		return ReadVariableNodeGen.create(frameSlot);
	}
	
	public ExpressionNode createCall(ExpressionNode functionLiteral, List<ExpressionNode> params){
		return InvokeNodeGen.create(params.toArray(new ExpressionNode[params.size()]), functionLiteral);
	}
	
	public ExpressionNode createStringLiteral(Token literalToken){
		/* Remove the trailing and ending " */
		String literal = literalToken.val;
		assert literal.length() >= 2 && literal.startsWith("'") && literal.endsWith("'");
		literal = literal.substring(1, literal.length() - 1);
		
		return new StringLiteralNode(literal);
	}
	
	public ExpressionNode createNumericLiteral(Token literalToken){
		try{
			return new LongLiteralNode(Long.parseLong(literalToken.val));
		} catch (NumberFormatException ex){
			return null;
		}
	}
	
	public ExpressionNode createAssignment(Token nameToken, ExpressionNode valueNode){
		FrameSlot slot = frameDescriptor.findFrameSlot(nameToken.val);
		if(slot == null)
			return null;
		
		return AssignmentNodeGen.create(valueNode, slot);
	}
	
	public ExpressionNode createBinary(Token operator, ExpressionNode leftNode, ExpressionNode rightNode){
		switch(operator.val){
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
		default:
			//TODO: this -> SemErr
            throw new RuntimeException("Unexpected binary operator: " + operator.val);
		}
	}
	
	public ExpressionNode createUnary(Token operator, ExpressionNode son){
		switch(operator.val){
		case "+":
			return son;   // unary + in Pascal is for identity
		case "-":
			return NegationNodeGen.create(son);
		default:
			//TODO: this -> SemErr
            throw new RuntimeException("Unexpected unary operator: " + operator.val);
		}
		
	}
}
