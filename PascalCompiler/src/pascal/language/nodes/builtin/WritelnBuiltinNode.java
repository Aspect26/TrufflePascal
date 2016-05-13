package pascal.language.nodes.builtin;

import java.io.PrintWriter;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "writeln")
public abstract class WritelnBuiltinNode extends BuiltinNode{
	
	@Specialization
    public String println(String value) {
        doPrint(getContext().getOutput(), value);
        return value;
    }
	
	@TruffleBoundary
    private static void doPrint(PrintWriter out, String value) {
        out.println(value);
    }
	
	@Specialization
    public Object println(Object value) {
        doPrint(getContext().getOutput(), value);
        return value;
    }

    @TruffleBoundary
    private static void doPrint(PrintWriter out, Object value) {
        out.println(value);
    }
}
