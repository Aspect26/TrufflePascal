// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.internal.SpecializationNode;
import com.oracle.truffle.api.dsl.internal.SpecializedNode;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.PascalTypesGen;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(AbsBuiltinNode.class)
public final class AbsBuiltinNodeGen extends AbsBuiltinNode implements SpecializedNode {

    @Child private ExpressionNode argument_;
    @CompilationFinal private Class<?> argumentType_;
    @Child private BaseNode_ specialization_;

    private AbsBuiltinNodeGen(ExpressionNode argument) {
        this.argument_ = argument;
        this.specialization_ = UninitializedNode_.create(this);
    }

    @Override
    protected ExpressionNode getArgument() {
        return this.argument_;
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

    public static AbsBuiltinNode create(ExpressionNode argument) {
        return new AbsBuiltinNodeGen(argument);
    }

    @GeneratedBy(AbsBuiltinNode.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected AbsBuiltinNodeGen root;

        BaseNode_(AbsBuiltinNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (AbsBuiltinNodeGen) root;
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
                return IntegerAbsoluteValueNode_.create(root);
            }
            if (PascalTypesGen.isImplicitDouble(argumentValue)) {
                return UbleAbsoluteValueNode_.create(root, argumentValue);
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
    @GeneratedBy(AbsBuiltinNode.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(AbsBuiltinNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object argumentValue) {
            return uninitialized(frameValue, argumentValue);
        }

        static BaseNode_ create(AbsBuiltinNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(AbsBuiltinNode.class)
    private static final class PolymorphicNode_ extends BaseNode_ {

        PolymorphicNode_(AbsBuiltinNodeGen root) {
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

        static BaseNode_ create(AbsBuiltinNodeGen root) {
            return new PolymorphicNode_(root);
        }

    }
    @GeneratedBy(methodName = "integerAbsoluteValue(long)", value = AbsBuiltinNode.class)
    private static final class IntegerAbsoluteValueNode_ extends BaseNode_ {

        IntegerAbsoluteValueNode_(AbsBuiltinNodeGen root) {
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
            return root.integerAbsoluteValue(argumentValue_);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object argumentValue) {
            if (argumentValue instanceof Long) {
                long argumentValue_ = (long) argumentValue;
                return root.integerAbsoluteValue(argumentValue_);
            }
            return getNext().execute_(frameValue, argumentValue);
        }

        static BaseNode_ create(AbsBuiltinNodeGen root) {
            return new IntegerAbsoluteValueNode_(root);
        }

    }
    @GeneratedBy(methodName = "doubleAbsoluteValue(double)", value = AbsBuiltinNode.class)
    private static final class UbleAbsoluteValueNode_ extends BaseNode_ {

        private final Class<?> argumentImplicitType;

        UbleAbsoluteValueNode_(AbsBuiltinNodeGen root, Object argumentValue) {
            super(root, 2);
            this.argumentImplicitType = PascalTypesGen.getImplicitDoubleClass(argumentValue);
        }

        @Override
        public boolean isSame(SpecializationNode other) {
            return super.isSame(other) && this.argumentImplicitType == ((UbleAbsoluteValueNode_) other).argumentImplicitType;
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object argumentValue) {
            if (PascalTypesGen.isImplicitDouble(argumentValue, argumentImplicitType)) {
                double argumentValue_ = PascalTypesGen.asImplicitDouble(argumentValue, argumentImplicitType);
                return root.doubleAbsoluteValue(argumentValue_);
            }
            return getNext().execute_(frameValue, argumentValue);
        }

        static BaseNode_ create(AbsBuiltinNodeGen root, Object argumentValue) {
            return new UbleAbsoluteValueNode_(root, argumentValue);
        }

    }
}