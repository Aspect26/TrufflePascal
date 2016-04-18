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
package com.oracle.truffle.dsl.processor.model;

import com.oracle.truffle.dsl.processor.java.ElementUtils;
import com.oracle.truffle.dsl.processor.util.FilteredIterable;
import com.oracle.truffle.dsl.processor.util.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class TemplateMethod extends MessageContainer implements Comparable<TemplateMethod> {

    public static final String FRAME_NAME = "frameValue";
    public static final int NO_NATURAL_ORDER = -1;

    private String id;
    private final Template template;
    private final int naturalOrder;
    private final MethodSpec specification;
    private final ExecutableElement method;
    private final AnnotationMirror markerAnnotation;
    private Parameter returnType;
    private final List<Parameter> parameters;
    private final Map<String, Parameter> parameterCache = new HashMap<>();

    public TemplateMethod(String id, int naturalOrder, Template template, MethodSpec specification, ExecutableElement method, AnnotationMirror markerAnnotation, Parameter returnType,
                    List<Parameter> parameters) {
        this.template = template;
        this.specification = specification;
        this.naturalOrder = naturalOrder;
        this.method = method;
        this.markerAnnotation = markerAnnotation;
        this.returnType = returnType;
        this.parameters = new ArrayList<>();
        for (Parameter param : parameters) {
            Parameter newParam = new Parameter(param);
            this.parameters.add(newParam);
            newParam.setMethod(this);
            parameterCache.put(param.getLocalName(), param);
        }
        if (returnType != null) {
            parameterCache.put(returnType.getLocalName(), returnType);
        }
        this.id = id;
    }

    public final Parameter getFrame() {
        return findParameter(FRAME_NAME);
    }

    public void removeParameter(Parameter p) {
        this.parameters.remove(p);
        this.parameterCache.remove(p.getLocalName());
        p.setMethod(this);
    }

    public void addParameter(int index, Parameter p) {
        this.parameters.add(index, p);
        this.parameterCache.put(p.getLocalName(), p);
        p.setMethod(this);
    }

    public String createReferenceName() {
        if (getMethod() == null) {
            return "-";
        }
        return ElementUtils.createReferenceName(getMethod());
    }

    public int getNaturalOrder() {
        return naturalOrder;
    }

    public TemplateMethod(TemplateMethod method) {
        this(method.id, method.naturalOrder, method.template, method.specification, method.method, method.markerAnnotation, method.returnType, method.parameters);
        getMessages().addAll(method.getMessages());
    }

    public TemplateMethod(TemplateMethod method, ExecutableElement executable) {
        this(method.id, method.naturalOrder, method.template, method.specification, executable, method.markerAnnotation, method.returnType, method.parameters);
        getMessages().addAll(method.getMessages());
    }

    @Override
    public Element getMessageElement() {
        return method;
    }

    @Override
    public AnnotationMirror getMessageAnnotation() {
        return markerAnnotation;
    }

    @Override
    protected List<MessageContainer> findChildContainers() {
        return Collections.emptyList();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Template getTemplate() {
        return template;
    }

    public MethodSpec getSpecification() {
        return specification;
    }

    public Parameter getReturnType() {
        return returnType;
    }

    public void replaceParameter(String localName, Parameter newParameter) {
        if (returnType.getLocalName().equals(localName)) {
            returnType = newParameter;
        } else {
            Parameter local = findParameter(localName);
            int index = parameters.indexOf(local);
            parameters.set(index, newParameter);
        }
        parameterCache.put(newParameter.getLocalName(), newParameter);
        newParameter.setMethod(this);
    }

    public Iterable<Parameter> getDynamicParameters() {
        return new FilteredIterable<>(getParameters(), new Predicate<Parameter>() {
            public boolean evaluate(Parameter value) {
                return !value.getSpecification().isLocal() && !value.getSpecification().isAnnotated();
            }
        });
    }

    public Iterable<Parameter> getSignatureParameters() {
        return new FilteredIterable<>(getParameters(), new Predicate<Parameter>() {
            public boolean evaluate(Parameter value) {
                return value.getSpecification().isSignature();
            }
        });
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public Parameter findParameterOrDie(NodeExecutionData execution) {
        for (Parameter parameter : parameters) {
            if (parameter.getSpecification().isSignature() && parameter.getSpecification().getExecution() == execution) {
                return parameter;
            }
        }
        throw new AssertionError("Could not find parameter for execution");
    }

    public List<Parameter> findByExecutionData(NodeExecutionData execution) {
        List<Parameter> foundParameters = new ArrayList<>();
        for (Parameter parameter : getParameters()) {
            ParameterSpec spec = parameter.getSpecification();
            if (spec != null && spec.getExecution() != null && spec.getExecution().equals(execution) && parameter.getSpecification().isSignature()) {
                foundParameters.add(parameter);
            }
        }
        return foundParameters;
    }

    public Parameter findParameter(String valueName) {
        return parameterCache.get(valueName);
    }

    public List<Parameter> getReturnTypeAndParameters() {
        List<Parameter> allParameters = new ArrayList<>(getParameters().size() + 1);
        if (getReturnType() != null) {
            allParameters.add(getReturnType());
        }
        allParameters.addAll(getParameters());
        return Collections.unmodifiableList(allParameters);
    }

    public ExecutableElement getMethod() {
        return method;
    }

    public String getMethodName() {
        if (getMethod() != null) {
            return getMethod().getSimpleName().toString();
        } else {
            return "$synthetic";
        }
    }

    public AnnotationMirror getMarkerAnnotation() {
        return markerAnnotation;
    }

    @Override
    public String toString() {
        return String.format("%s [id = %s, method = %s]", getClass().getSimpleName(), getId(), getMethod());
    }

    public Parameter getPreviousParam(Parameter searchParam) {
        Parameter prev = null;
        for (Parameter param : getParameters()) {
            if (param == searchParam) {
                return prev;
            }
            prev = param;
        }
        return prev;
    }

    @SuppressWarnings("unused")
    public int getSignatureSize() {
        int signatureSize = 0;
        for (Parameter parameter : getSignatureParameters()) {
            signatureSize++;
        }
        return signatureSize;
    }

    public TypeSignature getTypeSignature() {
        TypeSignature signature = new TypeSignature();
        signature.types.add(getReturnType().getType());
        for (Parameter parameter : getSignatureParameters()) {
            TypeMirror typeData = parameter.getType();
            if (typeData != null) {
                signature.types.add(typeData);
            }
        }
        return signature;
    }

    public void updateSignature(TypeSignature signature) {
        // TODO(CH): fails in normal usage - output ok though
        // assert signature.size() >= 1;

        int signatureIndex = 0;
        for (Parameter parameter : getReturnTypeAndParameters()) {
            if (!parameter.getSpecification().isSignature()) {
                continue;
            }
            if (signatureIndex >= signature.size()) {
                break;
            }
            TypeMirror newType = signature.get(signatureIndex++);
            if (!ElementUtils.typeEquals(newType, parameter.getType())) {
                replaceParameter(parameter.getLocalName(), new Parameter(parameter, newType));
            }
        }
    }

    @Override
    public int compareTo(TemplateMethod o) {
        if (this == o) {
            return 0;
        }

        int compare = compareBySignature(o);
        if (compare == 0) {
            // if signature sorting failed sort by id
            compare = getId().compareTo(o.getId());
        }
        if (compare == 0) {
            // if still no difference sort by enclosing type name
            TypeElement enclosingType1 = ElementUtils.findNearestEnclosingType(getMethod());
            TypeElement enclosingType2 = ElementUtils.findNearestEnclosingType(o.getMethod());
            compare = enclosingType1.getQualifiedName().toString().compareTo(enclosingType2.getQualifiedName().toString());
        }
        return compare;
    }

    public int compareBySignature(TemplateMethod compareMethod) {
        List<TypeMirror> signature1 = getDynamicTypes();
        List<TypeMirror> signature2 = compareMethod.getDynamicTypes();

        int result = 0;
        for (int i = 0; i < Math.max(signature1.size(), signature2.size()); i++) {
            TypeMirror t1 = i < signature1.size() ? signature1.get(i) : null;
            TypeMirror t2 = i < signature2.size() ? signature2.get(i) : null;
            result = ElementUtils.compareType(t1, t2);
            if (result != 0) {
                break;
            }
        }

        return result;
    }

    public List<TypeMirror> getDynamicTypes() {
        List<TypeMirror> types = new ArrayList<>();
        for (Parameter param : getDynamicParameters()) {
            types.add(param.getType());
        }
        return types;
    }

    public static class TypeSignature implements Iterable<TypeMirror> {

        private final List<TypeMirror> types;

        public TypeSignature() {
            this.types = new ArrayList<>();
        }

        public TypeSignature(List<TypeMirror> signature) {
            this.types = signature;
        }

        @Override
        public int hashCode() {
            return types.hashCode();
        }

        public int size() {
            return types.size();
        }

        public TypeMirror get(int index) {
            return types.get(index);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TypeSignature) {
                return ((TypeSignature) obj).types.equals(types);
            }
            return super.equals(obj);
        }

        public Iterator<TypeMirror> iterator() {
            return types.iterator();
        }

        @Override
        public String toString() {
            return types.toString();
        }
    }

}
