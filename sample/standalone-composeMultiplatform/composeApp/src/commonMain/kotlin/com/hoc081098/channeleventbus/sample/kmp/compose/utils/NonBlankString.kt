package com.hoc081098.channeleventbus.sample.kmp.compose.utils

/**
 * Representation of strings that have at least one character, excluding
 * whitespaces.
 */
@JvmInline
value class NonBlankString private constructor(private val value: String) : Comparable<NonBlankString> {
  init {
    require(value.isNotBlank()) { NotBlankStringException.message }
  }

  /**
   * Compares this string alphabetically with the [other] one for order.
   * Returns zero if this string equals the [other] one, a negative number if
   * it's less than the [other] one, or a positive number if it's greater than
   * the [other] one.
   */
  override infix fun compareTo(other: NonBlankString): Int = value.compareTo(other.value)

  /** Returns this string as a [String]. */
  override fun toString(): String = value

  fun asString(): String = value

  /** Returns the length of this string. */
  val length: Int get() = value.length

  companion object {
    /**
     * Returns this string as an encapsulated [NonBlankString],
     * or returns an encapsulated [IllegalArgumentException] if this string is
     * [blank][String.isBlank].
     */
    fun String.toNonBlankString(): Result<NonBlankString> =
      runCatching { NonBlankString(this) }
  }
}

internal object NotBlankStringException : IllegalArgumentException() {
  private fun readResolve(): Any = NotBlankStringException

  override val message: String = "Given string shouldn't be blank."
}
