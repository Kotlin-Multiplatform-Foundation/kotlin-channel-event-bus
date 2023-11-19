package com.hoc081098.channeleventbus

/**
 * Logger for [ChannelEventBus].
 * It is used to log events of [ChannelEventBus].
 */
public interface ChannelEventBusLogger {
  /**
   * Called when a bus associated with [key] is created.
   * @see [ChannelEventBus.send]
   */
  public fun onCreated(key: ChannelEventKey<*>, bus: ChannelEventBus)

  /**
   * Called when a bus associated with [key] is collecting.
   * @see [ChannelEventBus.receiveAsFlow]
   */
  public fun onStartCollection(key: ChannelEventKey<*>, bus: ChannelEventBus)

  /**
   * Called when a bus associated with [key] is stopped collecting.
   * @see [ChannelEventBus.receiveAsFlow]
   */
  public fun onStopCollection(key: ChannelEventKey<*>, bus: ChannelEventBus)

  /**
   * Called when a bus associated with [key] is closed.
   * @see [ChannelEventBus.closeKey]
   */
  public fun onClosed(key: ChannelEventKey<*>, bus: ChannelEventBus)

  /**
   * Called when all buses are closed.
   * @see [ChannelEventBus.close]
   */
  public fun onClosedAll(keys: Set<ChannelEventKey<*>>, bus: ChannelEventBus)
}

/**
 * The [ChannelEventBusLogger] that simply prints events to the console via [println].
 */
public object ConsoleChannelEventBusLogger : ChannelEventBusLogger {
  override fun onCreated(key: ChannelEventKey<*>, bus: ChannelEventBus): Unit =
    println("[$bus] onCreated: key=$key")

  override fun onStartCollection(key: ChannelEventKey<*>, bus: ChannelEventBus): Unit =
    println("[$bus] onStartCollection: key=$key")

  override fun onStopCollection(key: ChannelEventKey<*>, bus: ChannelEventBus): Unit =
    println("[$bus] onStopCollection: key=$key")

  override fun onClosed(key: ChannelEventKey<*>, bus: ChannelEventBus): Unit =
    println("[$bus] onClosed: key=$key")

  override fun onClosedAll(keys: Set<ChannelEventKey<*>>, bus: ChannelEventBus): Unit =
    println("[$bus] onClosedAll: keys=$keys")
}
