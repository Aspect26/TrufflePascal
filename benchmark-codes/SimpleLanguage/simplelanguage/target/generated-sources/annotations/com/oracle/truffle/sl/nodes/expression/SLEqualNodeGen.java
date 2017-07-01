// CheckStyle: start generated
package com.oracle.truffle.sl.nodes.expression;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.nodes.SLTypes;
import com.oracle.truffle.sl.nodes.SLTypesGen;
import com.oracle.truffle.sl.runtime.SLFunction;
import com.oracle.truffle.sl.runtime.SLNull;
import java.math.BigInteger;
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLEqualNode.class)
public final class SLEqualNodeGen extends SLEqualNode {

    @Child private SLExpressionNode leftNode_;
    @Child private SLExpressionNode rightNode_;
    @CompilationFinal private int state_ = 1;

    private SLEqualNodeGen(SLExpressionNode leftNode, SLExpressionNode rightNode) {
        this.leftNode_ = leftNode;
        this.rightNode_ = rightNode;
    }

    @Override
    public boolean executeBoolean(VirtualFrame frameValue) {
        int state = state_;
        if ((state & 0b11111101) == 0 /* only-active equal(long, long) */) {
            return executeBoolean_long_long0(frameValue, state);
        } else if ((state & 0b11110111) == 0 /* only-active equal(boolean, boolean) */) {
            return executeBoolean_boolean_boolean1(frameValue, state);
        } else {
            return executeBoolean_generic2(frameValue, state);
        }
    }

    private boolean executeBoolean_long_long0(VirtualFrame frameValue, int state) {
        long leftNodeValue_;
        try {
            leftNodeValue_ = leftNode_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            Object rightNodeValue = rightNode_.executeGeneric(frameValue);
            return executeAndSpecialize(ex.getResult(), rightNodeValue);
        }
        long rightNodeValue_;
        try {
            rightNodeValue_ = rightNode_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(leftNodeValue_, ex.getResult());
        }
        assert (state & 0b10) != 0 /* is-active equal(long, long) */;
        return equal(leftNodeValue_, rightNodeValue_);
    }

    private boolean executeBoolean_boolean_boolean1(VirtualFrame frameValue, int state) {
        boolean leftNodeValue_;
        try {
            leftNodeValue_ = leftNode_.executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            Object rightNodeValue = rightNode_.executeGeneric(frameValue);
            return executeAndSpecialize(ex.getResult(), rightNodeValue);
        }
        boolean rightNodeValue_;
        try {
            rightNodeValue_ = rightNode_.executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(leftNodeValue_, ex.getResult());
        }
        assert (state & 0b1000) != 0 /* is-active equal(boolean, boolean) */;
        return equal(leftNodeValue_, rightNodeValue_);
    }

    private boolean executeBoolean_generic2(VirtualFrame frameValue, int state) {
        Object leftNodeValue_ = leftNode_.executeGeneric(frameValue);
        Object rightNodeValue_ = rightNode_.executeGeneric(frameValue);
        if ((state & 0b10) != 0 /* is-active equal(long, long) */ && leftNodeValue_ instanceof Long) {
            long leftNodeValue__ = (long) leftNodeValue_;
            if (rightNodeValue_ instanceof Long) {
                long rightNodeValue__ = (long) rightNodeValue_;
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b100) != 0 /* is-active equal(BigInteger, BigInteger) */ && SLTypesGen.isImplicitBigInteger((state & 0b1100000000) >>> 8 /* extract-implicit-active 0:BigInteger */, leftNodeValue_)) {
            BigInteger leftNodeValue__ = SLTypesGen.asImplicitBigInteger((state & 0b1100000000) >>> 8 /* extract-implicit-active 0:BigInteger */, leftNodeValue_);
            if (SLTypesGen.isImplicitBigInteger((state & 0b110000000000) >>> 10 /* extract-implicit-active 1:BigInteger */, rightNodeValue_)) {
                BigInteger rightNodeValue__ = SLTypesGen.asImplicitBigInteger((state & 0b110000000000) >>> 10 /* extract-implicit-active 1:BigInteger */, rightNodeValue_);
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b1000) != 0 /* is-active equal(boolean, boolean) */ && leftNodeValue_ instanceof Boolean) {
            boolean leftNodeValue__ = (boolean) leftNodeValue_;
            if (rightNodeValue_ instanceof Boolean) {
                boolean rightNodeValue__ = (boolean) rightNodeValue_;
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b10000) != 0 /* is-active equal(String, String) */ && leftNodeValue_ instanceof String) {
            String leftNodeValue__ = (String) leftNodeValue_;
            if (rightNodeValue_ instanceof String) {
                String rightNodeValue__ = (String) rightNodeValue_;
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b100000) != 0 /* is-active equal(SLFunction, SLFunction) */ && leftNodeValue_ instanceof SLFunction) {
            SLFunction leftNodeValue__ = (SLFunction) leftNodeValue_;
            if (rightNodeValue_ instanceof SLFunction) {
                SLFunction rightNodeValue__ = (SLFunction) rightNodeValue_;
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b1000000) != 0 /* is-active equal(SLNull, SLNull) */ && SLTypes.isSLNull(leftNodeValue_)) {
            SLNull leftNodeValue__ = SLTypes.asSLNull(leftNodeValue_);
            if (SLTypes.isSLNull(rightNodeValue_)) {
                SLNull rightNodeValue__ = SLTypes.asSLNull(rightNodeValue_);
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b10000000) != 0 /* is-active equal(Object, Object) */) {
            if ((leftNodeValue_.getClass() != rightNodeValue_.getClass())) {
                return equal(leftNodeValue_, rightNodeValue_);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(leftNodeValue_, rightNodeValue_);
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

    private boolean executeAndSpecialize(Object leftNodeValue, Object rightNodeValue) {
        Lock lock = getLock();
        boolean hasLock = true;
        lock.lock();
        try {
            int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
            if (leftNodeValue instanceof Long) {
                long leftNodeValue_ = (long) leftNodeValue;
                if (rightNodeValue instanceof Long) {
                    long rightNodeValue_ = (long) rightNodeValue;
                    this.state_ = state | 0b10 /* add-active equal(long, long) */;
                    lock.unlock();
                    hasLock = false;
                    return equal(leftNodeValue_, rightNodeValue_);
                }
            }
            {
                int bigIntegerCast0;
                if ((bigIntegerCast0 = SLTypesGen.specializeImplicitBigInteger(leftNodeValue)) != 0) {
                    BigInteger leftNodeValue_ = SLTypesGen.asImplicitBigInteger(bigIntegerCast0, leftNodeValue);
                    int bigIntegerCast1;
                    if ((bigIntegerCast1 = SLTypesGen.specializeImplicitBigInteger(rightNodeValue)) != 0) {
                        BigInteger rightNodeValue_ = SLTypesGen.asImplicitBigInteger(bigIntegerCast1, rightNodeValue);
                        state = (state | (bigIntegerCast0 << 8) /* set-implicit-active 0:BigInteger */);
                        state = (state | (bigIntegerCast1 << 10) /* set-implicit-active 1:BigInteger */);
                        this.state_ = state | 0b100 /* add-active equal(BigInteger, BigInteger) */;
                        lock.unlock();
                        hasLock = false;
                        return equal(leftNodeValue_, rightNodeValue_);
                    }
                }
            }
            if (leftNodeValue instanceof Boolean) {
                boolean leftNodeValue_ = (boolean) leftNodeValue;
                if (rightNodeValue instanceof Boolean) {
                    boolean rightNodeValue_ = (boolean) rightNodeValue;
                    this.state_ = state | 0b1000 /* add-active equal(boolean, boolean) */;
                    lock.unlock();
                    hasLock = false;
                    return equal(leftNodeValue_, rightNodeValue_);
                }
            }
            if (leftNodeValue instanceof String) {
                String leftNodeValue_ = (String) leftNodeValue;
                if (rightNodeValue instanceof String) {
                    String rightNodeValue_ = (String) rightNodeValue;
                    this.state_ = state | 0b10000 /* add-active equal(String, String) */;
                    lock.unlock();
                    hasLock = false;
                    return equal(leftNodeValue_, rightNodeValue_);
                }
            }
            if (leftNodeValue instanceof SLFunction) {
                SLFunction leftNodeValue_ = (SLFunction) leftNodeValue;
                if (rightNodeValue instanceof SLFunction) {
                    SLFunction rightNodeValue_ = (SLFunction) rightNodeValue;
                    this.state_ = state | 0b100000 /* add-active equal(SLFunction, SLFunction) */;
                    lock.unlock();
                    hasLock = false;
                    return equal(leftNodeValue_, rightNodeValue_);
                }
            }
            if (SLTypes.isSLNull(leftNodeValue)) {
                SLNull leftNodeValue_ = SLTypes.asSLNull(leftNodeValue);
                if (SLTypes.isSLNull(rightNodeValue)) {
                    SLNull rightNodeValue_ = SLTypes.asSLNull(rightNodeValue);
                    this.state_ = state | 0b1000000 /* add-active equal(SLNull, SLNull) */;
                    lock.unlock();
                    hasLock = false;
                    return equal(leftNodeValue_, rightNodeValue_);
                }
            }
            if ((leftNodeValue.getClass() != rightNodeValue.getClass())) {
                this.state_ = state | 0b10000000 /* add-active equal(Object, Object) */;
                lock.unlock();
                hasLock = false;
                return equal(leftNodeValue, rightNodeValue);
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new UnsupportedSpecializationException(this, new Node[] {this.leftNode_, this.rightNode_}, leftNodeValue, rightNodeValue);
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
        } else if (((state & 0b11111110) & ((state & 0b11111110) - 1)) == 0 /* is-single-active  */) {
            return NodeCost.MONOMORPHIC;
        }
        return NodeCost.POLYMORPHIC;
    }

    public static SLEqualNode create(SLExpressionNode leftNode, SLExpressionNode rightNode) {
        return new SLEqualNodeGen(leftNode, rightNode);
    }

}
