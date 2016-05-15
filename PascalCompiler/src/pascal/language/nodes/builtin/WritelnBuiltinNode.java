package pascal.language.nodes.builtin;

import java.io.PrintStream;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "writeln")
public abstract class WritelnBuiltinNode extends BuiltinNode{

	public WritelnBuiltinNode() {
    }

    @Specialization
    public String writeln(String value) {
    	doWriteln(getContext().getOutput(), value);
        return value;
    }

    @TruffleBoundary
    private static void doWriteln(PrintStream out, String value) {
        out.println(value);
    }

    @Specialization
    public Object writeln(Object value) {
    	doWriteln(getContext().getOutput(), value);
        return value;
    }

    @TruffleBoundary
    private static void doWriteln(PrintStream out, Object value) {
        out.println(value);
    }
}
