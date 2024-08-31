# kotlin-channel-event-bus 🔆

[![maven-central](https://img.shields.io/maven-central/v/io.github.hoc081098/channel-event-bus)](https://search.maven.org/search?q=g:io.github.hoc081098%20channel-event-bus)
[![codecov](https://codecov.io/github/hoc081098/kotlin-channel-event-bus/graph/badge.svg?token=o5JcvqkEsR)](https://codecov.io/github/hoc081098/kotlin-channel-event-bus)
[![Build and publish snapshot](https://github.com/hoc081098/kotlin-channel-event-bus/actions/workflows/build.yml/badge.svg)](https://github.com/hoc081098/kotlin-channel-event-bus/actions/workflows/build.yml)
[![Build sample](https://github.com/hoc081098/kotlin-channel-event-bus/actions/workflows/sample.yml/badge.svg)](https://github.com/hoc081098/kotlin-channel-event-bus/actions/workflows/sample.yml)
[![Publish Release](https://github.com/hoc081098/kotlin-channel-event-bus/actions/workflows/publish-release.yml/badge.svg)](https://github.com/hoc081098/kotlin-channel-event-bus/actions/workflows/publish-release.yml)
[![Kotlin version](https://img.shields.io/badge/Kotlin-1.9.21-blueviolet?logo=kotlin&logoColor=white)](http://kotlinlang.org)
[![KotlinX Coroutines version](https://img.shields.io/badge/Kotlinx_Coroutines-1.7.3-blueviolet?logo=kotlin&logoColor=white)](https://github.com/Kotlin/kotlinx.coroutines/releases/tag/1.7.3)
[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fhoc081098%2Fkotlin-channel-event-bus&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)
![badge][badge-android]
![badge][badge-jvm]
![badge][badge-js]
![badge][badge-js-ir]
![badge][badge-wasm]
![badge][badge-nodejs]
![badge][badge-linux]
![badge][badge-windows]
![badge][badge-ios]
![badge][badge-mac]
![badge][badge-watchos]
![badge][badge-tvos]
![badge][badge-apple-silicon]

<p align="center">
    <img src="https://github.com/hoc081098/kotlin-channel-event-bus/raw/master/logo.png" width="400">
</p>

## Multi-keys, multi-producers, single-consumer event bus backed by `kotlinx.coroutines.channels.Channel`s.

- A Kotlin Multiplatform library that provides a simple event bus implementation using
  `kotlinx.coroutines.channels.Channel`s.
  This is useful for UI applications where you want to send events to communicate between
  different parts / scope of your application (e.g. send results from one screen to another).

- This bus is thread-safe to be used by multiple threads.
  It is safe to send events from multiple threads without any synchronization.

- `ChannelEvent.Key` will be used to identify a bus for a specific type of events.
  Each bus has a `Channel` to send events to and a `Flow` to receive events from.

- The `Channel` is unbounded (`Channel.UNLIMITED` - default) or conflated `Channel.CONFLATED`.
  The `Flow` is cold and only one collector is allowed at a time.
  This makes sure all events are consumed.

## Author: [Petrus Nguyễn Thái Học](https://github.com/hoc081098)

Like some of my work? Could you buy me a coffee (or more likely a beer)?

<a href="https://www.buymeacoffee.com/hoc081098" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-blue.png" alt="Buy Me A Coffee" height=64></a>

## Docs

- `0.x release` docs: https://kotlin-multiplatform-foundation.github.io/kotlin-channel-event-bus/docs/0.x
- Snapshot docs: https://kotlin-multiplatform-foundation.github.io/kotlin-channel-event-bus/docs/latest/

## Installation

```kotlin
allprojects {
  repositories {
    [...]
    mavenCentral()
  }
}
```

```kotlin
implementation("io.github.hoc081098:channel-event-bus:0.0.2")
```

### Snapshot

<details>
  <summary>Snapshots of the development version are available in Sonatype's snapshots repository.</summary>

- Kotlin

```kotlin
allprojects {
  repositories {
    [...]
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
  }
}

dependencies {
  implementation("io.github.hoc081098:channel-event-bus:0.0.3-SNAPSHOT")
}
```

- Groovy

```groovy
allprojects {
  repositories {
    [...]
    maven { url "https://s01.oss.sonatype.org/content/repositories/snapshots/" }
  }
}

dependencies {
  implementation 'io.github.hoc081098:channel-event-bus:0.0.3-SNAPSHOT'
}
```

</details>

## Basic usage

```kotlin
// Create your event type
data class AwesomeEvent(val payload: Int) : ChannelEvent<AwesomeEvent> {
  override val key get() = Key

  companion object Key : ChannelEventKey<AwesomeEvent>(AwesomeEvent::class)
}

// Create your bus instance
val bus = ChannelEventBus()

// Send events to the bus
bus.send(AwesomeEvent(1))
bus.send(AwesomeEvent(2))
bus.send(AwesomeEvent(3))

// Receive events from the bus
bus
  .receiveAsFlow(AwesomeEvent) // or bus.receiveAsFlow(AwesomeEvent.Key) if you want to be explicit
  .collect { e: AwesomeEvent -> println(e) }
```

## Supported targets

- `jvm` / `android`.
- `js` (`IR`).
- `wasmJs`.
- `Darwin` targets:
  - `iosArm64`, `iosX64`, `iosSimulatorArm64`.
  - `watchosArm32`, `watchosArm64`, `watchosX64`, `watchosSimulatorArm64`, `watchosDeviceArm64`.
  - `tvosX64`, `tvosSimulatorArm64`, `tvosArm64`.
  - `macosX64`, `macosArm64`.
- `mingwX64`
- `linuxX64`, `linuxArm64`.
- `androidNativeArm32`, `androidNativeArm64`, `androidNativeX86`, `androidNativeX64`.

## Sample

- [Android Compose sample](https://github.com/hoc081098/kotlin-channel-event-bus/tree/master/sample/app):
  an Android app using Compose UI to show how to use the library.
  It has two nested navigation graphs: `Register` and `Home`.

  - In `Register`, we have 3 steps (3 screens) to allow the user to input their information, step
    by
    step.
    - A `RegisterSharedViewModel` (bound to `Register` navigation graph scope) is used
      to hold the whole state of the registration process.
      It observes events from the `ChannelEventBus` and updates the state accordingly.

    - Each step screen has a `ViewModel` to hold the state of the screen and will send events to
      the `ChannelEventBus`,
      then the `RegisterSharedViewModel` will receive those events and update the state.

  - In the `Home` nav graph, we have 2 screens: `Home` and `Detail`.
    - The `Home` screen has a `HomeViewModel` to hold the results received from the `Detail` screen.
      Those result events are sent from the `Detail` screen to the `ChannelEventBus`,
      and the `HomeViewModel` will receive those events and update the state.

    - The `Detail` screen will send events to the `ChannelEventBus` when the user clicks on the button.
      The `HomeViewModel` will receive those events and update the state.

https://github.com/hoc081098/kotlin-channel-event-bus/assets/36917223/80015232-d5b5-4fb2-a779-4e6113ddb8f8

## Roadmap

- [x] Support more targets: `wasm` (depends on supported targets by `kotlinx.coroutines`) (since [0.1.0](https://github.com/Kotlin-Multiplatform-Foundation/kotlin-channel-event-bus/releases/tag/0.1.0)).
- [x] More samples.
- [ ] More docs.
- [ ] More tests.

## License

```license
                                 Apache License
                           Version 2.0, January 2004
                        http://www.apache.org/licenses/
```

[badge-android]: http://img.shields.io/badge/-android-6EDB8D.svg?style=flat
[badge-android-native]: http://img.shields.io/badge/support-[AndroidNative]-6EDB8D.svg?style=flat
[badge-wearos]: http://img.shields.io/badge/-wearos-8ECDA0.svg?style=flat
[badge-jvm]: http://img.shields.io/badge/-jvm-DB413D.svg?style=flat
[badge-js]: http://img.shields.io/badge/-js-F8DB5D.svg?style=flat
[badge-js-ir]: https://img.shields.io/badge/support-[IR]-AAC4E0.svg?style=flat
[badge-nodejs]: https://img.shields.io/badge/-nodejs-68a063.svg?style=flat
[badge-linux]: http://img.shields.io/badge/-linux-2D3F6C.svg?style=flat
[badge-windows]: http://img.shields.io/badge/-windows-4D76CD.svg?style=flat
[badge-wasm]: https://img.shields.io/badge/-wasm-624FE8.svg?style=flat
[badge-apple-silicon]: http://img.shields.io/badge/support-[AppleSilicon]-43BBFF.svg?style=flat
[badge-ios]: http://img.shields.io/badge/-ios-CDCDCD.svg?style=flat
[badge-mac]: http://img.shields.io/badge/-macos-111111.svg?style=flat
[badge-watchos]: http://img.shields.io/badge/-watchos-C0C0C0.svg?style=flat
[badge-tvos]: http://img.shields.io/badge/-tvos-808080.svg?style=flat
