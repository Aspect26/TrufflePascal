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
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import java.util.Arrays;
import java.util.List;

@GeneratedBy(ResetBuiltinNode.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class ResetBuiltinNodeFactory implements NodeFactory<ResetBuiltinNode> {

    private static ResetBuiltinNodeFactory instance;

    private ResetBuiltinNodeFactory() {
    }

    @Override
    public Class<ResetBuiltinNode> getNodeClass() {
        return ResetBuiltinNode.class;
    }

    @Override
    public List getExecutionSignature() {
        return Arrays.asList(ExpressionNode.class);
    }

    @Override
    public List getNodeSignatures() {
        return Arrays.asList(Arrays.asList(PascalContext.class, ExpressionNode.class));
    }

    @Override
    public ResetBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof PascalContext) && (arguments[1] == null || arguments[1] instanceof ExpressionNode)) {
            return create((PascalContext) arguments[0], (ExpressionNode) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<ResetBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new ResetBuiltinNodeFactory();
        }
        return instance;
    }

    public static ResetBuiltinNode create(PascalContext context, ExpressionNode file) {
        return new ResetBuiltinNodeGen(context, file);
    }

    @GeneratedBy(ResetBuiltinNode.class)
    public static final class ResetBuiltinNodeGen extends ResetBuiltinNode {

        @Child private ExpressionNode file_;
        @CompilationFinal private boolean seenUnsupported0;

        private ResetBuiltinNodeGen(PascalContext context, ExpressionNode file) {
            super(context);
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
            return this.reset(fileValue_);
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
