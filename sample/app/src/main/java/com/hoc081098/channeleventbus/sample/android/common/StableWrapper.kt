package com.hoc081098.channeleventbus.sample.android.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Stable
@JvmInline
value class StableWrapper<T>(val value: T)

@Suppress("NOTHING_TO_INLINE")
@Composable
inline fun <T> rememberStableWrapperOf(value: T) = remember(value) { StableWrapper(value) }
