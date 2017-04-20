// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.file;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(RewriteBuiltinNode.class)
public final class RewriteBuiltinNodeGen extends RewriteBuiltinNode {

    @Child private ExpressionNode file_;
    @CompilationFinal private boolean seenUnsupported0;

    private RewriteBuiltinNodeGen(ExpressionNode file) {
        this.file_ = file;
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
            throw unsupported(ex.getResult());
        }
        this.rewrite(fileValue_);
        return;
    }

    private UnsupportedSpecializationException unsupported(Object fileValue) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {file_}, fileValue);
    }

    private static FileValue expectFileValue(Object value) throws UnexpectedResultException {
        if (value instanceof FileValue) {
            return (FileValue) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static RewriteBuiltinNode create(ExpressionNode file) {
        return new RewriteBuiltinNodeGen(file);
    }

}
