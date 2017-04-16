// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.tp;

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
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalString;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import java.util.Arrays;
import java.util.List;

@GeneratedBy(AssignBuiltinNode.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class AssignBuiltinNodeFactory implements NodeFactory<AssignBuiltinNode> {

    private static AssignBuiltinNodeFactory instance;

    private AssignBuiltinNodeFactory() {
    }

    @Override
    public Class<AssignBuiltinNode> getNodeClass() {
        return AssignBuiltinNode.class;
    }

    @Override
    public List getExecutionSignature() {
        return Arrays.asList(ExpressionNode.class, ExpressionNode.class);
    }

    @Override
    public List getNodeSignatures() {
        return Arrays.asList(Arrays.asList(PascalContext.class, ExpressionNode.class, ExpressionNode.class));
    }

    @Override
    public AssignBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 3 && (arguments[0] == null || arguments[0] instanceof PascalContext) && (arguments[1] == null || arguments[1] instanceof ExpressionNode) && (arguments[2] == null || arguments[2] instanceof ExpressionNode)) {
            return create((PascalContext) arguments[0], (ExpressionNode) arguments[1], (ExpressionNode) arguments[2]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<AssignBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new AssignBuiltinNodeFactory();
        }
        return instance;
    }

    public static AssignBuiltinNode create(PascalContext context, ExpressionNode file, ExpressionNode path) {
        return new AssignBuiltinNodeGen(context, file, path);
    }

    @GeneratedBy(AssignBuiltinNode.class)
    public static final class AssignBuiltinNodeGen extends AssignBuiltinNode {

        @Child private ExpressionNode file_;
        @Child private ExpressionNode path_;
        @CompilationFinal private boolean seenUnsupported0;

        private AssignBuiltinNodeGen(PascalContext context, ExpressionNode file, ExpressionNode path) {
            super(context);
            this.file_ = file;
            this.path_ = path;
        }

        @Override
        public NodeCost getCost() {
            return NodeCost.MONOMORPHIC;
        }

        @Override
        public Object executeGeneric(VirtualFrame frameValue) {
            FileValue fileValue_;
            try {
                fileValue_ = PascalTypesGen.expectFileValue(file_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                Object pathValue = path_.executeGeneric(frameValue);
                throw unsupported(ex.getResult(), pathValue);
            }
            PascalString pathValue_;
            try {
                pathValue_ = PascalTypesGen.expectPascalString(path_.executeGeneric(frameValue));
            } catch (UnexpectedResultException ex) {
                throw unsupported(fileValue_, ex.getResult());
            }
            return this.assignFile(fileValue_, pathValue_);
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            executeGeneric(frameValue);
            return;
        }

        private UnsupportedSpecializationException unsupported(Object fileValue, Object pathValue) {
            if (!seenUnsupported0) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                seenUnsupported0 = true;
            }
            return new UnsupportedSpecializationException(this, new Node[] {file_, path_}, fileValue, pathValue);
        }

    }
}
