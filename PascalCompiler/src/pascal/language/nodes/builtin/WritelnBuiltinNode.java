package pascal.language.nodes.builtin;

import java.io.PrintStream;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "writeln")
public abstract class WritelnBuiltinNode extends BuiltinNode{

	public WritelnBuiltinNode() {
    }

	/*
    @Specialization
    public String writeln(String... arguments) {
    	doWriteln(getContext().getOutput(), arguments);
    	
    	// TODO: this return value?
        return arguments[0];
    }

    @TruffleBoundary
    private static void doWriteln(PrintStream out, String[] arguments) {
    	for(String agument : arguments)
    		out.println(agument);
    }
    
    @Specialization
    public long writeln(long... arguments) {
    	doWriteln(getContext().getOutput(), arguments);
    	
    	// TODO: this return value?
        return arguments[0];
    }

    @TruffleBoundary
    private static void doWriteln(PrintStream out, long... arguments) {
    	for(long agument : arguments)
    		out.println(agument);
    }

    @Specialization
    public Object writeln(Object... arguments) {
    	doWriteln(getContext().getOutput(), arguments);
        
    	// TODO: this return value?
        return arguments[0];
    }

    @TruffleBoundary
    private static void doWriteln(PrintStream out, Object... arguments) {
    	for(Object agument : arguments)
    		out.println(agument);
    }
    */
	
	@Specialization
    public String writeln(String argument) {
    	doWriteln(getContext().getOutput(), argument);
    	return argument;
    }

    @TruffleBoundary
    private static void doWriteln(PrintStream out, String argument) {
    	out.println(argument);
    }
    
    @Specialization
    public long writeln(long argument) {
    	doWriteln(getContext().getOutput(), argument);
        return argument;
    }

    @TruffleBoundary
    private static void doWriteln(PrintStream out, long argument) {
    	out.println(argument);
    }

    @Specialization
    public Object writeln(Object argument) {
    	doWriteln(getContext().getOutput(), argument);
        return argument;
    }

    @TruffleBoundary
    private static void doWriteln(PrintStream out, Object argument) {
    	out.println(argument);
    }
}
