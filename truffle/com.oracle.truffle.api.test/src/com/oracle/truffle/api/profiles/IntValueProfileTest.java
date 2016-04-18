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
public class IntValueProfileTest {

    @DataPoint public static final int VALUE0 = Integer.MIN_VALUE;
    @DataPoint public static final int VALUE1 = 0;
    @DataPoint public static final int VALUE2 = 14;
    @DataPoint public static final int VALUE3 = Integer.MAX_VALUE;

    private IntValueProfile.Enabled profile;

    @Before
    public void create() {
        profile = (IntValueProfile.Enabled) IntValueProfile.Enabled.create();
    }

    @Test
    public void testInitial() {
        assertThat(profile.isGeneric(), is(false));
        assertThat(profile.isUninitialized(), is(true));
        profile.toString(); // test that it is not crashing
    }

    @Theory
    public void testProfileOneObject(int value) {
        int result = profile.profile(value);

        assertThat(result, is(value));
        assertEquals(profile.getCachedValue(), value);
        assertThat(profile.isUninitialized(), is(false));
        profile.toString(); // test that it is not crashing
    }

    @Theory
    public void testProfileTwoObject(int value0, int value1) {
        int result0 = profile.profile(value0);
        int result1 = profile.profile(value1);

        assertThat(result0, is(value0));
        assertThat(result1, is(value1));

        if (value0 == value1) {
            assertThat(profile.getCachedValue(), is(value0));
            assertThat(profile.isGeneric(), is(false));
        } else {
            assertThat(profile.isGeneric(), is(true));
        }
        assertThat(profile.isUninitialized(), is(false));
        profile.toString(); // test that it is not crashing
    }

    @Theory
    public void testProfileThreeObject(int value0, int value1, int value2) {
        int result0 = profile.profile(value0);
        int result1 = profile.profile(value1);
        int result2 = profile.profile(value2);

        assertThat(result0, is(value0));
        assertThat(result1, is(value1));
        assertThat(result2, is(value2));

        if (value0 == value1 && value1 == value2) {
            assertThat(profile.getCachedValue(), is(value0));
            assertThat(profile.isGeneric(), is(false));
        } else {
            assertThat(profile.isGeneric(), is(true));
        }
        assertThat(profile.isUninitialized(), is(false));
        profile.toString(); // test that it is not crashing
    }

    @Test
    public void testDisabled() {
        IntValueProfile.Disabled p = (IntValueProfile.Disabled) IntValueProfile.Disabled.INSTANCE;
        assertThat(p.profile(VALUE0), is(VALUE0));
        assertThat(p.profile(VALUE1), is(VALUE1));
        assertThat(p.profile(VALUE2), is(VALUE2));
        assertThat(p.profile(VALUE3), is(VALUE3));
        p.toString(); // test that it is not crashing
    }

}
