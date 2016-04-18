/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.api.interop;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeUtil;

final class UnresolvedObjectAccessNode extends ObjectAccessNode {
    private static final int CACHE_SIZE = 8;
    private int cacheLength = 1;

    @Override
    public Object executeWith(VirtualFrame frame, TruffleObject receiver, Object[] arguments) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        ForeignObjectAccessHeadNode nthParent = (ForeignObjectAccessHeadNode) NodeUtil.getNthParent(this, cacheLength);
        ObjectAccessNode first = nthParent.getFirst();
        if (cacheLength < UnresolvedObjectAccessNode.CACHE_SIZE) {
            CachedObjectAccessNode createCachedAccess = createCachedAccess(receiver, nthParent.getAccessTree(), first);
            cacheLength++;
            return first.replace(createCachedAccess).executeWith(frame, receiver, arguments);
        } else {
            return first.replace(createGenericAccess(nthParent.getAccessTree())).executeWith(frame, receiver, arguments);
        }
    }

    private static CachedObjectAccessNode createCachedAccess(TruffleObject receiver, Message accessTree, ObjectAccessNode next) {
        ForeignAccess fa = receiver.getForeignAccess();
        final CallTarget ct = fa.access(accessTree);
        if (ct == null) {
            throw UnsupportedMessageException.raise(accessTree);
        }
        return new CachedObjectAccessNode(Truffle.getRuntime().createDirectCallNode(ct), next, fa);
    }

    private static GenericObjectAccessNode createGenericAccess(Message access) {
        return new GenericObjectAccessNode(access);
    }

}
