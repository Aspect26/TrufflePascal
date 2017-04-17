package cz.cuni.mff.d3s.trupple.language.nodes.function;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.FileDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.StringDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ProgramArgumentAssignmentNode extends StatementNode{

    private static List<Class<? extends TypeDescriptor>> supportedTypes = new ArrayList<Class<? extends TypeDescriptor>>() {
        {
            add(StringDescriptor.class);
            add(FileDescriptor.class);
        }
    };

    private final FrameSlot targetSlot;
    private final TypeDescriptor variableType;
    private final int argumentNumber;

    public ProgramArgumentAssignmentNode(FrameSlot targetSlot, TypeDescriptor variableType, int argumentNumber) {
        this.targetSlot = targetSlot;
        this.variableType = variableType;
        this.argumentNumber = argumentNumber + 1; // NOTE: the +1 is because first argument is always parent's frame
    }

    public static boolean supportsType(TypeDescriptor type) {
        return supportedTypes.contains(type.getClass());
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        Object[] programArguments = frame.getArguments();
        if (argumentNumber >= programArguments.length) {
            throw new PascalRuntimeException("Too many arguments supplied to the program.");
        }

        this.assignProgramArgument(frame, (String) programArguments[argumentNumber]);
    }

    private void assignProgramArgument(VirtualFrame frame, String argumentValue) {
        if (variableType instanceof StringDescriptor) {
            this.assignString(frame, argumentValue);
        } else if (variableType instanceof FileDescriptor) {
            this.assignFile(frame, argumentValue);
        }
    }

    private void assignString(VirtualFrame frame, String value) {
        frame.setObject(this.targetSlot, value);
    }

    private void assignFile(VirtualFrame frame, String filePath) {
        try {
            FileValue file = (FileValue) frame.getObject(targetSlot);
            file.assignFilePath(filePath);
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Something went wrong");
        }
    }
}
