/*
 * Copyright (c) 2015, 2016, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.object.dsl.processor;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import com.oracle.truffle.api.object.Layout;
import com.oracle.truffle.object.dsl.processor.model.LayoutModel;
import com.oracle.truffle.object.dsl.processor.model.NameUtils;
import com.oracle.truffle.object.dsl.processor.model.PropertyModel;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class LayoutGenerator {

    private final LayoutModel layout;

    public LayoutGenerator(LayoutModel layout) {
        this.layout = layout;
    }

    public void generate(final PrintStream stream) {
        stream.printf("package %s;%n", layout.getPackageName());
        stream.println();

        generateImports(stream);

        stream.println();
        stream.printf("@GeneratedBy(%s.class)%n", layout.getInterfaceFullName());
        stream.printf("public class %sLayoutImpl", layout.getName());

        if (layout.getSuperLayout() != null) {
            stream.printf(" extends %sLayoutImpl", layout.getSuperLayout().getName());
        }

        stream.printf(" implements %sLayout {%n", layout.getName());

        stream.println("    ");
        stream.printf("    public static final %sLayout INSTANCE = new %sLayoutImpl();%n", layout.getName(), layout.getName());
        stream.println("    ");

        generateObjectType(stream);

        if (!layout.hasShapeProperties()) {
            stream.printf("    protected static final %sType %s_TYPE = new %sType();%n", layout.getName(),
                            NameUtils.identifierToConstant(layout.getName()), layout.getName());
            stream.println("    ");
        }

        generateAllocator(stream);
        generateProperties(stream);

        if (!layout.hasShapeProperties()) {
            stream.printf("    private static final DynamicObjectFactory %s_FACTORY = create%sShape();%n",
                            NameUtils.identifierToConstant(layout.getName()), layout.getName());
            stream.println("    ");
        }

        stream.printf("    protected %sLayoutImpl() {%n", layout.getName());
        stream.println("    }");
        stream.println("    ");

        generateShapeFactory(stream);
        generateFactory(stream);
        generateGuards(stream);
        generateAccessors(stream);

        stream.println("}");
    }

    private void generateImports(PrintStream stream) {
        boolean needsAtomicInteger = false;
        boolean needsAtomicBoolean = false;
        boolean needsAtomicReference = false;
        boolean needsIncompatibleLocationException = false;
        boolean needsFinalLocationException = false;
        boolean needsHiddenKey = false;
        boolean needsBoundary = false;

        for (PropertyModel property : layout.getProperties()) {
            if (!property.isShapeProperty() && !property.hasIdentifier()) {
                needsHiddenKey = true;
            }

            if (property.isVolatile()) {
                if (property.getType().getKind() == TypeKind.INT) {
                    needsAtomicInteger = true;
                } else if (property.getType().getKind() == TypeKind.BOOLEAN) {
                    needsAtomicBoolean = true;
                } else {
                    needsAtomicReference = true;
                }
            } else {
                if (property.hasSetter()) {
                    if (!property.isShapeProperty()) {
                        needsIncompatibleLocationException = true;
                        needsFinalLocationException = true;
                    }
                }
            }

            if (property.isShapeProperty() && (property.hasSetter() || property.hasShapeSetter())) {
                needsBoundary = true;
            }
        }

        if (layout.hasFinalInstanceProperties() || layout.hasNonNullableInstanceProperties()) {
            stream.println("import java.util.EnumSet;");
        }

        if (needsAtomicBoolean) {
            stream.println("import java.util.concurrent.atomic.AtomicBoolean;");
        }

        if (needsAtomicInteger) {
            stream.println("import java.util.concurrent.atomic.AtomicInteger;");
        }

        if (needsAtomicReference) {
            stream.println("import java.util.concurrent.atomic.AtomicReference;");
        }

        stream.println("import com.oracle.truffle.api.CompilerAsserts;");
        stream.println("import com.oracle.truffle.api.dsl.GeneratedBy;");

        if (needsBoundary) {
            stream.println("import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;");
        }

        stream.println("import com.oracle.truffle.api.object.DynamicObject;");
        stream.println("import com.oracle.truffle.api.object.DynamicObjectFactory;");

        if (needsFinalLocationException) {
            stream.println("import com.oracle.truffle.api.object.FinalLocationException;");
        }

        if (needsHiddenKey) {
            stream.println("import com.oracle.truffle.api.object.HiddenKey;");
        }

        if (needsIncompatibleLocationException) {
            stream.println("import com.oracle.truffle.api.object.IncompatibleLocationException;");
        }

        if (layout.getSuperLayout() == null) {
            stream.println("import com.oracle.truffle.api.object.Layout;");
        }

        if (layout.hasFinalInstanceProperties() || layout.hasNonNullableInstanceProperties()) {
            stream.println("import com.oracle.truffle.api.object.LocationModifier;");
        }

        stream.println("import com.oracle.truffle.api.object.ObjectType;");

        if (!layout.getInstanceProperties().isEmpty()) {
            stream.println("import com.oracle.truffle.api.object.Property;");
        }

        stream.println("import com.oracle.truffle.api.object.Shape;");
        stream.printf("import %s;%n", layout.getInterfaceFullName());

        if (layout.getSuperLayout() != null) {
            stream.printf("import %s.%sLayoutImpl;%n", layout.getSuperLayout().getPackageName(), layout.getSuperLayout().getName());
        }
    }

    private void generateObjectType(final PrintStream stream) {
        final String typeSuperclass;

        if (layout.getSuperLayout() == null) {
            typeSuperclass = layout.getObjectTypeSuperclass().toString();
        } else {
            typeSuperclass = layout.getSuperLayout().getName() + "LayoutImpl." + layout.getSuperLayout().getName() + "Type";
        }

        stream.printf("    protected static class %sType extends %s {%n", layout.getName(), typeSuperclass);

        if (layout.hasShapeProperties()) {
            stream.println("        ");

            for (PropertyModel property : layout.getShapeProperties()) {
                stream.printf("        protected final %s %s;%n", property.getType(), property.getName());
            }

            if (!layout.getShapeProperties().isEmpty()) {
                stream.println("        ");
            }

            stream.printf("        public %sType(%n", layout.getName());

            iterateProperties(layout.getAllShapeProperties(), new PropertyIteratorAction() {

                @Override
                public void run(PropertyModel property, boolean last) {
                    stream.printf("                %s %s", property.getType().toString(), property.getName());

                    if (last) {
                        stream.println(") {");
                    } else {
                        stream.println(",");
                    }
                }

            });

            if (!layout.getInheritedShapeProperties().isEmpty()) {
                stream.println("            super(");

                iterateProperties(layout.getInheritedShapeProperties(), new PropertyIteratorAction() {

                    @Override
                    public void run(PropertyModel property, boolean last) {
                        stream.printf("                %s", property.getName());

                        if (last) {
                            stream.println(");");
                        } else {
                            stream.println(",");
                        }
                    }

                });
            }

            iterateProperties(layout.getShapeProperties(), new PropertyIteratorAction() {

                @Override
                public void run(PropertyModel property, boolean last) {
                    stream.printf("            this.%s = %s;%n", property.getName(), property.getName());
                }

            });

            stream.println("        }");
            stream.println("        ");

            for (PropertyModel property : layout.getAllShapeProperties()) {
                final boolean inherited = !layout.getShapeProperties().contains(property);

                if (!inherited) {
                    stream.printf("        public %s %s() {%n", property.getType(), NameUtils.asGetter(property.getName()));
                    stream.printf("            return %s;%n", property.getName());
                    stream.println("        }");
                    stream.println("        ");
                }

                if (inherited) {
                    stream.println("        @Override");
                }

                stream.printf("        public %sType %s(%s %s) {%n", layout.getName(), NameUtils.asSetter(property.getName()), property.getType(), property.getName());
                stream.printf("            return new %sType(%n", layout.getName());

                iterateProperties(layout.getAllShapeProperties(), new PropertyIteratorAction() {

                    @Override
                    public void run(PropertyModel p, boolean last) {
                        stream.printf("                %s", p.getName());

                        if (last) {
                            stream.println(");");
                        } else {
                            stream.println(",");
                        }
                    }

                });

                stream.println("        }");
                stream.println("        ");
            }
        } else {
            stream.println("        ");
        }

        stream.println("    }");
        stream.println("    ");
    }

    private void generateAllocator(final PrintStream stream) {
        if (layout.getSuperLayout() == null) {
            stream.print("    protected static final Layout LAYOUT = Layout.newLayout()");

            for (Layout.ImplicitCast implicitCast : layout.getImplicitCasts()) {
                stream.print(".addAllowedImplicitCast(Layout.ImplicitCast.");
                stream.print(implicitCast.name());
                stream.print(")");
            }

            stream.println(".build();");

            stream.printf("    protected static final Shape.Allocator %S_ALLOCATOR = LAYOUT.createAllocator();%n", NameUtils.identifierToConstant(layout.getName()));
        } else {
            stream.printf("    protected static final Shape.Allocator %S_ALLOCATOR = LAYOUT.createAllocator();%n", NameUtils.identifierToConstant(layout.getName()));
            stream.println("    ");

            if (layout.getSuperLayout().hasInstanceProperties()) {
                stream.println("    static {");

                for (PropertyModel property : layout.getSuperLayout().getAllInstanceProperties()) {
                    final List<String> modifiers = new ArrayList<>();

                    if (!property.isNullable()) {
                        modifiers.add("LocationModifier.NonNull");
                    }

                    if (property.isFinal()) {
                        modifiers.add("LocationModifier.Final");
                    }

                    final String modifiersExpression;

                    if (modifiers.isEmpty()) {
                        modifiersExpression = "";
                    } else {
                        final StringBuilder modifiersExpressionBuilder = new StringBuilder();
                        modifiersExpressionBuilder.append(", EnumSet.of(");

                        for (String modifier : modifiers) {
                            if (!modifier.equals(modifiers.get(0))) {
                                modifiersExpressionBuilder.append(", ");
                            }

                            modifiersExpressionBuilder.append(modifier);
                        }

                        modifiersExpressionBuilder.append(")");
                        modifiersExpression = modifiersExpressionBuilder.toString();
                    }

                    final String locationType;

                    if (property.isVolatile()) {
                        if (property.getType().getKind() == TypeKind.INT) {
                            locationType = "AtomicInteger";
                        } else if (property.getType().getKind() == TypeKind.BOOLEAN) {
                            locationType = "AtomicBoolean";
                        } else {
                            locationType = "AtomicReference";
                        }
                    } else {
                        locationType = NameUtils.typeWithoutParameters(property.getType().toString());
                    }

                    stream.printf("         %s_ALLOCATOR.locationForType(%s.class%s);%n",
                                    NameUtils.identifierToConstant(layout.getName()),
                                    locationType,
                                    modifiersExpression);
                }

                stream.println("    }");
                stream.println("    ");
            }
        }
    }

    private void generateProperties(final PrintStream stream) {
        for (PropertyModel property : layout.getInstanceProperties()) {
            if (!property.hasIdentifier()) {
                stream.printf("    protected static final HiddenKey %s_IDENTIFIER = new HiddenKey(\"%s\");%n", NameUtils.identifierToConstant(property.getName()), property.getName());
            }

            final List<String> modifiers = new ArrayList<>();

            if (!property.isNullable()) {
                modifiers.add("LocationModifier.NonNull");
            }

            if (property.isFinal()) {
                modifiers.add("LocationModifier.Final");
            }

            final String modifiersExpression;

            if (modifiers.isEmpty()) {
                modifiersExpression = "";
            } else {
                final StringBuilder modifiersExpressionBuilder = new StringBuilder();
                modifiersExpressionBuilder.append(", EnumSet.of(");

                for (String modifier : modifiers) {
                    if (!modifier.equals(modifiers.get(0))) {
                        modifiersExpressionBuilder.append(", ");
                    }

                    modifiersExpressionBuilder.append(modifier);
                }

                modifiersExpressionBuilder.append(")");
                modifiersExpression = modifiersExpressionBuilder.toString();
            }

            final String locationType;

            if (property.isVolatile()) {
                if (property.getType().getKind() == TypeKind.INT) {
                    locationType = "AtomicInteger";
                } else if (property.getType().getKind() == TypeKind.BOOLEAN) {
                    locationType = "AtomicBoolean";
                } else {
                    locationType = "AtomicReference";
                }
            } else {
                locationType = NameUtils.typeWithoutParameters(property.getType().toString());
            }

            stream.printf("    protected static final Property %S_PROPERTY = Property.create(%s_IDENTIFIER, %S_ALLOCATOR.locationForType(%s.class%s), 0);%n",
                            NameUtils.identifierToConstant(property.getName()),
                            NameUtils.identifierToConstant(property.getName()),
                            NameUtils.identifierToConstant(layout.getName()),
                            locationType,
                            modifiersExpression);

            stream.println("    ");
        }
    }

    private void generateShapeFactory(final PrintStream stream) {
        if (layout.hasShapeProperties()) {
            stream.println("    @Override");
            stream.print("    public");
        } else {
            stream.print("    private static");
        }

        stream.printf(" DynamicObjectFactory create%sShape(", layout.getName());

        if (layout.hasShapeProperties()) {
            stream.println();

            for (PropertyModel property : layout.getAllShapeProperties()) {
                stream.printf("            %s %s", property.getType().toString(), property.getName());

                if (property == layout.getAllShapeProperties().get(layout.getAllShapeProperties().size() - 1)) {
                    stream.println(") {");
                } else {
                    stream.println(",");
                }
            }
        } else {
            stream.println(") {");
        }

        for (PropertyModel property : layout.getAllShapeProperties()) {
            if (!property.getType().getKind().isPrimitive() && !property.isNullable()) {
                stream.printf("        assert %s != null;%n", property.getName());
            }
        }

        stream.printf("        return LAYOUT.createShape(new %sType(", layout.getName());

        if (layout.hasShapeProperties()) {
            stream.println();

            iterateProperties(layout.getAllShapeProperties(), new PropertyIteratorAction() {

                @Override
                public void run(PropertyModel property, boolean last) {
                    stream.printf("                %s", property.getName());

                    if (last) {
                        stream.println("))");
                    } else {
                        stream.println(",");
                    }
                }

            });
        } else {
            stream.println("))");
        }

        iterateProperties(layout.getAllInstanceProperties(), new PropertyIteratorAction() {

            @Override
            public void run(PropertyModel property, boolean last) {
                stream.printf("            .addProperty(%s_PROPERTY)%n", NameUtils.identifierToConstant(property.getName()));
            }

        });

        stream.println("            .createFactory();");

        stream.println("    }");
        stream.println("    ");
    }

    private void generateFactory(final PrintStream stream) {
        if (!layout.hasShapeProperties()) {
            stream.println("    @Override");
            stream.printf("    public DynamicObject create%s(", layout.getName());

            if (layout.getAllProperties().isEmpty()) {
                stream.println(") {");
            } else {
                stream.println();

                for (PropertyModel property : layout.getAllProperties()) {
                    stream.printf("            %s %s", property.getType().toString(), property.getName());

                    if (property == layout.getAllProperties().get(layout.getAllProperties().size() - 1)) {
                        stream.println(") {");
                    } else {
                        stream.println(",");
                    }
                }
            }

            stream.printf("        return create%s(%s_FACTORY", layout.getName(), NameUtils.identifierToConstant(layout.getName()));

            if (layout.getAllProperties().isEmpty()) {
                stream.println(");");
            } else {
                stream.println(",");

                for (PropertyModel property : layout.getAllProperties()) {
                    stream.printf("            %s", property.getName());

                    if (property == layout.getAllProperties().get(layout.getAllProperties().size() - 1)) {
                        stream.println(");");
                    } else {
                        stream.println(",");
                    }
                }
            }

            stream.println("    }");
            stream.println("    ");
        }

        if (layout.hasShapeProperties()) {
            stream.println("    @Override");
            stream.print("    public");
        } else {
            stream.print("    private");

            if (!layout.hasObjectTypeGuard()) {
                stream.printf(" static");
            }
        }

        if (layout.hasInstanceProperties()) {
            stream.printf(" DynamicObject create%s(%n", layout.getName());
            stream.println("            DynamicObjectFactory factory,");

            for (PropertyModel property : layout.getAllInstanceProperties()) {
                stream.printf("            %s %s", property.getType().toString(), property.getName());

                if (property == layout.getAllProperties().get(layout.getAllProperties().size() - 1)) {
                    stream.println(") {");
                } else {
                    stream.println(",");
                }
            }
        } else {
            stream.printf(" DynamicObject create%s(DynamicObjectFactory factory) {%n", layout.getName());
        }

        stream.println("        assert factory != null;");
        stream.println("        CompilerAsserts.partialEvaluationConstant(factory);");
        stream.printf("        assert creates%s(factory);%n", layout.getName());

        for (PropertyModel property : layout.getAllInstanceProperties()) {
            stream.printf("        assert factory.getShape().hasProperty(%s_IDENTIFIER);%n", NameUtils.identifierToConstant(property.getName()));
        }

        for (PropertyModel property : layout.getAllInstanceProperties()) {
            if (!property.getType().getKind().isPrimitive() && !property.isNullable()) {
                stream.printf("        assert %s != null;%n", property.getName());
            }
        }

        if (layout.hasInstanceProperties()) {
            stream.println("        return factory.newInstance(");

            for (PropertyModel property : layout.getAllInstanceProperties()) {
                if (property.isVolatile()) {
                    if (property.getType().getKind() == TypeKind.INT) {
                        stream.printf("            new AtomicInteger(%s)", property.getName());
                    } else if (property.getType().getKind() == TypeKind.BOOLEAN) {
                        stream.printf("            new AtomicBoolean(%s)", property.getName());
                    } else {
                        stream.printf("            new AtomicReference<%s>(%s)", property.getType(), property.getName());
                    }
                } else {
                    stream.printf("            %s", property.getName());
                }

                if (property == layout.getAllProperties().get(layout.getAllProperties().size() - 1)) {
                    stream.println(");");
                } else {
                    stream.println(",");
                }
            }
        } else {
            stream.println("        return factory.newInstance();");
        }

        stream.println("    }");
        stream.println("    ");
    }

    private void generateGuards(final PrintStream stream) {
        if (layout.hasObjectGuard()) {
            stream.println("    @Override");
            stream.printf("    public boolean is%s(Object object) {%n", layout.getName());
            stream.printf("        return (object instanceof DynamicObject) && is%s((DynamicObject) object);%n", layout.getName());
            stream.println("    }");
            stream.println("    ");
        }

        if (layout.hasDynamicObjectGuard() || layout.hasGettersOrSetters()) {
            if (layout.hasDynamicObjectGuard()) {
                stream.println("    @Override");
                stream.print("    public");
            } else {
                stream.print("    private");
            }

            if (!layout.hasDynamicObjectGuard() && !layout.hasObjectTypeGuard()) {
                stream.printf(" static");
            }

            stream.printf(" boolean is%s(DynamicObject object) {%n", layout.getName());
            stream.printf("        return is%s(object.getShape().getObjectType());%n", layout.getName());
            stream.println("    }");
            stream.println("    ");
        }

        if (layout.hasObjectTypeGuard()) {
            stream.println("    @Override");
            stream.print("    public");
        } else {
            stream.print("    private static");
        }

        stream.printf(" boolean is%s(ObjectType objectType) {%n", layout.getName());
        stream.printf("        return objectType instanceof %sType;%n", layout.getName());
        stream.println("    }");
        stream.println("    ");

        stream.printf("    private");

        if (!layout.hasObjectTypeGuard()) {
            stream.printf(" static");
        }

        stream.printf(" boolean creates%s(DynamicObjectFactory factory) {%n", layout.getName());
        stream.printf("        return is%s(factory.getShape().getObjectType());%n", layout.getName());
        stream.println("    }");
        stream.println("    ");
    }

    private void generateAccessors(final PrintStream stream) {
        for (PropertyModel property : layout.getProperties()) {
            if (property.hasObjectTypeGetter()) {
                stream.println("    @Override");
                stream.printf("    public %s %s(ObjectType objectType) {%n", property.getType(), NameUtils.asGetter(property.getName()));
                stream.printf("        assert is%s(objectType);%n", layout.getName());
                stream.printf("        return ((%sType) objectType).%s();%n", layout.getName(), NameUtils.asGetter(property.getName()));
                stream.println("    }");
                stream.println("    ");
            }

            if (property.hasShapeGetter()) {
                stream.println("    @Override");
                stream.printf("    public %s %s(DynamicObjectFactory factory) {%n", property.getType(), NameUtils.asGetter(property.getName()));
                stream.printf("        assert creates%s(factory);%n", layout.getName());
                stream.printf("        return ((%sType) factory.getShape().getObjectType()).%s();%n", layout.getName(), NameUtils.asGetter(property.getName()));
                stream.println("    }");
                stream.println("    ");
            }

            if (property.hasGetter()) {
                addUncheckedCastWarning(stream, property);
                stream.println("    @Override");
                stream.printf("    public %s %s(DynamicObject object) {%n", property.getType(), NameUtils.asGetter(property.getName()));
                stream.printf("        assert is%s(object);%n", layout.getName());

                if (property.isShapeProperty()) {
                    stream.printf("        return getObjectType(object).%s();%n", NameUtils.asGetter(property.getName()));
                } else {
                    stream.printf("        assert object.getShape().hasProperty(%s_IDENTIFIER);%n", NameUtils.identifierToConstant(property.getName()));
                    stream.println("        ");

                    if (property.isVolatile()) {
                        if (property.getType().getKind() == TypeKind.INT) {
                            stream.printf("        return ((AtomicInteger) %s_PROPERTY.get(object, is%s(object))).get();%n", NameUtils.identifierToConstant(property.getName()), layout.getName());
                        } else if (property.getType().getKind() == TypeKind.BOOLEAN) {
                            stream.printf("        return ((AtomicBoolean) %s_PROPERTY.get(object, is%s(object))).get();%n", NameUtils.identifierToConstant(property.getName()), layout.getName());
                        } else {
                            stream.printf("        return ((AtomicReference<%s>) %s_PROPERTY.get(object, is%s(object))).get();%n", property.getType(),
                                            NameUtils.identifierToConstant(property.getName()), layout.getName());
                        }
                    } else {
                        stream.printf("        return %s%s_PROPERTY.get(object, is%s(object));%n", cast(property.getType()), NameUtils.identifierToConstant(property.getName()), layout.getName());
                    }
                }

                stream.println("    }");
                stream.println("    ");
            }

            if (property.hasShapeSetter()) {
                stream.println("    @TruffleBoundary");
                stream.println("    @Override");
                stream.printf("    public DynamicObjectFactory %s(DynamicObjectFactory factory, %s value) {%n", NameUtils.asSetter(property.getName()), property.getType());
                stream.printf("        assert creates%s(factory);%n", layout.getName());
                stream.println("        final Shape shape = factory.getShape();");
                stream.printf("        return shape.changeType(((%sType) shape.getObjectType()).%s(value)).createFactory();%n", layout.getName(), NameUtils.asSetter(property.getName()));
                stream.println("    }");
                stream.println("    ");
            }

            if (property.hasSetter() || property.hasUnsafeSetter()) {
                addUncheckedCastWarning(stream, property);
                if (property.isShapeProperty()) {
                    stream.println("    @TruffleBoundary");
                }

                stream.println("    @Override");

                final String methodNameSuffix;

                if (property.hasUnsafeSetter()) {
                    methodNameSuffix = "Unsafe";
                } else {
                    methodNameSuffix = "";
                }

                stream.printf("    public void %s%s(DynamicObject object, %s value) {%n", NameUtils.asSetter(property.getName()), methodNameSuffix, property.getType());
                stream.printf("        assert is%s(object);%n", layout.getName());

                if (property.isShapeProperty()) {
                    stream.println("        final Shape shape = object.getShape();");
                    stream.printf("        object.setShapeAndResize(shape, shape.changeType(getObjectType(object).%s(value)));%n", NameUtils.asSetter(property.getName()));
                } else {
                    stream.printf("        assert object.getShape().hasProperty(%s_IDENTIFIER);%n", NameUtils.identifierToConstant(property.getName()));

                    if (!property.getType().getKind().isPrimitive() && !property.isNullable()) {
                        stream.println("        assert value != null;");
                    }

                    stream.println("        ");

                    if (property.hasUnsafeSetter()) {
                        stream.printf("        %s_PROPERTY.setInternal(object, value);%n", NameUtils.identifierToConstant(property.getName()));
                    } else if (property.isVolatile()) {
                        if (property.getType().getKind() == TypeKind.INT) {
                            stream.printf("        ((AtomicInteger) %s_PROPERTY.get(object, is%s(object))).set(value);%n", NameUtils.identifierToConstant(property.getName()), layout.getName());
                        } else if (property.getType().getKind() == TypeKind.BOOLEAN) {
                            stream.printf("        ((AtomicBoolean) %s_PROPERTY.get(object, is%s(object))).set(value);%n", NameUtils.identifierToConstant(property.getName()), layout.getName());
                        } else {
                            stream.printf("        ((AtomicReference<%s>) %s_PROPERTY.get(object, is%s(object))).set(value);%n", property.getType(),
                                            NameUtils.identifierToConstant(property.getName()),
                                            layout.getName());
                        }
                    } else {
                        stream.printf("        try {%n");
                        stream.printf("            %s_PROPERTY.set(object, value, object.getShape());%n", NameUtils.identifierToConstant(property.getName()));
                        stream.printf("        } catch (IncompatibleLocationException | FinalLocationException e) {%n");
                        stream.printf("            throw new UnsupportedOperationException(e);%n");
                        stream.printf("        }%n");
                    }
                }

                stream.println("    }");
                stream.println("    ");
            }

            if (property.hasCompareAndSet()) {
                addUncheckedCastWarning(stream, property);

                stream.println("    @Override");
                stream.printf("    public boolean %s(DynamicObject object, %s expected_value, %s value) {%n",
                                NameUtils.asCompareAndSet(property.getName()),
                                property.getType(),
                                property.getType());

                stream.printf("        assert is%s(object);%n", layout.getName());
                stream.printf("        assert object.getShape().hasProperty(%s_IDENTIFIER);%n",
                                NameUtils.identifierToConstant(property.getName()));
                if (!property.getType().getKind().isPrimitive() && !property.isNullable()) {
                    stream.println("        assert value != null;");
                }

                if (property.getType().getKind() == TypeKind.INT) {
                    stream.printf(
                                    "        return ((AtomicInteger) %s_PROPERTY.get(object, is%s(object))).compareAndSet(expected_value, value);%n",
                                    NameUtils.identifierToConstant(property.getName()), layout.getName());
                } else if (property.getType().getKind() == TypeKind.BOOLEAN) {
                    stream.printf(
                                    "        return ((AtomicBoolean) %s_PROPERTY.get(object, is%s(object))).compareAndSet(expected_value, value);%n",
                                    NameUtils.identifierToConstant(property.getName()), layout.getName());
                } else {
                    stream.printf(
                                    "        return ((AtomicReference<%s>) %s_PROPERTY.get(object, is%s(object))).compareAndSet(expected_value, value);%n",
                                    property.getType(),
                                    NameUtils.identifierToConstant(property.getName()), layout.getName());
                }

                stream.println("    }");
                stream.println("    ");
            }

            if (property.hasGetAndSet()) {
                addUncheckedCastWarning(stream, property);

                stream.println("    @Override");
                stream.printf("    public %s %s(DynamicObject object, %s value) {%n",
                                property.getType(),
                                NameUtils.asGetAndSet(property.getName()),
                                property.getType());

                stream.printf("        assert is%s(object);%n", layout.getName());
                stream.printf("        assert object.getShape().hasProperty(%s_IDENTIFIER);%n",
                                NameUtils.identifierToConstant(property.getName()));
                if (!property.getType().getKind().isPrimitive() && !property.isNullable()) {
                    stream.println("        assert value != null;");
                }

                if (property.getType().getKind() == TypeKind.INT) {
                    stream.printf(
                                    "        return ((AtomicInteger) %s_PROPERTY.get(object, is%s(object))).getAndSet(value);%n",
                                    NameUtils.identifierToConstant(property.getName()), layout.getName());
                } else if (property.getType().getKind() == TypeKind.BOOLEAN) {
                    stream.printf(
                                    "        return ((AtomicBoolean) %s_PROPERTY.get(object, is%s(object))).getAndSet(value);%n",
                                    NameUtils.identifierToConstant(property.getName()), layout.getName());
                } else {
                    stream.printf(
                                    "        return ((AtomicReference<%s>) %s_PROPERTY.get(object, is%s(object))).getAndSet(value);%n",
                                    property.getType(),
                                    NameUtils.identifierToConstant(property.getName()), layout.getName());
                }

                stream.println("    }");
                stream.println("    ");
            }
        }

        if (!layout.getShapeProperties().isEmpty()) {
            stream.print("    private");

            if (!layout.hasObjectTypeGuard()) {
                stream.print(" static");
            }

            stream.printf(" %sType getObjectType(DynamicObject object) {%n", layout.getName());
            stream.printf("        assert is%s(object);%n", layout.getName());
            stream.printf("        return (%sType) object.getShape().getObjectType();%n", layout.getName());
            stream.println("    }");
            stream.println("    ");
        }
    }

    private static void addUncheckedCastWarning(final PrintStream stream, PropertyModel property) {
        if (property.getType().toString().indexOf('<') != -1 ||
                        (property.isVolatile() && !property.getType().getKind().isPrimitive())) {
            stream.println("    @SuppressWarnings(\"unchecked\")");
        }
    }

    private static void iterateProperties(List<PropertyModel> properties, PropertyIteratorAction action) {
        for (int n = 0; n < properties.size(); n++) {
            action.run(properties.get(n), n == properties.size() - 1);
        }
    }

    private static String cast(TypeMirror type) {
        if (type.toString().equals(Object.class.getName())) {
            return "";
        } else {
            return String.format("(%s) ", type.toString());
        }
    }

    private interface PropertyIteratorAction {

        void run(PropertyModel property, boolean last);

    }

    public String getGeneratedClassName() {
        return layout.getPackageName() + "." + layout.getName() + "LayoutImpl";
    }

}
