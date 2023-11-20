package com.hoc081098.channeleventbus

import kotlin.jvm.JvmField

public enum class ChannelEventBusValidationWhenClosing {
  REQUIRE_FLOW_IS_NOT_COLLECTING,
  REQUIRE_BUS_IS_EMPTY,
  REQUIRE_BUS_IS_EXISTING,
  ;

  public companion object {
    @JvmField
    public val ALL: Set<ChannelEventBusValidationWhenClosing> = entries.toSet()

    public val NONE: Set<ChannelEventBusValidationWhenClosing> get() = emptySet()
  }
}
