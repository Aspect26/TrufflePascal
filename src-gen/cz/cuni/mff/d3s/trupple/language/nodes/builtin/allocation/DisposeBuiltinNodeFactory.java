// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.allocation;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.PascalTypesGen;
import cz.cuni.mff.d3s.trupple.language.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import java.util.Arrays;
import java.util.List;

@GeneratedBy(DisposeBuiltinNode.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class DisposeBuiltinNodeFactory implements NodeFactory<DisposeBuiltinNode> {

    private static DisposeBuiltinNodeFactory instance;

    private DisposeBuiltinNodeFactory() {
    }

    @Override
    public Class<DisposeBuiltinNode> getNodeClass() {
        return DisposeBuiltinNode.class;
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
    public DisposeBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof PascalContext) && (arguments[1] == null || arguments[1] instanceof ExpressionNode)) {
            return create((PascalContext) arguments[0], (ExpressionNode) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<DisposeBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new DisposeBuiltinNodeFactory();
        }
        return instance;
    }

    public static DisposeBuiltinNode create(PascalContext context, ExpressionNode child0) {
        return new DisposeBuiltinNodeGen(context, child0);
    }

    @GeneratedBy(DisposeBuiltinNode.class)
    public static final class DisposeBuiltinNodeGen extends DisposeBuiltinNode {

        @Child private ExpressionNode child0_;
        @CompilationFinal private boolean seenUnsupported0;

        private DisposeBuiltinNodeGen(PascalContext context, ExpressionNode child0) {
            super(context);
            this.child0_ = child0;
        }

        @Override
        public NodeCost getCost() {
            return NodeCost.MONOMORPHIC;
        }

        @Override
        public Object executeGeneric(VirtualFrame frameValue) {
            PointerValue child0Value_;
            try {
                child0Value_ = PascalTypesGen.expectPointerValue(child0_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                throw unsupported(ex.getResult());
            }
            return this.dispose(child0Value_);
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            executeGeneric(frameValue);
            return;
        }

        private UnsupportedSpecializationException unsupported(Object child0Value) {
            if (!seenUnsupported0) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                seenUnsupported0 = true;
            }
            return new UnsupportedSpecializationException(this, new Node[] {child0_}, child0Value);
        }

    }
}
