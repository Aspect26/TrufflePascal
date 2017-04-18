package cz.cuni.mff.d3s.trupple.language.nodes.builtin.io;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "readln")
public abstract class ReadlnBuiltinNode extends ReadBuiltinNode {

    @Specialization
    public Object read(Object[] arguments) {
	    if (arguments.length == 0) {
	        return this.consumeNewLine();
        } else {
            Object returnValue = super.read(arguments);
            this.consumeNewLine();

            return returnValue;
        }
    }

    private String consumeNewLine() {
        return PascalContext.getInstance().getInput().nextLine();
    }
}
