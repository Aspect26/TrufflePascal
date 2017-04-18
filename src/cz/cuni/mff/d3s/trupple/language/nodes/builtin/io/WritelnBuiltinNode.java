package cz.cuni.mff.d3s.trupple.language.nodes.builtin.io;

import java.io.PrintStream;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "writeln")
@NodeChild(value = "arguments", type = ExpressionNode[].class)
public abstract class WritelnBuiltinNode extends BuiltinNode {


	@Specialization
	public Object writeln(Object... arguments) {
		doWriteln(PascalContext.getInstance().getOutput(), arguments);
        return new Object();
	}

	@TruffleBoundary
	private static void doWriteln(PrintStream out, Object... arguments) {
		for (Object agument : arguments)
			out.print(agument);

		out.println();
	}
}
