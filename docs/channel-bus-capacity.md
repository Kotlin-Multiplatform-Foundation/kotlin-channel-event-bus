# Channel Bus Capacity

This document explains how channel capacity works in the Kotlin Channel Event Bus and how to configure it for your needs.

## Understanding Channel Capacity

Channel capacity refers to the number of events that can be stored in a channel before it suspends the sender. The capacity configuration directly affects how the event bus handles backpressure and memory usage.

## Available Capacity Modes

### 1. Unlimited Capacity
```kotlin
val eventBus = ChannelEventBus(capacity = Channel.UNLIMITED)
```
- No suspension on send operations
- Memory usage grows with queued events
- Best for scenarios where event loss is unacceptable
- Risk of OutOfMemoryError if consumers are slower than producers

### 2. Buffered Capacity
```kotlin
val eventBus = ChannelEventBus(capacity = 100)
```
- Fixed-size buffer for events
- Suspends sender when buffer is full
- Provides backpressure handling
- Good balance between memory usage and performance

### 3. Rendezvous (Zero Capacity)
```kotlin
val eventBus = ChannelEventBus(capacity = Channel.RENDEZVOUS)
```
- No buffer
- Direct hand-off between sender and receiver
- Strongest form of backpressure
- Best for synchronized producer-consumer scenarios

### 4. Conflated
```kotlin
val eventBus = ChannelEventBus(capacity = Channel.CONFLATED)
```
- Keeps only the latest event
- Never suspends on send
- Useful for "latest state" scenarios
- May drop events if consumer is slow

## Choosing the Right Capacity

Consider these factors when selecting channel capacity:

1. **Memory Constraints**
   - Higher capacity = More memory usage
   - Lower capacity = More suspension/blocking

2. **Event Loss Tolerance**
   - Critical events: Use unlimited or buffered
   - State updates: Consider conflated
   - Real-time data: Use rendezvous or small buffer

3. **Performance Requirements**
   - High throughput: Use buffered with appropriate size
   - Low latency: Use rendezvous or small buffer
   - Latest state only: Use conflated

4. **Producer-Consumer Patterns**
   - Many-to-one: Consider larger buffers
   - One-to-many: Smaller buffers might suffice
   - Balanced: Use rendezvous or small buffer

## Best Practices

1. Start with a reasonable buffer size (e.g., 50-100)
2. Monitor memory usage and adjust accordingly
3. Consider using different capacities for different event types
4. Test under expected load conditions
5. Implement monitoring and alerting for buffer overflow situations 