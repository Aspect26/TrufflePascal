// CheckStyle: start generated
package com.oracle.truffle.sl.runtime;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(SLForeignIsExecutableNodeSub.class)
public final class SLForeignIsExecutableNodeSubNodeGen extends SLForeignIsExecutableNodeSub {

    private SLForeignIsExecutableNodeSubNodeGen() {
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeWithTarget(VirtualFrame frameValue, Object arg0Value) {
        return this.accessWithTarget(arg0Value);
    }

    public static SLForeignIsExecutableNodeSub create() {
        return new SLForeignIsExecutableNodeSubNodeGen();
    }

}
