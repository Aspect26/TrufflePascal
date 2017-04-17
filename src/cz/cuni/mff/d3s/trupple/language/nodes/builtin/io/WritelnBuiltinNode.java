package cz.cuni.mff.d3s.trupple.language.nodes.builtin.io;

import java.io.PrintStream;
import java.util.Arrays;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
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

    @Specialization
    public Object writeln(Object[] values) {
        if (values[0] instanceof FileValue) {
            FileValue file = (FileValue) values[0];
            Object[] arguments = Arrays.copyOfRange(values, 1, values.length);
            file.writeln(arguments);
        } else {
            doWriteln(getContext().getOutput(), values);
        }

        // TODO: this return value
        return values[0];
    }

	@TruffleBoundary
	private static void doWriteln(PrintStream out, Object... arguments) {
		for (Object agument : arguments)
			out.print(agument);

		out.println();
	}

}
