package com.hoc081098.channeleventbus

import kotlin.jvm.JvmField

@JvmField
val TestEventIntKey = ChannelEventKey(TestEventInt::class)

@JvmField
val TestEventStringKey = ChannelEventKey(TestEventString::class)

@JvmField
val TestEventLongKey = ChannelEventKey(TestEventLong::class)

data class TestEventInt(val payload: Int) : ChannelEvent<TestEventInt> {
  override val key get() = Key

  companion object Key : ChannelEventKey<TestEventInt>(TestEventInt::class)
}

data class TestEventString(val payload: String) : ChannelEvent<TestEventString> {
  override val key get() = Key

  companion object Key : ChannelEventKey<TestEventString>(TestEventString::class)
}

data class TestEventLong(val payload: Long) : ChannelEvent<TestEventLong> {
  override val key get() = Key

  companion object Key : ChannelEventKey<TestEventLong>(TestEventLong::class)
}
