// CheckStyle: start generated
package pascal.language.nodes;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.internal.SpecializationNode;
import com.oracle.truffle.api.dsl.internal.SpecializedNode;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import pascal.language.runtime.PascalFunction;

@GeneratedBy(InvokeNode.class)
public final class InvokeNodeGen extends InvokeNode implements SpecializedNode {

    @Child private ExpressionNode functionNode_;
    @Child private BaseNode_ specialization_;

    private InvokeNodeGen(ExpressionNode[] argumentNodes, ExpressionNode functionNode) {
        super(argumentNodes);
        this.functionNode_ = functionNode;
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
    public SpecializationNode getSpecializationNode() {
        return specialization_;
    }

    @Override
    public Node deepCopy() {
        return SpecializationNode.updateRoot(super.deepCopy());
    }

    public static InvokeNode create(ExpressionNode[] argumentNodes, ExpressionNode functionNode) {
        return new InvokeNodeGen(argumentNodes, functionNode);
    }

    @GeneratedBy(InvokeNode.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected InvokeNodeGen root;

        BaseNode_(InvokeNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (InvokeNodeGen) root;
        }

        @Override
        protected final Node[] getSuppliedChildren() {
            return new Node[] {root.functionNode_};
        }

        @Override
        public final Object acceptAndExecute(Frame frameValue, Object functionNodeValue) {
            return this.execute_((VirtualFrame) frameValue, functionNodeValue);
        }

        public abstract Object execute_(VirtualFrame frameValue, Object functionNodeValue);

        public Object execute(VirtualFrame frameValue) {
            Object functionNodeValue_ = root.functionNode_.executeGeneric(frameValue);
            return execute_(frameValue, functionNodeValue_);
        }

        public void executeVoid(VirtualFrame frameValue) {
            execute(frameValue);
            return;
        }

        @Override
        protected final SpecializationNode createNext(Frame frameValue, Object functionNodeValue) {
            if (functionNodeValue instanceof PascalFunction) {
                return ExecuteGeneric0Node_.create(root);
            }
            if (functionNodeValue instanceof TruffleObject) {
                return ExecuteGeneric1Node_.create(root);
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

    }
    @GeneratedBy(InvokeNode.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(InvokeNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object functionNodeValue) {
            return uninitialized(frameValue, functionNodeValue);
        }

        static BaseNode_ create(InvokeNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(InvokeNode.class)
    private static final class PolymorphicNode_ extends BaseNode_ {

        PolymorphicNode_(InvokeNodeGen root) {
            super(root, 0);
        }

        @Override
        public SpecializationNode merge(SpecializationNode newNode, Frame frameValue, Object functionNodeValue) {
            return polymorphicMerge(newNode, super.merge(newNode, frameValue, functionNodeValue));
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object functionNodeValue) {
            return getNext().execute_(frameValue, functionNodeValue);
        }

        static BaseNode_ create(InvokeNodeGen root) {
            return new PolymorphicNode_(root);
        }

    }
    @GeneratedBy(methodName = "executeGeneric(VirtualFrame, PascalFunction)", value = InvokeNode.class)
    private static final class ExecuteGeneric0Node_ extends BaseNode_ {

        ExecuteGeneric0Node_(InvokeNodeGen root) {
            super(root, 1);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object functionNodeValue) {
            if (functionNodeValue instanceof PascalFunction) {
                PascalFunction functionNodeValue_ = (PascalFunction) functionNodeValue;
                return root.executeGeneric(frameValue, functionNodeValue_);
            }
            return getNext().execute_(frameValue, functionNodeValue);
        }

        static BaseNode_ create(InvokeNodeGen root) {
            return new ExecuteGeneric0Node_(root);
        }

    }
    @GeneratedBy(methodName = "executeGeneric(VirtualFrame, TruffleObject)", value = InvokeNode.class)
    private static final class ExecuteGeneric1Node_ extends BaseNode_ {

        ExecuteGeneric1Node_(InvokeNodeGen root) {
            super(root, 2);
        }

        @Override
        public Object execute_(VirtualFrame frameValue, Object functionNodeValue) {
            if (functionNodeValue instanceof TruffleObject) {
                TruffleObject functionNodeValue_ = (TruffleObject) functionNodeValue;
                return root.executeGeneric(frameValue, functionNodeValue_);
            }
            return getNext().execute_(frameValue, functionNodeValue);
        }

        static BaseNode_ create(InvokeNodeGen root) {
            return new ExecuteGeneric1Node_(root);
        }

    }
}
