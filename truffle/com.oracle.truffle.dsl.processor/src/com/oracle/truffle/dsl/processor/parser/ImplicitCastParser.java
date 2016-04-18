/*
 * Copyright (c) 2012, 2012, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.dsl.processor.parser;

import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.dsl.processor.ProcessorContext;
import com.oracle.truffle.dsl.processor.java.ElementUtils;
import com.oracle.truffle.dsl.processor.model.ImplicitCastData;
import com.oracle.truffle.dsl.processor.model.MethodSpec;
import com.oracle.truffle.dsl.processor.model.Parameter;
import com.oracle.truffle.dsl.processor.model.ParameterSpec;
import com.oracle.truffle.dsl.processor.model.TemplateMethod;
import com.oracle.truffle.dsl.processor.model.TypeSystemData;
import java.lang.annotation.Annotation;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

public class ImplicitCastParser extends TypeSystemMethodParser<ImplicitCastData> {

    public ImplicitCastParser(ProcessorContext context, TypeSystemData typeSystem) {
        super(context, typeSystem);
    }

    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return ImplicitCast.class;
    }

    @Override
    public MethodSpec createSpecification(ExecutableElement method, AnnotationMirror mirror) {
        MethodSpec spec = new MethodSpec(new ParameterSpec("target", getContext().getType(Object.class)));
        spec.addRequired(new ParameterSpec("source", getContext().getType(Object.class))).setSignature(true);
        return spec;
    }

    @Override
    public ImplicitCastData create(TemplateMethod method, boolean invalid) {
        if (invalid) {
            return new ImplicitCastData(method, null, null);
        }

        Parameter target = method.findParameter("targetValue");
        Parameter source = method.findParameter("sourceValue");

        TypeMirror targetType = target.getType();
        TypeMirror sourceType = source.getType();

        if (ElementUtils.typeEquals(targetType, sourceType)) {
            method.addError("Target type and source type of an @%s must not be the same type.", ImplicitCast.class.getSimpleName());
        }

        if (!method.getMethod().getModifiers().contains(Modifier.STATIC)) {
            method.addError("@%s annotated method %s must be static.", ImplicitCast.class.getSimpleName(), method.getMethodName());
        }

        return new ImplicitCastData(method, sourceType, targetType);
    }
}
