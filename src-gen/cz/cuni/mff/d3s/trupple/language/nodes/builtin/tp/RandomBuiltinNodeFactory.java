// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.tp;

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
import java.util.Arrays;
import java.util.List;

@GeneratedBy(RandomBuiltinNode.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class RandomBuiltinNodeFactory implements NodeFactory<RandomBuiltinNode> {

    private static RandomBuiltinNodeFactory instance;

    private RandomBuiltinNodeFactory() {
    }

    @Override
    public Class<RandomBuiltinNode> getNodeClass() {
        return RandomBuiltinNode.class;
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
    public RandomBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 1 && (arguments[0] == null || arguments[0] instanceof ExpressionNode)) {
            return create((ExpressionNode) arguments[0]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<RandomBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new RandomBuiltinNodeFactory();
        }
        return instance;
    }

    public static RandomBuiltinNode create(ExpressionNode argument) {
        return new RandomBuiltinNodeGen(argument);
    }

    @GeneratedBy(RandomBuiltinNode.class)
    public static final class RandomBuiltinNodeGen extends RandomBuiltinNode {

        @Child private ExpressionNode argument_;
        @CompilationFinal private boolean seenUnsupported0;

        private RandomBuiltinNodeGen(ExpressionNode argument) {
            this.argument_ = argument;
        }

        @Override
        public NodeCost getCost() {
            return NodeCost.MONOMORPHIC;
        }

        @Override
        public Object executeGeneric(VirtualFrame frameValue) {
            return executeLong(frameValue);
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            executeLong(frameValue);
            return;
        }

        @Override
        public long executeLong(VirtualFrame frameValue) {
            long argumentValue_;
            try {
                argumentValue_ = argument_.executeLong(frameValue);
            } catch (UnexpectedResultException ex) {
                throw unsupported(ex.getResult());
            }
            return this.getRandom(argumentValue_);
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
