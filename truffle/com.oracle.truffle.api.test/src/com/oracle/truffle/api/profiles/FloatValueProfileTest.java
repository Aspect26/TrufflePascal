/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.api.profiles;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.Theories.class)
public class FloatValueProfileTest {

    @DataPoint public static final float VALUE0 = Float.MIN_VALUE;
    @DataPoint public static final float VALUE1 = -0.0f;
    @DataPoint public static final float VALUE2 = +0.0f;
    @DataPoint public static final float VALUE3 = 14.5f;
    @DataPoint public static final float VALUE4 = Float.MAX_VALUE;

    private static final float FLOAT_DELTA = 0.00001f;

    private FloatValueProfile.Enabled profile;

    @Before
    public void create() {
        profile = (FloatValueProfile.Enabled) FloatValueProfile.Enabled.create();
    }

    @Test
    public void testInitial() {
        assertThat(profile.isGeneric(), is(false));
        assertThat(profile.isUninitialized(), is(true));
        profile.toString(); // test that it is not crashing
    }

    @Theory
    public void testProfileOneFloat(float value) {
        float result = profile.profile(value);

        assertThat(result, is(value));
        assertEquals(profile.getCachedValue(), value, FLOAT_DELTA);
        assertThat(profile.isUninitialized(), is(false));
        profile.toString(); // test that it is not crashing
    }

    @SuppressWarnings("deprecation")
    @Theory
    public void testProfileTwoFloat(float value0, float value1) {
        float result0 = profile.profile(value0);
        float result1 = profile.profile(value1);

        assertEquals(result0, value0, FLOAT_DELTA);
        assertEquals(result1, value1, FLOAT_DELTA);

        if (PrimitiveValueProfile.exactCompare(value0, value1)) {
            assertEquals(profile.getCachedValue(), value0, FLOAT_DELTA);
            assertThat(profile.isGeneric(), is(false));
        } else {
            assertThat(profile.isGeneric(), is(true));
        }
        assertThat(profile.isUninitialized(), is(false));
        profile.toString(); // test that it is not crashing
    }

    @SuppressWarnings("deprecation")
    @Theory
    public void testProfileThreeFloat(float value0, float value1, float value2) {
        float result0 = profile.profile(value0);
        float result1 = profile.profile(value1);
        float result2 = profile.profile(value2);

        assertEquals(result0, value0, FLOAT_DELTA);
        assertEquals(result1, value1, FLOAT_DELTA);
        assertEquals(result2, value2, FLOAT_DELTA);

        if (PrimitiveValueProfile.exactCompare(value0, value1) && PrimitiveValueProfile.exactCompare(value1, value2)) {
            assertEquals(profile.getCachedValue(), value0, FLOAT_DELTA);
            assertThat(profile.isGeneric(), is(false));
        } else {
            assertThat(profile.isGeneric(), is(true));
        }
        assertThat(profile.isUninitialized(), is(false));
        profile.toString(); // test that it is not crashing
    }

    @Test
    public void testDisabled() {
        FloatValueProfile.Disabled p = (FloatValueProfile.Disabled) FloatValueProfile.Disabled.INSTANCE;
        assertThat(p.profile(VALUE0), is(VALUE0));
        assertThat(p.profile(VALUE1), is(VALUE1));
        assertThat(p.profile(VALUE2), is(VALUE2));
        assertThat(p.profile(VALUE3), is(VALUE3));
        assertThat(p.profile(VALUE4), is(VALUE4));
        p.toString(); // test that it is not crashing
    }

}
