package pascal.language.nodes.builtin;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "writeln")
@GenerateNodeFactory
public abstract class WritelnBuiltinNode extends BuiltinNode {

	@Specialization
    public long writeln(long value) {
        getContext().getOutput().println(value);
        return value;
    }

    @Specialization
    public boolean writeln(boolean value) {
        getContext().getOutput().println(value);
        return value;
    }

    @Specialization
    public String writeln(String value) {
        getContext().getOutput().println(value);
        return value;
    }
	
	@Specialization
    public Object writeln(Object value) {
        getContext().getOutput().println(value);
        return value;
    }
}
