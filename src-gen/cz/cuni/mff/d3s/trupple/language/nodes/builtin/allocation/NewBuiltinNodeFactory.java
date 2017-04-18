// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.allocation;

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
import cz.cuni.mff.d3s.trupple.language.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import java.util.Arrays;
import java.util.List;

@GeneratedBy(NewBuiltinNode.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class NewBuiltinNodeFactory implements NodeFactory<NewBuiltinNode> {

    private static NewBuiltinNodeFactory instance;

    private NewBuiltinNodeFactory() {
    }

    @Override
    public Class<NewBuiltinNode> getNodeClass() {
        return NewBuiltinNode.class;
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
    public NewBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 1 && (arguments[0] == null || arguments[0] instanceof ExpressionNode)) {
            return create((ExpressionNode) arguments[0]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<NewBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new NewBuiltinNodeFactory();
        }
        return instance;
    }

    public static NewBuiltinNode create(ExpressionNode argument) {
        return new NewBuiltinNodeGen(argument);
    }

    @GeneratedBy(NewBuiltinNode.class)
    public static final class NewBuiltinNodeGen extends NewBuiltinNode {

        @Child private ExpressionNode argument_;
        @CompilationFinal private boolean seenUnsupported0;

        private NewBuiltinNodeGen(ExpressionNode argument) {
            this.argument_ = argument;
        }

        @Override
        public NodeCost getCost() {
            return NodeCost.MONOMORPHIC;
        }

        @Override
        public Object executeGeneric(VirtualFrame frameValue) {
            PointerValue argumentValue_;
            try {
                argumentValue_ = PascalTypesGen.expectPointerValue(argument_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                throw unsupported(ex.getResult());
            }
            return this.allocate(argumentValue_);
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            executeGeneric(frameValue);
            return;
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
