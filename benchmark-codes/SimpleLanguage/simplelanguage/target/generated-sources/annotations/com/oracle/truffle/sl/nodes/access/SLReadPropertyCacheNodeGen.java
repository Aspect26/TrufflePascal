// CheckStyle: start generated
package com.oracle.truffle.sl.nodes.access;

import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.ExplodeLoop.LoopExplosionKind;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.Location;
import com.oracle.truffle.api.object.Shape;
import com.oracle.truffle.sl.nodes.interop.SLForeignToSLTypeNode;
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLReadPropertyCacheNode.class)
public final class SLReadPropertyCacheNodeGen extends SLReadPropertyCacheNode {

    @CompilationFinal private int state_ = 1;
    @CompilationFinal private int exclude_;
    @CompilationFinal private ReadCachedData readCached_cache;
    @Child private Node readForeign_foreignReadNode_;
    @Child private SLForeignToSLTypeNode readForeign_toSLTypeNode_;

    private SLReadPropertyCacheNodeGen() {
    }

    @ExplodeLoop(kind = LoopExplosionKind.FULL_EXPLODE_UNTIL_RETURN)
    private boolean fallbackGuard_(Object arg0Value, Object arg1Value) {
        if (arg0Value instanceof DynamicObject) {
            ReadCachedData s1_ = readCached_cache;
            while (s1_ != null) {
                if ((SLPropertyCacheNode.namesEqual(s1_.cachedName_, arg1Value))) {
                    DynamicObject arg0Value_ = (DynamicObject) arg0Value;
                    if ((SLPropertyCacheNode.shapeCheck(s1_.shape_, arg0Value_)) && isValid_(s1_.assumption0_)) {
                        return false;
                    }
                }
                s1_ = s1_.next_;
            }
            {
                DynamicObject arg0Value_ = (DynamicObject) arg0Value;
                if ((SLPropertyCacheNode.isValidSLObject(arg0Value_))) {
                    return false;
                }
            }
        }
        if (arg0Value instanceof TruffleObject) {
            TruffleObject arg0Value_ = (TruffleObject) arg0Value;
            if ((SLPropertyCacheNode.isForeignObject(arg0Value_))) {
                return false;
            }
        }
        return true;
    }

    @ExplodeLoop(kind = LoopExplosionKind.FULL_EXPLODE_UNTIL_RETURN)
    @Override
    public Object executeRead(VirtualFrame frameValue, Object arg0Value, Object arg1Value) {
        int state = state_;
        if ((state & 0b11110) != 0 /* is-active readCached(DynamicObject, Object, Object, Shape, Location) || readUncached(DynamicObject, Object) || readForeign(VirtualFrame, TruffleObject, Object, Node, SLForeignToSLTypeNode) || updateShape(Object, Object) */) {
            if ((state & 0b110) != 0 /* is-active readCached(DynamicObject, Object, Object, Shape, Location) || readUncached(DynamicObject, Object) */ && arg0Value instanceof DynamicObject) {
                DynamicObject arg0Value_ = (DynamicObject) arg0Value;
                if ((state & 0b10) != 0 /* is-active readCached(DynamicObject, Object, Object, Shape, Location) */) {
                    ReadCachedData s1_ = readCached_cache;
                    while (s1_ != null) {
                        if (!isValid_(s1_.assumption0_)) {
                            CompilerDirectives.transferToInterpreterAndInvalidate();
                            removeReadCached_(s1_);
                            return executeAndSpecialize(frameValue, arg0Value_, arg1Value);
                        }
                        if ((SLPropertyCacheNode.namesEqual(s1_.cachedName_, arg1Value)) && (SLPropertyCacheNode.shapeCheck(s1_.shape_, arg0Value_))) {
                            return SLReadPropertyCacheNode.readCached(arg0Value_, arg1Value, s1_.cachedName_, s1_.shape_, s1_.location_);
                        }
                        s1_ = s1_.next_;
                    }
                }
                if ((state & 0b100) != 0 /* is-active readUncached(DynamicObject, Object) */) {
                    if ((SLPropertyCacheNode.isValidSLObject(arg0Value_))) {
                        return SLReadPropertyCacheNode.readUncached(arg0Value_, arg1Value);
                    }
                }
            }
            if ((state & 0b1000) != 0 /* is-active readForeign(VirtualFrame, TruffleObject, Object, Node, SLForeignToSLTypeNode) */ && arg0Value instanceof TruffleObject) {
                TruffleObject arg0Value_ = (TruffleObject) arg0Value;
                if ((SLPropertyCacheNode.isForeignObject(arg0Value_))) {
                    return SLReadPropertyCacheNode.readForeign(frameValue, arg0Value_, arg1Value, readForeign_foreignReadNode_, readForeign_toSLTypeNode_);
                }
            }
            if ((state & 0b10000) != 0 /* is-active updateShape(Object, Object) */) {
                if (fallbackGuard_(arg0Value, arg1Value)) {
                    return SLReadPropertyCacheNode.updateShape(arg0Value, arg1Value);
                }
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(frameValue, arg0Value, arg1Value);
    }

    private Object executeAndSpecialize(VirtualFrame frameValue, Object arg0Value, Object arg1Value) {
        Lock lock = getLock();
        boolean hasLock = true;
        lock.lock();
        try {
            int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
            int exclude = exclude_;
            if (arg0Value instanceof DynamicObject) {
                DynamicObject arg0Value_ = (DynamicObject) arg0Value;
                if ((exclude & 0b1) == 0 /* is-not-excluded readCached(DynamicObject, Object, Object, Shape, Location) */) {
                    int count1_ = 0;
                    ReadCachedData s1_ = readCached_cache;
                    if ((state & 0b10) != 0 /* is-active readCached(DynamicObject, Object, Object, Shape, Location) */) {
                        while (s1_ != null) {
                            if ((SLPropertyCacheNode.namesEqual(s1_.cachedName_, arg1Value)) && (SLPropertyCacheNode.shapeCheck(s1_.shape_, arg0Value_)) && isValid_(s1_.assumption0_)) {
                                break;
                            }
                            s1_ = s1_.next_;
                            count1_++;
                        }
                    }
                    if (s1_ == null) {
                        {
                            Object cachedName = (arg1Value);
                            if ((SLPropertyCacheNode.namesEqual(cachedName, arg1Value))) {
                                Shape shape = (SLPropertyCacheNode.lookupShape(arg0Value_));
                                if ((SLPropertyCacheNode.shapeCheck(shape, arg0Value_))) {
                                    Assumption assumption0 = (shape.getValidAssumption());
                                    if (isValid_(assumption0)) {
                                        if (count1_ < (SLPropertyCacheNode.CACHE_LIMIT)) {
                                            Location location = (SLReadPropertyCacheNode.lookupLocation(shape, arg1Value));
                                            s1_ = new ReadCachedData(readCached_cache, cachedName, shape, location, assumption0);
                                            this.readCached_cache = s1_;
                                            this.state_ = state | 0b10 /* add-active readCached(DynamicObject, Object, Object, Shape, Location) */;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (s1_ != null) {
                        lock.unlock();
                        hasLock = false;
                        return SLReadPropertyCacheNode.readCached(arg0Value_, arg1Value, s1_.cachedName_, s1_.shape_, s1_.location_);
                    }
                }
                if ((SLPropertyCacheNode.isValidSLObject(arg0Value_))) {
                    this.exclude_ = exclude | 0b1 /* add-excluded readCached(DynamicObject, Object, Object, Shape, Location) */;
                    this.readCached_cache = null;
                    state = state & 0xfffffffd /* remove-active readCached(DynamicObject, Object, Object, Shape, Location) */;
                    this.state_ = state | 0b100 /* add-active readUncached(DynamicObject, Object) */;
                    lock.unlock();
                    hasLock = false;
                    return SLReadPropertyCacheNode.readUncached(arg0Value_, arg1Value);
                }
            }
            if (arg0Value instanceof TruffleObject) {
                TruffleObject arg0Value_ = (TruffleObject) arg0Value;
                if ((SLPropertyCacheNode.isForeignObject(arg0Value_))) {
                    this.readForeign_foreignReadNode_ = super.insert((SLReadPropertyCacheNode.createForeignReadNode()));
                    this.readForeign_toSLTypeNode_ = super.insert((SLReadPropertyCacheNode.createToSLTypeNode()));
                    this.state_ = state | 0b1000 /* add-active readForeign(VirtualFrame, TruffleObject, Object, Node, SLForeignToSLTypeNode) */;
                    lock.unlock();
                    hasLock = false;
                    return SLReadPropertyCacheNode.readForeign(frameValue, arg0Value_, arg1Value, readForeign_foreignReadNode_, readForeign_toSLTypeNode_);
                }
            }
            this.state_ = state | 0b10000 /* add-active updateShape(Object, Object) */;
            lock.unlock();
            hasLock = false;
            return SLReadPropertyCacheNode.updateShape(arg0Value, arg1Value);
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
        } else if (((state & 0b11110) & ((state & 0b11110) - 1)) == 0 /* is-single-active  */) {
            ReadCachedData s1_ = this.readCached_cache;
            if ((s1_ == null || s1_.next_ == null)) {
                return NodeCost.MONOMORPHIC;
            }
        }
        return NodeCost.POLYMORPHIC;
    }

    void removeReadCached_(Object s1_) {
        Lock lock = getLock();
        lock.lock();
        try {
            ReadCachedData prev = null;
            ReadCachedData cur = this.readCached_cache;
            while (cur != null) {
                if (cur == s1_) {
                    if (prev == null) {
                        this.readCached_cache = cur.next_;
                    } else {
                        prev.next_ = cur.next_;
                    }
                    break;
                }
                prev = cur;
                cur = cur.next_;
            }
            if (this.readCached_cache == null) {
                this.state_ = this.state_ & 0xfffffffd /* remove-active readCached(DynamicObject, Object, Object, Shape, Location) */;
            }
        } finally {
            lock.unlock();
        }
    }

    private static boolean isValid_(Assumption assumption) {
        return assumption != null && assumption.isValid();
    }

    public static SLReadPropertyCacheNode create() {
        return new SLReadPropertyCacheNodeGen();
    }

    @GeneratedBy(SLReadPropertyCacheNode.class)
    private static final class ReadCachedData {

        @CompilationFinal ReadCachedData next_;
        final Object cachedName_;
        final Shape shape_;
        final Location location_;
        final Assumption assumption0_;

        ReadCachedData(ReadCachedData next_, Object cachedName_, Shape shape_, Location location_, Assumption assumption0_) {
            this.next_ = next_;
            this.cachedName_ = cachedName_;
            this.shape_ = shape_;
            this.location_ = location_;
            this.assumption0_ = assumption0_;
        }

    }
}
