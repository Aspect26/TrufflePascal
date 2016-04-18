/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.oracle.truffle.sl.builtins;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.sl.SLLanguage;
import java.io.IOException;

/**
 * Builtin function to evaluate source code in any supported language.
 * <p>
 * The call target is cached against the mime type and the source code, so that if they are the same
 * each time then a direct call will be made to a cached AST, allowing it to be compiled and
 * possibly inlined.
 */
@NodeInfo(shortName = "eval")
public abstract class SLEvalBuiltin extends SLBuiltinNode {

    public SLEvalBuiltin() {
        super(SourceSection.createUnavailable(SLLanguage.builtinKind, "eval"));
    }

    @SuppressWarnings("unused")
    @Specialization(guards = {"stringsEqual(mimeType, cachedMimeType)", "stringsEqual(code, cachedCode)"})
    public Object evalCached(VirtualFrame frame, String mimeType, String code, @Cached("mimeType") String cachedMimeType, @Cached("code") String cachedCode,
                    @Cached("create(parse(mimeType, code))") DirectCallNode callNode) {
        return callNode.call(frame, new Object[]{});
    }

    @TruffleBoundary
    @Specialization(contains = "evalCached")
    public Object evalUncached(String mimeType, String code) {
        return parse(mimeType, code).call();
    }

    protected CallTarget parse(String mimeType, String code) {
        final Source source = Source.fromText(code, "(eval)").withMimeType(mimeType);

        try {
            return getContext().parse(source);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    protected boolean stringsEqual(String a, String b) {
        return a.equals(b);
    }

}
