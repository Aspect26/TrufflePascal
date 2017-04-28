// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.tp;

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
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalString;

@GeneratedBy(AssignBuiltinNode.class)
public final class AssignBuiltinNodeGen extends AssignBuiltinNode implements SpecializedNode {

    @Child private ExpressionNode file_;
    @Child private ExpressionNode path_;
    @CompilationFinal private Class<?> pathType_;
    @Child private BaseNode_ specialization_;

    private AssignBuiltinNodeGen(ExpressionNode file, ExpressionNode path) {
        this.file_ = file;
        this.path_ = path;
        this.specialization_ = UninitializedNode_.create(this);
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

    public static AssignBuiltinNode create(ExpressionNode file, ExpressionNode path) {
        return new AssignBuiltinNodeGen(file, path);
    }

    @GeneratedBy(AssignBuiltinNode.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected AssignBuiltinNodeGen root;

        BaseNode_(AssignBuiltinNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (AssignBuiltinNodeGen) root;
        }

        @Override
        protected final Node[] getSuppliedChildren() {
            return new Node[] {root.file_, root.path_};
        }

        @Override
        public final Object acceptAndExecute(Frame frameValue, Object fileValue, Object pathValue) {
            this.executeVoid_((VirtualFrame) frameValue, fileValue, pathValue);
            return null;
        }

        public abstract void executeVoid_(VirtualFrame frameValue, Object fileValue, Object pathValue);

        public void executeVoid(VirtualFrame frameValue) {
            Object fileValue_ = root.file_.executeGeneric(frameValue);
            Object pathValue_ = executePath_(frameValue);
            executeVoid_(frameValue, fileValue_, pathValue_);
            return;
        }

        @Override
        protected final SpecializationNode createNext(Frame frameValue, Object fileValue, Object pathValue) {
            if (fileValue instanceof FileValue && PascalTypesGen.isImplicitPascalString(pathValue)) {
                return AssignFileNode_.create(root, pathValue);
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

        protected final Object executePath_(Frame frameValue) {
            Class<?> pathType_ = root.pathType_;
            try {
                if (pathType_ == char.class) {
                    return root.path_.executeChar((VirtualFrame) frameValue);
                } else if (pathType_ == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    Class<?> _type = Object.class;
                    try {
                        Object _value = root.path_.executeGeneric((VirtualFrame) frameValue);
                        if (_value instanceof Character) {
                            _type = char.class;
                        } else {
                            _type = Object.class;
                        }
                        return _value;
                    } finally {
                        root.pathType_ = _type;
                    }
                } else {
                    return root.path_.executeGeneric((VirtualFrame) frameValue);
                }
            } catch (UnexpectedResultException ex) {
                root.pathType_ = Object.class;
                return ex.getResult();
            }
        }

    }
    @GeneratedBy(AssignBuiltinNode.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(AssignBuiltinNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object fileValue, Object pathValue) {
            uninitialized(frameValue, fileValue, pathValue);
            return;
        }

        static BaseNode_ create(AssignBuiltinNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(AssignBuiltinNode.class)
    private static final class PolymorphicNode_ extends BaseNode_ {

        PolymorphicNode_(AssignBuiltinNodeGen root) {
            super(root, 0);
        }

        @Override
        public SpecializationNode merge(SpecializationNode newNode, Frame frameValue, Object fileValue, Object pathValue) {
            return polymorphicMerge(newNode, super.merge(newNode, frameValue, fileValue, pathValue));
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object fileValue, Object pathValue) {
            getNext().executeVoid_(frameValue, fileValue, pathValue);
            return;
        }

        static BaseNode_ create(AssignBuiltinNodeGen root) {
            return new PolymorphicNode_(root);
        }

    }
    @GeneratedBy(methodName = "assignFile(FileValue, PascalString)", value = AssignBuiltinNode.class)
    private static final class AssignFileNode_ extends BaseNode_ {

        private final Class<?> pathImplicitType;

        AssignFileNode_(AssignBuiltinNodeGen root, Object pathValue) {
            super(root, 1);
            this.pathImplicitType = PascalTypesGen.getImplicitPascalStringClass(pathValue);
        }

        @Override
        public boolean isSame(SpecializationNode other) {
            return super.isSame(other) && this.pathImplicitType == ((AssignFileNode_) other).pathImplicitType;
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object fileValue, Object pathValue) {
            if (fileValue instanceof FileValue && PascalTypesGen.isImplicitPascalString(pathValue, pathImplicitType)) {
                FileValue fileValue_ = (FileValue) fileValue;
                PascalString pathValue_ = PascalTypesGen.asImplicitPascalString(pathValue, pathImplicitType);
                root.assignFile(fileValue_, pathValue_);
                return;
            }
            getNext().executeVoid_(frameValue, fileValue, pathValue);
            return;
        }

        static BaseNode_ create(AssignBuiltinNodeGen root, Object pathValue) {
            return new AssignFileNode_(root, pathValue);
        }

    }
}
