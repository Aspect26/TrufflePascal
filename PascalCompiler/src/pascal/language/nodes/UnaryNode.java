package pascal.language.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;

@NodeChildren({@NodeChild("son")})
public abstract class UnaryNode extends ExpressionNode {

}
