package pascal.language.nodes.builtin;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "writeln")
public abstract class WritelnBuiltinNode extends BuiltinNode{
	
	@Specialization
    public String println(String value) {
       getContext().getOutput().println(value);
        return value;
    }
	
	@Specialization
    public Object println(Object value) {
       getContext().getOutput().println(value);
        return value;
    }
}
