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

@GeneratedBy(SLForeignPropertiesNodeSub.class)
public final class SLForeignPropertiesNodeSubNodeGen extends SLForeignPropertiesNodeSub {

    @CompilationFinal private boolean seenUnsupported0;

    private SLForeignPropertiesNodeSubNodeGen() {
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeWithTarget(VirtualFrame frameValue, Object arg0Value) {
        if (arg0Value instanceof DynamicObject) {
            DynamicObject arg0Value_ = (DynamicObject) arg0Value;
            return this.accessWithTarget(arg0Value_);
        }
        throw unsupported(arg0Value);
    }

    private UnsupportedSpecializationException unsupported(Object arg0Value) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {null}, arg0Value);
    }

    public static SLForeignPropertiesNodeSub create() {
        return new SLForeignPropertiesNodeSubNodeGen();
    }

}
