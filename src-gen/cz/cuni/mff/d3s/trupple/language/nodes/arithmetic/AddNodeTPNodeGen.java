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

@GeneratedBy(AddNodeTP.class)
public final class AddNodeTPNodeGen extends AddNodeTP implements SpecializedNode {

    @Child private ExpressionNode leftNode_;
    @Child private ExpressionNode rightNode_;
    @CompilationFinal private Class<?> leftNodeType_;
    @CompilationFinal private Class<?> rightNodeType_;
    @Child private BaseNode_ specialization_;

    private AddNodeTPNodeGen(ExpressionNode leftNode, ExpressionNode rightNode) {
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

    private static String expectString(Object value) throws UnexpectedResultException {
        if (value instanceof String) {
            return (String) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static AddNodeTP create(ExpressionNode leftNode, ExpressionNode rightNode) {
        return new AddNodeTPNodeGen(leftNode, rightNode);
    }

    @GeneratedBy(AddNodeTP.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected AddNodeTPNodeGen root;

        BaseNode_(AddNodeTPNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (AddNodeTPNodeGen) root;
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
                    return Add0Node_.create(root);
                }
                if (leftNodeValue instanceof Double) {
                    return Add1Node_.create(root);
                }
            }
            if (rightNodeValue instanceof Double) {
                if (leftNodeValue instanceof Long) {
                    return Add2Node_.create(root);
                }
                if (leftNodeValue instanceof Double) {
                    return Add3Node_.create(root);
                }
            }
            if (leftNodeValue instanceof SetTypeValue && rightNodeValue instanceof SetTypeValue) {
                return Add4Node_.create(root);
            }
            if (leftNodeValue instanceof Character) {
                if (rightNodeValue instanceof Character) {
                    return Add5Node_.create(root);
                }
                if (rightNodeValue instanceof String) {
                    return Add6Node_.create(root);
                }
            }
            if (leftNodeValue instanceof String) {
                if (rightNodeValue instanceof Character) {
                    return Add7Node_.create(root);
                }
                if (rightNodeValue instanceof String) {
                    return Add8Node_.create(root);
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

        protected final Object executeLeftNode_(Frame frameValue) {
            Class<?> leftNodeType_ = root.leftNodeType_;
            try {
                if (leftNodeType_ == char.class) {
                    return root.leftNode_.executeChar((VirtualFrame) frameValue);
                } else if (leftNodeType_ == long.class) {
                    return root.leftNode_.executeLong((VirtualFrame) frameValue);
                } else if (leftNodeType_ == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    Class<?> _type = Object.class;
                    try {
                        Object _value = root.leftNode_.executeGeneric((VirtualFrame) frameValue);
                        if (_value instanceof Character) {
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
                if (rightNodeType_ == char.class) {
                    return root.rightNode_.executeChar((VirtualFrame) frameValue);
                } else if (rightNodeType_ == long.class) {
                    return root.rightNode_.executeLong((VirtualFrame) frameValue);
                } else if (rightNodeType_ == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    Class<?> _type = Object.class;
                    try {
                        Object _value = root.rightNode_.executeGeneric((VirtualFrame) frameValue);
                        if (_value instanceof Character) {
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
    @GeneratedBy(AddNodeTP.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(AddNodeTPNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            return uninitialized(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(AddNodeTPNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(AddNodeTP.class)
    private static final class PolymorphicNode_ extends BaseNode_ {

        PolymorphicNode_(AddNodeTPNodeGen root) {
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

        static BaseNode_ create(AddNodeTPNodeGen root) {
            return new PolymorphicNode_(root);
        }

    }
    @GeneratedBy(methodName = "add(long, long)", value = AddNodeTP.class)
    private static final class Add0Node_ extends BaseNode_ {

        Add0Node_(AddNodeTPNodeGen root) {
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
            return root.add(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Long && rightNodeValue instanceof Long) {
                long leftNodeValue_ = (long) leftNodeValue;
                long rightNodeValue_ = (long) rightNodeValue;
                return root.add(leftNodeValue_, rightNodeValue_);
            }
            return getNext().execute_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(AddNodeTPNodeGen root) {
            return new Add0Node_(root);
        }

    }
    @GeneratedBy(methodName = "add(double, long)", value = AddNodeTP.class)
    private static final class Add1Node_ extends BaseNode_ {

        Add1Node_(AddNodeTPNodeGen root) {
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
            return root.add(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Double && rightNodeValue instanceof Long) {
                double leftNodeValue_ = (double) leftNodeValue;
                long rightNodeValue_ = (long) rightNodeValue;
                return root.add(leftNodeValue_, rightNodeValue_);
            }
            return getNext().execute_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(AddNodeTPNodeGen root) {
            return new Add1Node_(root);
        }

    }
    @GeneratedBy(methodName = "add(long, double)", value = AddNodeTP.class)
    private static final class Add2Node_ extends BaseNode_ {

        Add2Node_(AddNodeTPNodeGen root) {
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
            return root.add(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Long && rightNodeValue instanceof Double) {
                long leftNodeValue_ = (long) leftNodeValue;
                double rightNodeValue_ = (double) rightNodeValue;
                return root.add(leftNodeValue_, rightNodeValue_);
            }
            return getNext().execute_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(AddNodeTPNodeGen root) {
            return new Add2Node_(root);
        }

    }
    @GeneratedBy(methodName = "add(double, double)", value = AddNodeTP.class)
    private static final class Add3Node_ extends BaseNode_ {

        Add3Node_(AddNodeTPNodeGen root) {
            super(root, 4);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Double && rightNodeValue instanceof Double) {
                double leftNodeValue_ = (double) leftNodeValue;
                double rightNodeValue_ = (double) rightNodeValue;
                return root.add(leftNodeValue_, rightNodeValue_);
            }
            return getNext().execute_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(AddNodeTPNodeGen root) {
            return new Add3Node_(root);
        }

    }
    @GeneratedBy(methodName = "add(SetTypeValue, SetTypeValue)", value = AddNodeTP.class)
    private static final class Add4Node_ extends BaseNode_ {

        Add4Node_(AddNodeTPNodeGen root) {
            super(root, 5);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof SetTypeValue && rightNodeValue instanceof SetTypeValue) {
                SetTypeValue leftNodeValue_ = (SetTypeValue) leftNodeValue;
                SetTypeValue rightNodeValue_ = (SetTypeValue) rightNodeValue;
                return root.add(leftNodeValue_, rightNodeValue_);
            }
            return getNext().execute_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(AddNodeTPNodeGen root) {
            return new Add4Node_(root);
        }

    }
    @GeneratedBy(methodName = "add(char, char)", value = AddNodeTP.class)
    private static final class Add5Node_ extends BaseNode_ {

        Add5Node_(AddNodeTPNodeGen root) {
            super(root, 6);
        }

        @Override
        public Object execute(VirtualFrame frameValue) {
            char leftNodeValue_;
            try {
                leftNodeValue_ = root.leftNode_.executeChar(frameValue);
            } catch (UnexpectedResultException ex) {
                Object rightNodeValue = executeRightNode_(frameValue);
                return getNext().execute_(frameValue, ex.getResult(), rightNodeValue);
            }
            char rightNodeValue_;
            try {
                rightNodeValue_ = root.rightNode_.executeChar(frameValue);
            } catch (UnexpectedResultException ex) {
                return getNext().execute_(frameValue, leftNodeValue_, ex.getResult());
            }
            return root.add(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Character && rightNodeValue instanceof Character) {
                char leftNodeValue_ = (char) leftNodeValue;
                char rightNodeValue_ = (char) rightNodeValue;
                return root.add(leftNodeValue_, rightNodeValue_);
            }
            return getNext().execute_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(AddNodeTPNodeGen root) {
            return new Add5Node_(root);
        }

    }
    @GeneratedBy(methodName = "add(char, String)", value = AddNodeTP.class)
    private static final class Add6Node_ extends BaseNode_ {

        Add6Node_(AddNodeTPNodeGen root) {
            super(root, 7);
        }

        @Override
        public Object execute(VirtualFrame frameValue) {
            char leftNodeValue_;
            try {
                leftNodeValue_ = root.leftNode_.executeChar(frameValue);
            } catch (UnexpectedResultException ex) {
                Object rightNodeValue = executeRightNode_(frameValue);
                return getNext().execute_(frameValue, ex.getResult(), rightNodeValue);
            }
            String rightNodeValue_;
            try {
                rightNodeValue_ = expectString(root.rightNode_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                return getNext().execute_(frameValue, leftNodeValue_, ex.getResult());
            }
            return root.add(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof Character && rightNodeValue instanceof String) {
                char leftNodeValue_ = (char) leftNodeValue;
                String rightNodeValue_ = (String) rightNodeValue;
                return root.add(leftNodeValue_, rightNodeValue_);
            }
            return getNext().execute_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(AddNodeTPNodeGen root) {
            return new Add6Node_(root);
        }

    }
    @GeneratedBy(methodName = "add(String, char)", value = AddNodeTP.class)
    private static final class Add7Node_ extends BaseNode_ {

        Add7Node_(AddNodeTPNodeGen root) {
            super(root, 8);
        }

        @Override
        public Object execute(VirtualFrame frameValue) {
            String leftNodeValue_;
            try {
                leftNodeValue_ = expectString(root.leftNode_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                Object rightNodeValue = executeRightNode_(frameValue);
                return getNext().execute_(frameValue, ex.getResult(), rightNodeValue);
            }
            char rightNodeValue_;
            try {
                rightNodeValue_ = root.rightNode_.executeChar(frameValue);
            } catch (UnexpectedResultException ex) {
                return getNext().execute_(frameValue, leftNodeValue_, ex.getResult());
            }
            return root.add(leftNodeValue_, rightNodeValue_);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof String && rightNodeValue instanceof Character) {
                String leftNodeValue_ = (String) leftNodeValue;
                char rightNodeValue_ = (char) rightNodeValue;
                return root.add(leftNodeValue_, rightNodeValue_);
            }
            return getNext().execute_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(AddNodeTPNodeGen root) {
            return new Add7Node_(root);
        }

    }
    @GeneratedBy(methodName = "add(String, String)", value = AddNodeTP.class)
    private static final class Add8Node_ extends BaseNode_ {

        Add8Node_(AddNodeTPNodeGen root) {
            super(root, 9);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object leftNodeValue, Object rightNodeValue) {
            if (leftNodeValue instanceof String && rightNodeValue instanceof String) {
                String leftNodeValue_ = (String) leftNodeValue;
                String rightNodeValue_ = (String) rightNodeValue;
                return root.add(leftNodeValue_, rightNodeValue_);
            }
            return getNext().execute_(frameValue, leftNodeValue, rightNodeValue);
        }

        static BaseNode_ create(AddNodeTPNodeGen root) {
            return new Add8Node_(root);
        }

    }
}