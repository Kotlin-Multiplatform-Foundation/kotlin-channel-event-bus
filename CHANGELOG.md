# Change Log

## [Unreleased] - TBD

### Transfered to [Kotlin-Multiplatform-Foundation/kotlin-channel-event-bus](https://github.com/Kotlin-Multiplatform-Foundation/kotlin-channel-event-bus)

### Update dependencies

- [Kotlin `2.0.20` 🎉](https://github.com/JetBrains/kotlin/releases/tag/v2.0.20).

### Added

- **New**: Add support for Kotlin/Wasm (`wasmJs` target) 🎉 in [#41](https://github.com/Kotlin-Multiplatform-Foundation/kotlin-channel-event-bus/pull/41).

### Fixed

- Do not throw `KotlinNullPointerException` in `ChannelEventBusImpl.markAsNotCollecting` method, because `busMap[key]` can be null if it is removed and closed before calling this method.

### Docs

- TODO

## [0.0.2] - Dec 4, 2023

### Added

- Support more targets:
  - `mingwX64`
  - `linuxX64`
  - `linuxArm64`
  - `watchosDeviceArm64`
  - `androidNativeArm32`
  - `androidNativeArm64`
  - `androidNativeX86`
  - `androidNativeX64`

## [0.0.1] - Dec 3, 2023 🎉

- Initial release 🎉
  - Kotlin `1.9.21`.
  - KotlinX Coroutines `1.7.3`.
  - Gradle `8.5`.

[Unreleased]: https://github.com/hoc081098/kotlin-channel-event-bus/compare/0.0.2...HEAD

[0.0.2]: https://github.com/hoc081098/kotlin-channel-event-bus/releases/tag/0.0.2

[0.0.1]: https://github.com/hoc081098/kotlin-channel-event-bus/releases/tag/0.0.1
