// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.internal.DSLMetadata;
import com.oracle.truffle.api.dsl.internal.NodeFactoryBase;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@GeneratedBy(DecBuiltinNode.class)
public final class DecBuiltinNodeFactory extends NodeFactoryBase<DecBuiltinNode> {

    private static DecBuiltinNodeFactory instance;

    private DecBuiltinNodeFactory() {
        super(DecBuiltinNode.class, DSLMetadata.EMPTY_CLASS_ARRAY, new Class<?>[][] {new Class<?>[] {ExpressionNode[].class, PascalContext.class, FrameSlot.class}});
    }

    @Override
    public DecBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 3 && (arguments[0] == null || arguments[0] instanceof ExpressionNode[]) && (arguments[1] == null || arguments[1] instanceof PascalContext) && (arguments[2] == null || arguments[2] instanceof FrameSlot)) {
            return create((ExpressionNode[]) arguments[0], (PascalContext) arguments[1], (FrameSlot) arguments[2]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<DecBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new DecBuiltinNodeFactory();
        }
        return instance;
    }

    public static DecBuiltinNode create(ExpressionNode[] arguments, PascalContext context, FrameSlot slot) {
        return new DecBuiltinNodeGen(arguments, context, slot);
    }

    @GeneratedBy(DecBuiltinNode.class)
    public static final class DecBuiltinNodeGen extends DecBuiltinNode {

        private final PascalContext context;
        private final FrameSlot slot;

        @SuppressWarnings("unused")
        private DecBuiltinNodeGen(ExpressionNode[] arguments, PascalContext context, FrameSlot slot) {
            this.context = context;
            this.slot = slot;
        }

        @Override
        public PascalContext getContext() {
            return this.context;
        }

        @Override
        protected FrameSlot getSlot() {
            return this.slot;
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
            return this.decLong(frameValue);
        }

    }
}
