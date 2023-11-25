package com.hoc081098.channeleventbus.sample.android.ui.register

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
internal enum class Gender {
  MALE,
  FEMALE,
}

@Stable
internal val Gender.displayName: String
  get() = when (this) {
    Gender.MALE -> "Male"
    Gender.FEMALE -> "Female"
  }

@Immutable
internal sealed interface RegisterUiState {
  data object Unfilled : RegisterUiState

  data class Filled(
    val firstName: String,
    val lastName: String,
    val gender: Gender,
  ) : RegisterUiState

  companion object
}

internal fun RegisterUiState.Companion.from(
  firstName: String?,
  lastName: String?,
  gender: Gender?,
): RegisterUiState = if (
  firstName != null &&
  lastName != null &&
  gender != null
) {
  RegisterUiState.Filled(
    firstName = firstName,
    lastName = lastName,
    gender = gender,
  )
} else {
  RegisterUiState.Unfilled
}
