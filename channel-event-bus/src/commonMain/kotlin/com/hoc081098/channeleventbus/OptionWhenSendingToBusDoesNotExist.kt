package com.hoc081098.channeleventbus

/**
 * Option when sending an event to a bus that does not exist.
 * - Create a new bus if the bus associated with [ChannelEvent.key] does not exist.
 * - Throw [ChannelEventBusException.SendException.BusDoesNotExist] if the bus associated with [ChannelEvent.key]
 *   does not exist.
 * - Do nothing if the bus associated with [ChannelEvent.key] does not exist.
 */
public enum class OptionWhenSendingToBusDoesNotExist {
  /**
   * Create a new bus if the bus associated with [ChannelEvent.key] does not exist.
   * This is the default option.
   */
  CREATE_NEW_BUS,

  /**
   * Throw [ChannelEventBusException.SendException.BusDoesNotExist] if the bus associated with [ChannelEvent.key]
   * does not exist.
   */
  THROW_EXCEPTION,

  /**
   * Do nothing if the bus associated with [ChannelEvent.key] does not exist.
   * Basically, the event will be ignored (not sent) if the bus does not exist.
   */
  DO_NOTHING,
}
