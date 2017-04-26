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
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(CosBuiltinNode.class)
public final class CosBuiltinNodeGen extends CosBuiltinNode implements SpecializedNode {

    @Child private ExpressionNode argument_;
    @CompilationFinal private Class<?> argumentType_;
    @Child private BaseNode_ specialization_;

    private CosBuiltinNodeGen(ExpressionNode argument) {
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

    public static CosBuiltinNode create(ExpressionNode argument) {
        return new CosBuiltinNodeGen(argument);
    }

    @GeneratedBy(CosBuiltinNode.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected CosBuiltinNodeGen root;

        BaseNode_(CosBuiltinNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (CosBuiltinNodeGen) root;
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
                return IntegerCosValueNode_.create(root);
            }
            if (argumentValue instanceof Double) {
                return UbleCosValueNode_.create(root);
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
    @GeneratedBy(CosBuiltinNode.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(CosBuiltinNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public double executeDouble_(VirtualFrame frameValue, Object argumentValue) {
            return (double) uninitialized(frameValue, argumentValue);
        }

        static BaseNode_ create(CosBuiltinNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(CosBuiltinNode.class)
    private static final class PolymorphicNode_ extends BaseNode_ {

        PolymorphicNode_(CosBuiltinNodeGen root) {
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

        static BaseNode_ create(CosBuiltinNodeGen root) {
            return new PolymorphicNode_(root);
        }

    }
    @GeneratedBy(methodName = "integerCosValue(long)", value = CosBuiltinNode.class)
    private static final class IntegerCosValueNode_ extends BaseNode_ {

        IntegerCosValueNode_(CosBuiltinNodeGen root) {
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
            return root.integerCosValue(argumentValue_);
        }

        @Override
        public double executeDouble_(VirtualFrame frameValue, Object argumentValue) {
            if (argumentValue instanceof Long) {
                long argumentValue_ = (long) argumentValue;
                return root.integerCosValue(argumentValue_);
            }
            return getNext().executeDouble_(frameValue, argumentValue);
        }

        static BaseNode_ create(CosBuiltinNodeGen root) {
            return new IntegerCosValueNode_(root);
        }

    }
    @GeneratedBy(methodName = "doubleCosValue(double)", value = CosBuiltinNode.class)
    private static final class UbleCosValueNode_ extends BaseNode_ {

        UbleCosValueNode_(CosBuiltinNodeGen root) {
            super(root, 2);
        }

        @Override
        public double executeDouble_(VirtualFrame frameValue, Object argumentValue) {
            if (argumentValue instanceof Double) {
                double argumentValue_ = (double) argumentValue;
                return root.doubleCosValue(argumentValue_);
            }
            return getNext().executeDouble_(frameValue, argumentValue);
        }

        static BaseNode_ create(CosBuiltinNodeGen root) {
            return new UbleCosValueNode_(root);
        }

    }
}
