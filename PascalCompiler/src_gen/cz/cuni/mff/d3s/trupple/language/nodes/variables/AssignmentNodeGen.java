// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.internal.SpecializationNode;
import com.oracle.truffle.api.dsl.internal.SpecializedNode;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.PascalTypesGen;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@GeneratedBy(AssignmentNode.class)
public final class AssignmentNodeGen extends AssignmentNode implements SpecializedNode {

    private final FrameSlot slot;
    @Child private ExpressionNode valueNode_;
    @CompilationFinal private Class<?> valueNodeType_;
    @Child private BaseNode_ specialization_;

    private AssignmentNodeGen(ExpressionNode valueNode, FrameSlot slot) {
        this.slot = slot;
        this.valueNode_ = valueNode;
        this.specialization_ = UninitializedNode_.create(this);
    }

    @Override
    protected FrameSlot getSlot() {
        return this.slot;
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
    public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
        return specialization_.executeBoolean(frameValue);
    }

    @Override
    public char executeChar(VirtualFrame frameValue) throws UnexpectedResultException {
        return specialization_.executeChar(frameValue);
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

    public static AssignmentNode create(ExpressionNode valueNode, FrameSlot slot) {
        return new AssignmentNodeGen(valueNode, slot);
    }

    @GeneratedBy(AssignmentNode.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected AssignmentNodeGen root;

        BaseNode_(AssignmentNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (AssignmentNodeGen) root;
        }

        @Override
        protected final Node[] getSuppliedChildren() {
            return new Node[] {root.valueNode_};
        }

        @Override
        public final Object acceptAndExecute(Frame frameValue, Object valueNodeValue) {
            return this.execute_((VirtualFrame) frameValue, valueNodeValue);
        }

        public abstract Object execute_(VirtualFrame frameValue, Object valueNodeValue);

        public Object execute(VirtualFrame frameValue) {
            Object valueNodeValue_ = executeValueNode_(frameValue);
            return execute_(frameValue, valueNodeValue_);
        }

        public void executeVoid(VirtualFrame frameValue) {
            execute(frameValue);
            return;
        }

        public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
            return PascalTypesGen.expectBoolean(execute(frameValue));
        }

        public char executeChar(VirtualFrame frameValue) throws UnexpectedResultException {
            return PascalTypesGen.expectCharacter(execute(frameValue));
        }

        public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
            return PascalTypesGen.expectLong(execute(frameValue));
        }

        @Override
        protected final SpecializationNode createNext(Frame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof Long) {
                if ((root.isLongKind((VirtualFrame) frameValue))) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    return WriteLongNode_.create(root);
                }
            }
            if (valueNodeValue instanceof Boolean) {
                if ((root.isBoolKind((VirtualFrame) frameValue))) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    return WriteBooleanNode_.create(root);
                }
            }
            if (valueNodeValue instanceof Character) {
                if ((root.isCharKind((VirtualFrame) frameValue))) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    return WriteChar0Node_.create(root);
                }
            }
            if (valueNodeValue instanceof Double) {
                if ((root.isDoubleKind((VirtualFrame) frameValue))) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    return WriteChar1Node_.create(root);
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

        protected final Object executeValueNode_(Frame frameValue) {
            Class<?> valueNodeType_ = root.valueNodeType_;
            try {
                if (valueNodeType_ == boolean.class) {
                    return root.valueNode_.executeBoolean((VirtualFrame) frameValue);
                } else if (valueNodeType_ == char.class) {
                    return root.valueNode_.executeChar((VirtualFrame) frameValue);
                } else if (valueNodeType_ == long.class) {
                    return root.valueNode_.executeLong((VirtualFrame) frameValue);
                } else if (valueNodeType_ == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    Class<?> _type = Object.class;
                    try {
                        Object _value = root.valueNode_.executeGeneric((VirtualFrame) frameValue);
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
                        root.valueNodeType_ = _type;
                    }
                } else {
                    return root.valueNode_.executeGeneric((VirtualFrame) frameValue);
                }
            } catch (UnexpectedResultException ex) {
                root.valueNodeType_ = Object.class;
                return ex.getResult();
            }
        }

    }
    @GeneratedBy(AssignmentNode.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(AssignmentNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object valueNodeValue) {
            return uninitialized(frameValue, valueNodeValue);
        }

        static BaseNode_ create(AssignmentNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(AssignmentNode.class)
    private static final class PolymorphicNode_ extends BaseNode_ {

        PolymorphicNode_(AssignmentNodeGen root) {
            super(root, 0);
        }

        @Override
        public SpecializationNode merge(SpecializationNode newNode, Frame frameValue, Object valueNodeValue) {
            return polymorphicMerge(newNode, super.merge(newNode, frameValue, valueNodeValue));
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object valueNodeValue) {
            return getNext().execute_(frameValue, valueNodeValue);
        }

        static BaseNode_ create(AssignmentNodeGen root) {
            return new PolymorphicNode_(root);
        }

    }
    @GeneratedBy(methodName = "writeLong(VirtualFrame, long)", value = AssignmentNode.class)
    private static final class WriteLongNode_ extends BaseNode_ {

        WriteLongNode_(AssignmentNodeGen root) {
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
            long valueNodeValue_;
            try {
                valueNodeValue_ = root.valueNode_.executeLong(frameValue);
            } catch (UnexpectedResultException ex) {
                return PascalTypesGen.expectLong(getNext().execute_(frameValue, ex.getResult()));
            }
            if ((root.isLongKind(frameValue))) {
                return root.writeLong(frameValue, valueNodeValue_);
            }
            return PascalTypesGen.expectLong(getNext().execute_(frameValue, valueNodeValue_));
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof Long) {
                long valueNodeValue_ = (long) valueNodeValue;
                if ((root.isLongKind(frameValue))) {
                    return root.writeLong(frameValue, valueNodeValue_);
                }
            }
            return getNext().execute_(frameValue, valueNodeValue);
        }

        static BaseNode_ create(AssignmentNodeGen root) {
            return new WriteLongNode_(root);
        }

    }
    @GeneratedBy(methodName = "writeBoolean(VirtualFrame, boolean)", value = AssignmentNode.class)
    private static final class WriteBooleanNode_ extends BaseNode_ {

        WriteBooleanNode_(AssignmentNodeGen root) {
            super(root, 2);
        }

        @Override
        public Object execute(VirtualFrame frameValue) {
            try {
                return executeBoolean(frameValue);
            } catch (UnexpectedResultException ex) {
                return ex.getResult();
            }
        }

        @Override
        public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
            boolean valueNodeValue_;
            try {
                valueNodeValue_ = root.valueNode_.executeBoolean(frameValue);
            } catch (UnexpectedResultException ex) {
                return PascalTypesGen.expectBoolean(getNext().execute_(frameValue, ex.getResult()));
            }
            if ((root.isBoolKind(frameValue))) {
                return root.writeBoolean(frameValue, valueNodeValue_);
            }
            return PascalTypesGen.expectBoolean(getNext().execute_(frameValue, valueNodeValue_));
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof Boolean) {
                boolean valueNodeValue_ = (boolean) valueNodeValue;
                if ((root.isBoolKind(frameValue))) {
                    return root.writeBoolean(frameValue, valueNodeValue_);
                }
            }
            return getNext().execute_(frameValue, valueNodeValue);
        }

        static BaseNode_ create(AssignmentNodeGen root) {
            return new WriteBooleanNode_(root);
        }

    }
    @GeneratedBy(methodName = "writeChar(VirtualFrame, char)", value = AssignmentNode.class)
    private static final class WriteChar0Node_ extends BaseNode_ {

        WriteChar0Node_(AssignmentNodeGen root) {
            super(root, 3);
        }

        @Override
        public Object execute(VirtualFrame frameValue) {
            try {
                return executeChar(frameValue);
            } catch (UnexpectedResultException ex) {
                return ex.getResult();
            }
        }

        @Override
        public char executeChar(VirtualFrame frameValue) throws UnexpectedResultException {
            char valueNodeValue_;
            try {
                valueNodeValue_ = root.valueNode_.executeChar(frameValue);
            } catch (UnexpectedResultException ex) {
                return PascalTypesGen.expectCharacter(getNext().execute_(frameValue, ex.getResult()));
            }
            if ((root.isCharKind(frameValue))) {
                return root.writeChar(frameValue, valueNodeValue_);
            }
            return PascalTypesGen.expectCharacter(getNext().execute_(frameValue, valueNodeValue_));
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof Character) {
                char valueNodeValue_ = (char) valueNodeValue;
                if ((root.isCharKind(frameValue))) {
                    return root.writeChar(frameValue, valueNodeValue_);
                }
            }
            return getNext().execute_(frameValue, valueNodeValue);
        }

        static BaseNode_ create(AssignmentNodeGen root) {
            return new WriteChar0Node_(root);
        }

    }
    @GeneratedBy(methodName = "writeChar(VirtualFrame, double)", value = AssignmentNode.class)
    private static final class WriteChar1Node_ extends BaseNode_ {

        WriteChar1Node_(AssignmentNodeGen root) {
            super(root, 4);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof Double) {
                double valueNodeValue_ = (double) valueNodeValue;
                if ((root.isDoubleKind(frameValue))) {
                    return root.writeChar(frameValue, valueNodeValue_);
                }
            }
            return getNext().execute_(frameValue, valueNodeValue);
        }

        static BaseNode_ create(AssignmentNodeGen root) {
            return new WriteChar1Node_(root);
        }

    }
}
