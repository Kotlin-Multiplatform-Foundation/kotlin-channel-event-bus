package com.hoc081098.channeleventbus

import kotlin.test.Test
import kotlin.test.assertEquals

class ChannelEventTest {
  @Test
  fun testKeyEquals() {
    assertEquals(TestEventIntKey, TestEventInt)
    assertEquals(TestEventStringKey, TestEventString)
    assertEquals(TestEventLongKey, TestEventLong)
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
