// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.internal.SpecializationNode;
import com.oracle.truffle.api.dsl.internal.SpecializedNode;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import java.util.Arrays;
import java.util.List;

@GeneratedBy(OrdBuiltinNode.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class OrdBuiltinNodeFactory implements NodeFactory<OrdBuiltinNode> {

    private static OrdBuiltinNodeFactory instance;

    private OrdBuiltinNodeFactory() {
    }

    @Override
    public Class<OrdBuiltinNode> getNodeClass() {
        return OrdBuiltinNode.class;
    }

    @Override
    public List getExecutionSignature() {
        return Arrays.asList(ExpressionNode.class);
    }

    @Override
    public List getNodeSignatures() {
        return Arrays.asList(Arrays.asList(PascalContext.class, ExpressionNode.class));
    }

    @Override
    public OrdBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof PascalContext) && (arguments[1] == null || arguments[1] instanceof ExpressionNode)) {
            return create((PascalContext) arguments[0], (ExpressionNode) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<OrdBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new OrdBuiltinNodeFactory();
        }
        return instance;
    }

    public static OrdBuiltinNode create(PascalContext context, ExpressionNode argument) {
        return new OrdBuiltinNodeGen(context, argument);
    }

    @GeneratedBy(OrdBuiltinNode.class)
    public static final class OrdBuiltinNodeGen extends OrdBuiltinNode implements SpecializedNode {

        @Child private ExpressionNode argument_;
        @CompilationFinal private Class<?> argumentType_;
        @Child private BaseNode_ specialization_;

        private OrdBuiltinNodeGen(PascalContext context, ExpressionNode argument) {
            super(context);
            this.argument_ = argument;
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
        public long executeLong(VirtualFrame frameValue) {
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

        @GeneratedBy(OrdBuiltinNode.class)
        private abstract static class BaseNode_ extends SpecializationNode {

            @CompilationFinal protected OrdBuiltinNodeGen root;

            BaseNode_(OrdBuiltinNodeGen root, int index) {
                super(index);
                this.root = root;
            }

            @Override
            protected final void setRoot(Node root) {
                this.root = (OrdBuiltinNodeGen) root;
            }

            @Override
            protected final Node[] getSuppliedChildren() {
                return new Node[] {root.argument_};
            }

            @Override
            public final Object acceptAndExecute(Frame frameValue, Object argumentValue) {
                return this.executeLong_((VirtualFrame) frameValue, argumentValue);
            }

            public abstract long executeLong_(VirtualFrame frameValue, Object argumentValue);

            public Object execute(VirtualFrame frameValue) {
                Object argumentValue_ = executeArgument_(frameValue);
                return executeLong_(frameValue, argumentValue_);
            }

            public void executeVoid(VirtualFrame frameValue) {
                execute(frameValue);
                return;
            }

            public long executeLong(VirtualFrame frameValue) {
                return (long) execute(frameValue);
            }

            @Override
            protected final SpecializationNode createNext(Frame frameValue, Object argumentValue) {
                if (argumentValue instanceof Long) {
                    return Ord0Node_.create(root);
                }
                if (argumentValue instanceof Boolean) {
                    return Ord1Node_.create(root);
                }
                if (argumentValue instanceof Character) {
                    return Ord2Node_.create(root);
                }
                if (argumentValue instanceof EnumValue) {
                    return Ord3Node_.create(root);
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

            protected final Object executeArgument_(Frame frameValue) {
                Class<?> argumentType_ = root.argumentType_;
                try {
                    if (argumentType_ == boolean.class) {
                        return root.argument_.executeBoolean((VirtualFrame) frameValue);
                    } else if (argumentType_ == char.class) {
                        return root.argument_.executeChar((VirtualFrame) frameValue);
                    } else if (argumentType_ == long.class) {
                        return root.argument_.executeLong((VirtualFrame) frameValue);
                    } else if (argumentType_ == null) {
                        CompilerDirectives.transferToInterpreterAndInvalidate();
                        Class<?> _type = Object.class;
                        try {
                            Object _value = root.argument_.executeGeneric((VirtualFrame) frameValue);
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
                            root.argumentType_ = _type;
                        }
                    } else {
                        return root.argument_.executeGeneric((VirtualFrame) frameValue);
                    }
                } catch (UnexpectedResultException ex) {
                    root.argumentType_ = Object.class;
                    return ex.getResult();
                }
            }

        }
        @GeneratedBy(OrdBuiltinNode.class)
        private static final class UninitializedNode_ extends BaseNode_ {

            UninitializedNode_(OrdBuiltinNodeGen root) {
                super(root, 2147483647);
            }

            @Override
            public long executeLong_(VirtualFrame frameValue, Object argumentValue) {
                return (long) uninitialized(frameValue, argumentValue);
            }

            static BaseNode_ create(OrdBuiltinNodeGen root) {
                return new UninitializedNode_(root);
            }

        }
        @GeneratedBy(OrdBuiltinNode.class)
        private static final class PolymorphicNode_ extends BaseNode_ {

            PolymorphicNode_(OrdBuiltinNodeGen root) {
                super(root, 0);
            }

            @Override
            public SpecializationNode merge(SpecializationNode newNode, Frame frameValue, Object argumentValue) {
                return polymorphicMerge(newNode, super.merge(newNode, frameValue, argumentValue));
            }

            @Override
            public long executeLong(VirtualFrame frameValue) {
                Object argumentValue_ = executeArgument_(frameValue);
                return getNext().executeLong_(frameValue, argumentValue_);
            }

            @Override
            public long executeLong_(VirtualFrame frameValue, Object argumentValue) {
                return getNext().executeLong_(frameValue, argumentValue);
            }

            static BaseNode_ create(OrdBuiltinNodeGen root) {
                return new PolymorphicNode_(root);
            }

        }
        @GeneratedBy(methodName = "ord(long)", value = OrdBuiltinNode.class)
        private static final class Ord0Node_ extends BaseNode_ {

            Ord0Node_(OrdBuiltinNodeGen root) {
                super(root, 1);
            }

            @Override
            public Object execute(VirtualFrame frameValue) {
                return executeLong(frameValue);
            }

            @Override
            public long executeLong(VirtualFrame frameValue) {
                long argumentValue_;
                try {
                    argumentValue_ = root.argument_.executeLong(frameValue);
                } catch (UnexpectedResultException ex) {
                    return getNext().executeLong_(frameValue, ex.getResult());
                }
                return root.ord(argumentValue_);
            }

            @Override
            public long executeLong_(VirtualFrame frameValue, Object argumentValue) {
                if (argumentValue instanceof Long) {
                    long argumentValue_ = (long) argumentValue;
                    return root.ord(argumentValue_);
                }
                return getNext().executeLong_(frameValue, argumentValue);
            }

            static BaseNode_ create(OrdBuiltinNodeGen root) {
                return new Ord0Node_(root);
            }

        }
        @GeneratedBy(methodName = "ord(boolean)", value = OrdBuiltinNode.class)
        private static final class Ord1Node_ extends BaseNode_ {

            Ord1Node_(OrdBuiltinNodeGen root) {
                super(root, 2);
            }

            @Override
            public Object execute(VirtualFrame frameValue) {
                return executeLong(frameValue);
            }

            @Override
            public long executeLong(VirtualFrame frameValue) {
                boolean argumentValue_;
                try {
                    argumentValue_ = root.argument_.executeBoolean(frameValue);
                } catch (UnexpectedResultException ex) {
                    return getNext().executeLong_(frameValue, ex.getResult());
                }
                return root.ord(argumentValue_);
            }

            @Override
            public long executeLong_(VirtualFrame frameValue, Object argumentValue) {
                if (argumentValue instanceof Boolean) {
                    boolean argumentValue_ = (boolean) argumentValue;
                    return root.ord(argumentValue_);
                }
                return getNext().executeLong_(frameValue, argumentValue);
            }

            static BaseNode_ create(OrdBuiltinNodeGen root) {
                return new Ord1Node_(root);
            }

        }
        @GeneratedBy(methodName = "ord(char)", value = OrdBuiltinNode.class)
        private static final class Ord2Node_ extends BaseNode_ {

            Ord2Node_(OrdBuiltinNodeGen root) {
                super(root, 3);
            }

            @Override
            public Object execute(VirtualFrame frameValue) {
                return executeLong(frameValue);
            }

            @Override
            public long executeLong(VirtualFrame frameValue) {
                char argumentValue_;
                try {
                    argumentValue_ = root.argument_.executeChar(frameValue);
                } catch (UnexpectedResultException ex) {
                    return getNext().executeLong_(frameValue, ex.getResult());
                }
                return root.ord(argumentValue_);
            }

            @Override
            public long executeLong_(VirtualFrame frameValue, Object argumentValue) {
                if (argumentValue instanceof Character) {
                    char argumentValue_ = (char) argumentValue;
                    return root.ord(argumentValue_);
                }
                return getNext().executeLong_(frameValue, argumentValue);
            }

            static BaseNode_ create(OrdBuiltinNodeGen root) {
                return new Ord2Node_(root);
            }

        }
        @GeneratedBy(methodName = "ord(EnumValue)", value = OrdBuiltinNode.class)
        private static final class Ord3Node_ extends BaseNode_ {

            Ord3Node_(OrdBuiltinNodeGen root) {
                super(root, 4);
            }

            @Override
            public long executeLong(VirtualFrame frameValue) {
                EnumValue argumentValue_;
                try {
                    argumentValue_ = root.argument_.executeEnum(frameValue);
                } catch (UnexpectedResultException ex) {
                    return getNext().executeLong_(frameValue, ex.getResult());
                }
                return root.ord(argumentValue_);
            }

            @Override
            public long executeLong_(VirtualFrame frameValue, Object argumentValue) {
                if (argumentValue instanceof EnumValue) {
                    EnumValue argumentValue_ = (EnumValue) argumentValue;
                    return root.ord(argumentValue_);
                }
                return getNext().executeLong_(frameValue, argumentValue);
            }

            static BaseNode_ create(OrdBuiltinNodeGen root) {
                return new Ord3Node_(root);
            }

        }
    }
}
