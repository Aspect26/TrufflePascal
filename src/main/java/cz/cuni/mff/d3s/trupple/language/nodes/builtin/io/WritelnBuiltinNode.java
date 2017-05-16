package cz.cuni.mff.d3s.trupple.language.nodes.builtin.io;

import java.util.Arrays;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;

@NodeInfo(shortName = "writeln")
@NodeChild(value = "arguments", type = ExpressionNode[].class)
@GenerateNodeFactory
public abstract class WritelnBuiltinNode extends StatementNode {

    @Specialization
    public void writeln(Object... values) {
        if (values[0] instanceof FileValue) {
            FileValue file = (FileValue) values[0];
            Object[] arguments = Arrays.copyOfRange(values, 1, values.length);
            file.writeln(arguments);
        } else {
            doWriteln(values);
        }
    }

	@TruffleBoundary
	private static void doWriteln(Object... arguments) {
		for (Object agument : arguments)
			System.out.print(agument);

		System.out.println();
	}

}
