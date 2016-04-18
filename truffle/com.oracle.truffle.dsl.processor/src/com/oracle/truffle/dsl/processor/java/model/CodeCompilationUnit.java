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

import java.util.Collections;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public class CodeCompilationUnit extends CodeElement<TypeElement> {

    public CodeCompilationUnit() {
        super(Collections.<Modifier> emptySet());
    }

    @Override
    public TypeMirror asType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ElementKind getKind() {
        return ElementKind.OTHER;
    }

    @Override
    public Name getSimpleName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R, P> R accept(ElementVisitor<R, P> v, P p) {
        for (Element type : getEnclosedElements()) {
            if (type.getKind().isClass()) {
                type.accept(v, p);
            } else {
                throw new ClassCastException(type.getClass().getName());
            }
        }
        return null;
    }

}
