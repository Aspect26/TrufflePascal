package pascal.language.nodes.builtin;

import java.io.PrintStream;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "write")
public abstract class WriteBuiltinNode extends BuiltinNode{

	public WriteBuiltinNode() {
    }

    @Specialization
    public String write(String value) {
    	doWrite(getContext().getOutput(), value);
        return value;
    }

    @TruffleBoundary
    private static void doWrite(PrintStream out, String value) {
        out.print(value);
    }

    @Specialization
    public Object write(Object value) {
    	doWrite(getContext().getOutput(), value);
        return value;
    }

    @TruffleBoundary
    private static void doWrite(PrintStream out, Object value) {
        out.println(value);
    }
}
