/*
 * Copyright (c) 2015, 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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
package com.oracle.truffle.tck;

import java.io.PrintStream;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

interface TruffleJUnitRunListener {

    /**
     * Called before any tests have been run.
     *
     * @param description describes the tests to be run
     */
    void testRunStarted(Description description);

    /**
     * Called when all tests have finished.
     *
     * @param result the summary of the test run, including all the tests that failed
     */
    void testRunFinished(Result result);

    /**
     * Called when a test class is about to be started.
     *
     * @param clazz the test class
     */
    void testClassStarted(Class<?> clazz);

    /**
     * Called when all tests of a test class have finished.
     *
     * @param clazz the test class
     */
    void testClassFinished(Class<?> clazz);

    /**
     * Called when an atomic test is about to be started. This is also called for ignored tests.
     *
     * @param description the description of the test that is about to be run (generally a class and
     *            method name)
     */
    void testStarted(Description description);

    /**
     * Called when an atomic test has finished, whether the test succeeds, fails or is ignored.
     *
     * @param description the description of the test that just ran
     */
    void testFinished(Description description);

    /**
     * Called when an atomic test fails.
     *
     * @param failure describes the test that failed and the exception that was thrown
     */
    void testFailed(Failure failure);

    /**
     * Called when a test will not be run, generally because a test method is annotated with
     * {@link org.junit.Ignore}.
     *
     * @param description describes the test that will not be run
     */
    void testIgnored(Description description);

    /**
     * Called when an atomic test succeeds.
     *
     * @param description describes the test that will not be run
     */
    void testSucceeded(Description description);

    /**
     * Called when an atomic test flags that it assumes a condition that is false.
     *
     * @param failure describes the test that failed and the {@link AssumptionViolatedException}
     *            that was thrown
     */
    void testAssumptionFailure(Failure failure);

    /**
     * Called after {@link #testClassFinished(Class)}.
     */
    void testClassFinishedDelimiter();

    /**
     * Called after {@link #testClassStarted(Class)}.
     */
    void testClassStartedDelimiter();

    /**
     * Called after {@link #testStarted(Description)}.
     */
    void testStartedDelimiter();

    /**
     * Called after {@link #testFailed(Failure)}.
     */
    void testFinishedDelimiter();

    PrintStream getWriter();

}
