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
import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(SubtractNode.class)
public final class SubtractNodeGen extends SubtractNode implements SpecializedNode {

    @Child private ExpressionNode leftNode_;
    @Child private ExpressionNode rightNode_;
    @CompilationFinal private Class<?> leftNodeType_;
    @CompilationFinal private Class<?> rightNodeType_;
    @Child private BaseNode_ specialization_;

    private SubtractNodeGen(ExpressionNode leftNode, ExpressionNode rightNode) {
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

    public static SubtractNode create(ExpressionNode leftNode, ExpressionNode rightNode) {
        return new SubtractNodeGen(leftNode, rightNode);
    }

    @GeneratedBy(SubtractNode.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected SubtractNodeGen root;

        BaseNode_(SubtractNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (SubtractNodeGen) root;
        }

        @Override
        protected final Node[] getSuppliedChildren() {
            return new Node[] {root.leftNode_, root.rightNode_};
        }

        @Override
        public final Object acceptAndExecute(Frame frameValue, Object leftNodeValue, Object rightNodeValue) {
            return this.execute_((VirtualFrame) frameValue, leftNodeValue, rightNodeValue);
        }

        public abstract Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue);

        public Object execute(VirtualFrame frameValue) {
            Object leftNodeValue_ = executeLeftNode_(frameValue);
            Object rightNodeValue_ = executeRightNode_(frameValue);
            return execute_(frameValue, leftNodeValue_, rightNodeValue_);
        }

        public void executeVoid(VirtualFrame frameValue) {
            execute(frameValue);
            return;
        }

        public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
            return PascalTypesGen.expectLong(execute(frameValue));
        }

        @Override
        protected final SpecializationNode createNext(Frame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (rightNodeValue instanceof Long) {
                if (leftNodeValue instanceof Long) {
                    return Sub0Node_.create(root);
                }
                if (leftNodeValue instanceof Double) {
                    return Sub1Node_.create(root);
                }
            }
            if (rightNodeValue instanceof Double) {
                if (leftNodeValue instanceof Long) {
                    return Sub2Node_.create(root);
                }
                if (leftNodeValue instanceof Double) {
                    return Sub3Node_.create(root);
                }
            }
            if (leftNodeValue instanceof SetTypeValue && rightNodeValue instanceof SetTypeValue) {
                return Sub4Node_.create(root);
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
    @GeneratedBy(SubtractNode.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(SubtractNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            return uninitialized(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(SubtractNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(SubtractNode.class)
    private static final class PolymorphicNode_ extends BaseNode_ {

        PolymorphicNode_(SubtractNodeGen root) {
            super(root, 0);
        }

        @Override
        public SpecializationNode merge(SpecializationNode newNode, Frame frameValue, Object leftNodeValue, Object rightNodeValue) {
            return polymorphicMerge(newNode, super.merge(newNode, frameValue, leftNodeValue, rightNodeValue));
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            return getNext().execute_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(SubtractNodeGen root) {
            return new PolymorphicNode_(root);
        }

    }
    @GeneratedBy(methodName = "sub(long, long)", value = SubtractNode.class)
    private static final class Sub0Node_ extends BaseNode_ {

        Sub0Node_(SubtractNodeGen root) {
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
            long leftNodeValue_;
            try {
                leftNodeValue_ = root.leftNode_.executeLong(frameValue);
            } catch (UnexpectedResultException ex) {
                Object rightNodeValue = executeRightNode_(frameValue);
                return PascalTypesGen.expectLong(getNext().execute_(frameValue, ex.getResult(), rightNodeValue));
            }
            long rightNodeValue_;
            try {
                rightNodeValue_ = root.rightNode_.executeLong(frameValue);
            } catch (UnexpectedResultException ex) {
                return PascalTypesGen.expectLong(getNext().execute_(frameValue, leftNodeValue_, ex.getResult()));
            }
            return root.sub(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Long && rightNodeValue instanceof Long) {
                long leftNodeValue_ = (long) leftNodeValue;
                long rightNodeValue_ = (long) rightNodeValue;
                return root.sub(leftNodeValue_, rightNodeValue_);
            }
            return getNext().execute_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(SubtractNodeGen root) {
            return new Sub0Node_(root);
        }

    }
    @GeneratedBy(methodName = "sub(double, long)", value = SubtractNode.class)
    private static final class Sub1Node_ extends BaseNode_ {

        Sub1Node_(SubtractNodeGen root) {
            super(root, 2);
        }

        @Override
        public Object execute(VirtualFrame frameValue) {
            double leftNodeValue_;
            try {
                leftNodeValue_ = PascalTypesGen.expectDouble(root.leftNode_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                Object rightNodeValue = executeRightNode_(frameValue);
                return getNext().execute_(frameValue, ex.getResult(), rightNodeValue);
            }
            long rightNodeValue_;
            try {
                rightNodeValue_ = root.rightNode_.executeLong(frameValue);
            } catch (UnexpectedResultException ex) {
                return getNext().execute_(frameValue, leftNodeValue_, ex.getResult());
            }
            return root.sub(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Double && rightNodeValue instanceof Long) {
                double leftNodeValue_ = (double) leftNodeValue;
                long rightNodeValue_ = (long) rightNodeValue;
                return root.sub(leftNodeValue_, rightNodeValue_);
            }
            return getNext().execute_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(SubtractNodeGen root) {
            return new Sub1Node_(root);
        }

    }
    @GeneratedBy(methodName = "sub(long, double)", value = SubtractNode.class)
    private static final class Sub2Node_ extends BaseNode_ {

        Sub2Node_(SubtractNodeGen root) {
            super(root, 3);
        }

        @Override
        public Object execute(VirtualFrame frameValue) {
            long leftNodeValue_;
            try {
                leftNodeValue_ = root.leftNode_.executeLong(frameValue);
            } catch (UnexpectedResultException ex) {
                Object rightNodeValue = executeRightNode_(frameValue);
                return getNext().execute_(frameValue, ex.getResult(), rightNodeValue);
            }
            double rightNodeValue_;
            try {
                rightNodeValue_ = PascalTypesGen.expectDouble(root.rightNode_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                return getNext().execute_(frameValue, leftNodeValue_, ex.getResult());
            }
            return root.sub(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Long && rightNodeValue instanceof Double) {
                long leftNodeValue_ = (long) leftNodeValue;
                double rightNodeValue_ = (double) rightNodeValue;
                return root.sub(leftNodeValue_, rightNodeValue_);
            }
            return getNext().execute_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(SubtractNodeGen root) {
            return new Sub2Node_(root);
        }

    }
    @GeneratedBy(methodName = "sub(double, double)", value = SubtractNode.class)
    private static final class Sub3Node_ extends BaseNode_ {

        Sub3Node_(SubtractNodeGen root) {
            super(root, 4);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Double && rightNodeValue instanceof Double) {
                double leftNodeValue_ = (double) leftNodeValue;
                double rightNodeValue_ = (double) rightNodeValue;
                return root.sub(leftNodeValue_, rightNodeValue_);
            }
            return getNext().execute_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(SubtractNodeGen root) {
            return new Sub3Node_(root);
        }

    }
    @GeneratedBy(methodName = "sub(SetTypeValue, SetTypeValue)", value = SubtractNode.class)
    private static final class Sub4Node_ extends BaseNode_ {

        Sub4Node_(SubtractNodeGen root) {
            super(root, 5);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof SetTypeValue && rightNodeValue instanceof SetTypeValue) {
                SetTypeValue leftNodeValue_ = (SetTypeValue) leftNodeValue;
                SetTypeValue rightNodeValue_ = (SetTypeValue) rightNodeValue;
                return root.sub(leftNodeValue_, rightNodeValue_);
            }
            return getNext().execute_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(SubtractNodeGen root) {
            return new Sub4Node_(root);
        }

    }
}
