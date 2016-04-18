package pascal.language.nodes.builtin;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "write")
@GenerateNodeFactory
public abstract class WriteBuiltinNode extends BuiltinNode{

	@Specialization
    public long write(long value) {
        getContext().getOutput().print(value);
        return value;
    }

    @Specialization
    public boolean write(boolean value) {
        getContext().getOutput().print(value);
        return value;
    }

    @Specialization
    public String write(String value) {
        getContext().getOutput().print(value);
        return value;
    }
	
	@Specialization
    public Object write(Object value) {
        getContext().getOutput().print(value);
        return value;
    }
}
