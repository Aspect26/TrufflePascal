// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.internal.SpecializationNode;
import com.oracle.truffle.api.dsl.internal.SpecializedNode;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import java.util.Arrays;
import java.util.List;

@GeneratedBy(ExpBuiltinNode.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class ExpBuiltinNodeFactory implements NodeFactory<ExpBuiltinNode> {

    private static ExpBuiltinNodeFactory instance;

    private ExpBuiltinNodeFactory() {
    }

    @Override
    public Class<ExpBuiltinNode> getNodeClass() {
        return ExpBuiltinNode.class;
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
    public ExpBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 1 && (arguments[0] == null || arguments[0] instanceof ExpressionNode)) {
            return create((ExpressionNode) arguments[0]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<ExpBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new ExpBuiltinNodeFactory();
        }
        return instance;
    }

    public static ExpBuiltinNode create(ExpressionNode argument) {
        return new ExpBuiltinNodeGen(argument);
    }

    @GeneratedBy(ExpBuiltinNode.class)
    public static final class ExpBuiltinNodeGen extends ExpBuiltinNode implements SpecializedNode {

        @Child private ExpressionNode argument_;
        @CompilationFinal private Class<?> argumentType_;
        @Child private BaseNode_ specialization_;

        private ExpBuiltinNodeGen(ExpressionNode argument) {
            this.argument_ = argument;
            this.specialization_ = UninitializedNode_.create(this);
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

        @GeneratedBy(ExpBuiltinNode.class)
        private abstract static class BaseNode_ extends SpecializationNode {

            @CompilationFinal protected ExpBuiltinNodeGen root;

            BaseNode_(ExpBuiltinNodeGen root, int index) {
                super(index);
                this.root = root;
            }

            @Override
            protected final void setRoot(Node root) {
                this.root = (ExpBuiltinNodeGen) root;
            }

            @Override
            protected final Node[] getSuppliedChildren() {
                return new Node[] {root.argument_};
            }

            @Override
            public final Object acceptAndExecute(Frame frameValue, Object argumentValue) {
                return this.executeDouble_((VirtualFrame) frameValue, argumentValue);
            }

            public abstract double executeDouble_(VirtualFrame frameValue, Object argumentValue);

            public Object execute(VirtualFrame frameValue) {
                Object argumentValue_ = executeArgument_(frameValue);
                return executeDouble_(frameValue, argumentValue_);
            }

            public void executeVoid(VirtualFrame frameValue) {
                execute(frameValue);
                return;
            }

            @Override
            protected final SpecializationNode createNext(Frame frameValue, Object argumentValue) {
                if (argumentValue instanceof Long) {
                    return IntegerExponentialValueNode_.create(root);
                }
                if (argumentValue instanceof Double) {
                    return UbleExponentialValueNode_.create(root);
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

            protected final Object executeArgument_(Frame frameValue) {
                Class<?> argumentType_ = root.argumentType_;
                try {
                    if (argumentType_ == long.class) {
                        return root.argument_.executeLong((VirtualFrame) frameValue);
                    } else if (argumentType_ == null) {
                        CompilerDirectives.transferToInterpreterAndInvalidate();
                        Class<?> _type = Object.class;
                        try {
                            Object _value = root.argument_.executeGeneric((VirtualFrame) frameValue);
                            if (_value instanceof Long) {
                                _type = long.class;
                            } else {
                                _type = Object.class;
                            }
                            return _value;
                        } finally {
                            root.argumentType_ = _type;
                        }
                    } else {
                        return root.argument_.executeGeneric((VirtualFrame) frameValue);
                    }
                } catch (UnexpectedResultException ex) {
                    root.argumentType_ = Object.class;
                    return ex.getResult();
                }
            }

        }
        @GeneratedBy(ExpBuiltinNode.class)
        private static final class UninitializedNode_ extends BaseNode_ {

            UninitializedNode_(ExpBuiltinNodeGen root) {
                super(root, 2147483647);
            }

            @Override
            public double executeDouble_(VirtualFrame frameValue, Object argumentValue) {
                return (double) uninitialized(frameValue, argumentValue);
            }

            static BaseNode_ create(ExpBuiltinNodeGen root) {
                return new UninitializedNode_(root);
            }

        }
        @GeneratedBy(ExpBuiltinNode.class)
        private static final class PolymorphicNode_ extends BaseNode_ {

            PolymorphicNode_(ExpBuiltinNodeGen root) {
                super(root, 0);
            }

            @Override
            public SpecializationNode merge(SpecializationNode newNode, Frame frameValue, Object argumentValue) {
                return polymorphicMerge(newNode, super.merge(newNode, frameValue, argumentValue));
            }

            @Override
            public double executeDouble_(VirtualFrame frameValue, Object argumentValue) {
                return getNext().executeDouble_(frameValue, argumentValue);
            }

            static BaseNode_ create(ExpBuiltinNodeGen root) {
                return new PolymorphicNode_(root);
            }

        }
        @GeneratedBy(methodName = "integerExponentialValue(long)", value = ExpBuiltinNode.class)
        private static final class IntegerExponentialValueNode_ extends BaseNode_ {

            IntegerExponentialValueNode_(ExpBuiltinNodeGen root) {
                super(root, 1);
            }

            @Override
            public Object execute(VirtualFrame frameValue) {
                long argumentValue_;
                try {
                    argumentValue_ = root.argument_.executeLong(frameValue);
                } catch (UnexpectedResultException ex) {
                    return getNext().executeDouble_(frameValue, ex.getResult());
                }
                return root.integerExponentialValue(argumentValue_);
            }

            @Override
            public double executeDouble_(VirtualFrame frameValue, Object argumentValue) {
                if (argumentValue instanceof Long) {
                    long argumentValue_ = (long) argumentValue;
                    return root.integerExponentialValue(argumentValue_);
                }
                return getNext().executeDouble_(frameValue, argumentValue);
            }

            static BaseNode_ create(ExpBuiltinNodeGen root) {
                return new IntegerExponentialValueNode_(root);
            }

        }
        @GeneratedBy(methodName = "doubleExponentialValue(double)", value = ExpBuiltinNode.class)
        private static final class UbleExponentialValueNode_ extends BaseNode_ {

            UbleExponentialValueNode_(ExpBuiltinNodeGen root) {
                super(root, 2);
            }

            @Override
            public double executeDouble_(VirtualFrame frameValue, Object argumentValue) {
                if (argumentValue instanceof Double) {
                    double argumentValue_ = (double) argumentValue;
                    return root.doubleExponentialValue(argumentValue_);
                }
                return getNext().executeDouble_(frameValue, argumentValue);
            }

            static BaseNode_ create(ExpBuiltinNodeGen root) {
                return new UbleExponentialValueNode_(root);
            }

        }
    }
}
