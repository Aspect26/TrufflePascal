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

@GeneratedBy(SinBuiltinNode.class)
public final class SinBuiltinNodeGen extends SinBuiltinNode implements SpecializedNode {

    @Child private ExpressionNode argument_;
    @CompilationFinal private Class<?> argumentType_;
    @Child private BaseNode_ specialization_;

    private SinBuiltinNodeGen(ExpressionNode argument) {
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

    public static SinBuiltinNode create(ExpressionNode argument) {
        return new SinBuiltinNodeGen(argument);
    }

    @GeneratedBy(SinBuiltinNode.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected SinBuiltinNodeGen root;

        BaseNode_(SinBuiltinNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (SinBuiltinNodeGen) root;
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
            if (PascalTypesGen.isImplicitDouble(argumentValue)) {
                return SinNode_.create(root, argumentValue);
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
    @GeneratedBy(SinBuiltinNode.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(SinBuiltinNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public double executeDouble_(VirtualFrame frameValue, Object argumentValue) {
            return (double) uninitialized(frameValue, argumentValue);
        }

        static BaseNode_ create(SinBuiltinNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(SinBuiltinNode.class)
    private static final class PolymorphicNode_ extends BaseNode_ {

        PolymorphicNode_(SinBuiltinNodeGen root) {
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

        static BaseNode_ create(SinBuiltinNodeGen root) {
            return new PolymorphicNode_(root);
        }

    }
    @GeneratedBy(methodName = "sin(double)", value = SinBuiltinNode.class)
    private static final class SinNode_ extends BaseNode_ {

        private final Class<?> argumentImplicitType;

        SinNode_(SinBuiltinNodeGen root, Object argumentValue) {
            super(root, 1);
            this.argumentImplicitType = PascalTypesGen.getImplicitDoubleClass(argumentValue);
        }

        @Override
        public boolean isSame(SpecializationNode other) {
            return super.isSame(other) && this.argumentImplicitType == ((SinNode_) other).argumentImplicitType;
        }

        @Override
        public double executeDouble_(VirtualFrame frameValue, Object argumentValue) {
            if (PascalTypesGen.isImplicitDouble(argumentValue, argumentImplicitType)) {
                double argumentValue_ = PascalTypesGen.asImplicitDouble(argumentValue, argumentImplicitType);
                return root.sin(argumentValue_);
            }
            return getNext().executeDouble_(frameValue, argumentValue);
        }

        static BaseNode_ create(SinBuiltinNodeGen root, Object argumentValue) {
            return new SinNode_(root, argumentValue);
        }

    }
}
