package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.dos;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;

import java.io.IOException;

/**
 * Official specification:
 * Exec executes the program in command argument, with the options given by arguments argument. The program name should
 * not appear again in arguments, it is specified in command. Arguments contains only the parameters that are passed to
 * the program.
 * After the program has terminated, the procedure returns. The Exit value of the program can be consulted with the
 * DosExitCode function.
 *
 * Differences:
 * DosExitCode function is not implemented
 */
@NodeChildren({ @NodeChild(type = ExpressionNode.class), @NodeChild(type = ExpressionNode.class)})
public abstract class ExecNode extends StatementNode {

    @Specialization
    void exec(String command, String arguments) {
        try {
            Runtime.getRuntime().exec(command + " " + arguments).waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error executing command " + command + ". " + e.getMessage());
        }
    }

}
