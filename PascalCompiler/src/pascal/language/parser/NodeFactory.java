package pascal.language.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

import pascal.language.nodes.BlockNode;
import pascal.language.nodes.ExpressionNode;
import pascal.language.nodes.FunctionBodyNode;
import pascal.language.nodes.FunctionLiteralNode;
import pascal.language.nodes.PascalRootNode;
import pascal.language.nodes.StatementNode;
import pascal.language.nodes.StringLiteralNode;
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
    
	private List<StatementNode> functionNodes;
	private PascalRootNode mainNode;
	
	 /* State while parsing a source unit. */
    private final PascalContext context;
    private final Source source;
    
    /* State while parsing a function. */
    private int parameterCount;
    private FrameDescriptor frameDescriptor;
    
    /* State while parsing a block. */
    private LexicalScope lexicalScope;
    
    public NodeFactory(PascalContext context, Source source){
    	this.context = context;
    	this.source = source;
    }
	
	public void startMainFunction(){
		frameDescriptor = new FrameDescriptor();
		functionNodes = new ArrayList<>();
	}
	
	public void finishMainFunction(){
		mainNode = new PascalRootNode(context, frameDescriptor, 
				new FunctionBodyNode(new BlockNode(functionNodes.toArray(new StatementNode[functionNodes.size()]))));
	}
	
	public void startMainBlock(){
		lexicalScope = new LexicalScope(lexicalScope);
	}
	
	public void finishMainBlock(List<StatementNode> body){
		lexicalScope = lexicalScope.outer;
	}
	
	public ExpressionNode createFunctionNode(Token tokenName){
		final FrameSlot frameSlot = lexicalScope.locals.get(tokenName.val);
		return new FunctionLiteralNode(tokenName.val);
	}
	
	public ExpressionNode createCall(ExpressionNode functionNode, List<ExpressionNode> params){
		return InvokeNodeGen.create(params.toArray(new ExpressionNode[params.size()]), functionNode);
	}
	
	public ExpressionNode createStringLiteral(Token literalToken){
		/* Remove the trailing and ending " */
		String literal = literalToken.val;
		assert literal.length() >= 2 && literal.startsWith("'") && literal.endsWith("'");
		literal = literal.substring(1, literal.length() - 1);
		
		return new StringLiteralNode(literal);
	}
}
