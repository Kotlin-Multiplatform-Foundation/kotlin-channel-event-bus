package com.hoc081098.channeleventbus

import kotlin.jvm.JvmField

/**
 * Options for validating a bus before closing.
 */
public enum class ChannelEventBusValidationBeforeClosing {
  /**
   * Require the flow of the bus must not be collecting by any collector before closing.
   * If this requirement is not met, [ChannelEventBusException.CloseException.BusIsCollecting] will be thrown.
   */
  REQUIRE_FLOW_IS_NOT_COLLECTING,

  /**
   * Require the channel of the bus must be empty before closing.
   * If this requirement is not met, [ChannelEventBusException.CloseException.BusIsNotEmpty] will be thrown.
   */
  REQUIRE_BUS_IS_EMPTY,

  /**
   * Require the bus must exist before closing.
   * If this requirement is not met, [ChannelEventBusException.CloseException.BusDoesNotExist] will be thrown.
   */
  REQUIRE_BUS_IS_EXISTING,
  ;

  public companion object {
    @JvmField
    public val ALL: Set<ChannelEventBusValidationBeforeClosing> = entries.toSet()

    public val NONE: Set<ChannelEventBusValidationBeforeClosing> get() = emptySet()
  }
}
