package com.hoc081098.channeleventbus

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ChannelEventTest {
  @Test
  fun testKeyEquals() {
    assertEquals(TestEventIntKey, TestEventInt)
    assertEquals(TestEventInt, TestEventInt)
    assertEquals<Any>(channelEventKeyOf<TestEventInt>(), TestEventInt)

    assertEquals(TestEventStringKey, TestEventString)
    assertEquals(TestEventString, TestEventString)
    assertEquals<Any>(channelEventKeyOf<TestEventString>(), TestEventString)

    assertEquals(TestEventLongKey, TestEventLong)
    assertEquals(TestEventLong, TestEventLong)
    assertEquals<Any>(channelEventKeyOf<TestEventLong>(), TestEventLong)

    assertNotEquals<Any>(TestEventIntKey, TestEventStringKey)
    assertNotEquals<Any>(TestEventIntKey, TestEventLongKey)
    assertNotEquals<Any>(channelEventKeyOf<TestEventInt>(), channelEventKeyOf<TestEventString>())
  }

  @Test
  fun testKeyHashCode() {
    assertEquals(TestEventIntKey.hashCode(), TestEventInt.hashCode())
    assertEquals(channelEventKeyOf<TestEventInt>().hashCode(), TestEventInt.hashCode())

    assertEquals(TestEventStringKey.hashCode(), TestEventString.hashCode())
    assertEquals(channelEventKeyOf<TestEventString>().hashCode(), TestEventString.hashCode())

    assertEquals(TestEventLongKey.hashCode(), TestEventLong.hashCode())
    assertEquals(channelEventKeyOf<TestEventLong>().hashCode(), TestEventLong.hashCode())
  }

  @Test
  fun testKeyToString() {
    assertEquals("ChannelEvent.Key(TestEventInt)", TestEventIntKey.toString())
    assertEquals("ChannelEvent.Key(TestEventInt)", channelEventKeyOf<TestEventInt>().toString())

    assertEquals("ChannelEvent.Key(TestEventString)", TestEventStringKey.toString())
    assertEquals("ChannelEvent.Key(TestEventString)", channelEventKeyOf<TestEventString>().toString())

    assertEquals("ChannelEvent.Key(TestEventLong)", TestEventLongKey.toString())
    assertEquals("ChannelEvent.Key(TestEventLong)", channelEventKeyOf<TestEventLong>().toString())
  }
}
