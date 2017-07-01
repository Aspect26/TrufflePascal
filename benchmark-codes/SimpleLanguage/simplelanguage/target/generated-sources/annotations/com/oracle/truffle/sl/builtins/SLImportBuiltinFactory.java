// CheckStyle: start generated
package com.oracle.truffle.sl.builtins;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.ExplodeLoop.LoopExplosionKind;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.runtime.SLContext;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLImportBuiltin.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class SLImportBuiltinFactory implements NodeFactory<SLImportBuiltin> {

    private static SLImportBuiltinFactory instance;

    private SLImportBuiltinFactory() {
    }

    @Override
    public Class<SLImportBuiltin> getNodeClass() {
        return SLImportBuiltin.class;
    }

    @Override
    public List getExecutionSignature() {
        return Arrays.asList(SLExpressionNode.class);
    }

    @Override
    public List getNodeSignatures() {
        return Arrays.asList(Arrays.asList(SLExpressionNode[].class, SLContext.class));
    }

    @Override
    public SLImportBuiltin createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof SLExpressionNode[]) && (arguments[1] == null || arguments[1] instanceof SLContext)) {
            return create((SLExpressionNode[]) arguments[0], (SLContext) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<SLImportBuiltin> getInstance() {
        if (instance == null) {
            instance = new SLImportBuiltinFactory();
        }
        return instance;
    }

    public static SLImportBuiltin create(SLExpressionNode[] arguments, SLContext context) {
        return new SLImportBuiltinNodeGen(arguments, context);
    }

    @GeneratedBy(SLImportBuiltin.class)
    public static final class SLImportBuiltinNodeGen extends SLImportBuiltin {

        private final SLContext context;
        @Child private SLExpressionNode arguments0_;
        @CompilationFinal private int state_ = 1;
        @CompilationFinal private ImportSymbolData importSymbol_cache;

        private SLImportBuiltinNodeGen(SLExpressionNode[] arguments, SLContext context) {
            this.context = context;
            this.arguments0_ = arguments != null && 0 < arguments.length ? arguments[0] : null;
        }

        @Override
        public SLContext getContext() {
            return this.context;
        }

        @ExplodeLoop(kind = LoopExplosionKind.FULL_EXPLODE_UNTIL_RETURN)
        @Override
        public Object executeGeneric(VirtualFrame frameValue) {
            int state = state_;
            Object arguments0Value_ = arguments0_.executeGeneric(frameValue);
            if ((state & 0b10) != 0 /* is-active importSymbol(String, String, Object) */ && arguments0Value_ instanceof String) {
                String arguments0Value__ = (String) arguments0Value_;
                ImportSymbolData s1_ = importSymbol_cache;
                while (s1_ != null) {
                    if ((SLImportBuiltin.stringsEqual(s1_.cachedName_, arguments0Value__))) {
                        return importSymbol(arguments0Value__, s1_.cachedName_, s1_.symbol_);
                    }
                    s1_ = s1_.next_;
                }
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            return executeAndSpecialize(arguments0Value_);
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            executeGeneric(frameValue);
            return;
        }

        private Object executeAndSpecialize(Object arguments0Value) {
            Lock lock = getLock();
            boolean hasLock = true;
            lock.lock();
            try {
                int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
                if (arguments0Value instanceof String) {
                    String arguments0Value_ = (String) arguments0Value;
                    int count1_ = 0;
                    ImportSymbolData s1_ = importSymbol_cache;
                    if ((state & 0b10) != 0 /* is-active importSymbol(String, String, Object) */) {
                        while (s1_ != null) {
                            if ((SLImportBuiltin.stringsEqual(s1_.cachedName_, arguments0Value_))) {
                                break;
                            }
                            s1_ = s1_.next_;
                            count1_++;
                        }
                    }
                    if (s1_ == null) {
                        {
                            String cachedName = (arguments0Value_);
                            if ((SLImportBuiltin.stringsEqual(cachedName, arguments0Value_)) && count1_ < (3)) {
                                Object symbol = (doImport(arguments0Value_));
                                s1_ = new ImportSymbolData(importSymbol_cache, cachedName, symbol);
                                this.importSymbol_cache = s1_;
                                this.state_ = state | 0b10 /* add-active importSymbol(String, String, Object) */;
                            }
                        }
                    }
                    if (s1_ != null) {
                        lock.unlock();
                        hasLock = false;
                        return importSymbol(arguments0Value_, s1_.cachedName_, s1_.symbol_);
                    }
                }
                CompilerDirectives.transferToInterpreterAndInvalidate();
                throw new UnsupportedSpecializationException(this, new Node[] {this.arguments0_}, arguments0Value);
            } finally {
                if (hasLock) {
                    lock.unlock();
                }
            }
        }

        @Override
        public NodeCost getCost() {
            int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
            if (state == 0b0) {
                return NodeCost.UNINITIALIZED;
            } else if (((state & 0b10) & ((state & 0b10) - 1)) == 0 /* is-single-active  */) {
                ImportSymbolData s1_ = this.importSymbol_cache;
                if ((s1_ == null || s1_.next_ == null)) {
                    return NodeCost.MONOMORPHIC;
                }
            }
            return NodeCost.POLYMORPHIC;
        }

        @GeneratedBy(SLImportBuiltin.class)
        private static final class ImportSymbolData {

            @CompilationFinal ImportSymbolData next_;
            final String cachedName_;
            final Object symbol_;

            ImportSymbolData(ImportSymbolData next_, String cachedName_, Object symbol_) {
                this.next_ = next_;
                this.cachedName_ = cachedName_;
                this.symbol_ = symbol_;
            }

        }
    }
}
