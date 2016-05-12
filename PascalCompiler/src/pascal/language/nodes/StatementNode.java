package pascal.language.nodes;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.SourceSection;

public abstract class StatementNode extends Node{

	//@CompilationFinal private SourceSection section;
	
	public StatementNode(){
	}
	
	public abstract void executeVoid(VirtualFrame frame);
}
