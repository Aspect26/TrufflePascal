// CheckStyle: start generated
package com.oracle.truffle.sl.runtime;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.object.DynamicObject;

@GeneratedBy(SLForeignWriteNodeSub.class)
public final class SLForeignWriteNodeSubNodeGen extends SLForeignWriteNodeSub {

    @CompilationFinal private boolean seenUnsupported0;

    private SLForeignWriteNodeSubNodeGen() {
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeWithTarget(VirtualFrame frameValue, Object arg0Value, Object arg1Value, Object arg2Value) {
        if (arg0Value instanceof DynamicObject) {
            DynamicObject arg0Value_ = (DynamicObject) arg0Value;
            return this.accessWithTarget(frameValue, arg0Value_, arg1Value, arg2Value);
        }
        throw unsupported(arg0Value, arg1Value, arg2Value);
    }

    private UnsupportedSpecializationException unsupported(Object arg0Value, Object arg1Value, Object arg2Value) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {null, null, null}, arg0Value, arg1Value, arg2Value);
    }

    public static SLForeignWriteNodeSub create() {
        return new SLForeignWriteNodeSubNodeGen();
    }

}
