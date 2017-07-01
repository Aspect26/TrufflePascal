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
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLWritePropertyCacheNode.class)
public final class SLWritePropertyCacheNodeGen extends SLWritePropertyCacheNode {

    @CompilationFinal private int state_ = 1;
    @CompilationFinal private int exclude_;
    @CompilationFinal private WriteExistingPropertyCachedData writeExistingPropertyCached_cache;
    @CompilationFinal private WriteNewPropertyCachedData writeNewPropertyCached_cache;
    @Child private Node writeForeign_foreignWriteNode_;

    private SLWritePropertyCacheNodeGen() {
    }

    @ExplodeLoop(kind = LoopExplosionKind.FULL_EXPLODE_UNTIL_RETURN)
    private boolean fallbackGuard_(Object arg0Value, Object arg1Value, Object arg2Value) {
        if (arg0Value instanceof DynamicObject) {
            WriteExistingPropertyCachedData s1_ = writeExistingPropertyCached_cache;
            while (s1_ != null) {
                {
                    DynamicObject arg0Value_ = (DynamicObject) arg0Value;
                    // assert (s1_.cachedName_.equals(arg1Value));
                    if ((SLPropertyCacheNode.shapeCheck(s1_.shape_, arg0Value_)) && (s1_.location_ != null) && (SLWritePropertyCacheNode.canSet(s1_.location_, arg2Value)) && isValid_(s1_.assumption0_)) {
                        return false;
                    }
                }
                s1_ = s1_.next_;
            }
            WriteNewPropertyCachedData s2_ = writeNewPropertyCached_cache;
            while (s2_ != null) {
                if ((SLPropertyCacheNode.namesEqual(s2_.cachedName_, arg1Value))) {
                    DynamicObject arg0Value_ = (DynamicObject) arg0Value;
                    if ((SLPropertyCacheNode.shapeCheck(s2_.oldShape_, arg0Value_)) && (s2_.oldLocation_ == null) && (SLWritePropertyCacheNode.canStore(s2_.newLocation_, arg2Value)) && isValid_(s2_.assumption0_) && isValid_(s2_.assumption1_)) {
                        return false;
                    }
                }
                s2_ = s2_.next_;
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
    public void executeWrite(VirtualFrame frameValue, Object arg0Value, Object arg1Value, Object arg2Value) {
        int state = state_;
        if ((state & 0b111110) != 0 /* is-active writeExistingPropertyCached(DynamicObject, Object, Object, Object, Shape, Location) || writeNewPropertyCached(DynamicObject, Object, Object, Object, Shape, Location, Shape, Location) || writeUncached(DynamicObject, Object, Object) || writeForeign(VirtualFrame, TruffleObject, Object, Object, Node) || updateShape(Object, Object, Object) */) {
            if ((state & 0b1110) != 0 /* is-active writeExistingPropertyCached(DynamicObject, Object, Object, Object, Shape, Location) || writeNewPropertyCached(DynamicObject, Object, Object, Object, Shape, Location, Shape, Location) || writeUncached(DynamicObject, Object, Object) */ && arg0Value instanceof DynamicObject) {
                DynamicObject arg0Value_ = (DynamicObject) arg0Value;
                if ((state & 0b10) != 0 /* is-active writeExistingPropertyCached(DynamicObject, Object, Object, Object, Shape, Location) */) {
                    WriteExistingPropertyCachedData s1_ = writeExistingPropertyCached_cache;
                    while (s1_ != null) {
                        if (!isValid_(s1_.assumption0_)) {
                            CompilerDirectives.transferToInterpreterAndInvalidate();
                            removeWriteExistingPropertyCached_(s1_);
                            executeAndSpecialize(frameValue, arg0Value_, arg1Value, arg2Value);
                            return;
                        }
                        if ((s1_.cachedName_.equals(arg1Value)) && (SLPropertyCacheNode.shapeCheck(s1_.shape_, arg0Value_))) {
                            assert (s1_.location_ != null);
                            if ((SLWritePropertyCacheNode.canSet(s1_.location_, arg2Value))) {
                                SLWritePropertyCacheNode.writeExistingPropertyCached(arg0Value_, arg1Value, arg2Value, s1_.cachedName_, s1_.shape_, s1_.location_);
                                return;
                            }
                        }
                        s1_ = s1_.next_;
                    }
                }
                if ((state & 0b100) != 0 /* is-active writeNewPropertyCached(DynamicObject, Object, Object, Object, Shape, Location, Shape, Location) */) {
                    WriteNewPropertyCachedData s2_ = writeNewPropertyCached_cache;
                    while (s2_ != null) {
                        if (!isValid_(s2_.assumption0_) || !isValid_(s2_.assumption1_)) {
                            CompilerDirectives.transferToInterpreterAndInvalidate();
                            removeWriteNewPropertyCached_(s2_);
                            executeAndSpecialize(frameValue, arg0Value_, arg1Value, arg2Value);
                            return;
                        }
                        if ((SLPropertyCacheNode.namesEqual(s2_.cachedName_, arg1Value)) && (SLPropertyCacheNode.shapeCheck(s2_.oldShape_, arg0Value_))) {
                            assert (s2_.oldLocation_ == null);
                            if ((SLWritePropertyCacheNode.canStore(s2_.newLocation_, arg2Value))) {
                                SLWritePropertyCacheNode.writeNewPropertyCached(arg0Value_, arg1Value, arg2Value, s2_.cachedName_, s2_.oldShape_, s2_.oldLocation_, s2_.newShape_, s2_.newLocation_);
                                return;
                            }
                        }
                        s2_ = s2_.next_;
                    }
                }
                if ((state & 0b1000) != 0 /* is-active writeUncached(DynamicObject, Object, Object) */) {
                    if ((SLPropertyCacheNode.isValidSLObject(arg0Value_))) {
                        SLWritePropertyCacheNode.writeUncached(arg0Value_, arg1Value, arg2Value);
                        return;
                    }
                }
            }
            if ((state & 0b10000) != 0 /* is-active writeForeign(VirtualFrame, TruffleObject, Object, Object, Node) */ && arg0Value instanceof TruffleObject) {
                TruffleObject arg0Value_ = (TruffleObject) arg0Value;
                if ((SLPropertyCacheNode.isForeignObject(arg0Value_))) {
                    SLWritePropertyCacheNode.writeForeign(frameValue, arg0Value_, arg1Value, arg2Value, writeForeign_foreignWriteNode_);
                    return;
                }
            }
            if ((state & 0b100000) != 0 /* is-active updateShape(Object, Object, Object) */) {
                if (fallbackGuard_(arg0Value, arg1Value, arg2Value)) {
                    SLWritePropertyCacheNode.updateShape(arg0Value, arg1Value, arg2Value);
                    return;
                }
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        executeAndSpecialize(frameValue, arg0Value, arg1Value, arg2Value);
        return;
    }

    private void executeAndSpecialize(VirtualFrame frameValue, Object arg0Value, Object arg1Value, Object arg2Value) {
        Lock lock = getLock();
        boolean hasLock = true;
        lock.lock();
        try {
            int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
            int exclude = exclude_;
            if (arg0Value instanceof DynamicObject) {
                DynamicObject arg0Value_ = (DynamicObject) arg0Value;
                if ((exclude & 0b1) == 0 /* is-not-excluded writeExistingPropertyCached(DynamicObject, Object, Object, Object, Shape, Location) */) {
                    int count1_ = 0;
                    WriteExistingPropertyCachedData s1_ = writeExistingPropertyCached_cache;
                    if ((state & 0b10) != 0 /* is-active writeExistingPropertyCached(DynamicObject, Object, Object, Object, Shape, Location) */) {
                        while (s1_ != null) {
                            if ((s1_.cachedName_.equals(arg1Value)) && (SLPropertyCacheNode.shapeCheck(s1_.shape_, arg0Value_))) {
                                assert (s1_.location_ != null);
                                if ((SLWritePropertyCacheNode.canSet(s1_.location_, arg2Value)) && isValid_(s1_.assumption0_)) {
                                    break;
                                }
                            }
                            s1_ = s1_.next_;
                            count1_++;
                        }
                    }
                    if (s1_ == null) {
                        {
                            Object cachedName = (arg1Value);
                            Shape shape = (SLPropertyCacheNode.lookupShape(arg0Value_));
                            // assert (cachedName.equals(arg1Value));
                            if ((SLPropertyCacheNode.shapeCheck(shape, arg0Value_))) {
                                Location location = (SLWritePropertyCacheNode.lookupLocation(shape, arg1Value, arg2Value));
                                if ((location != null) && (SLWritePropertyCacheNode.canSet(location, arg2Value))) {
                                    Assumption assumption0 = (shape.getValidAssumption());
                                    if (isValid_(assumption0)) {
                                        if (count1_ < (SLPropertyCacheNode.CACHE_LIMIT)) {
                                            s1_ = new WriteExistingPropertyCachedData(writeExistingPropertyCached_cache, cachedName, shape, location, assumption0);
                                            this.writeExistingPropertyCached_cache = s1_;
                                            this.state_ = state | 0b10 /* add-active writeExistingPropertyCached(DynamicObject, Object, Object, Object, Shape, Location) */;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (s1_ != null) {
                        lock.unlock();
                        hasLock = false;
                        SLWritePropertyCacheNode.writeExistingPropertyCached(arg0Value_, arg1Value, arg2Value, s1_.cachedName_, s1_.shape_, s1_.location_);
                        return;
                    }
                }
                if ((exclude & 0b10) == 0 /* is-not-excluded writeNewPropertyCached(DynamicObject, Object, Object, Object, Shape, Location, Shape, Location) */) {
                    int count2_ = 0;
                    WriteNewPropertyCachedData s2_ = writeNewPropertyCached_cache;
                    if ((state & 0b100) != 0 /* is-active writeNewPropertyCached(DynamicObject, Object, Object, Object, Shape, Location, Shape, Location) */) {
                        while (s2_ != null) {
                            if ((SLPropertyCacheNode.namesEqual(s2_.cachedName_, arg1Value)) && (SLPropertyCacheNode.shapeCheck(s2_.oldShape_, arg0Value_))) {
                                assert (s2_.oldLocation_ == null);
                                if ((SLWritePropertyCacheNode.canStore(s2_.newLocation_, arg2Value)) && isValid_(s2_.assumption0_) && isValid_(s2_.assumption1_)) {
                                    break;
                                }
                            }
                            s2_ = s2_.next_;
                            count2_++;
                        }
                    }
                    if (s2_ == null) {
                        {
                            Object cachedName = (arg1Value);
                            if ((SLPropertyCacheNode.namesEqual(cachedName, arg1Value))) {
                                Shape oldShape = (SLPropertyCacheNode.lookupShape(arg0Value_));
                                if ((SLPropertyCacheNode.shapeCheck(oldShape, arg0Value_))) {
                                    Location oldLocation = (SLWritePropertyCacheNode.lookupLocation(oldShape, arg1Value, arg2Value));
                                    if ((oldLocation == null)) {
                                        Shape newShape = (SLWritePropertyCacheNode.defineProperty(oldShape, arg1Value, arg2Value));
                                        Location newLocation = (SLWritePropertyCacheNode.lookupLocation(newShape, arg1Value));
                                        if ((SLWritePropertyCacheNode.canStore(newLocation, arg2Value))) {
                                            Assumption assumption0 = (oldShape.getValidAssumption());
                                            if (isValid_(assumption0)) {
                                                Assumption assumption1 = (newShape.getValidAssumption());
                                                if (isValid_(assumption1)) {
                                                    if (count2_ < (SLPropertyCacheNode.CACHE_LIMIT)) {
                                                        s2_ = new WriteNewPropertyCachedData(writeNewPropertyCached_cache, cachedName, oldShape, oldLocation, newShape, newLocation, assumption0, assumption1);
                                                        this.writeNewPropertyCached_cache = s2_;
                                                        this.state_ = state | 0b100 /* add-active writeNewPropertyCached(DynamicObject, Object, Object, Object, Shape, Location, Shape, Location) */;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (s2_ != null) {
                        lock.unlock();
                        hasLock = false;
                        SLWritePropertyCacheNode.writeNewPropertyCached(arg0Value_, arg1Value, arg2Value, s2_.cachedName_, s2_.oldShape_, s2_.oldLocation_, s2_.newShape_, s2_.newLocation_);
                        return;
                    }
                }
                if ((SLPropertyCacheNode.isValidSLObject(arg0Value_))) {
                    this.exclude_ = exclude | 0b11 /* add-excluded writeExistingPropertyCached(DynamicObject, Object, Object, Object, Shape, Location), writeNewPropertyCached(DynamicObject, Object, Object, Object, Shape, Location, Shape, Location) */;
                    this.writeExistingPropertyCached_cache = null;
                    this.writeNewPropertyCached_cache = null;
                    state = state & 0xfffffff9 /* remove-active writeExistingPropertyCached(DynamicObject, Object, Object, Object, Shape, Location), writeNewPropertyCached(DynamicObject, Object, Object, Object, Shape, Location, Shape, Location) */;
                    this.state_ = state | 0b1000 /* add-active writeUncached(DynamicObject, Object, Object) */;
                    lock.unlock();
                    hasLock = false;
                    SLWritePropertyCacheNode.writeUncached(arg0Value_, arg1Value, arg2Value);
                    return;
                }
            }
            if (arg0Value instanceof TruffleObject) {
                TruffleObject arg0Value_ = (TruffleObject) arg0Value;
                if ((SLPropertyCacheNode.isForeignObject(arg0Value_))) {
                    this.writeForeign_foreignWriteNode_ = super.insert((SLWritePropertyCacheNode.createForeignWriteNode()));
                    this.state_ = state | 0b10000 /* add-active writeForeign(VirtualFrame, TruffleObject, Object, Object, Node) */;
                    lock.unlock();
                    hasLock = false;
                    SLWritePropertyCacheNode.writeForeign(frameValue, arg0Value_, arg1Value, arg2Value, writeForeign_foreignWriteNode_);
                    return;
                }
            }
            this.state_ = state | 0b100000 /* add-active updateShape(Object, Object, Object) */;
            lock.unlock();
            hasLock = false;
            SLWritePropertyCacheNode.updateShape(arg0Value, arg1Value, arg2Value);
            return;
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
        } else if (((state & 0b111110) & ((state & 0b111110) - 1)) == 0 /* is-single-active  */) {
            WriteExistingPropertyCachedData s1_ = this.writeExistingPropertyCached_cache;
            WriteNewPropertyCachedData s2_ = this.writeNewPropertyCached_cache;
            if ((s1_ == null || s1_.next_ == null) && (s2_ == null || s2_.next_ == null)) {
                return NodeCost.MONOMORPHIC;
            }
        }
        return NodeCost.POLYMORPHIC;
    }

    void removeWriteExistingPropertyCached_(Object s1_) {
        Lock lock = getLock();
        lock.lock();
        try {
            WriteExistingPropertyCachedData prev = null;
            WriteExistingPropertyCachedData cur = this.writeExistingPropertyCached_cache;
            while (cur != null) {
                if (cur == s1_) {
                    if (prev == null) {
                        this.writeExistingPropertyCached_cache = cur.next_;
                    } else {
                        prev.next_ = cur.next_;
                    }
                    break;
                }
                prev = cur;
                cur = cur.next_;
            }
            if (this.writeExistingPropertyCached_cache == null) {
                this.state_ = this.state_ & 0xfffffffd /* remove-active writeExistingPropertyCached(DynamicObject, Object, Object, Object, Shape, Location) */;
            }
        } finally {
            lock.unlock();
        }
    }

    void removeWriteNewPropertyCached_(Object s2_) {
        Lock lock = getLock();
        lock.lock();
        try {
            WriteNewPropertyCachedData prev = null;
            WriteNewPropertyCachedData cur = this.writeNewPropertyCached_cache;
            while (cur != null) {
                if (cur == s2_) {
                    if (prev == null) {
                        this.writeNewPropertyCached_cache = cur.next_;
                    } else {
                        prev.next_ = cur.next_;
                    }
                    break;
                }
                prev = cur;
                cur = cur.next_;
            }
            if (this.writeNewPropertyCached_cache == null) {
                this.state_ = this.state_ & 0xfffffffb /* remove-active writeNewPropertyCached(DynamicObject, Object, Object, Object, Shape, Location, Shape, Location) */;
            }
        } finally {
            lock.unlock();
        }
    }

    private static boolean isValid_(Assumption assumption) {
        return assumption != null && assumption.isValid();
    }

    public static SLWritePropertyCacheNode create() {
        return new SLWritePropertyCacheNodeGen();
    }

    @GeneratedBy(SLWritePropertyCacheNode.class)
    private static final class WriteExistingPropertyCachedData {

        @CompilationFinal WriteExistingPropertyCachedData next_;
        final Object cachedName_;
        final Shape shape_;
        final Location location_;
        final Assumption assumption0_;

        WriteExistingPropertyCachedData(WriteExistingPropertyCachedData next_, Object cachedName_, Shape shape_, Location location_, Assumption assumption0_) {
            this.next_ = next_;
            this.cachedName_ = cachedName_;
            this.shape_ = shape_;
            this.location_ = location_;
            this.assumption0_ = assumption0_;
        }

    }
    @GeneratedBy(SLWritePropertyCacheNode.class)
    private static final class WriteNewPropertyCachedData {

        @CompilationFinal WriteNewPropertyCachedData next_;
        final Object cachedName_;
        final Shape oldShape_;
        final Location oldLocation_;
        final Shape newShape_;
        final Location newLocation_;
        final Assumption assumption0_;
        final Assumption assumption1_;

        WriteNewPropertyCachedData(WriteNewPropertyCachedData next_, Object cachedName_, Shape oldShape_, Location oldLocation_, Shape newShape_, Location newLocation_, Assumption assumption0_, Assumption assumption1_) {
            this.next_ = next_;
            this.cachedName_ = cachedName_;
            this.oldShape_ = oldShape_;
            this.oldLocation_ = oldLocation_;
            this.newShape_ = newShape_;
            this.newLocation_ = newLocation_;
            this.assumption0_ = assumption0_;
            this.assumption1_ = assumption1_;
        }

    }
}
