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
import cz.cuni.mff.d3s.trupple.language.PascalTypesGen;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import java.util.Arrays;
import java.util.List;

@GeneratedBy(SqrBuiltinNode.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class SqrBuiltinNodeFactory implements NodeFactory<SqrBuiltinNode> {

    private static SqrBuiltinNodeFactory instance;

    private SqrBuiltinNodeFactory() {
    }

    @Override
    public Class<SqrBuiltinNode> getNodeClass() {
        return SqrBuiltinNode.class;
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
    public SqrBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 1 && (arguments[0] == null || arguments[0] instanceof ExpressionNode)) {
            return create((ExpressionNode) arguments[0]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<SqrBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new SqrBuiltinNodeFactory();
        }
        return instance;
    }

    public static SqrBuiltinNode create(ExpressionNode argument) {
        return new SqrBuiltinNodeGen(argument);
    }

    @GeneratedBy(SqrBuiltinNode.class)
    public static final class SqrBuiltinNodeGen extends SqrBuiltinNode implements SpecializedNode {

        @Child private ExpressionNode argument_;
        @CompilationFinal private Class<?> argumentType_;
        @Child private BaseNode_ specialization_;

        private SqrBuiltinNodeGen(ExpressionNode argument) {
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
        public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
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

        @GeneratedBy(SqrBuiltinNode.class)
        private abstract static class BaseNode_ extends SpecializationNode {

            @CompilationFinal protected SqrBuiltinNodeGen root;

            BaseNode_(SqrBuiltinNodeGen root, int index) {
                super(index);
                this.root = root;
            }

            @Override
            protected final void setRoot(Node root) {
                this.root = (SqrBuiltinNodeGen) root;
            }

            @Override
            protected final Node[] getSuppliedChildren() {
                return new Node[] {root.argument_};
            }

            @Override
            public final Object acceptAndExecute(Frame frameValue, Object argumentValue) {
                return this.execute_((VirtualFrame) frameValue, argumentValue);
            }

            public abstract Object execute_(VirtualFrame frameValue, Object argumentValue);

            public Object execute(VirtualFrame frameValue) {
                Object argumentValue_ = executeArgument_(frameValue);
                return execute_(frameValue, argumentValue_);
            }

            public void executeVoid(VirtualFrame frameValue) {
                execute(frameValue);
                return;
            }

            public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
                return PascalTypesGen.expectLong(execute(frameValue));
            }

            @Override
            protected final SpecializationNode createNext(Frame frameValue, Object argumentValue) {
                if (argumentValue instanceof Long) {
                    return IntegerSquareValueNode_.create(root);
                }
                if (argumentValue instanceof Double) {
                    return UbleSquareRootValueNode_.create(root);
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
        @GeneratedBy(SqrBuiltinNode.class)
        private static final class UninitializedNode_ extends BaseNode_ {

            UninitializedNode_(SqrBuiltinNodeGen root) {
                super(root, 2147483647);
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object argumentValue) {
                return uninitialized(frameValue, argumentValue);
            }

            static BaseNode_ create(SqrBuiltinNodeGen root) {
                return new UninitializedNode_(root);
            }

        }
        @GeneratedBy(SqrBuiltinNode.class)
        private static final class PolymorphicNode_ extends BaseNode_ {

            PolymorphicNode_(SqrBuiltinNodeGen root) {
                super(root, 0);
            }

            @Override
            public SpecializationNode merge(SpecializationNode newNode, Frame frameValue, Object argumentValue) {
                return polymorphicMerge(newNode, super.merge(newNode, frameValue, argumentValue));
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object argumentValue) {
                return getNext().execute_(frameValue, argumentValue);
            }

            static BaseNode_ create(SqrBuiltinNodeGen root) {
                return new PolymorphicNode_(root);
            }

        }
        @GeneratedBy(methodName = "integerSquareValue(long)", value = SqrBuiltinNode.class)
        private static final class IntegerSquareValueNode_ extends BaseNode_ {

            IntegerSquareValueNode_(SqrBuiltinNodeGen root) {
                super(root, 1);
            }

            @Override
            public Object execute(VirtualFrame frameValue) {
                try {
                    return executeLong(frameValue);
                } catch (UnexpectedResultException ex) {
                    return ex.getResult();
                }
            }

            @Override
            public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
                long argumentValue_;
                try {
                    argumentValue_ = root.argument_.executeLong(frameValue);
                } catch (UnexpectedResultException ex) {
                    return PascalTypesGen.expectLong(getNext().execute_(frameValue, ex.getResult()));
                }
                return root.integerSquareValue(argumentValue_);
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object argumentValue) {
                if (argumentValue instanceof Long) {
                    long argumentValue_ = (long) argumentValue;
                    return root.integerSquareValue(argumentValue_);
                }
                return getNext().execute_(frameValue, argumentValue);
            }

            static BaseNode_ create(SqrBuiltinNodeGen root) {
                return new IntegerSquareValueNode_(root);
            }

        }
        @GeneratedBy(methodName = "doubleSquareRootValue(double)", value = SqrBuiltinNode.class)
        private static final class UbleSquareRootValueNode_ extends BaseNode_ {

            UbleSquareRootValueNode_(SqrBuiltinNodeGen root) {
                super(root, 2);
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object argumentValue) {
                if (argumentValue instanceof Double) {
                    double argumentValue_ = (double) argumentValue;
                    return root.doubleSquareRootValue(argumentValue_);
                }
                return getNext().execute_(frameValue, argumentValue);
            }

            static BaseNode_ create(SqrBuiltinNodeGen root) {
                return new UbleSquareRootValueNode_(root);
            }

        }
    }
}
