// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.internal.NodeFactoryBase;
import com.oracle.truffle.api.dsl.internal.SpecializationNode;
import com.oracle.truffle.api.dsl.internal.SpecializedNode;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@GeneratedBy(WriteBuiltinNode.class)
public final class WriteBuiltinNodeFactory extends NodeFactoryBase<WriteBuiltinNode> {

    private static WriteBuiltinNodeFactory instance;

    private WriteBuiltinNodeFactory() {
        super(WriteBuiltinNode.class, new Class<?>[] {ExpressionNode.class}, new Class<?>[][] {new Class<?>[] {ExpressionNode[].class, PascalContext.class}});
    }

    @Override
    public WriteBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof ExpressionNode[]) && (arguments[1] == null || arguments[1] instanceof PascalContext)) {
            return create((ExpressionNode[]) arguments[0], (PascalContext) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<WriteBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new WriteBuiltinNodeFactory();
        }
        return instance;
    }

    public static WriteBuiltinNode create(ExpressionNode[] arguments, PascalContext context) {
        return new WriteBuiltinNodeGen(arguments, context);
    }

    @GeneratedBy(WriteBuiltinNode.class)
    public static final class WriteBuiltinNodeGen extends WriteBuiltinNode implements SpecializedNode {

        private final PascalContext context;
        @Child private ExpressionNode arguments0_;
        @Child private BaseNode_ specialization_;

        private WriteBuiltinNodeGen(ExpressionNode[] arguments, PascalContext context) {
            this.context = context;
            this.arguments0_ = arguments != null && 0 < arguments.length ? arguments[0] : null;
            this.specialization_ = UninitializedNode_.create(this);
        }

        @Override
        public PascalContext getContext() {
            return this.context;
        }

        @Override
        public NodeCost getCost() {
            return specialization_.getNodeCost();
        }

        @Override
        public Object executeGeneric(VirtualFrame frameValue) {
            return specialization_.execute(frameValue);
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            specialization_.executeVoid(frameValue);
            return;
        }

        @Override
        public SpecializationNode getSpecializationNode() {
            return specialization_;
        }

        @Override
        public Node deepCopy() {
            return SpecializationNode.updateRoot(super.deepCopy());
        }

        @GeneratedBy(WriteBuiltinNode.class)
        private abstract static class BaseNode_ extends SpecializationNode {

            @CompilationFinal protected WriteBuiltinNodeGen root;

            BaseNode_(WriteBuiltinNodeGen root, int index) {
                super(index);
                this.root = root;
            }

            @Override
            protected final void setRoot(Node root) {
                this.root = (WriteBuiltinNodeGen) root;
            }

            @Override
            protected final Node[] getSuppliedChildren() {
                return new Node[] {root.arguments0_};
            }

            @Override
            public final Object acceptAndExecute(Frame frameValue, Object arguments0Value) {
                return this.execute_((VirtualFrame) frameValue, arguments0Value);
            }

            public abstract Object execute_(VirtualFrame frameValue, Object arguments0Value);

            public Object execute(VirtualFrame frameValue) {
                Object arguments0Value_ = root.arguments0_.executeGeneric(frameValue);
                return execute_(frameValue, arguments0Value_);
            }

            public void executeVoid(VirtualFrame frameValue) {
                execute(frameValue);
                return;
            }

            @Override
            protected final SpecializationNode createNext(Frame frameValue, Object arguments0Value) {
                if (arguments0Value instanceof String[]) {
                    return Write0Node_.create(root);
                }
                if (arguments0Value instanceof Object[]) {
                    return Write1Node_.create(root);
                }
                return null;
            }

            @Override
            protected final SpecializationNode createPolymorphic() {
                return PolymorphicNode_.create(root);
            }

            protected final BaseNode_ getNext() {
                return (BaseNode_) this.next;
            }

        }
        @GeneratedBy(WriteBuiltinNode.class)
        private static final class UninitializedNode_ extends BaseNode_ {

            UninitializedNode_(WriteBuiltinNodeGen root) {
                super(root, 2147483647);
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object arguments0Value) {
                return uninitialized(frameValue, arguments0Value);
            }

            static BaseNode_ create(WriteBuiltinNodeGen root) {
                return new UninitializedNode_(root);
            }

        }
        @GeneratedBy(WriteBuiltinNode.class)
        private static final class PolymorphicNode_ extends BaseNode_ {

            PolymorphicNode_(WriteBuiltinNodeGen root) {
                super(root, 0);
            }

            @Override
            public SpecializationNode merge(SpecializationNode newNode, Frame frameValue, Object arguments0Value) {
                return polymorphicMerge(newNode, super.merge(newNode, frameValue, arguments0Value));
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object arguments0Value) {
                return getNext().execute_(frameValue, arguments0Value);
            }

            static BaseNode_ create(WriteBuiltinNodeGen root) {
                return new PolymorphicNode_(root);
            }

        }
        @GeneratedBy(methodName = "write(String[])", value = WriteBuiltinNode.class)
        private static final class Write0Node_ extends BaseNode_ {

            Write0Node_(WriteBuiltinNodeGen root) {
                super(root, 1);
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object arguments0Value) {
                if (arguments0Value instanceof String[]) {
                    String[] arguments0Value_ = (String[]) arguments0Value;
                    return root.write(arguments0Value_);
                }
                return getNext().execute_(frameValue, arguments0Value);
            }

            static BaseNode_ create(WriteBuiltinNodeGen root) {
                return new Write0Node_(root);
            }

        }
        @GeneratedBy(methodName = "write(Object[])", value = WriteBuiltinNode.class)
        private static final class Write1Node_ extends BaseNode_ {

            Write1Node_(WriteBuiltinNodeGen root) {
                super(root, 2);
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object arguments0Value) {
                if (arguments0Value instanceof Object[]) {
                    Object[] arguments0Value_ = (Object[]) arguments0Value;
                    return root.write(arguments0Value_);
                }
                return getNext().execute_(frameValue, arguments0Value);
            }

            static BaseNode_ create(WriteBuiltinNodeGen root) {
                return new Write1Node_(root);
            }

        }
    }
}
