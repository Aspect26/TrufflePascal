// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.function;

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
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

@GeneratedBy(FunctionBodyNode.class)
public final class FunctionBodyNodeGen extends FunctionBodyNode implements SpecializedNode {

    private final FrameSlot slot;
    private final TypeDescriptor typeDescriptor;
    @Child private BaseNode_ specialization_;

    private FunctionBodyNodeGen(StatementNode bodyNode, FrameSlot slot, TypeDescriptor typeDescriptor) {
        super(bodyNode);
        this.slot = slot;
        this.typeDescriptor = typeDescriptor;
        this.specialization_ = UninitializedNode_.create(this);
    }

    @Override
    protected FrameSlot getSlot() {
        return this.slot;
    }

    @Override
    protected TypeDescriptor getTypeDescriptor() {
        return this.typeDescriptor;
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

    public static FunctionBodyNode create(StatementNode bodyNode, FrameSlot slot, TypeDescriptor typeDescriptor) {
        return new FunctionBodyNodeGen(bodyNode, slot, typeDescriptor);
    }

    @GeneratedBy(FunctionBodyNode.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected FunctionBodyNodeGen root;

        BaseNode_(FunctionBodyNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (FunctionBodyNodeGen) root;
        }

        @Override
        protected final Node[] getSuppliedChildren() {
            return new Node[] {};
        }

        @Override
        public final Object acceptAndExecute(Frame frameValue) {
            return this.execute((VirtualFrame) frameValue);
        }

        public abstract Object execute(VirtualFrame frameValue);

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
        protected final SpecializationNode createNext(Frame frameValue) {
            if ((root.isLongKind())) {
                return ExecLongNode_.create(root);
            }
            if ((root.isBoolKind())) {
                return ExecBoolNode_.create(root);
            }
            if ((root.isCharKind())) {
                return ExecCharNode_.create(root);
            }
            return ExecGenericNode_.create(root);
        }

    }
    @GeneratedBy(FunctionBodyNode.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(FunctionBodyNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public Object execute(VirtualFrame frameValue) {
            return uninitialized(frameValue);
        }

        static BaseNode_ create(FunctionBodyNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(methodName = "execLong(VirtualFrame)", value = FunctionBodyNode.class)
    private static final class ExecLongNode_ extends BaseNode_ {

        ExecLongNode_(FunctionBodyNodeGen root) {
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
            assert (root.isLongKind());
            return root.execLong(frameValue);
        }

        static BaseNode_ create(FunctionBodyNodeGen root) {
            return new ExecLongNode_(root);
        }

    }
    @GeneratedBy(methodName = "execBool(VirtualFrame)", value = FunctionBodyNode.class)
    private static final class ExecBoolNode_ extends BaseNode_ {

        ExecBoolNode_(FunctionBodyNodeGen root) {
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
            assert (root.isBoolKind());
            return root.execBool(frameValue);
        }

        static BaseNode_ create(FunctionBodyNodeGen root) {
            return new ExecBoolNode_(root);
        }

    }
    @GeneratedBy(methodName = "execChar(VirtualFrame)", value = FunctionBodyNode.class)
    private static final class ExecCharNode_ extends BaseNode_ {

        ExecCharNode_(FunctionBodyNodeGen root) {
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
            assert (root.isCharKind());
            return root.execChar(frameValue);
        }

        static BaseNode_ create(FunctionBodyNodeGen root) {
            return new ExecCharNode_(root);
        }

    }
    @GeneratedBy(methodName = "execGeneric(VirtualFrame)", value = FunctionBodyNode.class)
    private static final class ExecGenericNode_ extends BaseNode_ {

        ExecGenericNode_(FunctionBodyNodeGen root) {
            super(root, 4);
        }

        @Override
        public Object execute(VirtualFrame frameValue) {
            return root.execGeneric(frameValue);
        }

        static BaseNode_ create(FunctionBodyNodeGen root) {
            return new ExecGenericNode_(root);
        }

    }
}
