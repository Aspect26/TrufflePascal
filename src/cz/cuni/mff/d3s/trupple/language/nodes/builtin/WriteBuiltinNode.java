package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import java.io.PrintStream;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@NodeInfo(shortName = "write")
@NodeChild(value = "arguments", type = ExpressionNode[].class)
public abstract class WriteBuiltinNode extends BuiltinNode {

	// TODO specializations

	@Specialization
	public String write(String[] values) {
		doWrite(getContext().getOutput(), values);

		// TODO: this return value
		return values[0];
	}

	@TruffleBoundary
	private static void doWrite(PrintStream out, String[] values) {
		for (Object value : values)
			out.print(value);
	}

	@Specialization
	public Object write(Object[] values) {
		doWrite(getContext().getOutput(), values);

		// TODO: this return value
		return values[0];
	}

	@TruffleBoundary
	private static void doWrite(PrintStream out, Object[] values) {
		for (Object value : values)
			out.print(value);
	}
}
