// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import java.util.Arrays;
import java.util.List;

@GeneratedBy(OddBuiltinNode.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class OddBuiltinNodeFactory implements NodeFactory<OddBuiltinNode> {

    private static OddBuiltinNodeFactory instance;

    private OddBuiltinNodeFactory() {
    }

    @Override
    public Class<OddBuiltinNode> getNodeClass() {
        return OddBuiltinNode.class;
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
    public OddBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof PascalContext) && (arguments[1] == null || arguments[1] instanceof ExpressionNode)) {
            return create((PascalContext) arguments[0], (ExpressionNode) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<OddBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new OddBuiltinNodeFactory();
        }
        return instance;
    }

    public static OddBuiltinNode create(PascalContext context, ExpressionNode argument) {
        return new OddBuiltinNodeGen(context, argument);
    }

    @GeneratedBy(OddBuiltinNode.class)
    public static final class OddBuiltinNodeGen extends OddBuiltinNode {

        @Child private ExpressionNode argument_;
        @CompilationFinal private boolean seenUnsupported0;

        private OddBuiltinNodeGen(PascalContext context, ExpressionNode argument) {
            super(context);
            this.argument_ = argument;
        }

        @Override
        public NodeCost getCost() {
            return NodeCost.MONOMORPHIC;
        }

        @Override
        public Object executeGeneric(VirtualFrame frameValue) {
            return executeBoolean(frameValue);
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            executeBoolean(frameValue);
            return;
        }

        @Override
        public boolean executeBoolean(VirtualFrame frameValue) {
            long argumentValue_;
            try {
                argumentValue_ = argument_.executeLong(frameValue);
            } catch (UnexpectedResultException ex) {
                throw unsupported(ex.getResult());
            }
            return this.odd(argumentValue_);
        }

        private UnsupportedSpecializationException unsupported(Object argumentValue) {
            if (!seenUnsupported0) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                seenUnsupported0 = true;
            }
            return new UnsupportedSpecializationException(this, new Node[] {argument_}, argumentValue);
        }

    }
}
