package cz.cuni.mff.d3s.trupple.language.nodes.builtin.file;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "eof")
@NodeChild(value = "arguments", type = ExpressionNode[].class)
public abstract class EofBuiltinNode extends BuiltinNode {

    @Specialization
    boolean isEof(Object... arguments) {
        return (arguments.length == 0)? eof() : eof((FileValue) arguments[0]);
    }

    private boolean eof() {
        return !PascalContext.getInstance().getInput().hasNext();
    }

    private boolean eof(FileValue file) {
        return file.eof();
    }

}
