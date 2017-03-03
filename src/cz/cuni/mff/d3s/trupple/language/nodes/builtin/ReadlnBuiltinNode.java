package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;

import java.io.IOException;
import java.util.regex.Pattern;

@NodeInfo(shortName = "readln")
public abstract class ReadlnBuiltinNode extends ReadBuiltinNode {

	@Specialization
    public Object read(Object[] arguments) {
        Object returnValue = super.read(arguments);
        this.consumeNewLine();

        return returnValue;
    }

    private void consumeNewLine() {
        this.getContext().getInput().nextLine();
    }
}
