// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.file;

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
import cz.cuni.mff.d3s.trupple.language.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import java.util.Arrays;
import java.util.List;

@GeneratedBy(EofBuiltinNode.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class EofBuiltinNodeFactory implements NodeFactory<EofBuiltinNode> {

    private static EofBuiltinNodeFactory instance;

    private EofBuiltinNodeFactory() {
    }

    @Override
    public Class<EofBuiltinNode> getNodeClass() {
        return EofBuiltinNode.class;
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
    public EofBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof PascalContext) && (arguments[1] == null || arguments[1] instanceof ExpressionNode)) {
            return create((PascalContext) arguments[0], (ExpressionNode) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<EofBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new EofBuiltinNodeFactory();
        }
        return instance;
    }

    public static EofBuiltinNode create(PascalContext context, ExpressionNode file) {
        return new EofBuiltinNodeGen(context, file);
    }

    @GeneratedBy(EofBuiltinNode.class)
    public static final class EofBuiltinNodeGen extends EofBuiltinNode {

        @Child private ExpressionNode file_;
        @CompilationFinal private boolean seenUnsupported0;

        private EofBuiltinNodeGen(PascalContext context, ExpressionNode file) {
            super(context);
            this.file_ = file;
        }

        @Override
        public NodeCost getCost() {
            return NodeCost.MONOMORPHIC;
        }

        @Override
        public Object executeGeneric(VirtualFrame frameValue) {
            return executeBoolean(frameValue);
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            executeBoolean(frameValue);
            return;
        }

        @Override
        public boolean executeBoolean(VirtualFrame frameValue) {
            FileValue fileValue_;
            try {
                fileValue_ = PascalTypesGen.expectFileValue(file_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                throw unsupported(ex.getResult());
            }
            return this.isEof(fileValue_);
        }

        private UnsupportedSpecializationException unsupported(Object fileValue) {
            if (!seenUnsupported0) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                seenUnsupported0 = true;
            }
            return new UnsupportedSpecializationException(this, new Node[] {file_}, fileValue);
        }

    }
}
