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
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(LessThanNode.class)
public final class LessThanNodeGen extends LessThanNode implements SpecializedNode {

    @Child private ExpressionNode leftNode_;
    @Child private ExpressionNode rightNode_;
    @CompilationFinal private Class<?> leftNodeType_;
    @CompilationFinal private Class<?> rightNodeType_;
    @Child private BaseNode_ specialization_;

    private LessThanNodeGen(ExpressionNode leftNode, ExpressionNode rightNode) {
        this.leftNode_ = leftNode;
        this.rightNode_ = rightNode;
        this.specialization_ = UninitializedNode_.create(this);
    }

    @Override
    protected ExpressionNode getLeftNode() {
        return this.leftNode_;
    }

    @Override
    protected ExpressionNode getRightNode() {
        return this.rightNode_;
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
    public boolean executeBoolean(VirtualFrame frameValue) {
        return specialization_.executeBoolean(frameValue);
    }

    @Override
    public SpecializationNode getSpecializationNode() {
        return specialization_;
    }

    @Override
    public Node deepCopy() {
        return SpecializationNode.updateRoot(super.deepCopy());
    }

    public static LessThanNode create(ExpressionNode leftNode, ExpressionNode rightNode) {
        return new LessThanNodeGen(leftNode, rightNode);
    }

    @GeneratedBy(LessThanNode.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected LessThanNodeGen root;

        BaseNode_(LessThanNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (LessThanNodeGen) root;
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

        public void executeVoid(VirtualFrame frameValue) {
            execute(frameValue);
            return;
        }

        public boolean executeBoolean(VirtualFrame frameValue) {
            return (boolean) execute(frameValue);
        }

        @Override
        protected final SpecializationNode createNext(Frame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (rightNodeValue instanceof Long) {
                if (leftNodeValue instanceof Long) {
                    return LessThan0Node_.create(root);
                }
                if (leftNodeValue instanceof Double) {
                    return LessThan1Node_.create(root);
                }
            }
            if (rightNodeValue instanceof Double) {
                if (leftNodeValue instanceof Long) {
                    return LessThan2Node_.create(root);
                }
                if (leftNodeValue instanceof Double) {
                    return LessThan3Node_.create(root);
                }
            }
            if (leftNodeValue instanceof Character && rightNodeValue instanceof Character) {
                return LessThan4Node_.create(root);
            }
            if (leftNodeValue instanceof Boolean && rightNodeValue instanceof Boolean) {
                return LessThan5Node_.create(root);
            }
            if (leftNodeValue instanceof SetTypeValue && rightNodeValue instanceof SetTypeValue) {
                return LessThan6Node_.create(root);
            }
            if (leftNodeValue instanceof EnumValue && rightNodeValue instanceof EnumValue) {
                return LessThan7Node_.create(root);
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
                if (leftNodeType_ == boolean.class) {
                    return root.leftNode_.executeBoolean((VirtualFrame) frameValue);
                } else if (leftNodeType_ == char.class) {
                    return root.leftNode_.executeChar((VirtualFrame) frameValue);
                } else if (leftNodeType_ == long.class) {
                    return root.leftNode_.executeLong((VirtualFrame) frameValue);
                } else if (leftNodeType_ == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    Class<?> _type = Object.class;
                    try {
                        Object _value = root.leftNode_.executeGeneric((VirtualFrame) frameValue);
                        if (_value instanceof Boolean) {
                            _type = boolean.class;
                        } else if (_value instanceof Character) {
                            _type = char.class;
                        } else if (_value instanceof Long) {
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
                if (rightNodeType_ == boolean.class) {
                    return root.rightNode_.executeBoolean((VirtualFrame) frameValue);
                } else if (rightNodeType_ == char.class) {
                    return root.rightNode_.executeChar((VirtualFrame) frameValue);
                } else if (rightNodeType_ == long.class) {
                    return root.rightNode_.executeLong((VirtualFrame) frameValue);
                } else if (rightNodeType_ == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    Class<?> _type = Object.class;
                    try {
                        Object _value = root.rightNode_.executeGeneric((VirtualFrame) frameValue);
                        if (_value instanceof Boolean) {
                            _type = boolean.class;
                        } else if (_value instanceof Character) {
                            _type = char.class;
                        } else if (_value instanceof Long) {
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
    @GeneratedBy(LessThanNode.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(LessThanNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public boolean executeBoolean_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            return (boolean) uninitialized(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(LessThanNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(LessThanNode.class)
    private static final class PolymorphicNode_ extends BaseNode_ {

        PolymorphicNode_(LessThanNodeGen root) {
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

        static BaseNode_ create(LessThanNodeGen root) {
            return new PolymorphicNode_(root);
        }

    }
    @GeneratedBy(methodName = "lessThan(long, long)", value = LessThanNode.class)
    private static final class LessThan0Node_ extends BaseNode_ {

        LessThan0Node_(LessThanNodeGen root) {
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
            return root.lessThan(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public boolean executeBoolean_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Long && rightNodeValue instanceof Long) {
                long leftNodeValue_ = (long) leftNodeValue;
                long rightNodeValue_ = (long) rightNodeValue;
                return root.lessThan(leftNodeValue_, rightNodeValue_);
            }
            return getNext().executeBoolean_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(LessThanNodeGen root) {
            return new LessThan0Node_(root);
        }

    }
    @GeneratedBy(methodName = "lessThan(double, long)", value = LessThanNode.class)
    private static final class LessThan1Node_ extends BaseNode_ {

        LessThan1Node_(LessThanNodeGen root) {
            super(root, 2);
        }

        @Override
        public Object execute(VirtualFrame frameValue) {
            return executeBoolean(frameValue);
        }

        @Override
        public boolean executeBoolean(VirtualFrame frameValue) {
            double leftNodeValue_;
            try {
                leftNodeValue_ = PascalTypesGen.expectDouble(root.leftNode_.executeGeneric(frameValue));
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
            return root.lessThan(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public boolean executeBoolean_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Double && rightNodeValue instanceof Long) {
                double leftNodeValue_ = (double) leftNodeValue;
                long rightNodeValue_ = (long) rightNodeValue;
                return root.lessThan(leftNodeValue_, rightNodeValue_);
            }
            return getNext().executeBoolean_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(LessThanNodeGen root) {
            return new LessThan1Node_(root);
        }

    }
    @GeneratedBy(methodName = "lessThan(long, double)", value = LessThanNode.class)
    private static final class LessThan2Node_ extends BaseNode_ {

        LessThan2Node_(LessThanNodeGen root) {
            super(root, 3);
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
            double rightNodeValue_;
            try {
                rightNodeValue_ = PascalTypesGen.expectDouble(root.rightNode_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                return getNext().executeBoolean_(frameValue, leftNodeValue_, ex.getResult());
            }
            return root.lessThan(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public boolean executeBoolean_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Long && rightNodeValue instanceof Double) {
                long leftNodeValue_ = (long) leftNodeValue;
                double rightNodeValue_ = (double) rightNodeValue;
                return root.lessThan(leftNodeValue_, rightNodeValue_);
            }
            return getNext().executeBoolean_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(LessThanNodeGen root) {
            return new LessThan2Node_(root);
        }

    }
    @GeneratedBy(methodName = "lessThan(double, double)", value = LessThanNode.class)
    private static final class LessThan3Node_ extends BaseNode_ {

        LessThan3Node_(LessThanNodeGen root) {
            super(root, 4);
        }

        @Override
        public boolean executeBoolean(VirtualFrame frameValue) {
            double leftNodeValue_;
            try {
                leftNodeValue_ = PascalTypesGen.expectDouble(root.leftNode_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                Object rightNodeValue = executeRightNode_(frameValue);
                return getNext().executeBoolean_(frameValue, ex.getResult(), rightNodeValue);
            }
            double rightNodeValue_;
            try {
                rightNodeValue_ = PascalTypesGen.expectDouble(root.rightNode_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                return getNext().executeBoolean_(frameValue, leftNodeValue_, ex.getResult());
            }
            return root.lessThan(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public boolean executeBoolean_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Double && rightNodeValue instanceof Double) {
                double leftNodeValue_ = (double) leftNodeValue;
                double rightNodeValue_ = (double) rightNodeValue;
                return root.lessThan(leftNodeValue_, rightNodeValue_);
            }
            return getNext().executeBoolean_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(LessThanNodeGen root) {
            return new LessThan3Node_(root);
        }

    }
    @GeneratedBy(methodName = "lessThan(char, char)", value = LessThanNode.class)
    private static final class LessThan4Node_ extends BaseNode_ {

        LessThan4Node_(LessThanNodeGen root) {
            super(root, 5);
        }

        @Override
        public Object execute(VirtualFrame frameValue) {
            return executeBoolean(frameValue);
        }

        @Override
        public boolean executeBoolean(VirtualFrame frameValue) {
            char leftNodeValue_;
            try {
                leftNodeValue_ = root.leftNode_.executeChar(frameValue);
            } catch (UnexpectedResultException ex) {
                Object rightNodeValue = executeRightNode_(frameValue);
                return getNext().executeBoolean_(frameValue, ex.getResult(), rightNodeValue);
            }
            char rightNodeValue_;
            try {
                rightNodeValue_ = root.rightNode_.executeChar(frameValue);
            } catch (UnexpectedResultException ex) {
                return getNext().executeBoolean_(frameValue, leftNodeValue_, ex.getResult());
            }
            return root.lessThan(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public boolean executeBoolean_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Character && rightNodeValue instanceof Character) {
                char leftNodeValue_ = (char) leftNodeValue;
                char rightNodeValue_ = (char) rightNodeValue;
                return root.lessThan(leftNodeValue_, rightNodeValue_);
            }
            return getNext().executeBoolean_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(LessThanNodeGen root) {
            return new LessThan4Node_(root);
        }

    }
    @GeneratedBy(methodName = "lessThan(boolean, boolean)", value = LessThanNode.class)
    private static final class LessThan5Node_ extends BaseNode_ {

        LessThan5Node_(LessThanNodeGen root) {
            super(root, 6);
        }

        @Override
        public Object execute(VirtualFrame frameValue) {
            return executeBoolean(frameValue);
        }

        @Override
        public boolean executeBoolean(VirtualFrame frameValue) {
            boolean leftNodeValue_;
            try {
                leftNodeValue_ = root.leftNode_.executeBoolean(frameValue);
            } catch (UnexpectedResultException ex) {
                Object rightNodeValue = executeRightNode_(frameValue);
                return getNext().executeBoolean_(frameValue, ex.getResult(), rightNodeValue);
            }
            boolean rightNodeValue_;
            try {
                rightNodeValue_ = root.rightNode_.executeBoolean(frameValue);
            } catch (UnexpectedResultException ex) {
                return getNext().executeBoolean_(frameValue, leftNodeValue_, ex.getResult());
            }
            return root.lessThan(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public boolean executeBoolean_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Boolean && rightNodeValue instanceof Boolean) {
                boolean leftNodeValue_ = (boolean) leftNodeValue;
                boolean rightNodeValue_ = (boolean) rightNodeValue;
                return root.lessThan(leftNodeValue_, rightNodeValue_);
            }
            return getNext().executeBoolean_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(LessThanNodeGen root) {
            return new LessThan5Node_(root);
        }

    }
    @GeneratedBy(methodName = "lessThan(SetTypeValue, SetTypeValue)", value = LessThanNode.class)
    private static final class LessThan6Node_ extends BaseNode_ {

        LessThan6Node_(LessThanNodeGen root) {
            super(root, 7);
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
            return root.lessThan(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public boolean executeBoolean_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof SetTypeValue && rightNodeValue instanceof SetTypeValue) {
                SetTypeValue leftNodeValue_ = (SetTypeValue) leftNodeValue;
                SetTypeValue rightNodeValue_ = (SetTypeValue) rightNodeValue;
                return root.lessThan(leftNodeValue_, rightNodeValue_);
            }
            return getNext().executeBoolean_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(LessThanNodeGen root) {
            return new LessThan6Node_(root);
        }

    }
    @GeneratedBy(methodName = "lessThan(EnumValue, EnumValue)", value = LessThanNode.class)
    private static final class LessThan7Node_ extends BaseNode_ {

        LessThan7Node_(LessThanNodeGen root) {
            super(root, 8);
        }

        @Override
        public boolean executeBoolean(VirtualFrame frameValue) {
            EnumValue leftNodeValue_;
            try {
                leftNodeValue_ = root.leftNode_.executeEnum(frameValue);
            } catch (UnexpectedResultException ex) {
                Object rightNodeValue = executeRightNode_(frameValue);
                return getNext().executeBoolean_(frameValue, ex.getResult(), rightNodeValue);
            }
            EnumValue rightNodeValue_;
            try {
                rightNodeValue_ = root.rightNode_.executeEnum(frameValue);
            } catch (UnexpectedResultException ex) {
                return getNext().executeBoolean_(frameValue, leftNodeValue_, ex.getResult());
            }
            return root.lessThan(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public boolean executeBoolean_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof EnumValue && rightNodeValue instanceof EnumValue) {
                EnumValue leftNodeValue_ = (EnumValue) leftNodeValue;
                EnumValue rightNodeValue_ = (EnumValue) rightNodeValue;
                return root.lessThan(leftNodeValue_, rightNodeValue_);
            }
            return getNext().executeBoolean_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(LessThanNodeGen root) {
            return new LessThan7Node_(root);
        }

    }
}
