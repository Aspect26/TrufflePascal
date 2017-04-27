package cz.cuni.mff.d3s.trupple.language.nodes.builtin.file;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;

@NodeInfo(shortName = "eol")
@NodeChild(value = "arguments", type = ExpressionNode[].class)
public abstract class EolBuiltinNode extends ExpressionNode {

    @Specialization
    boolean isEol(Object... arguments) {
        return (arguments.length == 0)? eol() : eol((FileValue) arguments[0]);
    }

    private boolean eol() {
        return !PascalContext.getInstance().getInput().hasNext();
    }

    private boolean eol(FileValue file) {
        return file.eol();
    }

    @Override
    public TypeDescriptor getType() {
        return BooleanDescriptor.getInstance();
    }

}
