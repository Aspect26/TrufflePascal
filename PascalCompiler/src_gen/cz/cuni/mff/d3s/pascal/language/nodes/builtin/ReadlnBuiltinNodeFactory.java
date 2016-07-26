// CheckStyle: start generated
package cz.cuni.mff.d3s.pascal.language.nodes.builtin;

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
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.pascal.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.pascal.language.runtime.PascalContext;

@GeneratedBy(ReadlnBuiltinNode.class)
public final class ReadlnBuiltinNodeFactory extends NodeFactoryBase<ReadlnBuiltinNode> {

    private static ReadlnBuiltinNodeFactory instance;

    private ReadlnBuiltinNodeFactory() {
        super(ReadlnBuiltinNode.class, new Class<?>[] {ExpressionNode.class}, new Class<?>[][] {new Class<?>[] {ExpressionNode[].class, PascalContext.class}});
    }

    @Override
    public ReadlnBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof ExpressionNode[]) && (arguments[1] == null || arguments[1] instanceof PascalContext)) {
            return create((ExpressionNode[]) arguments[0], (PascalContext) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<ReadlnBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new ReadlnBuiltinNodeFactory();
        }
        return instance;
    }

    public static ReadlnBuiltinNode create(ExpressionNode[] arguments, PascalContext context) {
        return new ReadlnBuiltinNodeGen(arguments, context);
    }

    @GeneratedBy(ReadlnBuiltinNode.class)
    public static final class ReadlnBuiltinNodeGen extends ReadlnBuiltinNode implements SpecializedNode {

        private final PascalContext context;
        @Child private ExpressionNode arguments0_;
        @Child private BaseNode_ specialization_;

        private ReadlnBuiltinNodeGen(ExpressionNode[] arguments, PascalContext context) {
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
        public long executeLong(VirtualFrame frameValue) {
            return specialization_.executeLong(frameValue);
        }

        @Override
        public SpecializationNode getSpecializationNode() {
            return specialization_;
        }

        @Override
        public Node deepCopy() {
            return SpecializationNode.updateRoot(super.deepCopy());
        }

        private static long[] expectLongArray(Object value) throws UnexpectedResultException {
            if (value instanceof long[]) {
                return (long[]) value;
            }
            throw new UnexpectedResultException(value);
        }

        @GeneratedBy(ReadlnBuiltinNode.class)
        private abstract static class BaseNode_ extends SpecializationNode {

            @CompilationFinal protected ReadlnBuiltinNodeGen root;

            BaseNode_(ReadlnBuiltinNodeGen root, int index) {
                super(index);
                this.root = root;
            }

            @Override
            protected final void setRoot(Node root) {
                this.root = (ReadlnBuiltinNodeGen) root;
            }

            @Override
            protected final Node[] getSuppliedChildren() {
                return new Node[] {root.arguments0_};
            }

            @Override
            public final Object acceptAndExecute(Frame frameValue, Object arguments0Value) {
                return this.executeLong_((VirtualFrame) frameValue, arguments0Value);
            }

            public abstract long executeLong_(VirtualFrame frameValue, Object arguments0Value);

            public Object execute(VirtualFrame frameValue) {
                Object arguments0Value_ = root.arguments0_.executeGeneric(frameValue);
                return executeLong_(frameValue, arguments0Value_);
            }

            public void executeVoid(VirtualFrame frameValue) {
                execute(frameValue);
                return;
            }

            public long executeLong(VirtualFrame frameValue) {
                return (long) execute(frameValue);
            }

            @Override
            protected final SpecializationNode createNext(Frame frameValue, Object arguments0Value) {
                if (arguments0Value instanceof String[]) {
                    return Readln0Node_.create(root);
                }
                if (arguments0Value instanceof long[]) {
                    return WritelnNode_.create(root);
                }
                if (arguments0Value instanceof Object[]) {
                    return Readln1Node_.create(root);
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
        @GeneratedBy(ReadlnBuiltinNode.class)
        private static final class UninitializedNode_ extends BaseNode_ {

            UninitializedNode_(ReadlnBuiltinNodeGen root) {
                super(root, 2147483647);
            }

            @Override
            public long executeLong_(VirtualFrame frameValue, Object arguments0Value) {
                return (long) uninitialized(frameValue, arguments0Value);
            }

            static BaseNode_ create(ReadlnBuiltinNodeGen root) {
                return new UninitializedNode_(root);
            }

        }
        @GeneratedBy(ReadlnBuiltinNode.class)
        private static final class PolymorphicNode_ extends BaseNode_ {

            PolymorphicNode_(ReadlnBuiltinNodeGen root) {
                super(root, 0);
            }

            @Override
            public SpecializationNode merge(SpecializationNode newNode, Frame frameValue, Object arguments0Value) {
                return polymorphicMerge(newNode, super.merge(newNode, frameValue, arguments0Value));
            }

            @Override
            public long executeLong(VirtualFrame frameValue) {
                Object arguments0Value_ = root.arguments0_.executeGeneric(frameValue);
                return getNext().executeLong_(frameValue, arguments0Value_);
            }

            @Override
            public long executeLong_(VirtualFrame frameValue, Object arguments0Value) {
                return getNext().executeLong_(frameValue, arguments0Value);
            }

            static BaseNode_ create(ReadlnBuiltinNodeGen root) {
                return new PolymorphicNode_(root);
            }

        }
        @GeneratedBy(methodName = "readln(String[])", value = ReadlnBuiltinNode.class)
        private static final class Readln0Node_ extends BaseNode_ {

            Readln0Node_(ReadlnBuiltinNodeGen root) {
                super(root, 1);
            }

            @Override
            public long executeLong_(VirtualFrame frameValue, Object arguments0Value) {
                if (arguments0Value instanceof String[]) {
                    String[] arguments0Value_ = (String[]) arguments0Value;
                    root.readln(arguments0Value_);
                    return 0L;
                }
                return getNext().executeLong_(frameValue, arguments0Value);
            }

            static BaseNode_ create(ReadlnBuiltinNodeGen root) {
                return new Readln0Node_(root);
            }

        }
        @GeneratedBy(methodName = "writeln(long[])", value = ReadlnBuiltinNode.class)
        private static final class WritelnNode_ extends BaseNode_ {

            WritelnNode_(ReadlnBuiltinNodeGen root) {
                super(root, 2);
            }

            @Override
            public long executeLong(VirtualFrame frameValue) {
                long[] arguments0Value_;
                try {
                    arguments0Value_ = expectLongArray(root.arguments0_.executeGeneric(frameValue));
                } catch (UnexpectedResultException ex) {
                    return getNext().executeLong_(frameValue, ex.getResult());
                }
                return root.writeln(arguments0Value_);
            }

            @Override
            public long executeLong_(VirtualFrame frameValue, Object arguments0Value) {
                if (arguments0Value instanceof long[]) {
                    long[] arguments0Value_ = (long[]) arguments0Value;
                    return root.writeln(arguments0Value_);
                }
                return getNext().executeLong_(frameValue, arguments0Value);
            }

            static BaseNode_ create(ReadlnBuiltinNodeGen root) {
                return new WritelnNode_(root);
            }

        }
        @GeneratedBy(methodName = "readln(Object[])", value = ReadlnBuiltinNode.class)
        private static final class Readln1Node_ extends BaseNode_ {

            Readln1Node_(ReadlnBuiltinNodeGen root) {
                super(root, 3);
            }

            @Override
            public long executeLong_(VirtualFrame frameValue, Object arguments0Value) {
                if (arguments0Value instanceof Object[]) {
                    Object[] arguments0Value_ = (Object[]) arguments0Value;
                    root.readln(arguments0Value_);
                    return 0L;
                }
                return getNext().executeLong_(frameValue, arguments0Value);
            }

            static BaseNode_ create(ReadlnBuiltinNodeGen root) {
                return new Readln1Node_(root);
            }

        }
    }
}
