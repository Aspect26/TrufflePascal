package cz.cuni.mff.d3s.trupple.language.nodes.builtin.io;

import java.io.PrintStream;
import java.util.Arrays;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.PascalContext;

@NodeInfo(shortName = "write")
@NodeChild(value = "arguments", type = ExpressionNode[].class)
@GenerateNodeFactory
public abstract class WriteBuiltinNode extends StatementNode {

	@Specialization
	public void write(Object[] values) {
        if (values[0] instanceof FileValue) {
            FileValue file = (FileValue) values[0];
            Object[] arguments = Arrays.copyOfRange(values, 1, values.length);
            file.write(arguments);
        } else {
            doWrite(values);
        }
	}

	@TruffleBoundary
	private static void doWrite(Object[] values) {
		for (Object value : values) {
            System.out.print(value);
        }
	}

}
