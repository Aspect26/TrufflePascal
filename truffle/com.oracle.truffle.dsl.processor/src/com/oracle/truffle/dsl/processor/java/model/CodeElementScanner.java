/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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
package com.oracle.truffle.dsl.processor.java.model;

import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner7;

public abstract class CodeElementScanner<R, P> extends ElementScanner7<R, P> {

    @Override
    public final R visitExecutable(ExecutableElement e, P p) {
        if (!(e instanceof CodeExecutableElement)) {
            throw new ClassCastException(e.toString());
        }
        return visitExecutable(cast(e, CodeExecutableElement.class), p);
    }

    public R visitExecutable(CodeExecutableElement e, P p) {
        R ret = super.visitExecutable(e, p);
        if (e.getBodyTree() != null) {
            visitTree(e.getBodyTree(), p, e);
        }
        return ret;
    }

    @Override
    public R visitVariable(VariableElement e, P p) {
        if (e instanceof CodeVariableElement) {
            CodeTree init = ((CodeVariableElement) e).getInit();
            if (init != null) {
                visitTree(init, p, e);
            }
        }
        return super.visitVariable(e, p);
    }

    @Override
    public R visitPackage(PackageElement e, P p) {
        return super.visitPackage(e, p);
    }

    @Override
    public final R visitType(TypeElement e, P p) {
        return visitType(cast(e, CodeTypeElement.class), p);
    }

    public R visitType(CodeTypeElement e, P p) {
        return super.visitType(e, p);
    }

    @Override
    public R visitTypeParameter(TypeParameterElement e, P p) {
        return super.visitTypeParameter(e, p);
    }

    private static <E> E cast(Element element, Class<E> clazz) {
        return clazz.cast(element);
    }

    public void visitTree(CodeTree e, P p, Element parent) {
        List<CodeTree> elements = e.getEnclosedElements();
        if (elements != null) {
            for (CodeTree tree : e.getEnclosedElements()) {
                visitTree(tree, p, parent);
            }
        }
    }

    @SuppressWarnings("unused")
    public void visitImport(CodeImport e, P p) {
    }

}
