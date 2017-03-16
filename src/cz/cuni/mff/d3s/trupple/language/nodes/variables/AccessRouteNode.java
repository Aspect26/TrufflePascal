package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;

import java.util.List;

public abstract class AccessRouteNode extends StatementNode {

    public static class EnterRecord extends AccessRouteNode {

        private final FrameSlot recordFrameSlot;

        public EnterRecord(FrameSlot recordFrameSlot) {
            this.recordFrameSlot = recordFrameSlot;
        }

        @Override
        public void executeVoid(VirtualFrame frame) {

        }

        public FrameSlot getRecordFrameSlot() {
            return this.recordFrameSlot;
        }

    }

    public static class ArrayIndex extends AccessRouteNode {

        @Children private final ExpressionNode[] indexExpressions;
        private Object[] indexes;

        public ArrayIndex(List<ExpressionNode> indexExpressions) {
            this.indexExpressions = indexExpressions.toArray(new ExpressionNode[indexExpressions.size()]);
        }

        @Override
        public void executeVoid(VirtualFrame frame) {
            indexes = new Object[this.indexExpressions.length];

            for (int i = 0; i < this.indexExpressions.length; ++i) {
                indexes[i] = this.indexExpressions[i].executeGeneric(frame);
            }
        }

        public Object[] getIndexes() {
            return this.indexes;
        }

    }

}
