package com.hoc081098.channeleventbus

/**
 * Represents an exception thrown by [ChannelEventBus].
 */
public sealed class ChannelEventBusException(message: String?, cause: Throwable?) : RuntimeException(message, cause) {
  public abstract val key: ChannelEventKey<*>

  /**
   * Represents an exception thrown when failed to send an event to a bus.
   *
   * @param event the event that failed to send.
   */
  public class FailedToSendEvent(
    public val event: ChannelEvent<*>,
    cause: Throwable?,
  ) : ChannelEventBusException("Failed to send event: $event", cause) {
    override val key: ChannelEventKey<*> get() = event.key
  }

  /**
   * Represents an exception thrown when trying to collect a flow that is already collected by another collector.
   */
  public class FlowAlreadyCollected(
    override val key: ChannelEventKey<*>,
  ) : ChannelEventBusException("Flow by key=$key is already collected", null)

  /**
   * Represents an exception thrown when trying to close a bus.
   */
  public sealed class CloseException(message: String?, cause: Throwable?) : ChannelEventBusException(message, cause) {
    /**
     * Represents an exception thrown when trying to close a bus that does not exist.
     */
    public class BusDoesNotExist(
      override val key: ChannelEventKey<*>,
    ) : CloseException("Bus by key=$key does not exist", null)

    /**
     * Represents an exception thrown when trying to close a bus that is collecting.
     */
    public class BusIsCollecting(
      override val key: ChannelEventKey<*>,
    ) : CloseException("Bus by key=$key is collecting, must cancel the collection before closing", null)

    /**
     * Represents an exception thrown when trying to close a bus
     * that is not empty (all events are not consumed completely).
     */
    public class BusIsNotEmpty(
      override val key: ChannelEventKey<*>,
    ) : CloseException("Bus by key=$key is not empty, try to consume all elements before closing", null)
  }
}
