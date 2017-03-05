// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.logic;

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
import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(LessThanOrEqualNode.class)
public final class LessThanOrEqualNodeGen extends LessThanOrEqualNode implements SpecializedNode {

    @Child private ExpressionNode leftNode_;
    @Child private ExpressionNode rightNode_;
    @CompilationFinal private Class<?> leftNodeType_;
    @CompilationFinal private Class<?> rightNodeType_;
    @Child private BaseNode_ specialization_;

    private LessThanOrEqualNodeGen(ExpressionNode leftNode, ExpressionNode rightNode) {
        this.leftNode_ = leftNode;
        this.rightNode_ = rightNode;
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
    public boolean executeBoolean(VirtualFrame frameValue) {
        return specialization_.executeBoolean(frameValue);
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

    public static LessThanOrEqualNode create(ExpressionNode leftNode, ExpressionNode rightNode) {
        return new LessThanOrEqualNodeGen(leftNode, rightNode);
    }

    @GeneratedBy(LessThanOrEqualNode.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected LessThanOrEqualNodeGen root;

        BaseNode_(LessThanOrEqualNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (LessThanOrEqualNodeGen) root;
        }

        @Override
        protected final Node[] getSuppliedChildren() {
            return new Node[] {root.leftNode_, root.rightNode_};
        }

        @Override
        public final Object acceptAndExecute(Frame frameValue, Object leftNodeValue, Object rightNodeValue) {
            return this.executeBoolean_((VirtualFrame) frameValue, leftNodeValue, rightNodeValue);
        }

        public abstract boolean executeBoolean_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue);

        public Object execute(VirtualFrame frameValue) {
            Object leftNodeValue_ = executeLeftNode_(frameValue);
            Object rightNodeValue_ = executeRightNode_(frameValue);
            return executeBoolean_(frameValue, leftNodeValue_, rightNodeValue_);
        }

        public boolean executeBoolean(VirtualFrame frameValue) {
            return (boolean) execute(frameValue);
        }

        public void executeVoid(VirtualFrame frameValue) {
            executeBoolean(frameValue);
            return;
        }

        @Override
        protected final SpecializationNode createNext(Frame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Long && rightNodeValue instanceof Long) {
                return LessThanOrEqual0Node_.create(root);
            }
            if (leftNodeValue instanceof SetTypeValue && rightNodeValue instanceof SetTypeValue) {
                return LessThanOrEqual1Node_.create(root);
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

        protected final Object executeLeftNode_(Frame frameValue) {
            Class<?> leftNodeType_ = root.leftNodeType_;
            try {
                if (leftNodeType_ == long.class) {
                    return root.leftNode_.executeLong((VirtualFrame) frameValue);
                } else if (leftNodeType_ == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    Class<?> _type = Object.class;
                    try {
                        Object _value = root.leftNode_.executeGeneric((VirtualFrame) frameValue);
                        if (_value instanceof Long) {
                            _type = long.class;
                        } else {
                            _type = Object.class;
                        }
                        return _value;
                    } finally {
                        root.leftNodeType_ = _type;
                    }
                } else {
                    return root.leftNode_.executeGeneric((VirtualFrame) frameValue);
                }
            } catch (UnexpectedResultException ex) {
                root.leftNodeType_ = Object.class;
                return ex.getResult();
            }
        }

        protected final Object executeRightNode_(Frame frameValue) {
            Class<?> rightNodeType_ = root.rightNodeType_;
            try {
                if (rightNodeType_ == long.class) {
                    return root.rightNode_.executeLong((VirtualFrame) frameValue);
                } else if (rightNodeType_ == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    Class<?> _type = Object.class;
                    try {
                        Object _value = root.rightNode_.executeGeneric((VirtualFrame) frameValue);
                        if (_value instanceof Long) {
                            _type = long.class;
                        } else {
                            _type = Object.class;
                        }
                        return _value;
                    } finally {
                        root.rightNodeType_ = _type;
                    }
                } else {
                    return root.rightNode_.executeGeneric((VirtualFrame) frameValue);
                }
            } catch (UnexpectedResultException ex) {
                root.rightNodeType_ = Object.class;
                return ex.getResult();
            }
        }

    }
    @GeneratedBy(LessThanOrEqualNode.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(LessThanOrEqualNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public boolean executeBoolean_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            return (boolean) uninitialized(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(LessThanOrEqualNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(LessThanOrEqualNode.class)
    private static final class PolymorphicNode_ extends BaseNode_ {

        PolymorphicNode_(LessThanOrEqualNodeGen root) {
            super(root, 0);
        }

        @Override
        public SpecializationNode merge(SpecializationNode newNode, Frame frameValue, Object leftNodeValue, Object rightNodeValue) {
            return polymorphicMerge(newNode, super.merge(newNode, frameValue, leftNodeValue, rightNodeValue));
        }

        @Override
        public boolean executeBoolean(VirtualFrame frameValue) {
            Object leftNodeValue_ = executeLeftNode_(frameValue);
            Object rightNodeValue_ = executeRightNode_(frameValue);
            return getNext().executeBoolean_(frameValue, leftNodeValue_, rightNodeValue_);
        }

        @Override
        public boolean executeBoolean_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            return getNext().executeBoolean_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(LessThanOrEqualNodeGen root) {
            return new PolymorphicNode_(root);
        }

    }
    @GeneratedBy(methodName = "lessThanOrEqual(long, long)", value = LessThanOrEqualNode.class)
    private static final class LessThanOrEqual0Node_ extends BaseNode_ {

        LessThanOrEqual0Node_(LessThanOrEqualNodeGen root) {
            super(root, 1);
        }

        @Override
        public Object execute(VirtualFrame frameValue) {
            return executeBoolean(frameValue);
        }

        @Override
        public boolean executeBoolean(VirtualFrame frameValue) {
            long leftNodeValue_;
            try {
                leftNodeValue_ = root.leftNode_.executeLong(frameValue);
            } catch (UnexpectedResultException ex) {
                Object rightNodeValue = executeRightNode_(frameValue);
                return getNext().executeBoolean_(frameValue, ex.getResult(), rightNodeValue);
            }
            long rightNodeValue_;
            try {
                rightNodeValue_ = root.rightNode_.executeLong(frameValue);
            } catch (UnexpectedResultException ex) {
                return getNext().executeBoolean_(frameValue, leftNodeValue_, ex.getResult());
            }
            return root.lessThanOrEqual(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public boolean executeBoolean_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Long && rightNodeValue instanceof Long) {
                long leftNodeValue_ = (long) leftNodeValue;
                long rightNodeValue_ = (long) rightNodeValue;
                return root.lessThanOrEqual(leftNodeValue_, rightNodeValue_);
            }
            return getNext().executeBoolean_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(LessThanOrEqualNodeGen root) {
            return new LessThanOrEqual0Node_(root);
        }

    }
    @GeneratedBy(methodName = "lessThanOrEqual(SetTypeValue, SetTypeValue)", value = LessThanOrEqualNode.class)
    private static final class LessThanOrEqual1Node_ extends BaseNode_ {

        LessThanOrEqual1Node_(LessThanOrEqualNodeGen root) {
            super(root, 2);
        }

        @Override
        public boolean executeBoolean(VirtualFrame frameValue) {
            SetTypeValue leftNodeValue_;
            try {
                leftNodeValue_ = PascalTypesGen.expectSetTypeValue(root.leftNode_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                Object rightNodeValue = executeRightNode_(frameValue);
                return getNext().executeBoolean_(frameValue, ex.getResult(), rightNodeValue);
            }
            SetTypeValue rightNodeValue_;
            try {
                rightNodeValue_ = PascalTypesGen.expectSetTypeValue(root.rightNode_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                return getNext().executeBoolean_(frameValue, leftNodeValue_, ex.getResult());
            }
            return root.lessThanOrEqual(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public boolean executeBoolean_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof SetTypeValue && rightNodeValue instanceof SetTypeValue) {
                SetTypeValue leftNodeValue_ = (SetTypeValue) leftNodeValue;
                SetTypeValue rightNodeValue_ = (SetTypeValue) rightNodeValue;
                return root.lessThanOrEqual(leftNodeValue_, rightNodeValue_);
            }
            return getNext().executeBoolean_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(LessThanOrEqualNodeGen root) {
            return new LessThanOrEqual1Node_(root);
        }

    }
}