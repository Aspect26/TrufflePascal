package cz.cuni.mff.d3s.trupple.language.nodes.builtin.tp;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "assign")
@NodeChildren({@NodeChild(value = "file", type = ExpressionNode.class), @NodeChild(value="path", type = ExpressionNode.class)})
public abstract class AssignBuiltinNode extends BuiltinNode {

    @Specialization
    Object assignFile(FileValue file, String filePath) {
        file.assignFilePath(filePath);
        return file;
    }
}
