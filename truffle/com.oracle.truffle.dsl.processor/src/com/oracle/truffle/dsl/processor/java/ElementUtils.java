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
package com.oracle.truffle.dsl.processor.java;

import com.oracle.truffle.dsl.processor.CompileErrorException;
import com.oracle.truffle.dsl.processor.ProcessorContext;
import com.oracle.truffle.dsl.processor.java.model.CodeTypeMirror;
import com.oracle.truffle.dsl.processor.java.model.CodeTypeMirror.DeclaredCodeTypeMirror;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.AbstractAnnotationValueVisitor7;
import javax.lang.model.util.ElementFilter;

/**
 * THIS IS NOT PUBLIC API.
 */
public class ElementUtils {

    public static TypeMirror getType(ProcessingEnvironment processingEnv, Class<?> element) {
        if (element.isArray()) {
            return processingEnv.getTypeUtils().getArrayType(getType(processingEnv, element.getComponentType()));
        }
        if (element.isPrimitive()) {
            if (element == void.class) {
                return processingEnv.getTypeUtils().getNoType(TypeKind.VOID);
            }
            TypeKind typeKind;
            if (element == boolean.class) {
                typeKind = TypeKind.BOOLEAN;
            } else if (element == byte.class) {
                typeKind = TypeKind.BYTE;
            } else if (element == short.class) {
                typeKind = TypeKind.SHORT;
            } else if (element == char.class) {
                typeKind = TypeKind.CHAR;
            } else if (element == int.class) {
                typeKind = TypeKind.INT;
            } else if (element == long.class) {
                typeKind = TypeKind.LONG;
            } else if (element == float.class) {
                typeKind = TypeKind.FLOAT;
            } else if (element == double.class) {
                typeKind = TypeKind.DOUBLE;
            } else {
                assert false;
                return null;
            }
            return processingEnv.getTypeUtils().getPrimitiveType(typeKind);
        } else {
            TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(element.getCanonicalName());
            if (typeElement == null) {
                return null;
            }
            return typeElement.asType();
        }
    }

    public static ExecutableElement findExecutableElement(DeclaredType type, String name) {
        List<? extends ExecutableElement> elements = ElementFilter.methodsIn(type.asElement().getEnclosedElements());
        for (ExecutableElement executableElement : elements) {
            if (executableElement.getSimpleName().toString().equals(name)) {
                return executableElement;
            }
        }
        return null;
    }

    public static boolean needsCastTo(TypeMirror sourceType, TypeMirror targetType) {
        if (typeEquals(sourceType, targetType)) {
            return false;
        } else if (isObject(targetType)) {
            return false;
        } else if (isVoid(targetType)) {
            return false;
        } else if (isAssignable(sourceType, targetType)) {
            return false;
        }
        return true;
    }

    public static String createReferenceName(ExecutableElement method) {
        StringBuilder b = new StringBuilder();

        b.append(method.getSimpleName().toString());
        b.append("(");

        String sep = "";
        for (VariableElement parameter : method.getParameters()) {
            b.append(sep);
            b.append(ElementUtils.getSimpleName(parameter.asType()));
            sep = ", ";
        }

        b.append(")");
        return b.toString();
    }

    public static TypeMirror boxType(ProcessorContext context, TypeMirror primitiveType) {
        if (primitiveType == null) {
            return null;
        }
        TypeMirror boxedType = primitiveType;
        if (boxedType.getKind().isPrimitive()) {
            boxedType = context.getEnvironment().getTypeUtils().boxedClass((PrimitiveType) boxedType).asType();
        }
        return boxedType;
    }

    public static DeclaredType getDeclaredType(TypeElement typeElem, TypeMirror... typeArgs) {
        return new DeclaredCodeTypeMirror(typeElem, Arrays.asList(typeArgs));
    }

    public static List<AnnotationMirror> collectAnnotations(ProcessorContext context, AnnotationMirror markerAnnotation, String elementName, Element element,
                    Class<? extends Annotation> annotationClass) {
        List<AnnotationMirror> result = new ArrayList<>();
        if (markerAnnotation != null) {
            result.addAll(ElementUtils.getAnnotationValueList(AnnotationMirror.class, markerAnnotation, elementName));
        }
        AnnotationMirror explicit = ElementUtils.findAnnotationMirror(context.getEnvironment(), element, annotationClass);
        if (explicit != null) {
            result.add(explicit);
        }
        return result;
    }

    public static TypeMirror getCommonSuperType(ProcessorContext context, Collection<TypeMirror> types) {
        if (types.isEmpty()) {
            return context.getType(Object.class);
        }
        Iterator<TypeMirror> typesIterator = types.iterator();
        TypeMirror prev = typesIterator.next();
        while (typesIterator.hasNext()) {
            prev = getCommonSuperType(context, prev, typesIterator.next());
        }
        return prev;
    }

    private static TypeMirror getCommonSuperType(ProcessorContext context, TypeMirror type1, TypeMirror type2) {
        if (typeEquals(type1, type2)) {
            return type1;
        }
        if (isVoid(type1)) {
            return type2;
        } else if (isVoid(type2)) {
            return type1;
        }
        if (isObject(type1)) {
            return type1;
        } else if (isObject(type2)) {
            return type2;
        }

        if (isPrimitive(type1) || isPrimitive(type2)) {
            return context.getType(Object.class);
        }

        if (isSubtype(type1, type2)) {
            return type2;
        } else if (isSubtype(type2, type1)) {
            return type1;
        }

        TypeElement element1 = fromTypeMirror(type1);
        TypeElement element2 = fromTypeMirror(type2);

        if (element1 == null || element2 == null) {
            return context.getType(Object.class);
        }

        List<TypeElement> element1Types = getSuperTypes(element1);
        List<TypeElement> element2Types = getSuperTypes(element2);

        for (TypeElement superType1 : element1Types) {
            for (TypeElement superType2 : element2Types) {
                if (typeEquals(superType1.asType(), superType2.asType())) {
                    return superType2.asType();
                }
            }
        }

        return context.getType(Object.class);
    }

    public static String getReadableSignature(ExecutableElement method) {
        StringBuilder builder = new StringBuilder();
        builder.append(method.getSimpleName().toString());
        builder.append("(");
        String sep = "";
        for (VariableElement var : method.getParameters()) {
            builder.append(sep);
            builder.append(getSimpleName(var.asType()));
            sep = ", ";
        }
        builder.append(")");
        return builder.toString();
    }

    public static boolean hasError(TypeMirror mirror) {
        switch (mirror.getKind()) {
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case SHORT:
            case LONG:
            case DECLARED:
            case VOID:
            case TYPEVAR:
                return false;
            case ARRAY:
                return hasError(((ArrayType) mirror).getComponentType());
            case ERROR:
                return true;
            default:
                throw new RuntimeException("Unknown type specified " + mirror.getKind() + " mirror: " + mirror);
        }
    }

    public static boolean isSubtypeBoxed(ProcessorContext context, TypeMirror from, TypeMirror to) {
        return isSubtype(boxType(context, from), boxType(context, to));
    }

    public static boolean isSubtype(TypeMirror type1, TypeMirror type2) {
        if (type1 instanceof CodeTypeMirror || type2 instanceof CodeTypeMirror) {
            throw new UnsupportedOperationException();
        }
        return ProcessorContext.getInstance().getEnvironment().getTypeUtils().isSubtype(type1, type2);
    }

    public static boolean isAssignable(TypeMirror from, TypeMirror to) {
        if (typeEquals(from, to)) {
            return true;
        } else if (isVoid(to)) {
            return true;
        } else if (isObject(to)) {
            return true;
        }
        ProcessorContext context = ProcessorContext.getInstance();
        if (!(from instanceof CodeTypeMirror) && !(to instanceof CodeTypeMirror)) {
            return context.getEnvironment().getTypeUtils().isAssignable(context.reloadType(from), context.reloadType(to));
        } else {
            return isAssignableImpl(from, to);
        }
    }

    private static boolean isAssignableImpl(TypeMirror from, TypeMirror to) {
        // JLS 5.1.1 identity conversion
        if (ElementUtils.typeEquals(from, to)) {
            return true;
        }

        if (isObject(to)) {
            return true;
        }

        // JLS 5.1.2 widening primitives
        if (ElementUtils.isPrimitive(from) && ElementUtils.isPrimitive(to)) {
            TypeKind fromKind = from.getKind();
            TypeKind toKind = to.getKind();
            switch (fromKind) {
                case BYTE:
                    switch (toKind) {
                        case SHORT:
                        case INT:
                        case LONG:
                        case FLOAT:
                        case DOUBLE:
                            return true;
                    }
                    break;
                case SHORT:
                    switch (toKind) {
                        case INT:
                        case LONG:
                        case FLOAT:
                        case DOUBLE:
                            return true;
                    }
                    break;
                case CHAR:
                    switch (toKind) {
                        case INT:
                        case LONG:
                        case FLOAT:
                        case DOUBLE:
                            return true;
                    }
                    break;
                case INT:
                    switch (toKind) {
                        case LONG:
                        case FLOAT:
                        case DOUBLE:
                            return true;
                    }
                    break;
                case LONG:
                    switch (toKind) {
                        case FLOAT:
                        case DOUBLE:
                            return true;
                    }
                    break;
                case FLOAT:
                    switch (toKind) {
                        case DOUBLE:
                            return true;
                    }
                    break;

            }
            return false;
        } else if (ElementUtils.isPrimitive(from) || ElementUtils.isPrimitive(to)) {
            return false;
        }

        if (from instanceof ArrayType && to instanceof ArrayType) {
            return isAssignable(((ArrayType) from).getComponentType(), ((ArrayType) to).getComponentType());
        }

        if (from instanceof ArrayType || to instanceof ArrayType) {
            return false;
        }

        TypeElement fromType = ElementUtils.fromTypeMirror(from);
        TypeElement toType = ElementUtils.fromTypeMirror(to);
        if (fromType == null || toType == null) {
            return false;
        }
        // JLS 5.1.6 narrowing reference conversion

        List<TypeElement> superTypes = ElementUtils.getSuperTypes(fromType);
        for (TypeElement superType : superTypes) {
            if (ElementUtils.typeEquals(superType.asType(), to)) {
                return true;
            }
        }

        // TODO more spec
        return false;
    }

    public static Set<Modifier> modifiers(Modifier... modifier) {
        return new LinkedHashSet<>(Arrays.asList(modifier));
    }

    public static String getTypeId(TypeMirror mirror) {
        switch (mirror.getKind()) {
            case BOOLEAN:
                return "Boolean";
            case BYTE:
                return "Byte";
            case CHAR:
                return "Char";
            case DOUBLE:
                return "Double";
            case FLOAT:
                return "Float";
            case SHORT:
                return "Short";
            case INT:
                return "Int";
            case LONG:
                return "Long";
            case DECLARED:
                return fixECJBinaryNameIssue(((DeclaredType) mirror).asElement().getSimpleName().toString());
            case ARRAY:
                return getTypeId(((ArrayType) mirror).getComponentType()) + "Array";
            case VOID:
                return "Void";
            case NULL:
                return "Null";
            case WILDCARD:
                StringBuilder b = new StringBuilder();
                WildcardType type = (WildcardType) mirror;
                if (type.getExtendsBound() != null) {
                    b.append("Extends").append(getTypeId(type.getExtendsBound()));
                } else if (type.getSuperBound() != null) {
                    b.append("Super").append(getTypeId(type.getExtendsBound()));
                }
                return b.toString();
            case TYPEVAR:
                return "Any";
            case ERROR:
                throw new CompileErrorException("Type error " + mirror);
            default:
                throw new RuntimeException("Unknown type specified " + mirror.getKind() + " mirror: " + mirror);
        }
    }

    public static String getSimpleName(TypeElement element) {
        return getSimpleName(element.asType());
    }

    public static String getSimpleName(TypeMirror mirror) {
        switch (mirror.getKind()) {
            case BOOLEAN:
                return "boolean";
            case BYTE:
                return "byte";
            case CHAR:
                return "char";
            case DOUBLE:
                return "double";
            case FLOAT:
                return "float";
            case SHORT:
                return "short";
            case INT:
                return "int";
            case LONG:
                return "long";
            case DECLARED:
                return getDeclaredName((DeclaredType) mirror, true);
            case ARRAY:
                return getSimpleName(((ArrayType) mirror).getComponentType()) + "[]";
            case VOID:
                return "void";
            case NULL:
                return "null";
            case WILDCARD:
                return getWildcardName((WildcardType) mirror);
            case TYPEVAR:
                return "?";
            case ERROR:
                throw new CompileErrorException("Type error " + mirror);
            default:
                throw new RuntimeException("Unknown type specified " + mirror.getKind() + " mirror: " + mirror);
        }
    }

    private static String getWildcardName(WildcardType type) {
        StringBuilder b = new StringBuilder();
        if (type.getExtendsBound() != null) {
            b.append("? extends ").append(getSimpleName(type.getExtendsBound()));
        } else if (type.getSuperBound() != null) {
            b.append("? super ").append(getSimpleName(type.getExtendsBound()));
        }
        return b.toString();
    }

    public static String getDeclaredName(DeclaredType element, boolean includeTypeVariables) {
        String simpleName = fixECJBinaryNameIssue(element.asElement().getSimpleName().toString());

        if (!includeTypeVariables || element.getTypeArguments().size() == 0) {
            return simpleName;
        }

        StringBuilder b = new StringBuilder(simpleName);
        b.append("<");
        if (element.getTypeArguments().size() > 0) {
            for (int i = 0; i < element.getTypeArguments().size(); i++) {
                b.append(getSimpleName(element.getTypeArguments().get(i)));
                if (i < element.getTypeArguments().size() - 1) {
                    b.append(", ");
                }
            }
        }
        b.append(">");
        return b.toString();
    }

    public static String fixECJBinaryNameIssue(String name) {
        if (name.contains("$")) {
            int lastIndex = name.lastIndexOf('$');
            return name.substring(lastIndex + 1, name.length());
        }
        return name;
    }

    public static String getQualifiedName(TypeElement element) {
        String qualifiedName = element.getQualifiedName().toString();
        if (qualifiedName.contains("$")) {
            /*
             * If a class gets loaded in its binary form by the ECJ compiler it fails to produce the
             * proper canonical class name. It leaves the $ in the qualified name of the class. So
             * one instance of a TypeElement may be loaded in binary and one in source form. The
             * current type comparison in #typeEquals compares by the qualified name so the
             * qualified name must match. This is basically a hack to fix the returned qualified
             * name of eclipse.
             */
            qualifiedName = qualifiedName.replace('$', '.');
        }
        return qualifiedName;
    }

    public static String getQualifiedName(TypeMirror mirror) {
        switch (mirror.getKind()) {
            case BOOLEAN:
                return "boolean";
            case BYTE:
                return "byte";
            case CHAR:
                return "char";
            case DOUBLE:
                return "double";
            case SHORT:
                return "short";
            case FLOAT:
                return "float";
            case INT:
                return "int";
            case LONG:
                return "long";
            case DECLARED:
                return getQualifiedName(fromTypeMirror(mirror));
            case ARRAY:
                return getQualifiedName(((ArrayType) mirror).getComponentType());
            case VOID:
                return "void";
            case NULL:
                return "null";
            case TYPEVAR:
                return getSimpleName(mirror);
            case ERROR:
                throw new CompileErrorException("Type error " + mirror);
            case EXECUTABLE:
                return ((ExecutableType) mirror).toString();
            case NONE:
                return "$none";
            default:
                throw new RuntimeException("Unknown type specified " + mirror + " mirror: " + mirror);
        }
    }

    public static boolean isVoid(TypeMirror mirror) {
        return mirror != null && mirror.getKind() == TypeKind.VOID;
    }

    public static boolean isPrimitive(TypeMirror mirror) {
        return mirror != null && mirror.getKind().isPrimitive();
    }

    public static List<String> getQualifiedSuperTypeNames(TypeElement element) {
        List<TypeElement> types = getSuperTypes(element);
        List<String> qualifiedNames = new ArrayList<>();
        for (TypeElement type : types) {
            qualifiedNames.add(getQualifiedName(type));
        }
        return qualifiedNames;
    }

    public static List<TypeElement> getDeclaredTypes(TypeElement element) {
        return ElementFilter.typesIn(element.getEnclosedElements());
    }

    public static boolean isEnclosedIn(Element enclosedIn, Element element) {
        if (element == null) {
            return false;
        } else if (typeEquals(enclosedIn.asType(), element.asType())) {
            return true;
        } else {
            return isEnclosedIn(enclosedIn, element.getEnclosingElement());
        }
    }

    public static TypeElement findRootEnclosingType(Element element) {
        List<Element> elements = getElementHierarchy(element);

        for (int i = elements.size() - 1; i >= 0; i--) {
            if (elements.get(i).getKind().isClass()) {
                return (TypeElement) elements.get(i);
            }
        }

        return null;
    }

    public static List<Element> getElementHierarchy(Element e) {
        List<Element> elements = new ArrayList<>();
        elements.add(e);

        Element enclosing = e.getEnclosingElement();
        while (enclosing != null && enclosing.getKind() != ElementKind.PACKAGE) {
            elements.add(enclosing);
            enclosing = enclosing.getEnclosingElement();
        }
        if (enclosing != null) {
            elements.add(enclosing);
        }
        return elements;
    }

    public static TypeElement findNearestEnclosingType(Element element) {
        List<Element> elements = getElementHierarchy(element);
        for (Element e : elements) {
            if (e.getKind().isClass() || e.getKind().isInterface()) {
                return (TypeElement) e;
            }
        }
        return null;
    }

    public static List<TypeElement> getDirectSuperTypes(TypeElement element) {
        List<TypeElement> types = new ArrayList<>();
        TypeElement superElement = getSuperType(element);
        if (superElement != null) {
            types.add(superElement);
            types.addAll(getDirectSuperTypes(superElement));
        }

        return types;
    }

    /**
     * Gets the element representing the {@linkplain TypeElement#getSuperclass() super class} of a
     * given type element.
     */
    public static TypeElement getSuperType(TypeElement element) {
        if (element.getSuperclass() != null) {
            return fromTypeMirror(element.getSuperclass());
        }
        return null;
    }

    public static List<TypeElement> getSuperTypes(TypeElement element) {
        List<TypeElement> types = new ArrayList<>();
        List<TypeElement> superTypes = null;
        List<TypeElement> superInterfaces = null;
        TypeElement superElement = getSuperType(element);
        if (superElement != null) {
            types.add(superElement);
            superTypes = getSuperTypes(superElement);
        }
        for (TypeMirror interfaceMirror : element.getInterfaces()) {
            TypeElement interfaceElement = fromTypeMirror(interfaceMirror);
            if (interfaceElement != null) {
                types.add(interfaceElement);
                if (superInterfaces == null) {
                    superInterfaces = getSuperTypes(interfaceElement);
                } else {
                    superInterfaces.addAll(getSuperTypes(interfaceElement));
                }
            }
        }

        if (superTypes != null) {
            types.addAll(superTypes);
        }

        if (superInterfaces != null) {
            types.addAll(superInterfaces);
        }

        return types;
    }

    public static String getPackageName(TypeElement element) {
        return findPackageElement(element).getQualifiedName().toString();
    }

    public static String getEnclosedQualifiedName(DeclaredType mirror) {
        Element e = ((TypeElement) mirror.asElement()).getEnclosingElement();
        if (e.getKind() == ElementKind.PACKAGE) {
            return ((PackageElement) e).getQualifiedName().toString();
        } else if (e.getKind().isInterface() || e.getKind().isClass()) {
            return getQualifiedName((TypeElement) e);
        } else {
            return null;
        }
    }

    public static String getPackageName(TypeMirror mirror) {
        switch (mirror.getKind()) {
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case SHORT:
            case INT:
            case LONG:
            case VOID:
            case NULL:
            case TYPEVAR:
                return null;
            case DECLARED:
                PackageElement pack = findPackageElement(fromTypeMirror(mirror));
                if (pack == null) {
                    throw new IllegalArgumentException("No package element found for declared type " + getSimpleName(mirror));
                }
                return pack.getQualifiedName().toString();
            case ARRAY:
                return getSimpleName(((ArrayType) mirror).getComponentType());
            case EXECUTABLE:
                return null;
            default:
                throw new RuntimeException("Unknown type specified " + mirror.getKind());
        }
    }

    public static String createConstantName(String simpleName) {
        // TODO use camel case to produce underscores.
        return simpleName.toString().toUpperCase();
    }

    public static TypeElement fromTypeMirror(TypeMirror mirror) {
        switch (mirror.getKind()) {
            case DECLARED:
                return (TypeElement) ((DeclaredType) mirror).asElement();
            case ARRAY:
                return fromTypeMirror(((ArrayType) mirror).getComponentType());
            default:
                return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getAnnotationValueList(Class<T> expectedListType, AnnotationMirror mirror, String name) {
        List<? extends AnnotationValue> values = getAnnotationValue(List.class, mirror, name);
        List<T> result = new ArrayList<>();

        if (values != null) {
            for (AnnotationValue value : values) {
                T annotationValue = resolveAnnotationValue(expectedListType, value);
                if (annotationValue != null) {
                    result.add(annotationValue);
                }
            }
        }
        return result;
    }

    public static <T> T getAnnotationValue(Class<T> expectedType, AnnotationMirror mirror, String name) {
        return resolveAnnotationValue(expectedType, getAnnotationValue(mirror, name));
    }

    @SuppressWarnings({"unchecked"})
    private static <T> T resolveAnnotationValue(Class<T> expectedType, AnnotationValue value) {
        if (value == null) {
            return null;
        }

        Object unboxedValue = value.accept(new AnnotationValueVisitorImpl(), null);
        if (unboxedValue != null) {
            if (expectedType == TypeMirror.class && unboxedValue instanceof String) {
                return null;
            }
            if (!expectedType.isAssignableFrom(unboxedValue.getClass())) {
                throw new ClassCastException(unboxedValue.getClass().getName() + " not assignable from " + expectedType.getName());
            }
        }
        return (T) unboxedValue;
    }

    public static AnnotationValue getAnnotationValue(AnnotationMirror mirror, String name) {
        ExecutableElement valueMethod = null;
        for (ExecutableElement method : ElementFilter.methodsIn(mirror.getAnnotationType().asElement().getEnclosedElements())) {
            if (method.getSimpleName().toString().equals(name)) {
                valueMethod = method;
                break;
            }
        }

        if (valueMethod == null) {
            return null;
        }

        AnnotationValue value = mirror.getElementValues().get(valueMethod);
        if (value == null) {
            value = valueMethod.getDefaultValue();
        }

        return value;
    }

    private static class AnnotationValueVisitorImpl extends AbstractAnnotationValueVisitor7<Object, Void> {

        @Override
        public Object visitBoolean(boolean b, Void p) {
            return Boolean.valueOf(b);
        }

        @Override
        public Object visitByte(byte b, Void p) {
            return Byte.valueOf(b);
        }

        @Override
        public Object visitChar(char c, Void p) {
            return c;
        }

        @Override
        public Object visitDouble(double d, Void p) {
            return d;
        }

        @Override
        public Object visitFloat(float f, Void p) {
            return f;
        }

        @Override
        public Object visitInt(int i, Void p) {
            return i;
        }

        @Override
        public Object visitLong(long i, Void p) {
            return i;
        }

        @Override
        public Object visitShort(short s, Void p) {
            return s;
        }

        @Override
        public Object visitString(String s, Void p) {
            return s;
        }

        @Override
        public Object visitType(TypeMirror t, Void p) {
            return t;
        }

        @Override
        public Object visitEnumConstant(VariableElement c, Void p) {
            return c;
        }

        @Override
        public Object visitAnnotation(AnnotationMirror a, Void p) {
            return a;
        }

        @Override
        public Object visitArray(List<? extends AnnotationValue> vals, Void p) {
            return vals;
        }

    }

    public static String printException(Throwable e) {
        StringWriter string = new StringWriter();
        PrintWriter writer = new PrintWriter(string);
        e.printStackTrace(writer);
        writer.flush();
        string.flush();
        return e.getMessage() + "\r\n" + string.toString();
    }

    public static AnnotationMirror findAnnotationMirror(ProcessingEnvironment processingEnv, Element element, Class<?> annotationClass) {
        return findAnnotationMirror(processingEnv, element.getAnnotationMirrors(), annotationClass);
    }

    public static AnnotationMirror findAnnotationMirror(ProcessingEnvironment processingEnv, List<? extends AnnotationMirror> mirrors, Class<?> annotationClass) {
        TypeElement expectedAnnotationType = processingEnv.getElementUtils().getTypeElement(annotationClass.getCanonicalName());
        return findAnnotationMirror(mirrors, expectedAnnotationType.asType());
    }

    public static AnnotationMirror findAnnotationMirror(List<? extends AnnotationMirror> mirrors, TypeElement expectedAnnotationType) {
        return findAnnotationMirror(mirrors, expectedAnnotationType.asType());
    }

    public static AnnotationMirror findAnnotationMirror(List<? extends AnnotationMirror> mirrors, TypeMirror expectedAnnotationType) {
        for (AnnotationMirror mirror : mirrors) {
            if (typeEquals(mirror.getAnnotationType(), expectedAnnotationType)) {
                return mirror;
            }
        }
        return null;
    }

    public static PackageElement findPackageElement(Element type) {
        List<Element> hierarchy = getElementHierarchy(type);
        for (Element element : hierarchy) {
            if (element.getKind() == ElementKind.PACKAGE) {
                return (PackageElement) element;
            }
        }
        return null;
    }

    public static String firstLetterUpperCase(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return Character.toUpperCase(name.charAt(0)) + name.substring(1, name.length());
    }

    public static String firstLetterLowerCase(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return Character.toLowerCase(name.charAt(0)) + name.substring(1, name.length());
    }

    private static ExecutableElement getDeclaredMethod(TypeElement element, String name, TypeMirror[] params) {
        List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());
        method: for (ExecutableElement method : methods) {
            if (!method.getSimpleName().toString().equals(name)) {
                continue;
            }
            if (method.getParameters().size() != params.length) {
                continue;
            }
            for (int i = 0; i < params.length; i++) {
                TypeMirror param1 = params[i];
                TypeMirror param2 = method.getParameters().get(i).asType();
                if (param1 != null && param1.getKind() != TypeKind.TYPEVAR && param2 != null && param2.getKind() != TypeKind.TYPEVAR) {
                    if (!getQualifiedName(param1).equals(getQualifiedName(param2))) {
                        continue method;
                    }
                }
            }
            return method;
        }
        return null;
    }

    public static boolean isDeclaredMethodInSuperType(TypeElement element, String name, TypeMirror[] params) {
        return !getDeclaredMethodsInSuperTypes(element, name, params).isEmpty();
    }

    /**
     * Gets the methods in the super type hierarchy (excluding interfaces) that are overridden by a
     * method in a subtype.
     *
     * @param declaringElement the subtype element declaring the method
     * @param name the name of the method
     * @param params the signature of the method
     */
    public static List<ExecutableElement> getDeclaredMethodsInSuperTypes(TypeElement declaringElement, String name, TypeMirror... params) {
        List<ExecutableElement> superMethods = new ArrayList<>();
        List<TypeElement> superElements = getSuperTypes(declaringElement);

        for (TypeElement superElement : superElements) {
            ExecutableElement superMethod = getDeclaredMethod(superElement, name, params);
            if (superMethod != null) {
                superMethods.add(superMethod);
            }
        }
        return superMethods;
    }

    public static boolean typeEquals(TypeMirror type1, TypeMirror type2) {
        if (type1 == type2) {
            return true;
        } else if (type1 == null || type2 == null) {
            return false;
        } else {
            if (type1.getKind() == type2.getKind()) {
                return getUniqueIdentifier(type1).equals(getUniqueIdentifier(type2));
            } else {
                return false;
            }
        }
    }

    public static boolean areTypesCompatible(TypeMirror type1, TypeMirror type2) {
        if (typeEquals(type1, type2)) {
            return true;
        } else if (kindIsIntegral(type1.getKind())) {
            return kindIsIntegral(type2.getKind());
        } else if (type1.getKind() == TypeKind.NULL) {
            if (type2.getKind() == TypeKind.NULL) {
                return false;
            }
            return true;
        } else if (type2.getKind() == TypeKind.NULL) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean kindIsIntegral(TypeKind kind) {
        return kind == TypeKind.BYTE || kind == TypeKind.SHORT || kind == TypeKind.INT || kind == TypeKind.LONG;
    }

    public static List<String> getUniqueIdentifiers(List<TypeMirror> typeMirror) {
        List<String> ids = new ArrayList<>();
        for (TypeMirror type : typeMirror) {
            ids.add(getUniqueIdentifier(type));
        }
        return ids;
    }

    public static String getUniqueIdentifier(TypeMirror typeMirror) {
        if (typeMirror.getKind() == TypeKind.ARRAY) {
            return getUniqueIdentifier(((ArrayType) typeMirror).getComponentType()) + "[]";
        } else {
            return getQualifiedName(typeMirror);
        }
    }

    public static int compareByTypeHierarchy(TypeMirror t1, TypeMirror t2) {
        if (typeEquals(t1, t2)) {
            return 0;
        }
        Set<String> t1SuperSet = new HashSet<>(getQualifiedSuperTypeNames(fromTypeMirror(t1)));
        if (t1SuperSet.contains(getQualifiedName(t2))) {
            return -1;
        }

        Set<String> t2SuperSet = new HashSet<>(getQualifiedSuperTypeNames(fromTypeMirror(t2)));
        if (t2SuperSet.contains(getQualifiedName(t1))) {
            return 1;
        }
        return 0;
    }

    public static int compareByTypeHierarchy(TypeMirror t1, Set<String> t1SuperSet, TypeMirror t2, Set<String> t2SuperSet) {
        if (typeEquals(t1, t2)) {
            return 0;
        }
        if (t1SuperSet.contains(getQualifiedName(t2))) {
            return -1;
        }

        if (t2SuperSet.contains(getQualifiedName(t1))) {
            return 1;
        }
        return 0;
    }

    public static boolean canThrowType(List<? extends TypeMirror> thrownTypes, TypeMirror exceptionType) {
        if (ElementUtils.containsType(thrownTypes, exceptionType)) {
            return true;
        }

        if (isRuntimeException(exceptionType)) {
            return true;
        }

        // search for any super types
        for (TypeElement typeElement : getSuperTypes(fromTypeMirror(exceptionType))) {
            if (ElementUtils.containsType(thrownTypes, typeElement.asType())) {
                return true;
            }
        }

        return false;
    }

    public static void setVisibility(Set<Modifier> modifiers, Modifier visibility) {
        Modifier current = getVisibility(modifiers);
        if (current != visibility) {
            if (current != null) {
                modifiers.remove(current);
            }
            if (visibility != null) {
                modifiers.add(visibility);
            }
        }
    }

    public static Modifier getVisibility(Set<Modifier> modifier) {
        for (Modifier mod : modifier) {
            if (mod == Modifier.PUBLIC || mod == Modifier.PRIVATE || mod == Modifier.PROTECTED) {
                return mod;
            }
        }
        return null;
    }

    private static boolean isRuntimeException(TypeMirror type) {
        Set<String> typeSuperSet = new HashSet<>(getQualifiedSuperTypeNames(fromTypeMirror(type)));
        return typeSuperSet.contains(RuntimeException.class.getCanonicalName()) || getQualifiedName(type).equals(RuntimeException.class.getCanonicalName());
    }

    private static boolean containsType(Collection<? extends TypeMirror> collection, TypeMirror type) {
        for (TypeMirror otherTypeMirror : collection) {
            if (typeEquals(otherTypeMirror, type)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTopLevelClass(TypeMirror importType) {
        TypeElement type = fromTypeMirror(importType);
        if (type != null && type.getEnclosingElement() != null) {
            return !type.getEnclosingElement().getKind().isClass();
        }
        return true;
    }

    public static boolean isObject(TypeMirror actualType) {
        return actualType.getKind() == TypeKind.DECLARED && getQualifiedName(actualType).equals("java.lang.Object");
    }

    public static TypeMirror fillInGenericWildcards(TypeMirror type) {
        if (type.getKind() != TypeKind.DECLARED) {
            return type;
        }
        DeclaredType declaredType = (DeclaredType) type;
        TypeElement element = (TypeElement) declaredType.asElement();
        if (element == null) {
            return type;
        }
        int typeParameters = element.getTypeParameters().size();
        if (typeParameters > 0 && declaredType.getTypeArguments().size() != typeParameters) {
            return ProcessorContext.getInstance().getEnvironment().getTypeUtils().erasure(type);
        }
        return type;
    }

    public static TypeMirror eraseGenericTypes(TypeMirror type) {
        if (type.getKind() != TypeKind.DECLARED) {
            return type;
        }
        DeclaredType declaredType = (DeclaredType) type;
        if (declaredType.getTypeArguments().size() == 0) {
            return type;
        }
        return new DeclaredCodeTypeMirror((TypeElement) declaredType.asElement());
    }

    public static boolean variableEquals(VariableElement var1, VariableElement var2) {
        if (!var1.getSimpleName().equals(var2.getSimpleName())) {
            return false;
        }
        if (!ElementUtils.typeEquals(var1.asType(), var2.asType())) {
            return false;
        }
        if (!ElementUtils.elementEquals(var1.getEnclosingElement(), var2.getEnclosingElement())) {
            return false;
        }
        return true;
    }

    public static boolean executableEquals(ExecutableElement var1, ExecutableElement var2) {
        if (!var1.getSimpleName().equals(var2.getSimpleName())) {
            return false;
        }
        if (var1.getParameters().size() != var2.getParameters().size()) {
            return false;
        }
        if (!ElementUtils.typeEquals(var1.asType(), var2.asType())) {
            return false;
        }
        if (!ElementUtils.elementEquals(var1.getEnclosingElement(), var2.getEnclosingElement())) {
            return false;
        }
        for (int i = 0; i < var1.getParameters().size(); i++) {
            if (!typeEquals(var1.getParameters().get(i).asType(), var2.getParameters().get(i).asType())) {
                return false;
            }
        }
        return true;
    }

    public static boolean elementEquals(Element element1, Element element2) {
        if (element1.getKind() != element2.getKind()) {
            return false;
        } else if (element1 instanceof VariableElement) {
            return variableEquals((VariableElement) element1, (VariableElement) element2);
        } else if (element1 instanceof ExecutableElement) {
            return executableEquals((ExecutableElement) element1, (ExecutableElement) element2);
        } else if (element1 instanceof TypeElement) {
            return typeEquals(element1.asType(), element2.asType());
        } else if (element1 instanceof PackageElement) {
            return element1.getSimpleName().equals(element2.getSimpleName());
        } else {
            throw new AssertionError("unsupported element type");
        }
    }

    public static List<TypeMirror> sortTypes(List<TypeMirror> list, final boolean reverse) {
        Collections.sort(list, new Comparator<TypeMirror>() {
            public int compare(TypeMirror o1, TypeMirror o2) {
                if (reverse) {
                    return compareType(o2, o1);
                } else {
                    return compareType(o1, o2);
                }
            }
        });
        return list;
    }

    public static int compareType(TypeMirror signature1, TypeMirror signature2) {
        if (signature1 == null) {
            return 1;
        } else if (signature2 == null) {
            return -1;
        }

        if (ElementUtils.typeEquals(signature1, signature2)) {
            return 0;
        }

        if (signature1.getKind() == TypeKind.DECLARED && signature2.getKind() == TypeKind.DECLARED) {
            TypeElement element1 = ElementUtils.fromTypeMirror(signature1);
            TypeElement element2 = ElementUtils.fromTypeMirror(signature2);

            if (ElementUtils.getDirectSuperTypes(element1).contains(element2)) {
                return -1;
            } else if (ElementUtils.getDirectSuperTypes(element2).contains(element1)) {
                return 1;
            }
        }
        return ElementUtils.getSimpleName(signature1).compareTo(ElementUtils.getSimpleName(signature2));
    }

    public static List<TypeMirror> uniqueSortedTypes(Collection<TypeMirror> types, boolean reverse) {
        if (types.isEmpty()) {
            return new ArrayList<>(0);
        } else if (types.size() <= 1) {
            if (types instanceof List) {
                return (List<TypeMirror>) types;
            } else {
                return new ArrayList<>(types);
            }
        }
        Map<String, TypeMirror> sourceTypes = new HashMap<>();
        for (TypeMirror type : types) {
            sourceTypes.put(ElementUtils.getTypeId(type), type);
        }
        return sortTypes(new ArrayList<>(sourceTypes.values()), reverse);
    }

    public static int compareMethod(ExecutableElement method1, ExecutableElement method2) {
        List<? extends VariableElement> parameters1 = method1.getParameters();
        List<? extends VariableElement> parameters2 = method2.getParameters();
        if (parameters1.size() != parameters2.size()) {
            return Integer.compare(parameters1.size(), parameters2.size());
        }

        int result = 0;
        for (int i = 0; i < parameters1.size(); i++) {
            VariableElement var1 = parameters1.get(i);
            VariableElement var2 = parameters2.get(i);
            result = compareType(var1.asType(), var2.asType());
            if (result != 0) {
                return result;
            }
        }

        result = method1.getSimpleName().toString().compareTo(method2.getSimpleName().toString());
        if (result == 0) {
            // if still no difference sort by enclosing type name
            TypeElement enclosingType1 = ElementUtils.findNearestEnclosingType(method1);
            TypeElement enclosingType2 = ElementUtils.findNearestEnclosingType(method2);
            result = enclosingType1.getQualifiedName().toString().compareTo(enclosingType2.getQualifiedName().toString());
        }
        return result;
    }

}
