# kotlin-channel-event-bus 🔆

[![Build and publish snapshot](https://github.com/hoc081098/kotlin-channel-event-bus/actions/workflows/build.yml/badge.svg)](https://github.com/hoc081098/kotlin-channel-event-bus/actions/workflows/build.yml)

[![Kotlin version](https://img.shields.io/badge/Kotlin-1.9.20-blueviolet?logo=kotlin&logoColor=white)](http://kotlinlang.org)
[![KotlinX Coroutines version](https://img.shields.io/badge/Kotlinx_Coroutines-1.7.3-blueviolet?logo=kotlin&logoColor=white)](https://github.com/Kotlin/kotlinx.coroutines/releases/tag/1.7.3)

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fhoc081098%2Fkotlin-channel-event-bus&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

![badge][badge-jvm]
![badge][badge-android]
![badge][badge-js]
![badge][badge-ios]
![badge][badge-mac]
![badge][badge-tvos]
![badge][badge-watchos]

<p align="center">
    <img src="https://github.com/hoc081098/kmp-viewmodel/raw/master/logo.png" width="400">
</p>

## Multi-keys, multi-producers, single-consumer event bus backed by `kotlinx.coroutines.channels.Channel`s.

- A Kotlin Multiplatform library that provides a simple event bus implementation using
  `kotlinx.coroutines.channels.Channel`s.
  This is useful for UI applications where you want to send events to communicate between
  different parts / scope of your application.

- This bus is thread-safe to be used by multiple threads.
  It is safe to send events from multiple threads without any synchronization.

- `ChannelEvent.Key` will be used to identify a bus for a specific type of events.
  Each bus has a `Channel` to send events to and a `Flow` to receive events from.

- The `Channel` is unbounded (`Channel.UNLIMITED` - default) or conflated `Channel.CONFLATED`.
  The `Flow` is cold and only one collector is allowed at a time.
  This make sure all events are consumed.

## Author: [Petrus Nguyễn Thái Học](https://github.com/hoc081098)

Liked some of my work? Buy me a coffee (or more likely a beer)

<a href="https://www.buymeacoffee.com/hoc081098" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-blue.png" alt="Buy Me A Coffee" height=64></a>

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

- `android`.
- `jvm` (must add `kotlinx-coroutines-swing`/`kotlinx-coroutines-javafx` to your dependencies to
  make sure `Dispatchers.Main` available).
- `js` (`IR`).
- `Darwin` targets:
  - `iosArm64`, `iosArm32`, `iosX64`, `iosSimulatorArm64`.
  - `watchosArm32`, `watchosArm64`, `watchosX64`, `watchosX86`, `watchosSimulatorArm64`.
  - `tvosX64`, `tvosSimulatorArm64`, `tvosArm64`.
  - `macosX64`, `macosArm64`.

## Docs

TODO

## License

```license
MIT License
Copyright (c) 2023 Petrus Nguyễn Thái Học
```

[badge-android]: http://img.shields.io/badge/android-6EDB8D.svg?style=flat
[badge-ios]: http://img.shields.io/badge/ios-CDCDCD.svg?style=flat
[badge-js]: http://img.shields.io/badge/js-F8DB5D.svg?style=flat
[badge-jvm]: http://img.shields.io/badge/jvm-DB413D.svg?style=flat
[badge-linux]: http://img.shields.io/badge/linux-2D3F6C.svg?style=flat
[badge-windows]: http://img.shields.io/badge/windows-4D76CD.svg?style=flat
[badge-mac]: http://img.shields.io/badge/macos-111111.svg?style=flat
[badge-watchos]: http://img.shields.io/badge/watchos-C0C0C0.svg?style=flat
[badge-tvos]: http://img.shields.io/badge/tvos-808080.svg?style=flat
[badge-wasm]: https://img.shields.io/badge/wasm-624FE8.svg?style=flat
[badge-nodejs]: https://img.shields.io/badge/nodejs-68a063.svg?style=flat
