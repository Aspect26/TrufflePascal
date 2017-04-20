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
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalString;
import cz.cuni.mff.d3s.trupple.language.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.RecordValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute.AccessNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalSubroutine;

@GeneratedBy(AssignmentNodeWithRoute.class)
public final class AssignmentNodeWithRouteNodeGen extends AssignmentNodeWithRoute implements SpecializedNode {

    private final FrameSlot slot;
    @Child private ExpressionNode valueNode_;
    @CompilationFinal private Class<?> valueNodeType_;
    @Child private BaseNode_ specialization_;

    private AssignmentNodeWithRouteNodeGen(AccessNode accessNode, ExpressionNode valueNode, FrameSlot slot) {
        super(accessNode);
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

    public static AssignmentNodeWithRoute create(AccessNode accessNode, ExpressionNode valueNode, FrameSlot slot) {
        return new AssignmentNodeWithRouteNodeGen(accessNode, valueNode, slot);
    }

    @GeneratedBy(AssignmentNodeWithRoute.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected AssignmentNodeWithRouteNodeGen root;

        BaseNode_(AssignmentNodeWithRouteNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (AssignmentNodeWithRouteNodeGen) root;
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
            if (valueNodeValue instanceof Double) {
                return WriteDoubleNode_.create(root);
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
            if (valueNodeValue instanceof PascalString) {
                return AssignStringNode_.create(root);
            }
            if (valueNodeValue instanceof PascalSubroutine) {
                return AssignSubroutineNode_.create(root);
            }
            if (valueNodeValue instanceof PascalArray) {
                return AssignArrayNode_.create(root);
            }
            if (valueNodeValue instanceof VirtualFrame) {
                return TotallyUnnecessarSpecializationFunctionWhichWillNeverBeUsedButTruffleDSLJustFuckingNeedsItSoItCanGenerateTheActualNodeFromThisClass_IJustWantedToCreateTheLongestIdentifierIHaveEverCreateInMyLifeNode_.create(root);
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
    @GeneratedBy(AssignmentNodeWithRoute.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(AssignmentNodeWithRouteNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            uninitialized(frameValue, valueNodeValue);
            return;
        }

        static BaseNode_ create(AssignmentNodeWithRouteNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(AssignmentNodeWithRoute.class)
    private static final class PolymorphicNode_ extends BaseNode_ {

        PolymorphicNode_(AssignmentNodeWithRouteNodeGen root) {
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

        static BaseNode_ create(AssignmentNodeWithRouteNodeGen root) {
            return new PolymorphicNode_(root);
        }

    }
    @GeneratedBy(methodName = "writeLong(VirtualFrame, long)", value = AssignmentNodeWithRoute.class)
    private static final class WriteLongNode_ extends BaseNode_ {

        WriteLongNode_(AssignmentNodeWithRouteNodeGen root) {
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

        static BaseNode_ create(AssignmentNodeWithRouteNodeGen root) {
            return new WriteLongNode_(root);
        }

    }
    @GeneratedBy(methodName = "writeBoolean(VirtualFrame, boolean)", value = AssignmentNodeWithRoute.class)
    private static final class WriteBooleanNode_ extends BaseNode_ {

        WriteBooleanNode_(AssignmentNodeWithRouteNodeGen root) {
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

        static BaseNode_ create(AssignmentNodeWithRouteNodeGen root) {
            return new WriteBooleanNode_(root);
        }

    }
    @GeneratedBy(methodName = "writeChar(VirtualFrame, char)", value = AssignmentNodeWithRoute.class)
    private static final class WriteCharNode_ extends BaseNode_ {

        WriteCharNode_(AssignmentNodeWithRouteNodeGen root) {
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

        static BaseNode_ create(AssignmentNodeWithRouteNodeGen root) {
            return new WriteCharNode_(root);
        }

    }
    @GeneratedBy(methodName = "writeDouble(VirtualFrame, double)", value = AssignmentNodeWithRoute.class)
    private static final class WriteDoubleNode_ extends BaseNode_ {

        WriteDoubleNode_(AssignmentNodeWithRouteNodeGen root) {
            super(root, 4);
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof Double) {
                double valueNodeValue_ = (double) valueNodeValue;
                root.writeDouble(frameValue, valueNodeValue_);
                return;
            }
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
        }

        static BaseNode_ create(AssignmentNodeWithRouteNodeGen root) {
            return new WriteDoubleNode_(root);
        }

    }
    @GeneratedBy(methodName = "writeEnum(VirtualFrame, EnumValue)", value = AssignmentNodeWithRoute.class)
    private static final class WriteEnumNode_ extends BaseNode_ {

        WriteEnumNode_(AssignmentNodeWithRouteNodeGen root) {
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

        static BaseNode_ create(AssignmentNodeWithRouteNodeGen root) {
            return new WriteEnumNode_(root);
        }

    }
    @GeneratedBy(methodName = "assignSet(VirtualFrame, SetTypeValue)", value = AssignmentNodeWithRoute.class)
    private static final class AssignSetNode_ extends BaseNode_ {

        AssignSetNode_(AssignmentNodeWithRouteNodeGen root) {
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

        static BaseNode_ create(AssignmentNodeWithRouteNodeGen root) {
            return new AssignSetNode_(root);
        }

    }
    @GeneratedBy(methodName = "assignRecord(VirtualFrame, RecordValue)", value = AssignmentNodeWithRoute.class)
    private static final class AssignRecordNode_ extends BaseNode_ {

        AssignRecordNode_(AssignmentNodeWithRouteNodeGen root) {
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

        static BaseNode_ create(AssignmentNodeWithRouteNodeGen root) {
            return new AssignRecordNode_(root);
        }

    }
    @GeneratedBy(methodName = "assignReference(VirtualFrame, Reference)", value = AssignmentNodeWithRoute.class)
    private static final class AssignReferenceNode_ extends BaseNode_ {

        AssignReferenceNode_(AssignmentNodeWithRouteNodeGen root) {
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

        static BaseNode_ create(AssignmentNodeWithRouteNodeGen root) {
            return new AssignReferenceNode_(root);
        }

    }
    @GeneratedBy(methodName = "assignPointers(VirtualFrame, PointerValue)", value = AssignmentNodeWithRoute.class)
    private static final class AssignPointersNode_ extends BaseNode_ {

        AssignPointersNode_(AssignmentNodeWithRouteNodeGen root) {
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

        static BaseNode_ create(AssignmentNodeWithRouteNodeGen root) {
            return new AssignPointersNode_(root);
        }

    }
    @GeneratedBy(methodName = "assignString(VirtualFrame, PascalString)", value = AssignmentNodeWithRoute.class)
    private static final class AssignStringNode_ extends BaseNode_ {

        AssignStringNode_(AssignmentNodeWithRouteNodeGen root) {
            super(root, 10);
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof PascalString) {
                PascalString valueNodeValue_ = (PascalString) valueNodeValue;
                root.assignString(frameValue, valueNodeValue_);
                return;
            }
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
        }

        static BaseNode_ create(AssignmentNodeWithRouteNodeGen root) {
            return new AssignStringNode_(root);
        }

    }
    @GeneratedBy(methodName = "assignSubroutine(VirtualFrame, PascalSubroutine)", value = AssignmentNodeWithRoute.class)
    private static final class AssignSubroutineNode_ extends BaseNode_ {

        AssignSubroutineNode_(AssignmentNodeWithRouteNodeGen root) {
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

        static BaseNode_ create(AssignmentNodeWithRouteNodeGen root) {
            return new AssignSubroutineNode_(root);
        }

    }
    @GeneratedBy(methodName = "assignArray(VirtualFrame, PascalArray)", value = AssignmentNodeWithRoute.class)
    private static final class AssignArrayNode_ extends BaseNode_ {

        AssignArrayNode_(AssignmentNodeWithRouteNodeGen root) {
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

        static BaseNode_ create(AssignmentNodeWithRouteNodeGen root) {
            return new AssignArrayNode_(root);
        }

    }
    @GeneratedBy(methodName = "totallyUnnecessarSpecializationFunctionWhichWillNeverBeUsedButTruffleDSLJustFuckingNeedsItSoItCanGenerateTheActualNodeFromThisClass_IJustWantedToCreateTheLongestIdentifierIHaveEverCreateInMyLife(VirtualFrame)", value = AssignmentNodeWithRoute.class)
    private static final class TotallyUnnecessarSpecializationFunctionWhichWillNeverBeUsedButTruffleDSLJustFuckingNeedsItSoItCanGenerateTheActualNodeFromThisClass_IJustWantedToCreateTheLongestIdentifierIHaveEverCreateInMyLifeNode_ extends BaseNode_ {

        TotallyUnnecessarSpecializationFunctionWhichWillNeverBeUsedButTruffleDSLJustFuckingNeedsItSoItCanGenerateTheActualNodeFromThisClass_IJustWantedToCreateTheLongestIdentifierIHaveEverCreateInMyLifeNode_(AssignmentNodeWithRouteNodeGen root) {
            super(root, 13);
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object valueNodeValue) {
            if (valueNodeValue instanceof VirtualFrame) {
                VirtualFrame valueNodeValue_ = (VirtualFrame) valueNodeValue;
                root.totallyUnnecessarSpecializationFunctionWhichWillNeverBeUsedButTruffleDSLJustFuckingNeedsItSoItCanGenerateTheActualNodeFromThisClass_IJustWantedToCreateTheLongestIdentifierIHaveEverCreateInMyLife(valueNodeValue_);
                return;
            }
            getNext().executeVoid_(frameValue, valueNodeValue);
            return;
        }

        static BaseNode_ create(AssignmentNodeWithRouteNodeGen root) {
            return new TotallyUnnecessarSpecializationFunctionWhichWillNeverBeUsedButTruffleDSLJustFuckingNeedsItSoItCanGenerateTheActualNodeFromThisClass_IJustWantedToCreateTheLongestIdentifierIHaveEverCreateInMyLifeNode_(root);
        }

    }
}
