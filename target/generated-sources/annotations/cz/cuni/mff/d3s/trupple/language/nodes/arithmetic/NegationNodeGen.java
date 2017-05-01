// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

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

@GeneratedBy(NegationNode.class)
public final class NegationNodeGen extends NegationNode implements SpecializedNode {

    @Child private ExpressionNode argument_;
    @CompilationFinal private Class<?> argumentType_;
    @Child private BaseNode_ specialization_;

    private NegationNodeGen(ExpressionNode argument) {
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

    public static NegationNode create(ExpressionNode argument) {
        return new NegationNodeGen(argument);
    }

    @GeneratedBy(NegationNode.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected NegationNodeGen root;

        BaseNode_(NegationNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (NegationNodeGen) root;
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
                return Neg0Node_.create(root);
            }
            if (PascalTypesGen.isImplicitDouble(argumentValue)) {
                return Neg1Node_.create(root, argumentValue);
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
    @GeneratedBy(NegationNode.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(NegationNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object argumentValue) {
            return uninitialized(frameValue, argumentValue);
        }

        static BaseNode_ create(NegationNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(NegationNode.class)
    private static final class PolymorphicNode_ extends BaseNode_ {

        PolymorphicNode_(NegationNodeGen root) {
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

        static BaseNode_ create(NegationNodeGen root) {
            return new PolymorphicNode_(root);
        }

    }
    @GeneratedBy(methodName = "neg(long)", value = NegationNode.class)
    private static final class Neg0Node_ extends BaseNode_ {

        Neg0Node_(NegationNodeGen root) {
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
            return root.neg(argumentValue_);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object argumentValue) {
            if (argumentValue instanceof Long) {
                long argumentValue_ = (long) argumentValue;
                return root.neg(argumentValue_);
            }
            return getNext().execute_(frameValue, argumentValue);
        }

        static BaseNode_ create(NegationNodeGen root) {
            return new Neg0Node_(root);
        }

    }
    @GeneratedBy(methodName = "neg(double)", value = NegationNode.class)
    private static final class Neg1Node_ extends BaseNode_ {

        private final Class<?> argumentImplicitType;

        Neg1Node_(NegationNodeGen root, Object argumentValue) {
            super(root, 2);
            this.argumentImplicitType = PascalTypesGen.getImplicitDoubleClass(argumentValue);
        }

        @Override
        public boolean isSame(SpecializationNode other) {
            return super.isSame(other) && this.argumentImplicitType == ((Neg1Node_) other).argumentImplicitType;
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object argumentValue) {
            if (PascalTypesGen.isImplicitDouble(argumentValue, argumentImplicitType)) {
                double argumentValue_ = PascalTypesGen.asImplicitDouble(argumentValue, argumentImplicitType);
                return root.neg(argumentValue_);
            }
            return getNext().execute_(frameValue, argumentValue);
        }

        static BaseNode_ create(NegationNodeGen root, Object argumentValue) {
            return new Neg1Node_(root, argumentValue);
        }

    }
}
