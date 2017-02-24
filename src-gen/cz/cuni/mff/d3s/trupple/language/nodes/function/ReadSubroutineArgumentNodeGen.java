// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.function;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.internal.SpecializationNode;
import com.oracle.truffle.api.dsl.internal.SpecializedNode;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.PascalTypesGen;

@GeneratedBy(ReadSubroutineArgumentNode.class)
public final class ReadSubroutineArgumentNodeGen extends ReadSubroutineArgumentNode implements SpecializedNode {

    private final FrameSlotKind slotKind;
    @Child private BaseNode_ specialization_;

    private ReadSubroutineArgumentNodeGen(int index, FrameSlotKind slotKind) {
        super(index);
        this.slotKind = slotKind;
        this.specialization_ = UninitializedNode_.create(this);
    }

    @Override
    protected FrameSlotKind getSlotKind() {
        return this.slotKind;
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

    public static ReadSubroutineArgumentNode create(int index, FrameSlotKind slotKind) {
        return new ReadSubroutineArgumentNodeGen(index, slotKind);
    }

    @GeneratedBy(ReadSubroutineArgumentNode.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected ReadSubroutineArgumentNodeGen root;

        BaseNode_(ReadSubroutineArgumentNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (ReadSubroutineArgumentNodeGen) root;
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
                return ReadLongNode_.create(root);
            }
            if ((root.isBoolKind())) {
                return ReadBooleanNode_.create(root);
            }
            if ((root.isCharKind())) {
                return ReadCharNode_.create(root);
            }
            return ReadObjectNode_.create(root);
        }

    }
    @GeneratedBy(ReadSubroutineArgumentNode.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(ReadSubroutineArgumentNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public Object execute(VirtualFrame frameValue) {
            return uninitialized(frameValue);
        }

        static BaseNode_ create(ReadSubroutineArgumentNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(methodName = "readLong(VirtualFrame)", value = ReadSubroutineArgumentNode.class)
    private static final class ReadLongNode_ extends BaseNode_ {

        ReadLongNode_(ReadSubroutineArgumentNodeGen root) {
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
            return root.readLong(frameValue);
        }

        static BaseNode_ create(ReadSubroutineArgumentNodeGen root) {
            return new ReadLongNode_(root);
        }

    }
    @GeneratedBy(methodName = "readBoolean(VirtualFrame)", value = ReadSubroutineArgumentNode.class)
    private static final class ReadBooleanNode_ extends BaseNode_ {

        ReadBooleanNode_(ReadSubroutineArgumentNodeGen root) {
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
            return root.readBoolean(frameValue);
        }

        static BaseNode_ create(ReadSubroutineArgumentNodeGen root) {
            return new ReadBooleanNode_(root);
        }

    }
    @GeneratedBy(methodName = "readChar(VirtualFrame)", value = ReadSubroutineArgumentNode.class)
    private static final class ReadCharNode_ extends BaseNode_ {

        ReadCharNode_(ReadSubroutineArgumentNodeGen root) {
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
            return root.readChar(frameValue);
        }

        static BaseNode_ create(ReadSubroutineArgumentNodeGen root) {
            return new ReadCharNode_(root);
        }

    }
    @GeneratedBy(methodName = "readObject(VirtualFrame)", value = ReadSubroutineArgumentNode.class)
    private static final class ReadObjectNode_ extends BaseNode_ {

        ReadObjectNode_(ReadSubroutineArgumentNodeGen root) {
            super(root, 4);
        }

        @Override
        public Object execute(VirtualFrame frameValue) {
            return root.readObject(frameValue);
        }

        static BaseNode_ create(ReadSubroutineArgumentNodeGen root) {
            return new ReadObjectNode_(root);
        }

    }
}
