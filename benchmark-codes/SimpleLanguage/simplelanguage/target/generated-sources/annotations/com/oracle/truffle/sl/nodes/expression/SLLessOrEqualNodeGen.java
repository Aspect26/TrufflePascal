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
import com.oracle.truffle.sl.nodes.SLTypesGen;
import java.math.BigInteger;
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLLessOrEqualNode.class)
public final class SLLessOrEqualNodeGen extends SLLessOrEqualNode {

    @Child private SLExpressionNode leftNode_;
    @Child private SLExpressionNode rightNode_;
    @CompilationFinal private int state_ = 1;

    private SLLessOrEqualNodeGen(SLExpressionNode leftNode, SLExpressionNode rightNode) {
        this.leftNode_ = leftNode;
        this.rightNode_ = rightNode;
    }

    @Override
    public boolean executeBoolean(VirtualFrame frameValue) {
        int state = state_;
        if ((state & 0b101) == 0 /* only-active lessOrEqual(long, long) */) {
            return executeBoolean_long_long0(frameValue, state);
        } else {
            return executeBoolean_generic1(frameValue, state);
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
        assert (state & 0b10) != 0 /* is-active lessOrEqual(long, long) */;
        return lessOrEqual(leftNodeValue_, rightNodeValue_);
    }

    private boolean executeBoolean_generic1(VirtualFrame frameValue, int state) {
        Object leftNodeValue_ = leftNode_.executeGeneric(frameValue);
        Object rightNodeValue_ = rightNode_.executeGeneric(frameValue);
        if ((state & 0b10) != 0 /* is-active lessOrEqual(long, long) */ && leftNodeValue_ instanceof Long) {
            long leftNodeValue__ = (long) leftNodeValue_;
            if (rightNodeValue_ instanceof Long) {
                long rightNodeValue__ = (long) rightNodeValue_;
                return lessOrEqual(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b100) != 0 /* is-active lessOrEqual(BigInteger, BigInteger) */ && SLTypesGen.isImplicitBigInteger((state & 0b11000) >>> 3 /* extract-implicit-active 0:BigInteger */, leftNodeValue_)) {
            BigInteger leftNodeValue__ = SLTypesGen.asImplicitBigInteger((state & 0b11000) >>> 3 /* extract-implicit-active 0:BigInteger */, leftNodeValue_);
            if (SLTypesGen.isImplicitBigInteger((state & 0b1100000) >>> 5 /* extract-implicit-active 1:BigInteger */, rightNodeValue_)) {
                BigInteger rightNodeValue__ = SLTypesGen.asImplicitBigInteger((state & 0b1100000) >>> 5 /* extract-implicit-active 1:BigInteger */, rightNodeValue_);
                return lessOrEqual(leftNodeValue__, rightNodeValue__);
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
                    this.state_ = state | 0b10 /* add-active lessOrEqual(long, long) */;
                    lock.unlock();
                    hasLock = false;
                    return lessOrEqual(leftNodeValue_, rightNodeValue_);
                }
            }
            {
                int bigIntegerCast0;
                if ((bigIntegerCast0 = SLTypesGen.specializeImplicitBigInteger(leftNodeValue)) != 0) {
                    BigInteger leftNodeValue_ = SLTypesGen.asImplicitBigInteger(bigIntegerCast0, leftNodeValue);
                    int bigIntegerCast1;
                    if ((bigIntegerCast1 = SLTypesGen.specializeImplicitBigInteger(rightNodeValue)) != 0) {
                        BigInteger rightNodeValue_ = SLTypesGen.asImplicitBigInteger(bigIntegerCast1, rightNodeValue);
                        state = (state | (bigIntegerCast0 << 3) /* set-implicit-active 0:BigInteger */);
                        state = (state | (bigIntegerCast1 << 5) /* set-implicit-active 1:BigInteger */);
                        this.state_ = state | 0b100 /* add-active lessOrEqual(BigInteger, BigInteger) */;
                        lock.unlock();
                        hasLock = false;
                        return lessOrEqual(leftNodeValue_, rightNodeValue_);
                    }
                }
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
        } else if (((state & 0b110) & ((state & 0b110) - 1)) == 0 /* is-single-active  */) {
            return NodeCost.MONOMORPHIC;
        }
        return NodeCost.POLYMORPHIC;
    }

    public static SLLessOrEqualNode create(SLExpressionNode leftNode, SLExpressionNode rightNode) {
        return new SLLessOrEqualNodeGen(leftNode, rightNode);
    }

}
