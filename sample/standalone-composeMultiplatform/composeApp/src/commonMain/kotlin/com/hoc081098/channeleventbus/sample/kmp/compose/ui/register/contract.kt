package com.hoc081098.channeleventbus.sample.kmp.compose.ui.register

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import com.hoc081098.kmp.viewmodel.safe.NullableSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.string
import kotlin.LazyThreadSafetyMode.PUBLICATION

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

internal val FirstNameKey by lazy(PUBLICATION) { NullableSavedStateHandleKey.string("first_name", null) }
internal val LastNameKey by lazy(PUBLICATION) { NullableSavedStateHandleKey.string("last_name", null) }
internal val GenderKey by lazy(PUBLICATION) {
  // TODO: When Serializable is supported, remove the below workaround
  @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
  @OptIn(InternalKmpViewModelApi::class)
  NullableSavedStateHandleKey<Gender>("gender", null)
}
