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

@GeneratedBy(SLForeignInvokeNodeSub.class)
public final class SLForeignInvokeNodeSubNodeGen extends SLForeignInvokeNodeSub {

    @CompilationFinal private boolean seenUnsupported0;

    private SLForeignInvokeNodeSubNodeGen() {
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeWithTarget(VirtualFrame frameValue, Object arg0Value, Object arg1Value, Object arg2Value) {
        if (arg0Value instanceof DynamicObject && arg1Value instanceof String && arg2Value instanceof Object[]) {
            DynamicObject arg0Value_ = (DynamicObject) arg0Value;
            String arg1Value_ = (String) arg1Value;
            Object[] arg2Value_ = (Object[]) arg2Value;
            return this.accessWithTarget(frameValue, arg0Value_, arg1Value_, arg2Value_);
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

    public static SLForeignInvokeNodeSub create() {
        return new SLForeignInvokeNodeSubNodeGen();
    }

}
