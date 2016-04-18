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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.Theories.class)
public class EqualityValueProfileTest {

    @DataPoint public static final String O1 = new String();
    @DataPoint public static final String O2 = O1;
    @DataPoint public static final Object O3 = new Object();
    @DataPoint public static final Integer O4 = new Integer(1);
    @DataPoint public static final Integer O5 = new Integer(1);
    @DataPoint public static final Integer O6 = null;

    private ValueProfile.Equality profile;

    @Before
    public void create() {
        profile = (ValueProfile.Equality) ValueProfile.Equality.create();
    }

    @Test
    public void testInitial() throws Exception {
        assertThat(profile.isGeneric(), is(false));
        assertThat(profile.isUninitialized(), is(true));
        profile.toString(); // test that it is not crashing
    }

    @Theory
    public void testProfileOne(Object value) throws Exception {
        Object result = profile.profile(value);
        assertThat(result, is(equalTo(value)));
        if (value == null) {
            assertThat(profile.isUninitialized(), is(false));
            assertThat(profile.isGeneric(), is(true));
        } else {
            assertEquals(profile.getCachedValue(), value);
            assertThat(profile.isUninitialized(), is(false));
            assertThat(profile.isGeneric(), is(false));
        }
        profile.toString(); // test that it is not crashing
    }

    @Theory
    public void testProfileTwo(Object value0, Object value1) throws Exception {
        Object result0 = profile.profile(value0);
        Object result1 = profile.profile(value1);

        assertThat(result0, is(equalTo(value0)));
        assertThat(result1, is(equalTo(value1)));

        if (value0 != null && value0.equals(value1)) {
            assertThat(profile.getCachedValue(), is(equalTo(value0)));
            assertThat(profile.isGeneric(), is(false));
        } else {
            assertThat(profile.isGeneric(), is(true));
        }
        assertThat(profile.isUninitialized(), is(false));
        profile.toString(); // test that it is not crashing
    }

    @Theory
    public void testProfileThree(Object value0, Object value1, Object value2) throws Exception {
        Object result0 = profile.profile(value0);
        Object result1 = profile.profile(value1);
        Object result2 = profile.profile(value2);

        assertThat(result0, is(equalTo(value0)));
        assertThat(result1, is(equalTo(value1)));
        assertThat(result2, is(equalTo(value2)));

        if (value0 != null && value0.equals(value1) && value1 != null && value1.equals(value2)) {
            assertThat(profile.getCachedValue(), is(equalTo(value0)));
            assertThat(profile.isGeneric(), is(false));
        } else {
            assertThat(profile.isGeneric(), is(true));
        }
        assertThat(profile.isUninitialized(), is(false));
        profile.toString(); // test that it is not crashing
    }

}
