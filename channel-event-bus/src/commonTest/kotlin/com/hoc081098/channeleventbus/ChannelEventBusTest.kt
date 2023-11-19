package com.hoc081098.channeleventbus

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest

class ChannelEventBusTest {
  @Test
  fun sendAndReceiveMultiple() = runTest {
    val bus = ChannelEventBus(ConsoleChannelEventBusLogger)

    val sentTestEventInts = mutableListOf<TestEventInt>()
    val sentTestEventStrings = mutableListOf<TestEventString>()
    val sentTestEventLongs = mutableListOf<TestEventLong>()

    repeat(100 * 3) { i ->
      launch {
        when (i % 3) {
          0 -> bus.send(TestEventInt(i).also { sentTestEventInts += it })
          1 -> bus.send(TestEventString(i.toString()).also { sentTestEventStrings += it })
          2 -> bus.send(TestEventLong(i.toLong()).also { sentTestEventLongs += it })
          else -> error("Unreachable")
        }
      }
    }

    launch {
      val testEventInts = bus
        .receiveAsFlow(TestEventInt)
        .take(100)
        .toList()

      assertContentEquals(
        expected = sentTestEventInts,
        actual = testEventInts,
      )
    }

    launch {
      val testEventStrings = bus
        .receiveAsFlow(TestEventString)
        .take(100)
        .toList()

      assertContentEquals(
        expected = sentTestEventStrings,
        actual = testEventStrings,
      )
    }

    launch {
      val testEventStrings = bus
        .receiveAsFlow(TestEventLong)
        .take(100)
        .toList()

      assertContentEquals(
        expected = sentTestEventLongs,
        actual = testEventStrings,
      )
    }
  }
}
