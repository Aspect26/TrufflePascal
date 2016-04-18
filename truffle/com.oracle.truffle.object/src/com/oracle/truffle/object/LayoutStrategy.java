/*
 * Copyright (c) 2014, 2014, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.object;

import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.Location;
import com.oracle.truffle.api.object.Property;
import com.oracle.truffle.api.object.Shape;
import com.oracle.truffle.object.ShapeImpl.BaseAllocator;

public abstract class LayoutStrategy {
    public abstract boolean updateShape(DynamicObject object);

    public abstract ShapeImpl ensureValid(ShapeImpl newShape);

    public abstract ShapeImpl ensureSpace(ShapeImpl shape, Location location);

    public abstract boolean isAutoExtArray();

    public abstract BaseAllocator createAllocator(LayoutImpl shape);

    public abstract BaseAllocator createAllocator(ShapeImpl shape);

    protected abstract ShapeAndProperty generalizeProperty(Property oldProperty, Object value, ShapeImpl currentShape, ShapeImpl nextShape);

    public static class ShapeAndProperty {
        private final Shape shape;
        private final Property property;

        public ShapeAndProperty(Shape shape, Property property) {
            this.shape = shape;
            this.property = property;
        }

        public Shape getShape() {
            return shape;
        }

        public Property getProperty() {
            return property;
        }
    }
}
