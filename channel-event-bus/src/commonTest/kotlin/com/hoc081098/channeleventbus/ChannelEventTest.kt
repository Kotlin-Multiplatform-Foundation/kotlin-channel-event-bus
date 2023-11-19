package com.hoc081098.channeleventbus

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ChannelEventTest {
  @Test
  fun testKeyEquals() {
    assertEquals(TestEventIntKey, TestEventInt)
    assertEquals(TestEventInt, TestEventInt)

    assertEquals(TestEventStringKey, TestEventString)
    assertEquals(TestEventString, TestEventString)

    assertEquals(TestEventLongKey, TestEventLong)
    assertEquals(TestEventLong, TestEventLong)

    assertNotEquals<Any>(TestEventIntKey, TestEventStringKey)
    assertNotEquals<Any>(TestEventIntKey, TestEventLongKey)
  }

  @Test
  fun testKeyHashCode() {
    assertEquals(TestEventIntKey.hashCode(), TestEventInt.hashCode())
    assertEquals(TestEventStringKey.hashCode(), TestEventString.hashCode())
    assertEquals(TestEventLongKey.hashCode(), TestEventLong.hashCode())
  }

  @Test
  fun testKeyToString() {
    assertEquals("ChannelEvent.Key(TestEventInt)", TestEventIntKey.toString())
    assertEquals("ChannelEvent.Key(TestEventString)", TestEventStringKey.toString())
    assertEquals("ChannelEvent.Key(TestEventLong)", TestEventLongKey.toString())
  }
}
