package cz.cuni.mff.d3s.trupple.language.nodes.builtin.file;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "rewrite")
@NodeChild(value = "file", type = ExpressionNode.class)
public abstract class RewriteBuiltinNode extends BuiltinNode {

    public RewriteBuiltinNode(PascalContext context) {
        super(context);
    }

    @Specialization
    Object rewrite(FileValue file) {
        file.openToWrite();
        return file;
    }

}
