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

@GeneratedBy(SLSubNode.class)
public final class SLSubNodeGen extends SLSubNode {

    @Child private SLExpressionNode leftNode_;
    @Child private SLExpressionNode rightNode_;
    @CompilationFinal private int state_ = 1;
    @CompilationFinal private int exclude_;

    private SLSubNodeGen(SLExpressionNode leftNode, SLExpressionNode rightNode) {
        this.leftNode_ = leftNode;
        this.rightNode_ = rightNode;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state = state_;
        if ((state & 0b101) == 0 /* only-active sub(long, long) */) {
            return executeGeneric_long_long0(frameValue, state);
        } else {
            return executeGeneric_generic1(frameValue, state);
        }
    }

    private Object executeGeneric_long_long0(VirtualFrame frameValue, int state) {
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
        assert (state & 0b10) != 0 /* is-active sub(long, long) */;
        try {
            return sub(leftNodeValue_, rightNodeValue_);
        } catch (ArithmeticException ex) {
            // implicit transferToInterpreterAndInvalidate()
            Lock lock = getLock();
            lock.lock();
            try {
                this.exclude_ = this.exclude_ | 0b1 /* add-excluded sub(long, long) */;
                this.state_ = this.state_ & 0xfffffffd /* remove-active sub(long, long) */;
            } finally {
                lock.unlock();
            }
            return executeAndSpecialize(leftNodeValue_, rightNodeValue_);
        }
    }

    private Object executeGeneric_generic1(VirtualFrame frameValue, int state) {
        Object leftNodeValue_ = leftNode_.executeGeneric(frameValue);
        Object rightNodeValue_ = rightNode_.executeGeneric(frameValue);
        if ((state & 0b10) != 0 /* is-active sub(long, long) */ && leftNodeValue_ instanceof Long) {
            long leftNodeValue__ = (long) leftNodeValue_;
            if (rightNodeValue_ instanceof Long) {
                long rightNodeValue__ = (long) rightNodeValue_;
                try {
                    return sub(leftNodeValue__, rightNodeValue__);
                } catch (ArithmeticException ex) {
                    // implicit transferToInterpreterAndInvalidate()
                    Lock lock = getLock();
                    lock.lock();
                    try {
                        this.exclude_ = this.exclude_ | 0b1 /* add-excluded sub(long, long) */;
                        this.state_ = this.state_ & 0xfffffffd /* remove-active sub(long, long) */;
                    } finally {
                        lock.unlock();
                    }
                    return executeAndSpecialize(leftNodeValue__, rightNodeValue__);
                }
            }
        }
        if ((state & 0b100) != 0 /* is-active sub(BigInteger, BigInteger) */ && SLTypesGen.isImplicitBigInteger((state & 0b11000) >>> 3 /* extract-implicit-active 0:BigInteger */, leftNodeValue_)) {
            BigInteger leftNodeValue__ = SLTypesGen.asImplicitBigInteger((state & 0b11000) >>> 3 /* extract-implicit-active 0:BigInteger */, leftNodeValue_);
            if (SLTypesGen.isImplicitBigInteger((state & 0b1100000) >>> 5 /* extract-implicit-active 1:BigInteger */, rightNodeValue_)) {
                BigInteger rightNodeValue__ = SLTypesGen.asImplicitBigInteger((state & 0b1100000) >>> 5 /* extract-implicit-active 1:BigInteger */, rightNodeValue_);
                return sub(leftNodeValue__, rightNodeValue__);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(leftNodeValue_, rightNodeValue_);
    }

    @Override
    public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
        int state = state_;
        long leftNodeValue_;
        try {
            leftNodeValue_ = leftNode_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            Object rightNodeValue = rightNode_.executeGeneric(frameValue);
            return SLTypesGen.expectLong(executeAndSpecialize(ex.getResult(), rightNodeValue));
        }
        long rightNodeValue_;
        try {
            rightNodeValue_ = rightNode_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            return SLTypesGen.expectLong(executeAndSpecialize(leftNodeValue_, ex.getResult()));
        }
        if ((state & 0b10) != 0 /* is-active sub(long, long) */) {
            try {
                return sub(leftNodeValue_, rightNodeValue_);
            } catch (ArithmeticException ex) {
                // implicit transferToInterpreterAndInvalidate()
                Lock lock = getLock();
                lock.lock();
                try {
                    this.exclude_ = this.exclude_ | 0b1 /* add-excluded sub(long, long) */;
                    this.state_ = this.state_ & 0xfffffffd /* remove-active sub(long, long) */;
                } finally {
                    lock.unlock();
                }
                return SLTypesGen.expectLong(executeAndSpecialize(leftNodeValue_, rightNodeValue_));
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SLTypesGen.expectLong(executeAndSpecialize(leftNodeValue_, rightNodeValue_));
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        int state = state_;
        try {
            if ((state & 0b101) == 0 /* only-active sub(long, long) */) {
                executeLong(frameValue);
                return;
            }
            executeGeneric(frameValue);
            return;
        } catch (UnexpectedResultException ex) {
            return;
        }
    }

    private Object executeAndSpecialize(Object leftNodeValue, Object rightNodeValue) {
        Lock lock = getLock();
        boolean hasLock = true;
        lock.lock();
        try {
            int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
            int exclude = exclude_;
            if ((exclude & 0b1) == 0 /* is-not-excluded sub(long, long) */ && leftNodeValue instanceof Long) {
                long leftNodeValue_ = (long) leftNodeValue;
                if (rightNodeValue instanceof Long) {
                    long rightNodeValue_ = (long) rightNodeValue;
                    this.state_ = state | 0b10 /* add-active sub(long, long) */;
                    try {
                        lock.unlock();
                        hasLock = false;
                        return sub(leftNodeValue_, rightNodeValue_);
                    } catch (ArithmeticException ex) {
                        // implicit transferToInterpreterAndInvalidate()
                        lock.lock();
                        try {
                            this.exclude_ = this.exclude_ | 0b1 /* add-excluded sub(long, long) */;
                            this.state_ = this.state_ & 0xfffffffd /* remove-active sub(long, long) */;
                        } finally {
                            lock.unlock();
                        }
                        return executeAndSpecialize(leftNodeValue_, rightNodeValue_);
                    }
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
                        this.state_ = state | 0b100 /* add-active sub(BigInteger, BigInteger) */;
                        lock.unlock();
                        hasLock = false;
                        return sub(leftNodeValue_, rightNodeValue_);
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

    public static SLSubNode create(SLExpressionNode leftNode, SLExpressionNode rightNode) {
        return new SLSubNodeGen(leftNode, rightNode);
    }

}
