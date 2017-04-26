package cz.cuni.mff.d3s.trupple.language.nodes.builtin.tp;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalString;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;

@NodeInfo(shortName = "assign")
@NodeChildren({@NodeChild(value = "file", type = ExpressionNode.class), @NodeChild(value="path", type = ExpressionNode.class)})
public abstract class AssignBuiltinNode extends StatementNode {

    @Specialization
    void assignFile(FileValue file, PascalString filePath) {
        file.assignFilePath(filePath.toString());
    }

}
