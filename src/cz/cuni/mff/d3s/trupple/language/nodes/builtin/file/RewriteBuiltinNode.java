package cz.cuni.mff.d3s.trupple.language.nodes.builtin.file;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;

@NodeInfo(shortName = "rewrite")
@NodeChild(value = "file", type = ExpressionNode.class)
public abstract class RewriteBuiltinNode extends StatementNode {

    @Specialization
    void rewrite(FileValue file) {
        file.openToWrite();
    }

}
