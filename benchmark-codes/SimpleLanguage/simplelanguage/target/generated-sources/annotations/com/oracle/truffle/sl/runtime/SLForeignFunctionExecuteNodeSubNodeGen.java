// CheckStyle: start generated
package com.oracle.truffle.sl.runtime;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(SLForeignFunctionExecuteNodeSub.class)
public final class SLForeignFunctionExecuteNodeSubNodeGen extends SLForeignFunctionExecuteNodeSub {

    @CompilationFinal private boolean seenUnsupported0;

    private SLForeignFunctionExecuteNodeSubNodeGen() {
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeWithTarget(VirtualFrame frameValue, Object arg0Value, Object arg1Value) {
        if (arg0Value instanceof SLFunction && arg1Value instanceof Object[]) {
            SLFunction arg0Value_ = (SLFunction) arg0Value;
            Object[] arg1Value_ = (Object[]) arg1Value;
            return this.accessWithTarget(frameValue, arg0Value_, arg1Value_);
        }
        throw unsupported(arg0Value, arg1Value);
    }

    private UnsupportedSpecializationException unsupported(Object arg0Value, Object arg1Value) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {null, null}, arg0Value, arg1Value);
    }

    public static SLForeignFunctionExecuteNodeSub create() {
        return new SLForeignFunctionExecuteNodeSubNodeGen();
    }

}
