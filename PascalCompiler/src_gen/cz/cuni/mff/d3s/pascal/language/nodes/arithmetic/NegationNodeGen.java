// CheckStyle: start generated
package cz.cuni.mff.d3s.pascal.language.nodes.arithmetic;

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
import cz.cuni.mff.d3s.pascal.language.PascalTypesGen;
import cz.cuni.mff.d3s.pascal.language.nodes.ExpressionNode;

@GeneratedBy(NegationNode.class)
public final class NegationNodeGen extends NegationNode implements SpecializedNode {

    @Child private ExpressionNode son_;
    @CompilationFinal private Class<?> sonType_;
    @CompilationFinal private boolean excludeNeg0_;
    @CompilationFinal private boolean excludeNeg1_;
    @Child private BaseNode_ specialization_;

    private NegationNodeGen(ExpressionNode son) {
        this.son_ = son;
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

    public static NegationNode create(ExpressionNode son) {
        return new NegationNodeGen(son);
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
            return new Node[] {root.son_};
        }

        @Override
        public final Object acceptAndExecute(Frame frameValue, Object sonValue) {
            return this.execute_((VirtualFrame) frameValue, sonValue);
        }

        public abstract Object execute_(VirtualFrame frameValue, Object sonValue);

        public Object execute(VirtualFrame frameValue) {
            Object sonValue_ = executeSon_(frameValue);
            return execute_(frameValue, sonValue_);
        }

        public void executeVoid(VirtualFrame frameValue) {
            execute(frameValue);
            return;
        }

        public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
            return PascalTypesGen.expectLong(execute(frameValue));
        }

        @Override
        protected final SpecializationNode createNext(Frame frameValue, Object sonValue) {
            if (sonValue instanceof Long) {
                if (!root.excludeNeg0_) {
                    return Neg0Node_.create(root);
                }
            }
            if (sonValue instanceof Double) {
                if (!root.excludeNeg1_) {
                    return Neg1Node_.create(root);
                }
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

        protected final Object executeSon_(Frame frameValue) {
            Class<?> sonType_ = root.sonType_;
            try {
                if (sonType_ == long.class) {
                    return root.son_.executeLong((VirtualFrame) frameValue);
                } else if (sonType_ == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    Class<?> _type = Object.class;
                    try {
                        Object _value = root.son_.executeGeneric((VirtualFrame) frameValue);
                        if (_value instanceof Long) {
                            _type = long.class;
                        } else {
                            _type = Object.class;
                        }
                        return _value;
                    } finally {
                        root.sonType_ = _type;
                    }
                } else {
                    return root.son_.executeGeneric((VirtualFrame) frameValue);
                }
            } catch (UnexpectedResultException ex) {
                root.sonType_ = Object.class;
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
        public Object execute_(VirtualFrame frameValue, Object sonValue) {
            return uninitialized(frameValue, sonValue);
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
        public SpecializationNode merge(SpecializationNode newNode, Frame frameValue, Object sonValue) {
            return polymorphicMerge(newNode, super.merge(newNode, frameValue, sonValue));
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object sonValue) {
            return getNext().execute_(frameValue, sonValue);
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
            long sonValue_;
            try {
                sonValue_ = root.son_.executeLong(frameValue);
            } catch (UnexpectedResultException ex) {
                return PascalTypesGen.expectLong(getNext().execute_(frameValue, ex.getResult()));
            }
            try {
                return root.neg(sonValue_);
            } catch (ArithmeticException ex) {
                root.excludeNeg0_ = true;
                return PascalTypesGen.expectLong(remove("threw rewrite exception", frameValue, sonValue_));
            }
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object sonValue) {
            if (sonValue instanceof Long) {
                long sonValue_ = (long) sonValue;
                try {
                    return root.neg(sonValue_);
                } catch (ArithmeticException ex) {
                    root.excludeNeg0_ = true;
                    return remove("threw rewrite exception", frameValue, sonValue_);
                }
            }
            return getNext().execute_(frameValue, sonValue);
        }

        static BaseNode_ create(NegationNodeGen root) {
            return new Neg0Node_(root);
        }

    }
    @GeneratedBy(methodName = "neg(double)", value = NegationNode.class)
    private static final class Neg1Node_ extends BaseNode_ {

        Neg1Node_(NegationNodeGen root) {
            super(root, 2);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object sonValue) {
            if (sonValue instanceof Double) {
                double sonValue_ = (double) sonValue;
                try {
                    return root.neg(sonValue_);
                } catch (ArithmeticException ex) {
                    root.excludeNeg1_ = true;
                    return remove("threw rewrite exception", frameValue, sonValue_);
                }
            }
            return getNext().execute_(frameValue, sonValue);
        }

        static BaseNode_ create(NegationNodeGen root) {
            return new Neg1Node_(root);
        }

    }
}
