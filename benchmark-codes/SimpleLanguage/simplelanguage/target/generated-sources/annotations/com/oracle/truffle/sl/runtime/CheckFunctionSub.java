package com.oracle.truffle.sl.runtime;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
/**
 * Generated for {@link com.oracle.truffle.sl.runtime.SLFunction}
 */

/**
 * This message resolution is generated by {@link com.oracle.truffle.sl.runtime.SLFunctionMessageResolution.CheckFunction}
 * It is used by the foreign access factory {@link com.oracle.truffle.sl.runtime.SLFunctionMessageResolutionForeign}
 */
public abstract class CheckFunctionSub extends com.oracle.truffle.sl.runtime.SLFunctionMessageResolution.CheckFunction {
    public abstract Object executeWithTarget(VirtualFrame frame, Object o);
    @Specialization
    protected Object testWithTarget(com.oracle.truffle.api.interop.TruffleObject receiver) {
        return test(receiver);
    }
    private static final class LanguageCheckRootNode extends RootNode {
        protected LanguageCheckRootNode(Class<? extends TruffleLanguage<?>> language) {
            super(language, null, null);
        }

        @Child private CheckFunctionSub node = com.oracle.truffle.sl.runtime.CheckFunctionSubNodeGen.create();
        @Override
        public Object execute(VirtualFrame frame) {
            try {
              Object receiver = ForeignAccess.getReceiver(frame);
              return node.executeWithTarget(frame, receiver);
            } catch (UnsupportedSpecializationException e) {
                if (e.getNode() instanceof CheckFunctionSub) {
                  throw UnsupportedTypeException.raise(e, e.getSuppliedValues());
                } else {
                  throw e;
                }
            }
        }

    }
    public static RootNode createRoot(Class<? extends TruffleLanguage<?>> language) {
        return new LanguageCheckRootNode(language);
    }
}
