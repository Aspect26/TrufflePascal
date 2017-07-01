package cz.cuni.mff.d3s.trupple.language.nodes.builtin.file;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.PascalLanguage;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.TextFileValue;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;

@NodeInfo(shortName = "eol")
@NodeChild(value = "arguments", type = ExpressionNode[].class)
public abstract class EolBuiltinNode extends ExpressionNode {

    @Specialization
    boolean isEol(Object... arguments) {
        return (arguments.length == 0)? eol() : eol((TextFileValue) arguments[0]);
    }

    private boolean eol() {
        return !PascalLanguage.INSTANCE.findContext().getInput().hasNext();
    }

    private boolean eol(TextFileValue file) {
        return file.eol();
    }

    @Override
    public TypeDescriptor getType() {
        return BooleanDescriptor.getInstance();
    }

}
