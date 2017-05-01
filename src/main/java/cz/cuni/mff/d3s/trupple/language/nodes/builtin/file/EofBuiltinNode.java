package cz.cuni.mff.d3s.trupple.language.nodes.builtin.file;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.PascalLanguage;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;

@NodeInfo(shortName = "eof")
@NodeChild(value = "arguments", type = ExpressionNode[].class)
public abstract class EofBuiltinNode extends ExpressionNode {

    @Specialization
    boolean isEof(Object... arguments) {
        return (arguments.length == 0)? eof() : eof((FileValue) arguments[0]);
    }

    private boolean eof() {
        return !PascalLanguage.INSTANCE.findContext().getInput().hasNext();
    }

    private boolean eof(FileValue file) {
        return file.eof();
    }

    @Override
    public TypeDescriptor getType() {
        return BooleanDescriptor.getInstance();
    }

}
