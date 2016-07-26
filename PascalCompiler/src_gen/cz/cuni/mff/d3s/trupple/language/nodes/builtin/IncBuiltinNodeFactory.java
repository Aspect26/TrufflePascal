// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.dsl.internal.NodeFactoryBase;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@GeneratedBy(IncBuiltinNode.class)
public final class IncBuiltinNodeFactory extends NodeFactoryBase<IncBuiltinNode> {

    private static IncBuiltinNodeFactory instance;

    private IncBuiltinNodeFactory() {
        super(IncBuiltinNode.class, new Class<?>[] {ExpressionNode.class}, new Class<?>[][] {new Class<?>[] {ExpressionNode[].class, PascalContext.class}});
    }

    @Override
    public IncBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof ExpressionNode[]) && (arguments[1] == null || arguments[1] instanceof PascalContext)) {
            return create((ExpressionNode[]) arguments[0], (PascalContext) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<IncBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new IncBuiltinNodeFactory();
        }
        return instance;
    }

    public static IncBuiltinNode create(ExpressionNode[] arguments, PascalContext context) {
        return new IncBuiltinNodeGen(arguments, context);
    }

    @GeneratedBy(IncBuiltinNode.class)
    public static final class IncBuiltinNodeGen extends IncBuiltinNode {

        private final PascalContext context;
        @Child private ExpressionNode arguments0_;
        @CompilationFinal private boolean seenUnsupported0;

        private IncBuiltinNodeGen(ExpressionNode[] arguments, PascalContext context) {
            this.context = context;
            this.arguments0_ = arguments != null && 0 < arguments.length ? arguments[0] : null;
        }

        @Override
        public PascalContext getContext() {
            return this.context;
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
            FrameSlot arguments0Value_;
            try {
                arguments0Value_ = expectFrameSlot(arguments0_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                throw unsupported(ex.getResult());
            }
            return this.incLong(frameValue, arguments0Value_);
        }

        private UnsupportedSpecializationException unsupported(Object arguments0Value) {
            if (!seenUnsupported0) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                seenUnsupported0 = true;
            }
            return new UnsupportedSpecializationException(this, new Node[] {arguments0_}, arguments0Value);
        }

        private static FrameSlot expectFrameSlot(Object value) throws UnexpectedResultException {
            if (value instanceof FrameSlot) {
                return (FrameSlot) value;
            }
            throw new UnexpectedResultException(value);
        }

    }
}
