// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.graph;

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
import cz.cuni.mff.d3s.trupple.language.PascalTypes;
import cz.cuni.mff.d3s.trupple.language.PascalTypesGen;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalString;

@GeneratedBy(InitGraphNode.class)
public final class InitGraphNodeGen extends InitGraphNode implements SpecializedNode {

    @Child private ExpressionNode child0_;
    @Child private ExpressionNode child1_;
    @Child private ExpressionNode child2_;
    @CompilationFinal private Class<?> child0Type_;
    @CompilationFinal private Class<?> child1Type_;
    @CompilationFinal private Class<?> child2Type_;
    @Child private BaseNode_ specialization_;

    private InitGraphNodeGen(ExpressionNode child0, ExpressionNode child1, ExpressionNode child2) {
        this.child0_ = child0;
        this.child1_ = child1;
        this.child2_ = child2;
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

    public static InitGraphNode create(ExpressionNode child0, ExpressionNode child1, ExpressionNode child2) {
        return new InitGraphNodeGen(child0, child1, child2);
    }

    @GeneratedBy(InitGraphNode.class)
    private abstract static class BaseNode_ extends SpecializationNode {

        @CompilationFinal protected InitGraphNodeGen root;

        BaseNode_(InitGraphNodeGen root, int index) {
            super(index);
            this.root = root;
        }

        @Override
        protected final void setRoot(Node root) {
            this.root = (InitGraphNodeGen) root;
        }

        @Override
        protected final Node[] getSuppliedChildren() {
            return new Node[] {root.child0_, root.child1_, root.child2_};
        }

        @Override
        public final Object acceptAndExecute(Frame frameValue, Object child0Value, Object child1Value, Object child2Value) {
            this.executeVoid_((VirtualFrame) frameValue, child0Value, child1Value, child2Value);
            return null;
        }

        public abstract void executeVoid_(VirtualFrame frameValue, Object child0Value, Object child1Value, Object child2Value);

        public void executeVoid(VirtualFrame frameValue) {
            Object child0Value_ = executeChild0_(frameValue);
            Object child1Value_ = executeChild1_(frameValue);
            Object child2Value_ = executeChild2_(frameValue);
            executeVoid_(frameValue, child0Value_, child1Value_, child2Value_);
            return;
        }

        @Override
        protected final SpecializationNode createNext(Frame frameValue, Object child0Value, Object child1Value, Object child2Value) {
            if (child0Value instanceof Long && child1Value instanceof Long && PascalTypesGen.isImplicitPascalString(child2Value)) {
                return InitGraphNode_.create(root, child2Value);
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

        protected final Object executeChild0_(Frame frameValue) {
            Class<?> child0Type_ = root.child0Type_;
            try {
                if (child0Type_ == long.class) {
                    return root.child0_.executeLong((VirtualFrame) frameValue);
                } else if (child0Type_ == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    Class<?> _type = Object.class;
                    try {
                        Object _value = root.child0_.executeGeneric((VirtualFrame) frameValue);
                        if (_value instanceof Long) {
                            _type = long.class;
                        } else {
                            _type = Object.class;
                        }
                        return _value;
                    } finally {
                        root.child0Type_ = _type;
                    }
                } else {
                    return root.child0_.executeGeneric((VirtualFrame) frameValue);
                }
            } catch (UnexpectedResultException ex) {
                root.child0Type_ = Object.class;
                return ex.getResult();
            }
        }

        protected final Object executeChild1_(Frame frameValue) {
            Class<?> child1Type_ = root.child1Type_;
            try {
                if (child1Type_ == long.class) {
                    return root.child1_.executeLong((VirtualFrame) frameValue);
                } else if (child1Type_ == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    Class<?> _type = Object.class;
                    try {
                        Object _value = root.child1_.executeGeneric((VirtualFrame) frameValue);
                        if (_value instanceof Long) {
                            _type = long.class;
                        } else {
                            _type = Object.class;
                        }
                        return _value;
                    } finally {
                        root.child1Type_ = _type;
                    }
                } else {
                    return root.child1_.executeGeneric((VirtualFrame) frameValue);
                }
            } catch (UnexpectedResultException ex) {
                root.child1Type_ = Object.class;
                return ex.getResult();
            }
        }

        protected final Object executeChild2_(Frame frameValue) {
            Class<?> child2Type_ = root.child2Type_;
            try {
                if (child2Type_ == char.class) {
                    return root.child2_.executeChar((VirtualFrame) frameValue);
                } else if (child2Type_ == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    Class<?> _type = Object.class;
                    try {
                        Object _value = root.child2_.executeGeneric((VirtualFrame) frameValue);
                        if (_value instanceof Character) {
                            _type = char.class;
                        } else {
                            _type = Object.class;
                        }
                        return _value;
                    } finally {
                        root.child2Type_ = _type;
                    }
                } else {
                    return root.child2_.executeGeneric((VirtualFrame) frameValue);
                }
            } catch (UnexpectedResultException ex) {
                root.child2Type_ = Object.class;
                return ex.getResult();
            }
        }

    }
    @GeneratedBy(InitGraphNode.class)
    private static final class UninitializedNode_ extends BaseNode_ {

        UninitializedNode_(InitGraphNodeGen root) {
            super(root, 2147483647);
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object child0Value, Object child1Value, Object child2Value) {
            uninitialized(frameValue, child0Value, child1Value, child2Value);
            return;
        }

        static BaseNode_ create(InitGraphNodeGen root) {
            return new UninitializedNode_(root);
        }

    }
    @GeneratedBy(InitGraphNode.class)
    private static final class PolymorphicNode_ extends BaseNode_ {

        PolymorphicNode_(InitGraphNodeGen root) {
            super(root, 0);
        }

        @Override
        public SpecializationNode merge(SpecializationNode newNode, Frame frameValue, Object child0Value, Object child1Value, Object child2Value) {
            return polymorphicMerge(newNode, super.merge(newNode, frameValue, child0Value, child1Value, child2Value));
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object child0Value, Object child1Value, Object child2Value) {
            getNext().executeVoid_(frameValue, child0Value, child1Value, child2Value);
            return;
        }

        static BaseNode_ create(InitGraphNodeGen root) {
            return new PolymorphicNode_(root);
        }

    }
    @GeneratedBy(methodName = "initGraph(long, long, PascalString)", value = InitGraphNode.class)
    private static final class InitGraphNode_ extends BaseNode_ {

        private final Class<?> child2ImplicitType;

        InitGraphNode_(InitGraphNodeGen root, Object child2Value) {
            super(root, 1);
            this.child2ImplicitType = PascalTypesGen.getImplicitPascalStringClass(child2Value);
        }

        @Override
        public boolean isSame(SpecializationNode other) {
            return super.isSame(other) && this.child2ImplicitType == ((InitGraphNode_) other).child2ImplicitType;
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            long child0Value_;
            try {
                child0Value_ = root.child0_.executeLong(frameValue);
            } catch (UnexpectedResultException ex) {
                Object child1Value = executeChild1_(frameValue);
                Object child2Value = executeChild2_(frameValue);
                getNext().executeVoid_(frameValue, ex.getResult(), child1Value, child2Value);
                return;
            }
            long child1Value_;
            try {
                child1Value_ = root.child1_.executeLong(frameValue);
            } catch (UnexpectedResultException ex) {
                Object child2Value = executeChild2_(frameValue);
                getNext().executeVoid_(frameValue, child0Value_, ex.getResult(), child2Value);
                return;
            }
            PascalString child2Value_;
            try {
                if (child2ImplicitType == char.class) {
                    child2Value_ = PascalTypes.castCharToString(root.child2_.executeChar(frameValue));
                } else {
                    Object child2Value__ = executeChild2_(frameValue);
                    child2Value_ = PascalTypesGen.expectImplicitPascalString(child2Value__, child2ImplicitType);
                }
            } catch (UnexpectedResultException ex) {
                getNext().executeVoid_(frameValue, child0Value_, child1Value_, ex.getResult());
                return;
            }
            root.initGraph(child0Value_, child1Value_, child2Value_);
            return;
        }

        @Override
        public void executeVoid_(VirtualFrame frameValue, Object child0Value, Object child1Value, Object child2Value) {
            if (child0Value instanceof Long && child1Value instanceof Long && PascalTypesGen.isImplicitPascalString(child2Value, child2ImplicitType)) {
                long child0Value_ = (long) child0Value;
                long child1Value_ = (long) child1Value;
                PascalString child2Value_ = PascalTypesGen.asImplicitPascalString(child2Value, child2ImplicitType);
                root.initGraph(child0Value_, child1Value_, child2Value_);
                return;
            }
            getNext().executeVoid_(frameValue, child0Value, child1Value, child2Value);
            return;
        }

        static BaseNode_ create(InitGraphNodeGen root, Object child2Value) {
            return new InitGraphNode_(root, child2Value);
        }

    }
}
