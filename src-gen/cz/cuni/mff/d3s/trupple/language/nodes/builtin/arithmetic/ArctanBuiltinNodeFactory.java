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
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import java.util.Arrays;
import java.util.List;

@GeneratedBy(ArctanBuiltinNode.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class ArctanBuiltinNodeFactory implements NodeFactory<ArctanBuiltinNode> {

    private static ArctanBuiltinNodeFactory instance;

    private ArctanBuiltinNodeFactory() {
    }

    @Override
    public Class<ArctanBuiltinNode> getNodeClass() {
        return ArctanBuiltinNode.class;
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
    public ArctanBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof PascalContext) && (arguments[1] == null || arguments[1] instanceof ExpressionNode)) {
            return create((PascalContext) arguments[0], (ExpressionNode) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<ArctanBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new ArctanBuiltinNodeFactory();
        }
        return instance;
    }

    public static ArctanBuiltinNode create(PascalContext context, ExpressionNode argument) {
        return new ArctanBuiltinNodeGen(context, argument);
    }

    @GeneratedBy(ArctanBuiltinNode.class)
    public static final class ArctanBuiltinNodeGen extends ArctanBuiltinNode implements SpecializedNode {

        @Child private ExpressionNode argument_;
        @CompilationFinal private Class<?> argumentType_;
        @Child private BaseNode_ specialization_;

        private ArctanBuiltinNodeGen(PascalContext context, ExpressionNode argument) {
            super(context);
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

        @GeneratedBy(ArctanBuiltinNode.class)
        private abstract static class BaseNode_ extends SpecializationNode {

            @CompilationFinal protected ArctanBuiltinNodeGen root;

            BaseNode_(ArctanBuiltinNodeGen root, int index) {
                super(index);
                this.root = root;
            }

            @Override
            protected final void setRoot(Node root) {
                this.root = (ArctanBuiltinNodeGen) root;
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
                    return IntegerArctanValueNode_.create(root);
                }
                if (argumentValue instanceof Double) {
                    return UbleArctanValueNode_.create(root);
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
        @GeneratedBy(ArctanBuiltinNode.class)
        private static final class UninitializedNode_ extends BaseNode_ {

            UninitializedNode_(ArctanBuiltinNodeGen root) {
                super(root, 2147483647);
            }

            @Override
            public double executeDouble_(VirtualFrame frameValue, Object argumentValue) {
                return (double) uninitialized(frameValue, argumentValue);
            }

            static BaseNode_ create(ArctanBuiltinNodeGen root) {
                return new UninitializedNode_(root);
            }

        }
        @GeneratedBy(ArctanBuiltinNode.class)
        private static final class PolymorphicNode_ extends BaseNode_ {

            PolymorphicNode_(ArctanBuiltinNodeGen root) {
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

            static BaseNode_ create(ArctanBuiltinNodeGen root) {
                return new PolymorphicNode_(root);
            }

        }
        @GeneratedBy(methodName = "integerArctanValue(long)", value = ArctanBuiltinNode.class)
        private static final class IntegerArctanValueNode_ extends BaseNode_ {

            IntegerArctanValueNode_(ArctanBuiltinNodeGen root) {
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
                return root.integerArctanValue(argumentValue_);
            }

            @Override
            public double executeDouble_(VirtualFrame frameValue, Object argumentValue) {
                if (argumentValue instanceof Long) {
                    long argumentValue_ = (long) argumentValue;
                    return root.integerArctanValue(argumentValue_);
                }
                return getNext().executeDouble_(frameValue, argumentValue);
            }

            static BaseNode_ create(ArctanBuiltinNodeGen root) {
                return new IntegerArctanValueNode_(root);
            }

        }
        @GeneratedBy(methodName = "doubleArctanValue(double)", value = ArctanBuiltinNode.class)
        private static final class UbleArctanValueNode_ extends BaseNode_ {

            UbleArctanValueNode_(ArctanBuiltinNodeGen root) {
                super(root, 2);
            }

            @Override
            public double executeDouble_(VirtualFrame frameValue, Object argumentValue) {
                if (argumentValue instanceof Double) {
                    double argumentValue_ = (double) argumentValue;
                    return root.doubleArctanValue(argumentValue_);
                }
                return getNext().executeDouble_(frameValue, argumentValue);
            }

            static BaseNode_ create(ArctanBuiltinNodeGen root) {
                return new UbleArctanValueNode_(root);
            }

        }
    }
}
