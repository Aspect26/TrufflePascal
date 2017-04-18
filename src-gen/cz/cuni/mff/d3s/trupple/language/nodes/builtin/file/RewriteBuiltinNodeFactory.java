// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.file;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.PascalTypesGen;
import cz.cuni.mff.d3s.trupple.language.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import java.util.Arrays;
import java.util.List;

@GeneratedBy(RewriteBuiltinNode.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class RewriteBuiltinNodeFactory implements NodeFactory<RewriteBuiltinNode> {

    private static RewriteBuiltinNodeFactory instance;

    private RewriteBuiltinNodeFactory() {
    }

    @Override
    public Class<RewriteBuiltinNode> getNodeClass() {
        return RewriteBuiltinNode.class;
    }

    @Override
    public List getExecutionSignature() {
        return Arrays.asList(ExpressionNode.class);
    }

    @Override
    public List getNodeSignatures() {
        return Arrays.asList(Arrays.asList(ExpressionNode.class));
    }

    @Override
    public RewriteBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 1 && (arguments[0] == null || arguments[0] instanceof ExpressionNode)) {
            return create((ExpressionNode) arguments[0]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<RewriteBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new RewriteBuiltinNodeFactory();
        }
        return instance;
    }

    public static RewriteBuiltinNode create(ExpressionNode file) {
        return new RewriteBuiltinNodeGen(file);
    }

    @GeneratedBy(RewriteBuiltinNode.class)
    public static final class RewriteBuiltinNodeGen extends RewriteBuiltinNode {

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
        public Object executeGeneric(VirtualFrame frameValue) {
            FileValue fileValue_;
            try {
                fileValue_ = PascalTypesGen.expectFileValue(file_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                throw unsupported(ex.getResult());
            }
            return this.rewrite(fileValue_);
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            executeGeneric(frameValue);
            return;
        }

        private UnsupportedSpecializationException unsupported(Object fileValue) {
            if (!seenUnsupported0) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                seenUnsupported0 = true;
            }
            return new UnsupportedSpecializationException(this, new Node[] {file_}, fileValue);
        }

    }
}
