// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.call;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.internal.SpecializationNode;
import com.oracle.truffle.api.dsl.internal.SpecializedNode;
import com.oracle.truffle.api.dsl.internal.SuppressFBWarnings;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalFunction;

@GeneratedBy(DispatchNode.class)
@SuppressFBWarnings("SA_LOCAL_SELF_COMPARISON")
public final class DispatchNodeGen extends DispatchNode implements SpecializedNode {

    @Child private BaseNode_ specialization_;

    private DispatchNodeGen() {
        this.specialization_ = UninitializedNode_.create(this);
    }

    @Override
    public NodeCost getCost() {
        return specialization_.getNodeCost();
    }

    @Override
    public Object executeDispatch(VirtualFrame frameValue, PascalFunction arg0Value, Object[] arg1Value) {
        return specialization_.execute(frameValue, arg0Value, arg1Value);
    }

    @Override
    public SpecializationNode getSpecializationNode() {
        return specialization_;
    }

    @Override
    public Node deepCopy() {
        return SpecializationNode.updateRoot(super.deepCopy());
    }

    public static DispatchNode create() {
        return new DispatchNodeGen();
    }

    @GeneratedBy(DispatchNode.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected DispatchNodeGen root;

        BaseNode_(DispatchNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (DispatchNodeGen) root;
        }

        @Override
        protected final Node[] getSuppliedChildren() {
            return new Node[] {null, null};
        }

        @Override
        public final Object acceptAndExecute(Frame frameValue, Object arg0Value, Object arg1Value) {
            return this.execute((VirtualFrame) frameValue, (PascalFunction) arg0Value, (Object[]) arg1Value);
        }

        public abstract Object execute(VirtualFrame frameValue, PascalFunction arg0Value, Object[] arg1Value);

        @Override
        protected final SpecializationNode createNext(Frame frameValue, Object arg0Value, Object arg1Value) {
            if (arg0Value instanceof PascalFunction && arg1Value instanceof Object[]) {
                PascalFunction arg0Value_ = (PascalFunction) arg0Value;
                if ((arg0Value_.getCallTarget() == null)) {
                    return UndefinedFunctionNode_.create(root);
                }
                return DirectNode_.create(root);
            }
            return null;
        }

        protected final BaseNode_ getNext() {
            return (BaseNode_) this.next;
        }

    }
    @GeneratedBy(DispatchNode.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(DispatchNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public Object execute(VirtualFrame frameValue, PascalFunction arg0Value, Object[] arg1Value) {
            return uninitialized(frameValue, arg0Value, arg1Value);
        }

        static BaseNode_ create(DispatchNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(methodName = "doundefinedFunction(PascalFunction, Object[])", value = DispatchNode.class)
    private static final class UndefinedFunctionNode_ extends BaseNode_ {

        UndefinedFunctionNode_(DispatchNodeGen root) {
            super(root, 1);
        }

        @Override
        public Object execute(VirtualFrame frameValue, PascalFunction arg0Value, Object[] arg1Value) {
            if ((arg0Value.getCallTarget() == null)) {
                return root.doundefinedFunction(arg0Value, arg1Value);
            }
            return getNext().execute(frameValue, arg0Value, arg1Value);
        }

        static BaseNode_ create(DispatchNodeGen root) {
            return new UndefinedFunctionNode_(root);
        }

    }
    @GeneratedBy(methodName = "doDirect(VirtualFrame, PascalFunction, Object[])", value = DispatchNode.class)
    private static final class DirectNode_ extends BaseNode_ {

        DirectNode_(DispatchNodeGen root) {
            super(root, 2);
        }

        @Override
        public Object execute(VirtualFrame frameValue, PascalFunction arg0Value, Object[] arg1Value) {
            return DispatchNode.doDirect(frameValue, arg0Value, arg1Value);
        }

        static BaseNode_ create(DispatchNodeGen root) {
            return new DirectNode_(root);
        }

    }
}
