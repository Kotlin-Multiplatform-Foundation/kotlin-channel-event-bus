@file:Suppress("NOTHING_TO_INLINE")

package com.hoc081098.channeleventbus.sample.android.common

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow

class SavedStateHandleKey<T>(
  val key: String,
  val defaultValue: T,
)

inline fun <T> SavedStateHandle.safeGet(key: SavedStateHandleKey<T>): T {
  return if (contains(key.key)) {
    @Suppress("UNCHECKED_CAST", "RemoveExplicitTypeArguments")
    get<T>(key.key) as T
  } else {
    safeSet(key, key.defaultValue)
    key.defaultValue
  }
}

inline fun <T> SavedStateHandle.safeSet(key: SavedStateHandleKey<T>, value: T) = set(key.key, value)

inline fun <T> SavedStateHandle.safeStateFlow(key: SavedStateHandleKey<T>) =
  getStateFlow(key.key, key.defaultValue)

@JvmInline
value class SafeSavedStateHandle(val savedStateHandle: SavedStateHandle) {
  inline operator fun <T> get(key: SavedStateHandleKey<T>): T = savedStateHandle.safeGet(key)

  inline operator fun <T> set(key: SavedStateHandleKey<T>, value: T) = savedStateHandle.safeSet(key, value)

  inline fun <T> getStateFlow(key: SavedStateHandleKey<T>): StateFlow<T> = savedStateHandle.safeStateFlow(key)
}
