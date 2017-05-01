package cz.cuni.mff.d3s.trupple.language.nodes.builtin.io;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.PascalLanguage;

import java.util.Scanner;

@NodeInfo(shortName = "readln")
public abstract class ReadlnBuiltinNode extends ReadBuiltinNode {

    @Specialization
    public void read(Object[] arguments) {
	    if (arguments.length == 0) {
	        this.consumeNewLine();
        } else {
            super.read(arguments);
            this.consumeNewLine();
        }
    }

    private void consumeNewLine() {
        PascalLanguage.INSTANCE.findContext().getInput().nextLine();
    }
}
