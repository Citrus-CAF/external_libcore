/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package libcore.java.util.function;

import junit.framework.TestCase;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class PredicateTest extends TestCase {

  public void testAnd() throws Exception {
    Object arg = new Object();

    AtomicBoolean alwaysTrueInvoked = new AtomicBoolean(false);
    AtomicBoolean alwaysTrue2Invoked = new AtomicBoolean(false);
    AtomicBoolean alwaysFalseInvoked = new AtomicBoolean(false);
    AtomicBoolean alwaysFalse2Invoked = new AtomicBoolean(false);
    AtomicBoolean[] invocationState = {
        alwaysTrueInvoked, alwaysTrue2Invoked, alwaysFalseInvoked, alwaysFalse2Invoked };

    Predicate<Object> alwaysTrue =
        x -> { alwaysTrueInvoked.set(true); assertSame(x, arg); return true; };
    Predicate<Object> alwaysTrue2 =
        x -> { alwaysTrue2Invoked.set(true); assertSame(x, arg); return true; };
    Predicate<Object> alwaysFalse =
        x -> { alwaysFalseInvoked.set(true); assertSame(x, arg); return false; };
    Predicate<Object> alwaysFalse2 =
        x -> { alwaysFalse2Invoked.set(true); assertSame(x, arg); return false; };

    // true && true
    resetToFalse(invocationState);
    assertTrue(alwaysTrue.and(alwaysTrue2).test(arg));
    assertTrue(alwaysTrueInvoked.get() && alwaysTrue2Invoked.get());

    // true && false
    resetToFalse(invocationState);
    assertFalse(alwaysTrue.and(alwaysFalse).test(arg));
    assertTrue(alwaysTrueInvoked.get() && alwaysFalseInvoked.get());

    // false && false
    resetToFalse(invocationState);
    assertFalse(alwaysFalse.and(alwaysFalse2).test(arg));
    assertTrue(alwaysFalseInvoked.get() && !alwaysFalse2Invoked.get());

    // false && true
    resetToFalse(invocationState);
    assertFalse(alwaysFalse.and(alwaysTrue).test(arg));
    assertTrue(alwaysFalseInvoked.get() && !alwaysTrueInvoked.get());
  }

  public void testAnd_null() throws Exception {
    Object arg = new Object();
    Predicate<Object> alwaysTrue = x -> { assertSame(arg, x); return true; };
    try {
      alwaysTrue.and(null);
      fail();
    } catch (NullPointerException expected) {}
  }

  public void testNegate() throws Exception {
    Predicate<Object> alwaysTrue = x -> true;
    assertFalse(alwaysTrue.negate().test(null));

    Predicate<Object> alwaysFalse = x -> false;
    assertTrue(alwaysFalse.negate().test(null));
  }

  public void testOr() throws Exception {
    Object arg = new Object();

    AtomicBoolean alwaysTrueInvoked = new AtomicBoolean(false);
    AtomicBoolean alwaysTrue2Invoked = new AtomicBoolean(false);
    AtomicBoolean alwaysFalseInvoked = new AtomicBoolean(false);
    AtomicBoolean alwaysFalse2Invoked = new AtomicBoolean(false);
    AtomicBoolean[] invocationState = {
        alwaysTrueInvoked, alwaysTrue2Invoked, alwaysFalseInvoked, alwaysFalse2Invoked };

    Predicate<Object> alwaysTrue =
        x -> { alwaysTrueInvoked.set(true); assertSame(x, arg); return true; };
    Predicate<Object> alwaysTrue2 =
        x -> { alwaysTrue2Invoked.set(true); assertSame(x, arg); return true; };
    Predicate<Object> alwaysFalse =
        x -> { alwaysFalseInvoked.set(true); assertSame(x, arg); return false; };
    Predicate<Object> alwaysFalse2 =
        x -> { alwaysFalse2Invoked.set(true); assertSame(x, arg); return false; };

    // true || true
    resetToFalse(invocationState);
    assertTrue(alwaysTrue.or(alwaysTrue2).test(arg));
    assertTrue(alwaysTrueInvoked.get() && !alwaysTrue2Invoked.get());

    // true || false
    resetToFalse(invocationState);
    assertTrue(alwaysTrue.or(alwaysFalse).test(arg));
    assertTrue(alwaysTrueInvoked.get() && !alwaysFalseInvoked.get());

    // false || false
    resetToFalse(invocationState);
    assertFalse(alwaysFalse.or(alwaysFalse2).test(arg));
    assertTrue(alwaysFalseInvoked.get() && alwaysFalse2Invoked.get());

    // false || true
    resetToFalse(invocationState);
    assertTrue(alwaysFalse.or(alwaysTrue).test(arg));
    assertTrue(alwaysFalseInvoked.get() && alwaysTrueInvoked.get());
  }

  public void testOr_null() throws Exception {
    Predicate<Object> alwaysTrue = x -> true;
    try {
      alwaysTrue.or(null);
      fail();
    } catch (NullPointerException expected) {}
  }

  private static void resetToFalse(AtomicBoolean... toResets) {
    for (AtomicBoolean toReset : toResets) {
      toReset.set(false);
    }
  }
}
