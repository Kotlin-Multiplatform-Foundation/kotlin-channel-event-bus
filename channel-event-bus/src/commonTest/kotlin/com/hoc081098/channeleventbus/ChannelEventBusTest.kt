package com.hoc081098.channeleventbus

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
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

  @Test
  fun onlyOneCollectorAtATime() = runTest {
    val bus = ChannelEventBus(ConsoleChannelEventBusLogger)

    repeat(10) {
      launch { bus.send(TestEventInt(it)) }
    }

    val job = launch {
      bus.receiveAsFlow(TestEventInt).collect()
    }
    launch {
      val e = assertFailsWith<ChannelEventBusException.FlowAlreadyCollected> {
        bus.receiveAsFlow(TestEventInt).collect()
      }
      assertEquals(expected = TestEventIntKey, actual = e.key)
      job.cancel()
    }
  }

  @Test
  fun cancel_ThenCollectMultipleTimes_DoesNotWorks() = runTest {
    val bus = ChannelEventBus(ConsoleChannelEventBusLogger)

    repeat(10) {
      launch { bus.send(TestEventInt(it)) }
    }

    val job = launch {
      bus.receiveAsFlow(TestEventInt).collect()
    }

    launch {
      delay(100)
      job.cancel()

      val e = assertFailsWith<ChannelEventBusException.FlowAlreadyCollected> {
        bus.receiveAsFlow(TestEventInt).collect()
      }
      assertEquals(expected = TestEventIntKey, actual = e.key)
    }
  }

  @Test
  fun cancelAndJoin_ThenCollectMultipleTimes_Works() = runTest {
    val bus = ChannelEventBus(ConsoleChannelEventBusLogger)

    repeat(10) {
      launch { bus.send(TestEventInt(it)) }
    }

    val job = launch {
      assertEquals(
        expected = TestEventInt(0),
        actual = bus.receiveAsFlow(TestEventInt).first(),
      )
    }

    launch {
      delay(100)
      job.cancelAndJoin()
      assertEquals(
        expected = TestEventInt(1),
        actual = bus.receiveAsFlow(TestEventInt).first(),
      )
    }
  }

  @Test
  fun take_ThenCollectMultipleTimes_Works() = runTest {
    val bus = ChannelEventBus(ConsoleChannelEventBusLogger)

    repeat(10) {
      launch { bus.send(TestEventInt(it)) }
    }

    launch {
      repeat(10) {
        bus.receiveAsFlow(TestEventInt).take(1)
      }
    }
  }

  @Test
  fun take_ThenCollectMultipleTimes_WithStandardTestDispatcher_Works() = runTest(StandardTestDispatcher()) {
    val bus = ChannelEventBus(ConsoleChannelEventBusLogger)

    repeat(10) {
      launch { bus.send(TestEventInt(it)) }
    }

    launch {
      repeat(10) {
        bus.receiveAsFlow(TestEventInt).take(1)
      }
    }
  }
}
