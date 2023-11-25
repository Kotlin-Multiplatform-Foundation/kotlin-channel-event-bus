@file:Suppress("NOTHING_TO_INLINE")

package com.hoc081098.channeleventbus.sample.android.common

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow

class SavedStateHandleKey<T>(
  val key: String,
  val defaultValue: T,
)

inline fun <T> SavedStateHandle.safeGet(key: SavedStateHandleKey<T>): T = get(key.key) ?: key.defaultValue

inline fun <T> SavedStateHandle.safeSet(key: SavedStateHandleKey<T>, value: T) = set(key.key, value)

inline fun <T> SavedStateHandle.safeStateFlow(key: SavedStateHandleKey<T>) =
  getStateFlow(key.key, key.defaultValue)

@JvmInline
value class SafeSavedStateHandle(private val savedStateHandle: SavedStateHandle) {
  operator fun <T> get(key: SavedStateHandleKey<T>): T = savedStateHandle.safeGet(key)

  operator fun <T> set(key: SavedStateHandleKey<T>, value: T) = savedStateHandle.safeSet(key, value)

  fun <T> getStateFlow(key: SavedStateHandleKey<T>): StateFlow<T> = savedStateHandle.safeStateFlow(key)
}
