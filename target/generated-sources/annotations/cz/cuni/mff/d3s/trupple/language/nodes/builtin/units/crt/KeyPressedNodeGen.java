// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.crt;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(KeyPressedNode.class)
public final class KeyPressedNodeGen extends KeyPressedNode {

    private KeyPressedNodeGen() {
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
        return this.keyPressed(frameValue);
    }

    public static KeyPressedNode create() {
        return new KeyPressedNodeGen();
    }

}
