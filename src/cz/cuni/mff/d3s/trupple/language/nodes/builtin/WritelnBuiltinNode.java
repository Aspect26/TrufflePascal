package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import java.io.PrintStream;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "writeln")
@NodeChild(value = "arguments", type = ExpressionNode[].class)
public abstract class WritelnBuiltinNode extends BuiltinNode {

    public WritelnBuiltinNode(PascalContext context) {
        super(context);
    }

    private PascalContext getContext() {
        return this.context;
    }

	// TODO: specializations

	@Specialization
	public String writeln(String... arguments) {
		doWriteln(getContext().getOutput(), arguments);

		// TODO: this return value?
		return arguments[0];
	}

	@TruffleBoundary
	private static void doWriteln(PrintStream out, String[] arguments) {
		for (String agument : arguments)
			out.print(agument);

		out.println();
	}

	@Specialization
	public long writeln(long... arguments) {
		doWriteln(getContext().getOutput(), arguments);

		// TODO: this return value?
		return arguments[0];
	}

	@TruffleBoundary
	private static void doWriteln(PrintStream out, long... arguments) {
		for (long agument : arguments)
			out.print(agument);

		out.println();
	}

	@Specialization
	public Object writeln(Object... arguments) {
		doWriteln(getContext().getOutput(), arguments);

		// TODO: this return value?
		return arguments[0];
	}

	@TruffleBoundary
	private static void doWriteln(PrintStream out, Object... arguments) {
		for (Object agument : arguments)
			out.print(agument);

		out.println();
	}
}
