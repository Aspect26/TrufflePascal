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
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalString;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.RecordValue;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.SetTypeValue;

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
            this.executeVoid_((VirtualFrame) frameValue, valueNodeValue);
            return null;
        }

        public abstract void executeVoid_(VirtualFrame frameValue, Object valueNodeValue);

        public void executeVoid(VirtualFrame frameValue) {
            Object valueNodeValue_ = executeValueNode_(frameValue);
            executeVoid_(frameValue, valueNodeValue_);
            return;
        }

        @Override
        protected final SpecializationNode createNext(Frame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof Long) {
                return WriteLongNode_.create(root);
            }
            if (valueNodeValue instanceof Boolean) {
                return WriteBooleanNode_.create(root);
            }
            if (valueNodeValue instanceof Character) {
                return WriteCharNode_.create(root);
            }
            if (PascalTypesGen.isImplicitDouble(valueNodeValue)) {
                return WriteDoubleNode_.create(root, valueNodeValue);
            }
            if (valueNodeValue instanceof EnumValue) {
                return WriteEnumNode_.create(root);
            }
            if (valueNodeValue instanceof SetTypeValue) {
                return AssignSetNode_.create(root);
            }
            if (valueNodeValue instanceof RecordValue) {
                return AssignRecordNode_.create(root);
            }
            if (valueNodeValue instanceof Reference) {
                return AssignReferenceNode_.create(root);
            }
            if (valueNodeValue instanceof PointerValue) {
                return AssignPointersNode_.create(root);
            }
            if (PascalTypesGen.isImplicitPascalString(valueNodeValue)) {
                return AssignStringNode_.create(root, valueNodeValue);
            }
            if (valueNodeValue instanceof PascalSubroutine) {
                return AssignSubroutineNode_.create(root);
            }
            if (valueNodeValue instanceof PascalArray) {
                return AssignArrayNode_.create(root);
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
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            uninitialized(frameValue, valueNodeValue);
            return;
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
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
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
        public void executeVoid(VirtualFrame frameValue) {
            long valueNodeValue_;
            try {
                valueNodeValue_ = root.valueNode_.executeLong(frameValue);
            } catch (UnexpectedResultException ex) {
                getNext().executeVoid_(frameValue, ex.getResult());
                return;
            }
            root.writeLong(frameValue, valueNodeValue_);
            return;
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof Long) {
                long valueNodeValue_ = (long) valueNodeValue;
                root.writeLong(frameValue, valueNodeValue_);
                return;
            }
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
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
        public void executeVoid(VirtualFrame frameValue) {
            boolean valueNodeValue_;
            try {
                valueNodeValue_ = root.valueNode_.executeBoolean(frameValue);
            } catch (UnexpectedResultException ex) {
                getNext().executeVoid_(frameValue, ex.getResult());
                return;
            }
            root.writeBoolean(frameValue, valueNodeValue_);
            return;
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof Boolean) {
                boolean valueNodeValue_ = (boolean) valueNodeValue;
                root.writeBoolean(frameValue, valueNodeValue_);
                return;
            }
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
        }

        static BaseNode_ create(AssignmentNodeGen root) {
            return new WriteBooleanNode_(root);
        }

    }
    @GeneratedBy(methodName = "writeChar(VirtualFrame, char)", value = AssignmentNode.class)
    private static final class WriteCharNode_ extends BaseNode_ {

        WriteCharNode_(AssignmentNodeGen root) {
            super(root, 3);
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            char valueNodeValue_;
            try {
                valueNodeValue_ = root.valueNode_.executeChar(frameValue);
            } catch (UnexpectedResultException ex) {
                getNext().executeVoid_(frameValue, ex.getResult());
                return;
            }
            root.writeChar(frameValue, valueNodeValue_);
            return;
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof Character) {
                char valueNodeValue_ = (char) valueNodeValue;
                root.writeChar(frameValue, valueNodeValue_);
                return;
            }
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
        }

        static BaseNode_ create(AssignmentNodeGen root) {
            return new WriteCharNode_(root);
        }

    }
    @GeneratedBy(methodName = "writeDouble(VirtualFrame, double)", value = AssignmentNode.class)
    private static final class WriteDoubleNode_ extends BaseNode_ {

        private final Class<?> valueNodeImplicitType;

        WriteDoubleNode_(AssignmentNodeGen root, Object valueNodeValue) {
            super(root, 4);
            this.valueNodeImplicitType = PascalTypesGen.getImplicitDoubleClass(valueNodeValue);
        }

        @Override
        public boolean isSame(SpecializationNode other) {
            return super.isSame(other) && this.valueNodeImplicitType == ((WriteDoubleNode_) other).valueNodeImplicitType;
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            if (PascalTypesGen.isImplicitDouble(valueNodeValue, valueNodeImplicitType)) {
                double valueNodeValue_ = PascalTypesGen.asImplicitDouble(valueNodeValue, valueNodeImplicitType);
                root.writeDouble(frameValue, valueNodeValue_);
                return;
            }
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
        }

        static BaseNode_ create(AssignmentNodeGen root, Object valueNodeValue) {
            return new WriteDoubleNode_(root, valueNodeValue);
        }

    }
    @GeneratedBy(methodName = "writeEnum(VirtualFrame, EnumValue)", value = AssignmentNode.class)
    private static final class WriteEnumNode_ extends BaseNode_ {

        WriteEnumNode_(AssignmentNodeGen root) {
            super(root, 5);
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof EnumValue) {
                EnumValue valueNodeValue_ = (EnumValue) valueNodeValue;
                root.writeEnum(frameValue, valueNodeValue_);
                return;
            }
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
        }

        static BaseNode_ create(AssignmentNodeGen root) {
            return new WriteEnumNode_(root);
        }

    }
    @GeneratedBy(methodName = "assignSet(VirtualFrame, SetTypeValue)", value = AssignmentNode.class)
    private static final class AssignSetNode_ extends BaseNode_ {

        AssignSetNode_(AssignmentNodeGen root) {
            super(root, 6);
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof SetTypeValue) {
                SetTypeValue valueNodeValue_ = (SetTypeValue) valueNodeValue;
                root.assignSet(frameValue, valueNodeValue_);
                return;
            }
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
        }

        static BaseNode_ create(AssignmentNodeGen root) {
            return new AssignSetNode_(root);
        }

    }
    @GeneratedBy(methodName = "assignRecord(VirtualFrame, RecordValue)", value = AssignmentNode.class)
    private static final class AssignRecordNode_ extends BaseNode_ {

        AssignRecordNode_(AssignmentNodeGen root) {
            super(root, 7);
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof RecordValue) {
                RecordValue valueNodeValue_ = (RecordValue) valueNodeValue;
                root.assignRecord(frameValue, valueNodeValue_);
                return;
            }
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
        }

        static BaseNode_ create(AssignmentNodeGen root) {
            return new AssignRecordNode_(root);
        }

    }
    @GeneratedBy(methodName = "assignReference(VirtualFrame, Reference)", value = AssignmentNode.class)
    private static final class AssignReferenceNode_ extends BaseNode_ {

        AssignReferenceNode_(AssignmentNodeGen root) {
            super(root, 8);
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof Reference) {
                Reference valueNodeValue_ = (Reference) valueNodeValue;
                root.assignReference(frameValue, valueNodeValue_);
                return;
            }
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
        }

        static BaseNode_ create(AssignmentNodeGen root) {
            return new AssignReferenceNode_(root);
        }

    }
    @GeneratedBy(methodName = "assignPointers(VirtualFrame, PointerValue)", value = AssignmentNode.class)
    private static final class AssignPointersNode_ extends BaseNode_ {

        AssignPointersNode_(AssignmentNodeGen root) {
            super(root, 9);
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof PointerValue) {
                PointerValue valueNodeValue_ = (PointerValue) valueNodeValue;
                root.assignPointers(frameValue, valueNodeValue_);
                return;
            }
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
        }

        static BaseNode_ create(AssignmentNodeGen root) {
            return new AssignPointersNode_(root);
        }

    }
    @GeneratedBy(methodName = "assignString(VirtualFrame, PascalString)", value = AssignmentNode.class)
    private static final class AssignStringNode_ extends BaseNode_ {

        private final Class<?> valueNodeImplicitType;

        AssignStringNode_(AssignmentNodeGen root, Object valueNodeValue) {
            super(root, 10);
            this.valueNodeImplicitType = PascalTypesGen.getImplicitPascalStringClass(valueNodeValue);
        }

        @Override
        public boolean isSame(SpecializationNode other) {
            return super.isSame(other) && this.valueNodeImplicitType == ((AssignStringNode_) other).valueNodeImplicitType;
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            if (PascalTypesGen.isImplicitPascalString(valueNodeValue, valueNodeImplicitType)) {
                PascalString valueNodeValue_ = PascalTypesGen.asImplicitPascalString(valueNodeValue, valueNodeImplicitType);
                root.assignString(frameValue, valueNodeValue_);
                return;
            }
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
        }

        static BaseNode_ create(AssignmentNodeGen root, Object valueNodeValue) {
            return new AssignStringNode_(root, valueNodeValue);
        }

    }
    @GeneratedBy(methodName = "assignSubroutine(VirtualFrame, PascalSubroutine)", value = AssignmentNode.class)
    private static final class AssignSubroutineNode_ extends BaseNode_ {

        AssignSubroutineNode_(AssignmentNodeGen root) {
            super(root, 11);
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof PascalSubroutine) {
                PascalSubroutine valueNodeValue_ = (PascalSubroutine) valueNodeValue;
                root.assignSubroutine(frameValue, valueNodeValue_);
                return;
            }
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
        }

        static BaseNode_ create(AssignmentNodeGen root) {
            return new AssignSubroutineNode_(root);
        }

    }
    @GeneratedBy(methodName = "assignArray(VirtualFrame, PascalArray)", value = AssignmentNode.class)
    private static final class AssignArrayNode_ extends BaseNode_ {

        AssignArrayNode_(AssignmentNodeGen root) {
            super(root, 12);
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof PascalArray) {
                PascalArray valueNodeValue_ = (PascalArray) valueNodeValue;
                root.assignArray(frameValue, valueNodeValue_);
                return;
            }
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
        }

        static BaseNode_ create(AssignmentNodeGen root) {
            return new AssignArrayNode_(root);
        }

    }
}
