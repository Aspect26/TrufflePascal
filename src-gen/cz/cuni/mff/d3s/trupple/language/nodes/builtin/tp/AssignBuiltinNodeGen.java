// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.tp;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalString;

@GeneratedBy(AssignBuiltinNode.class)
public final class AssignBuiltinNodeGen extends AssignBuiltinNode {

    @Child private ExpressionNode file_;
    @Child private ExpressionNode path_;
    @CompilationFinal private boolean seenUnsupported0;

    private AssignBuiltinNodeGen(ExpressionNode file, ExpressionNode path) {
        this.file_ = file;
        this.path_ = path;
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        FileValue fileValue_;
        try {
            fileValue_ = expectFileValue(file_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            Object pathValue = path_.executeGeneric(frameValue);
            throw unsupported(ex.getResult(), pathValue);
        }
        PascalString pathValue_;
        try {
            pathValue_ = expectPascalString(path_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            throw unsupported(fileValue_, ex.getResult());
        }
        this.assignFile(fileValue_, pathValue_);
        return;
    }

    private UnsupportedSpecializationException unsupported(Object fileValue, Object pathValue) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {file_, path_}, fileValue, pathValue);
    }

    private static FileValue expectFileValue(Object value) throws UnexpectedResultException {
        if (value instanceof FileValue) {
            return (FileValue) value;
        }
        throw new UnexpectedResultException(value);
    }

    private static PascalString expectPascalString(Object value) throws UnexpectedResultException {
        if (value instanceof PascalString) {
            return (PascalString) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static AssignBuiltinNode create(ExpressionNode file, ExpressionNode path) {
        return new AssignBuiltinNodeGen(file, path);
    }

}
