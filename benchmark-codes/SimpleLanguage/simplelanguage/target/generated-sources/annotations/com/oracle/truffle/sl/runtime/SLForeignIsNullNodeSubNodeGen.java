// CheckStyle: start generated
package com.oracle.truffle.sl.runtime;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(SLForeignIsNullNodeSub.class)
public final class SLForeignIsNullNodeSubNodeGen extends SLForeignIsNullNodeSub {

    private SLForeignIsNullNodeSubNodeGen() {
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeWithTarget(VirtualFrame frameValue, Object arg0Value) {
        return this.accessWithTarget(arg0Value);
    }

    public static SLForeignIsNullNodeSub create() {
        return new SLForeignIsNullNodeSubNodeGen();
    }

}
