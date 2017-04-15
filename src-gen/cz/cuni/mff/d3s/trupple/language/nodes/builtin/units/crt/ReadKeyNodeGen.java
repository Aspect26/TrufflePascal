// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.crt;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(ReadKeyNode.class)
public final class ReadKeyNodeGen extends ReadKeyNode {

    private ReadKeyNodeGen() {
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        return executeChar(frameValue);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeChar(frameValue);
        return;
    }

    @Override
    public char executeChar(VirtualFrame frameValue) {
        return this.readKey(frameValue);
    }

    public static ReadKeyNode create() {
        return new ReadKeyNodeGen();
    }

}
